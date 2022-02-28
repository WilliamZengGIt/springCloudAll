package com.zengxianlong.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * TOOD
 *
 * @author long
 * @date 2022-02-26 14:48
 */
@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    /**
     * 在这个方法中，我们将会发起一个远程调用，去调用provider中提供的/hello接口
     *
     * 但是，这个调用可能会失败。
     * 我们在这个方法上添加@HystrixCommand注解，配置fallbackMethod属性，这个属性表示该方法调用失败时的临时代替方法。
     * @return
     */
    @HystrixCommand(fallbackMethod = "error")
    public String hello(){
        return restTemplate.getForObject("http://eureka-provider/hello",String.class);
    }



    /**
     * 通过注解实现请求异步调用
     * @return
     */
    @HystrixCommand(fallbackMethod = "error")
    public Future<String> hello2(){
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                return restTemplate.getForObject("http://eureka-provider/hello",String.class);
            }
        };
    }

    /**
     * Hystrix 异常处理
     * 执行这段代码时会抛出异常，会进行服务降级，进入到error方法中，在error方法中，可以获取异常的详细信息。
     * @return
     */
    @HystrixCommand(fallbackMethod = "error1")
    public String hello3(){
        int i=1/0;
        return restTemplate.getForObject("http://eureka-provider/hello",String.class);
    }

    public String error1(Throwable throwable){
        return "error:"+throwable.getMessage();
    }

    /**
     * 这种配置表示当hello方法抛出ArithmeticException异常时，不要进行服务降级，直接将错误抛出。
     * ignoreExceptions = ArithmeticException.class
     * @return
     */
    @HystrixCommand(fallbackMethod = "error")
    public String hello4(){
        int i=1/0;
        return restTemplate.getForObject("http://eureka-provider/hello",String.class);
    }
    @HystrixCommand(fallbackMethod = "error2")
    @CacheResult//这个注解表示该方法的请求结果会被缓存起来，默认情况下，缓存的key就是方法的参数，缓存的value就是方法的值。
    public String hello5(String name){
        return restTemplate.getForObject("http://eureka-provider/hello7?name={1}",
                String.class,name);

    }
    @HystrixCommand
    @CacheRemove(commandKey = "hello5")
    public String deleteUserByName(String name){
        return null;
    }
    /**
     * 注意，这个方法名字要和fallbackMethod一致，方法返回值也要和对应的方法一致。
     * @return
     */
    public String error(){
        return restTemplate.getForObject("http://eureka-provider/hello",String.class);
    }
    public String error2( ){
        return "error";
    }
}
