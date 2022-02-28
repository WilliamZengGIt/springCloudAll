package com.zengxianlong.hystrix.command;

import com.ctc.wstx.shaded.msv_core.reader.xmlschema.IdentityConstraintState;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import com.zengxianlong.hystrix.bean.User;
import com.zengxianlong.hystrix.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TOOD
 *
 * @author long
 * @date 2022-02-26 22:35
 */
public class UserCollapseCommand extends HystrixCollapser<List<User>,User,Integer> {
    private UserService userService;
    private Integer id;
    public UserCollapseCommand(UserService userService,Integer id){
        super(HystrixCollapser.Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("UserCollapseCommand")).andCollapserPropertiesDefaults(
                HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(200)
        ));
        this.userService=userService;
        this.id=id;

    }

    /**
     * 请求参数
     * @return
     */
    @Override
    public Integer getRequestArgument() {
        return id;
    }

    /**
     * 请求合并的方法
     * @param collection
     * @return
     */
    @Override
    protected HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User,
            Integer>> collection) {
        List<Integer> ids = new ArrayList<>(collection.size());
        collection.forEach(
                userIntegerCollapsedRequest -> {
                    ids.add(userIntegerCollapsedRequest.getArgument());
                }

        );
        return new UserBatchCommand(ids,userService);
    }

    /**
     * 将请求结果分发
     * @param users
     * @param collection
     */
    @Override
    protected void mapResponseToRequests(List<User> users,
                                         Collection<CollapsedRequest<User, Integer>> collection) {
        int count=0;
        for (CollapsedRequest<User,Integer> request:collection){
            request.setResponse(users.get(count++));
        }
    }


}
