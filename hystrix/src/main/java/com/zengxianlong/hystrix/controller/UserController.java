package com.zengxianlong.hystrix.controller;

import com.zengxianlong.hystrix.bean.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求合并
 * 如果consumer中，频繁的调用provider中的同一个接口，在调用时，只是参数不一样，那么这样情况下
 * 我们就可以将多个请求合并成一个，这样可以有效提高请求发送的效率
 *
 * @author long
 * @date 2022-02-26 22:03
 */
@RestController
public class UserController {
    @GetMapping("/user/{ids}")//假设consumer 传过来的多个id格式是1，2，3，4...
    public List<User> getUserByIds(@PathVariable String ids){
         String[] split = ids.split(",");
         ArrayList<User> users = new ArrayList<>();
         for (String s:split){
             User user=new User();
             user.setId(Integer.parseInt(s));
             users.add(user);
         }
         return users;
    }

}
