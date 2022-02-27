package com.zengxianlong.eurekaconsumer.controller;


import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * TOOD
 *
 * @author long
 * @date 2022-01-23 14:41
 */
@RestController
public class ConsumerController {
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;
    /**
     * 这种写法是将provider提供的服务直接调用，相当于调用url地址，整个过程不会涉及到eureka-server服务的
     * 利用了httpUrlConnection 发起亲求，请求中provider的地址是写死了，意味着provider和consumer高度绑定在一起
     * 一旦provider的服务变更了，consumer也要发生改变。
     * 不符合微服务的思想
     * 需要改造它。我们可以借助Eureka Client提供的DiscoveryClient工具，利用这个工具，我们可以根据服务名从Eureka Server
     * 上查询到一个服务的详细信息。
     * 改造后代码 hello2
     * @return
     */
    @GetMapping("/hello1")
    public String hello1(){
        HttpURLConnection con=null;
        try {
            URL url=new URL("http://localhost:1113/hello");
            con= (HttpURLConnection)url.openConnection();
            if (con.getResponseCode()==200){
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String s = br.readLine();
                br.close();
                return s;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     *DiscoveryClient 查询到的服务列表是一个列表。
     * 因为服务在部署的过程中，可能是集群化部署，集合中的每一项就是一个实例
     * @return
     */
    @GetMapping("/hello2")
    public  String hello2(){
        List<ServiceInstance> list = discoveryClient.getInstances("eureka-provider");
        System.out.println(JSON.toJSONString(list));
        ServiceInstance instance = list.get(0);
        String host=instance.getHost();
        int port=instance.getPort();
        StringBuffer sb = new StringBuffer();
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
        String s = restTemplate.getForObject(sb.toString(), String.class);
       /* HttpURLConnection con=null;
        try {
          URL url = new URL(sb.toString());
          con=(HttpURLConnection) url.openConnection();
          if (con.getResponseCode()==200){
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String s = br.readLine();
                br.close();
                return s;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return s;
    }
    int count=0;
    @GetMapping("/hello3")
    public String hello3(){
        List<ServiceInstance> list = discoveryClient.getInstances("eureka-provider");
        //实现线性负载均衡
        ServiceInstance instance = list.get((count++) % list.size());
        System.out.println(JSON.toJSONString(list));
        String host = instance.getHost();
        int port=instance.getPort();
        StringBuffer sb = new StringBuffer();
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
        System.out.println(sb.toString());
        return restTemplate.getForObject("http://eureka-provider/hello",String.class);
    }

    @GetMapping("/hello9")
    public void  hello9(){
        restTemplate.delete("http://eureka-provider/user1?id={1}",99);
        restTemplate.delete("http://eureka-provider/user2/{1}",99);
    }


}
