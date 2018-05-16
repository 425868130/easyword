package com.nchu.easyword;


import com.nchu.easyword.dao.model.NewsWithBLOBs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 爬虫测试程序
 */
public class SpiderTest {
    static final String HomeUrl = "http://news.iyuba.com/";
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    static List<NewsWithBLOBs> newsWithBLOBsList = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("正在抓取新闻目录........");
        List<String> catalogUrl = getCategoryUrls(HomeUrl);
        System.out.println("目录抓取成功.");
        System.out.println("正在获取新闻列表........");
        List<String> newsList = getNewsListByCatalog(catalogUrl, null);
        System.out.println("新闻列表获取成功.");
        System.out.println("正在解析新闻内容........");
        newsList.stream().forEach(url -> pool.execute(new NewsAnalyze(url)));
        System.out.println("新闻抓取成功!");
    }

    public synchronized static Document getDocByUrl(String url) {
        try {
            /*抓取爱语吧首页html*/
            return Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0")
                    .header("Connection", "close")//如果是这种方式，这里务必带上
                    .timeout(8000)//超时时间
                    .get();
        } catch (Exception e) {//可以精确处理timeoutException
            //超时等异常处理
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取新闻分类页面地址
     *
     * @return
     */
    public static List<String> getCategoryUrls(String homeUrl) {
        /*读取首页文档*/
        Document homDoc = getDocByUrl(homeUrl);
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
     * 抓取文章列表并通过时间进行筛选
     *
     * @param doc  html文档对象
     * @param date 时间字符串格式必须为 xxxx-xx-xx 如 2018-04-16
     */
    private static List<String> getArticleListByDate(Document doc, String date) {
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
    public static List<String> getNewsListByCatalog(List<String> categoryList, String date) {
        /*目录列表和新闻列表*/
        List<String> newsList = new ArrayList<>();
        /*遍历目录获取对应目录下的新闻正文地址*/
        for (int i = 0, len = categoryList.size(); i < len; i++) {
            newsList.addAll(getArticleListByDate(getDocByUrl(categoryList.get(i)), date));
        }
        return newsList;
    }

    /**
     * 通过新闻地址抓取新闻内容
     *
     * @param url
     * @return 新闻对象
     */
    public static NewsWithBLOBs getNewsByUrl(String url) {
        /*要移除的DOM元素选择器，主要用于移除与内容无关的按钮*/
        final String removeSelect = "span.span1,span.span2,p#leibie,input#mp_,div.bdsharebuttonbox,div.bofangqi,p.p3,p.p4,p.p5";
        /*新闻封面选择器*/
        final String coverPicSelect = "p.tupian>img";
        /*新闻发音地址选择器*/
        final String voiceSelect = "input#mp_";
        /*选中新闻主体DOM*/
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0")
                    .header("Connection", "close")//如果是这种方式，这里务必带上
                    .timeout(8000)//超时时间
                    .get();
        } catch (IOException e) {
            try {
                document = Jsoup.connect(url)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0")
                        .header("Connection", "close")//如果是这种方式，这里务必带上
                        .timeout(8000)//超时时间
                        .get();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        Elements elements = document.select("div#work");
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

    static class NewsAnalyze implements Runnable {
        String url;

        public NewsAnalyze(String url) {
            this.url = url;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            System.out.println(getNewsByUrl(url).getTitleCn());
        }
    }
}
