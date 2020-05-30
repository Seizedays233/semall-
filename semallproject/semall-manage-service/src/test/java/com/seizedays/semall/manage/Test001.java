package com.seizedays.semall.manage;

import com.alibaba.fastjson.JSON;
import com.seizedays.semall.beans.PmsBaseAttrInfo;
import com.seizedays.semall.beans.PmsBaseAttrValue;
import com.seizedays.semall.beans.PmsSkuImage;
import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.manage.mappers.*;
import com.seizedays.semall.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class Test001 {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Test
    public void spuListTest() {
        Long catalog3Id = 61L;
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);

        List<PmsBaseAttrInfo> attrInfoList = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);

        for (PmsBaseAttrInfo baseAttrInfo : attrInfoList) {

            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);

            baseAttrInfo.setAttrValueList(attrValueList);
        }

        System.out.println(attrInfoList);
    }

    @Test
    public void contectTest() {
        Jedis jedis = redisUtil.getJedis();
        System.out.println(jedis);
    }

    @Test
    public void JedisTest() {
        Long skuId = 130L;

        //连接redis缓存
        Jedis jedis = redisUtil.getJedis();
        //缓存中如果没有 查询mysql
        //访问数据库之前 先设置分布式锁--防止缓存穿透
        SetParams setParams = new SetParams();
        setParams.ex(10);
        setParams.nx();

        for (int i = 0; i < 10; i++) {
            String ok = jedis.set("sku:" + skuId + ":lock", "1", setParams);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第" + i + "次测试结果：" + ok);
        }

        //关闭redis连接
        jedis.close();

    }
}
