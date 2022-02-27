package com.zengxianlong.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.zengxianlong.hystrix.bean.User;
import com.zengxianlong.hystrix.service.UserService;

import java.util.List;

/**
 * TOOD
 *
 * @author long
 * @date 2022-02-26 22:32
 */
public class UserBatchCommand extends HystrixCommand<List<User>> {
    private List<Integer> ids;
    private UserService userService;
    public UserBatchCommand(List<Integer> ids,UserService userService){
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("batchCmd"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("batchKey")));
        this.ids=ids;
        this.userService=userService;
    }

    @Override
    protected List<User> run() throws Exception {
        return userService.getUserByIds(ids);
    }
}
