package com.seizedays.semall.cart.services.impl;

import com.alibaba.fastjson.JSON;
import com.seizedays.semall.beans.OmsCartItem;
import com.seizedays.semall.cart.mappers.OmsCartItemMapper;
import com.seizedays.semall.services.CartService;
import com.seizedays.semall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Override
    public OmsCartItem getCartsByUser(Long memberId, Long skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        if (null != omsCartItem.getMemberId()) {
            omsCartItemMapper.insert(omsCartItem);
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {
        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("id", omsCartItemFromDb.getId());

        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDb, e);

    }

    @Override
    public void flushCartCache(Long memberId) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("memberId", memberId);
        example.orderBy("id").asc();
        List<OmsCartItem> cartItems = omsCartItemMapper.selectByExample(example);

        //同步到redis缓存中
        Jedis jedis = redisUtil.getJedis();
        Map<String, String> map = new HashMap<>();

        for (OmsCartItem cartItem : cartItems) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            map.put(cartItem.getProductSkuId().toString(), JSON.toJSONString(cartItem));
        }
        try {
            jedis.del("User:" + memberId + ":cart");
            jedis.hmset("User:" + memberId + ":cart", map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

    }

    @Override
    public List<OmsCartItem> cartList(Long memberId) {

        Jedis jedis = redisUtil.getJedis();
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        try {

            List<String> hvals = jedis.hvals("User:" + memberId + ":cart");
            for (String hval : hvals) {
                OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
                omsCartItems.add(omsCartItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return omsCartItems;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("memberId", omsCartItem.getMemberId()).andEqualTo("productSkuId", omsCartItem.getProductSkuId());

        omsCartItemMapper.updateByExampleSelective(omsCartItem, e);
        //缓存同步
        flushCartCache(omsCartItem.getMemberId());
    }


}
