package com.nchu.easyword.service.impl;

import com.nchu.easyword.dao.model.NewsWithBLOBs;
import com.nchu.easyword.service.inface.NewsService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author xujw
 * 2018-4-17 13:41:06
 * 爬虫工具类用于爬取双语新闻内容
 */
@Service
public class SpiderService {
    static final String HomeUrl = "http://news.iyuba.com/";
    /*新闻正文文档对象*/
    private Vector<Document> documentVectors;
    @Autowired
    NewsService newsService;
    @Autowired
    HttpClient spiderHttpClient;
    @Autowired
    ExecutorService pool;

    public boolean startCrawl(String date) throws IOException {
        documentVectors = new Vector();
        /*用于接收线程返回值*/
        List<Future> futureList = new ArrayList<>();
        System.out.println("正在抓取新闻分类目录........");
        /*获取任务起始时间*/
        long startTime = System.currentTimeMillis();
        List<String> catalogList = getCategoryUrls(HomeUrl);
        System.out.println("目录抓取成功,共有新闻分类目录 " + catalogList.size() + "个.");
        System.out.println("正在获取新闻列表........");
        List<String> newsList = getNewsUrlListByCatalog(catalogList, date);
        System.out.println("新闻列表获取成功.");
        System.out.println("开始抓取新闻正文网页......");
        /*遍历新闻url地址抓取新闻正文网页*/
        for (int i = 0, len = newsList.size(); i < len; i++) {
            futureList.add(pool.submit(new GetDocRunnable(newsList.get(i), documentVectors)));
        }
        /*等待任务执行完成，然后清空futureList用于接受后续任务返回值*/
        waitCallable(futureList).clear();
        /*网页文档抓取成功后统一进行内容解析*/
        System.out.println("新闻正文抓取成功!\n共 " + documentVectors.size() + "条");
        System.out.println("开始解析新闻并保存到数据库中.......");
        for (int i = 0, len = documentVectors.size(); i < len; i++) {
            futureList.add(pool.submit(new NewsAnalyze(documentVectors.get(i))));
        }
        waitCallable(futureList).clear();
        /*获取任务结苏时间*/
        long endTime = System.currentTimeMillis();
        float excTime = (float) (endTime - startTime) / 1000;
        System.out.println("全部操作完成,共耗时:" + excTime + "秒");
        return true;
    }

