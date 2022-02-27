package com.zengxianlong.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.zengxianlong.hystrix.bean.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

/**
 * TOOD
 *
 * @author long
 * @date 2022-02-26 22:22
 */
@Service
public class UserService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCollapser(batchMethod = "getUsersByIds",collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds",value = "200")
    })
    public Future<User> getUserById(Integer id){
        return null;
    }
    @HystrixCommand
    public List<User> getUserByIds(List<Integer> ids){
        final User[] users = restTemplate.getForObject("http://eureka-provider/user/{1}", User[].class,
                StringUtils.join(ids, ','));
        return Arrays.asList(users);
    }
}
