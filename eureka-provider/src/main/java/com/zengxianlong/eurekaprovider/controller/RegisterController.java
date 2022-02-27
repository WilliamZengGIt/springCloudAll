package com.zengxianlong.eurekaprovider.controller;

import com.zengxianlong.eurekaprovider.dto.User;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * TOOD
 *
 * @author long
 * @date 2022-01-23 16:40
 */
@Controller
public class RegisterController {
    /**
     * 这里的post接口，响应一定是302，否则postForLocation无效。
     * 注意，重定向的地址，一定要写成绝对路径，不要写相对路径，否则consumer中调用时会出问题。
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String register(User user){
        return "redirect:http://eureka-provider/loginPage?name="+user.getName();
    }
    @GetMapping("/loginPage")
    @ResponseBody
    public String loginPage(String name){
        return "loginPage:"+name;
    }
    @GetMapping("/user")
    @ResponseBody
    public void user(Integer id){
        System.out.println(id);
    }
    @DeleteMapping("/user1")
    public void deleteUser1(Integer id){
        System.out.println(id);
    }
    @DeleteMapping("/user2/{id}")
    public void deleteUser2(@PathVariable Integer id){
        System.out.println(id);
    }

}
