package com.zengxianlong.eurekaprovider.controller;

import com.zengxianlong.eurekaprovider.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * TOOD
 *
 * @author long
 * @date 2022-01-23 14:31
 */
@RestController
public class HelloController {
    @Value("${server.port}")
    Integer port;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello(){
        return "hello zengxianlong:"+port;
    }
    @GetMapping("/hello2")
    public String hello2(String name){
        return "hello "+name;
    }

    /**
     * restTemplate.getForObject 返回的是一个对象，这个对象就是服务端返回的具体值。
     * getForEntity返回的是一个ResponseEntity，这个ResponseEntity中除了服务端返回的具体数据外，还保留了Http响应头的数据。
     * getForObject直接拿到了服务的返回值
     * getForEntity不仅仅拿到服务的返回值，还拿到http响应的状体码。
     */
    @GetMapping("/hello3")
    public void  hello3(){
        String s1=restTemplate.getForObject("http://eureka-provider/hello2?name={1}",String.class,"zengxianlong");
        System.out.println(s1);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://provider/helo2", String.class, "zengxianlong");
        String body=responseEntity.getBody();
        System.out.println("body:"+body);
        HttpStatus statusCode=responseEntity.getStatusCode();
        System.out.println("HttpStatus:"+statusCode);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        System.out.println("statusCodeValue:"+statusCodeValue);
        HttpHeaders headers = responseEntity.getHeaders();
        Set<String> keySet = headers.keySet();
        System.out.println("------header------");
        for (String s:keySet){
            System.out.println(s+" "+headers.get(s));
        }
    }
    @GetMapping("/hello4")
    public void hello4() throws UnsupportedEncodingException {
        String s1=restTemplate.getForObject("http://eureka-provider/hello2?name={1}",String.class,"zengxianlong");
        System.out.println(s1);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","zengxianlong");
        s1=restTemplate.getForObject("http://eureka-provider/hello2?name={name}",String.class,map);
        System.out.println(s1);
        String url="http://eureka-provider/hello2?name="+ URLEncoder.encode("张三","UTF-8");
        URI uri = URI.create(url);
        s1=restTemplate.getForObject(uri,String.class);
        System.out.println(s1);
    }
    @PostMapping("/user1")
    public User addUser1(User user){
        return user;
    }
    @PostMapping("/user2")
    public User addUser2(@RequestBody User user){
        return user;
    }


    @GetMapping("/hello5")
    public void hello5(){
         LinkedMultiValueMap<String , Object> map = new LinkedMultiValueMap<>();
         map.add("username","zengxianlong");
         map.add("id",18);
         User user=restTemplate.postForObject("http://eureka-provider/user1",map, User.class);
         System.out.println(user);
         user.setId(19);
         user=restTemplate.postForObject("http://eureka-provider/user2",user,User.class);
         System.out.println(user);
    }
    @GetMapping("/hello6")
    public void hello6(){
         LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
         map.add("name","zengxianlong");
         map.add("id",18);
         URI uri = restTemplate.postForLocation("http://eureka-provider/register", map);
         String s=restTemplate.getForObject(uri,String.class);
         System.out.println(s);
    }

    @GetMapping("/hello7")
    public String hello7(String name){
        System.out.println(new Date()+">>>"+name);
        return "hello"+name;
    }



}
