package com.seizedays.semall.controller;

import com.seizedays.semall.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * Redisson实现了JUC的可重入锁 并且可以在分布式的redis环境下使用
 */

@Controller
public class RedissonTestController {

    //测试redisson的连接

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedissonClient redissonClient;

    @RequestMapping("/testRedisson")
    @ResponseBody
    public String TestRedisson(){
        //Redis的可重入锁 类似有 JUC的锁

        RLock lock = redissonClient.getLock("lock");

        Jedis jedis = redisUtil.getJedis();
        //加锁
        lock.lock(30L, TimeUnit.SECONDS);
        try {
            String v = jedis.get("k");
            if (StringUtils.isBlank(v)){
                v = "1";
            }
            System.out.println("->" + v);
            jedis.set("k", Integer.parseInt(v) + 1 + "");

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }finally {
            jedis.close();
            lock.unlock();
        }

        return "success";
    }

}
