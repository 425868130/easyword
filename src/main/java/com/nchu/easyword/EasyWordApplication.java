package com.nchu.easyword;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.nchu.easyword.interceptor.LoginInterceptor;
import com.nchu.easyword.interceptor.ResponseInterceptor;
import com.nchu.easyword.listener.UserSessionListener;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*开启对计划任务的支持*/
@EnableScheduling
@SpringBootApplication
/*mybatis mapper接口扫描*/
@MapperScan("com.nchu.easyword.dao.mapperInterface")
public class EasyWordApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(EasyWordApplication.class, args);
    }

    /*配置fastJson*/
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        /*创建装换对象*/
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        /*创建配置文件对象*/
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);

        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(fastMediaTypes);
        converter.setFastJsonConfig(fastJsonConfig);

        return new HttpMessageConverters(converter);
    }

    /*
     * 注册自定义监听器
     * */
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        /*添加session监听器*/
        servletListenerRegistrationBean.setListener(new UserSessionListener());
        return servletListenerRegistrationBean;
    }

    /*简单邮件对象*/
    @Bean
    public SimpleMailMessage simpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("system@peaktop.top");
        return simpleMailMessage;
    }

    /*爬虫使用的支持多线程并发连接的httpclient对象*/
    @Bean
    public HttpClient getSpiderClient(@Value("${spiderMaxConnections}") int spiderMaxConnections) {
        /*参数配置对象*/
        HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
        /*设置最大连接数*/
        managerParams.setMaxTotalConnections(spiderMaxConnections);
        managerParams.setDefaultMaxConnectionsPerHost(spiderMaxConnections);
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.setParams(managerParams);
        HttpClient client = new HttpClient(connectionManager);
        return client;
    }

    /**
     * 后台工作线程池
     *
     * @return
     */

    @Bean
    public ExecutorService getExecutorService(@Value("${maxBackThread}") int maxBackThread) {
        return Executors.newFixedThreadPool(maxBackThread);
    }

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Autowired
    ResponseInterceptor responseInterceptor;
    @Autowired
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*注册拦截器,按注册顺序生效,先进入responseInterceptor创建统一响应对象,然后进入loginInterceptor检测用户的登录状态*/
        registry.addInterceptor(responseInterceptor).addPathPatterns("/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/logout", "/user/userAuthentication", "/user/register", "/user/unLoginError"
                        , "/news/getNewsByPage", "/news/getNewsById/*", "/getRandomMean/*", "/resFiles/getFileListByPage", "/sentence/getNetSentences/*",
                        "/sentence/getUserSentences", "/words/searchByKeywordsEn/*", "/user/unLoginError", "/news/crawlNews"
                );
    }
}
