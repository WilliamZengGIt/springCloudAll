package com.zengxianlong.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;

/**
 * 使用继承方式使用 缓存功能
 *
 * @author long
 * @date 2022-02-26 21:31
 */
public class Hello2Command extends HystrixCommand<String> {
    RestTemplate restTemplate;
    String name;

    public Hello2Command(Setter setter,RestTemplate restTemplate,String name){
        super(setter);
        this.name=name;
        this.restTemplate=restTemplate;
    }

    @Override
    protected String run() throws Exception {
        return restTemplate.getForObject("http://eureka-provider/hello7?name={1}",String.class,name);
    }

    @Override
    protected String getCacheKey() {
        return name;
    }

    /**
     * 这个方法就是请求失败的回调
     * @return
     */
    @Override
    protected String getFallback() {
        return"error-extends:"+getExecutionException().getMessage();
    }
}