    /*等待线程池中的任务执行完毕*/
    public List<Future> waitCallable(List<Future> futureList) {
        for (int i = 0, len = futureList.size(); i < len; i++) {
            try {
                /*此方法会阻塞直到当前线程的任务完成并产生了返回值*/
                futureList.get(i).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*任务完成后清空返回值列表*/
        return futureList;
    }

    /**
     * @deprecated 通过url地址获取网页主体内容, 多线程支持较差
     */
    public Document getDocByUrl(String url) throws IOException {
        try {
            return Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0")
                    .header("Connection", "close")//如果是这种方式，这里务必带上
                    .timeout(8000)//超时时间
                    .get();
        } catch (Exception e) {//可以精确处理timeoutException
            //超时等异常处理
            e.printStackTrace();
            return Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0")
                    .header("Connection", "close")//如果是这种方式，这里务必带上
                    .timeout(80000)//超时时间
                    .get();
        }
    }

    /**
     * 通过url地址获取网页主体内容, 支持多线程
     *
     * @param url
     * @return
     * @throws IOException
     */
    public Document getDocByUrlMultiThread(String url) throws IOException {
        GetMethod getMethod = new GetMethod(url);
        getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0");
        getMethod.addRequestHeader("Connection", "close");
        spiderHttpClient.executeMethod(getMethod);
        Document document = Jsoup.parse(getMethod.getResponseBodyAsStream(), "utf-8", url);
        //System.out.println("状态码：" + statusCode);
        getMethod.releaseConnection();
        return document;
    }

    /**
     * 获取新闻分类页面地址
     *
     * @return
     */
    public List<String> getCategoryUrls(String homeUrl) throws IOException {
        /*读取首页文档*/
        Document homDoc = getDocByUrlMultiThread(homeUrl);
        List<String> urls = new ArrayList<>();
        if (homDoc != null) {
            /*选取目录导航栏DOM元素*/
            Elements links = homDoc.select("ul.nav.navbar-nav a[href]");
            for (Element link : links) {
                /*如果为首页地址则舍去*/
                String href = link.attr("href").trim();
                if (HomeUrl.equals(href)) {
                    continue;
                }
                urls.add(href);
            }
        }
        return urls;
    }

    /**
     * 给定某一新闻分类的html页面，然后抓取当前分类下的文章列表并通过时间进行筛选
     *
     * @param doc  某一新闻分类下的新闻列表html文档对象
     * @param date 时间字符串格式必须为 xxxx-xx-xx 如 2018-04-16
     */
    private List<String> getArticleListByDate(Document doc, String date) {
        if (doc == null) {
            return null;
        }
        List<String> newsUrl = new ArrayList<>();
        Elements links = doc.select("div.c_left2 a[href]");
        for (Element link : links) {
            /*如果时间符合才加入url列表*/
            if (date != null && !date.equals(link.select("li.deta").text())) {
                continue;
            }
            String href = link.attr("href").trim();
            /*如果地址以../开头是翻页按钮连接也要舍去*/
            if (href.startsWith("../")) {
                continue;
            }
            newsUrl.add(HomeUrl + href);
        }
        return newsUrl;
    }


    /**
     * 获取目录列表下的全部文章正文地址
     *
     * @param categoryList 目录页地址列表
     * @param date         新闻时间
     * @return
     */
    public List<String> getNewsUrlListByCatalog(List<String> categoryList, String date) throws IOException {
        /*存储每个目录下的新闻列表总和*/
        List<String> newsList = new ArrayList<>();
        /*每个新闻分类对应的网页*/
        List<Document> categoryDocs = new ArrayList<>();
        /*遍历目录获取对应目录下的新闻正文地址url*/
        for (int i = 0, len = categoryList.size(); i < len; i++) {
            categoryDocs.add(getDocByUrlMultiThread(categoryList.get(i)));
        }
        for (Document doc : categoryDocs) {
            newsList.addAll(getArticleListByDate(doc, date));
        }
        return newsList;
    }

    /**
     * 通过新闻正文文档抓取新闻内容
     *
     * @param doc 新闻网页正文文档
     * @return 新闻对象
     */
    public NewsWithBLOBs getNewsByDoc(Document doc) {
        /*要移除的DOM元素选择器，主要用于移除与内容无关的按钮*/
        String removeSelect = "span.span1,span.span2,p#leibie,input#mp_,div.bdsharebuttonbox,div.bofangqi,p.p3,p.p4,p.p5";
        /*新闻封面选择器*/
        String coverPicSelect = "p.tupian>img";
        /*新闻发音地址选择器*/
        String voiceSelect = "input#mp_";
        /*选中新闻主体DOM*/
        Elements elements = doc.select("div#work");
        String title_en = elements.select("h1").text();
        String title_cn = elements.select("span.title_cn").html();
        /*截取中文标题*/
        title_cn = title_cn.substring(title_cn.indexOf("</h1>") + "</h1>".length(), title_cn.length());
        /*获取新闻摘要*/
        String summary = elements.select("p.jieshao").text().replace("导读:", "");
        /*获取英文原文*/
        String english_text = Jsoup.clean(elements.select("p.p1").outerHtml(), Whitelist.basic());
        /*获取中文原文*/
        String translation_text = Jsoup.clean(elements.select("p.p2").outerHtml(), Whitelist.basic());
        /*获取新闻单词数量*/
        int wordNum = Integer.parseInt(elements.select("b#wordcount").text());
        /*获取封面图片*/
        String coverPic = elements.select(coverPicSelect).attr("src");
        /*获取的是相对地址，要加上域名形成完整地址*/
        String voiceUrl = HomeUrl + elements.select(voiceSelect).attr("value");
        /*获取来源文本*/
        elements.select("p.p4>span").remove();
        String source = elements.select("p.p4").text();
        /*移除按钮等元素*/
        elements.select(removeSelect).remove();
        /*截取新闻主主体内容*/
        String html_content = Jsoup.clean(elements.html(), Whitelist.basic().addAttributes(":all", "class", "style", "src").addTags("h1", "img"));
        return new NewsWithBLOBs(title_en, title_cn, summary, coverPic, wordNum,
                source, voiceUrl, html_content, english_text, translation_text);
    }

    /**
     * 文章解析器用于多线程解析文章内容并保存到数据库
     */
    class NewsAnalyze implements Callable<Boolean> {
        private Document document;

        public NewsAnalyze(Document document) {
            this.document = document;
        }

        @Override
        public Boolean call() {
            boolean isError;
            do {
                isError = false;
                /*解析并保存到数据库*/
                try {
                    newsService.createNews(getNewsByDoc(document));
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("新闻解析失败,正在重试......");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    isError = true;
                }
                /*如果出错了就重试*/
            } while (isError);
            return false;
        }
    }

    /*多线程抓取新闻主体文档*/
    class GetDocRunnable implements Callable<Boolean> {
        String url;
        Vector<Document> documentVectors;

        public GetDocRunnable(String url, Vector<Document> documentVectors) {
            this.url = url;
            this.documentVectors = documentVectors;
        }

        @Override
        public Boolean call() {
            /*是否出错的标志位*/
            boolean isError;
            do {
                isError = false;
                try {
                    documentVectors.add(getDocByUrlMultiThread(url));
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("网页抓取失败,正在重试......");
                    /*如果出错则再次重试*/
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    isError = true;
                }
            } while (isError);
            return false;
        }
    }
}
