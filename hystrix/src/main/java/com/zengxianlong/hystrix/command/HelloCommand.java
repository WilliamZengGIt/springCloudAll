package com.zengxianlong.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;

/**
 * 请求命令
 * 请求命令就是以继承类的方式来代替前面的注解方式。
 *
 * @author long
 * @date 2022-02-26 15:36
 */
public class HelloCommand  extends HystrixCommand<String> {

    RestTemplate restTemplate;

    public HelloCommand(Setter setter,RestTemplate restTemplate){
        super(setter);
        this.restTemplate=restTemplate;
    }

    @Override
    protected String run() throws Exception {
        return restTemplate.getForObject("http://eureka-provider/hello",String.class);
    }

    /**
     * 这个方法就是请求失败的回调
     * @return
     */
    @Override
    protected String getFallback() {
        return "error-extends";
    }
}
