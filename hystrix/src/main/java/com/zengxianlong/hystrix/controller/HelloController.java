package com.zengxianlong.hystrix.controller;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.sun.org.apache.xpath.internal.functions.FuncTrue;
import com.zengxianlong.hystrix.bean.User;
import com.zengxianlong.hystrix.command.Hello2Command;
import com.zengxianlong.hystrix.command.HelloCommand;
import com.zengxianlong.hystrix.command.UserCollapseCommand;
import com.zengxianlong.hystrix.service.HelloService;
import com.zengxianlong.hystrix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * TOOD
 *
 * @author long
 * @date 2022-02-26 15:00
 */
@RestController
public class HelloController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserService userService;
    @Autowired
    HelloService helloService;
    @GetMapping("/hello")
    public String hello(){
        return helloService.hello();
    }
    @GetMapping("/hello2")
    public void hello2(){
        HelloCommand helloCommand=new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory
                .asKey("zengxianlong")),restTemplate);
        String execute = helloCommand.execute();//直接执行。
        System.out.println(execute);
        HelloCommand helloCommand2=new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory
                .asKey("zengxianlong")),restTemplate);
        try {
             Future<String> queue = helloCommand2.queue();
            final String s = queue.get();
            System.out.println(s);//先入队，后执行。
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/hello3")
    public void hello3(){
        final Future<String> hello2 = helloService.hello2();
        try {
            String s = hello2.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    @GetMapping("/hello4")
    public void  hello4(){
         String s = helloService.hello4();
    }

    //请求缓存s
    @GetMapping("/hello5")
    public void hello5(){
        HystrixRequestContext ctx=HystrixRequestContext.initializeContext();
        //第一请求完，数据已经缓存下来了。
        String zengxianlong= helloService.hello5("zengxianlong");
        //删除数据，同时缓存中的数据也会被删除
        helloService.deleteUserByName("zengxianlong");
        //第二次请求时，虽然参数还是zengxianlong，但是缓存数据已经没了，所以这一次，provider还是会收到请求。
          zengxianlong = helloService.hello5("zengxianlong");
          ctx.close();
    }
    @GetMapping("/hello6")
    public void hello6(){
        final HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
        Hello2Command hello2Command=new Hello2Command(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("zengxianlong")),restTemplate,"zengxianlong");
        String execute=hello2Command.execute();//直接执行
        System.out.println(execute);
        Hello2Command hello3Command=new Hello2Command(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("zengxianlong")),restTemplate,"zengxianlong");
        Future<String> queue = hello3Command.queue();
        try {
            final String s = queue.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        hystrixRequestContext.close();
    }

    @GetMapping("/hello7")
    public void  hello7() throws ExecutionException,InterruptedException{
         HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
         UserCollapseCommand cmd1 = new UserCollapseCommand(userService, 99);
         UserCollapseCommand cmd2 = new UserCollapseCommand(userService, 98);
         UserCollapseCommand cmd3 = new UserCollapseCommand(userService, 97);
         UserCollapseCommand cmd4 = new UserCollapseCommand(userService, 96);
         Future<User> q1 = cmd1.queue();
         Future<User> q2 = cmd2.queue();
         Future<User> q3 = cmd3.queue();
         Future<User> q4 = cmd4.queue();
         User u1=q1.get();
         User u2=q2.get();
         User u3=q3.get();
         User u4=q4.get();
         System.out.println(u1);
         System.out.println(u2);
         System.out.println(u3);
         System.out.println(u4);
         ctx.close();
    }
    @GetMapping("/hello8")
    public void hello8() throws ExecutionException, InterruptedException {
         HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
         Future<User> q1=userService.getUserById(99);
         Future<User> q2 = userService.getUserById(98);
         Future<User> q3 = userService.getUserById(97);
         User u1 = q1.get();
         User u2 =  q2.get();
         User u3 = q3.get();
         System.out.println(u1);
         System.out.println(u2);
         System.out.println(u3);
         Thread.sleep(2000);
         Future<User> q4 = userService.getUserById(96);
         User u4 = q4.get();
         System.out.println(u4);
         ctx.close();
    }

}
