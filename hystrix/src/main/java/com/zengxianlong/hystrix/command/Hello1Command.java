package com.zengxianlong.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;

/**
 * 通过命令即继承的方式实现服务降级查看报错信息
 *在getFallback方法中，我们可以通过getExecutionException方法来获取执行的异常信息。
 * @author long
 * @date 2022-02-26 16:11
 */
public class Hello1Command extends HystrixCommand<String> {
    RestTemplate restTemplate;

    public Hello1Command(Setter setter,RestTemplate restTemplate){
        super(setter);
        this.restTemplate=restTemplate;
    }

    @Override
    protected String run() throws Exception {
        int i=1/0;
        return restTemplate.getForObject("http://eureka-provider/hello",String.class);
    }

    @Override
    protected String getFallback() {
        return "error-extends:"+getExecutionException().getMessage();
    }
}
