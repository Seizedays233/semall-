package com.seizedays.semall.passport.service.impl;

import com.alibaba.fastjson.JSON;
import com.seizedays.semall.beans.UmsMember;
import com.seizedays.semall.beans.UmsMemberReceiveAddress;
import com.seizedays.semall.services.UserService;
import com.seizedays.semall.passport.mapper.UmsMemberReceiveAddressMapper;
import com.seizedays.semall.passport.mapper.UserMemberMapper;
import com.seizedays.semall.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMemberMapper userMemberMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUSer() {
        return userMemberMapper.selectAll();
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(Long memberId) {


        //封装参数对象
//        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
//        umsMemberReceiveAddress.setMemberId(memberId);
//
//        List<UmsMemberReceiveAddress> addressesList = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);

        //用Example封装的用法
        Example example = new Example(UmsMemberReceiveAddress.class);
        example.createCriteria().andEqualTo("memberId", memberId);
        List<UmsMemberReceiveAddress> addressesList = umsMemberReceiveAddressMapper.selectByExample(example);

        return addressesList;
    }

    @Override
    public UmsMember login(UmsMember umsMember) {

        //先读取redis redis没有再去找mysql
        try (Jedis jedis = redisUtil.getJedis()) {
            String password = jedis.get("user:" + umsMember.getUsername() + ":password");

            if (StringUtils.isNotBlank(password) && password.equals(umsMember.getPassword())) {
                //密码正确 返回缓存的用户信息
                String umsMemberStr = jedis.get("user:" + umsMember.getUsername() + ":userInfo");
                return JSON.parseObject(umsMemberStr, UmsMember.class);
            }

            //若redis读取失败 从mysql读取
            UmsMember umsMemberFromDB = loginFromDB(umsMember);
            if (null != umsMemberFromDB) {
                jedis.setex("user:" + umsMember.getUsername() + ":password", 60 * 60 * 2, umsMember.getPassword());
                jedis.setex("user:" + umsMember.getUsername() + ":userInfo", 60 * 60 * 2, JSON.toJSONString(umsMemberFromDB));
            }

            return umsMemberFromDB;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public void addUserToken(String token, Long memberId) {
        try(Jedis jedis = redisUtil.getJedis()){
            jedis.setex("user:" + memberId + ":token", 60*60*2, token);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void addUser(UmsMember umsMember) {
        userMemberMapper.insert(umsMember);
    }

    @Override
    public UmsMember getUserBySourceUid(Long sourceUid) {
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceUid(sourceUid);
        return userMemberMapper.selectOne(umsMember);
    }

    @Override
    public UmsMemberReceiveAddress getReceiveAddressByAddressId(String receiveAddressId) {

        return umsMemberReceiveAddressMapper.selectByPrimaryKey(receiveAddressId);
    }


    private UmsMember loginFromDB(UmsMember umsMember) {
        //防止可能出现重复用户名的bug造成查询结果不唯一的情况
        List<UmsMember> umsMembers = userMemberMapper.select(umsMember);
        if (null != umsMembers){
            return umsMembers.get(0);
        }
        return null;
    }
}
