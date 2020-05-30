package com.seizedays.semall.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.seizedays.semall.beans.PmsSkuAttrValue;
import com.seizedays.semall.beans.PmsSkuImage;
import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.beans.PmsSkuSaleAttrValue;
import com.seizedays.semall.manage.mappers.PmsSkuAttrValueMapper;
import com.seizedays.semall.manage.mappers.PmsSkuImageMapper;
import com.seizedays.semall.manage.mappers.PmsSkuInfoMapper;
import com.seizedays.semall.manage.mappers.PmsSkuSaleAttrValueMapper;
import com.seizedays.semall.services.SkuService;
import com.seizedays.semall.util.RedisUtil;
import com.seizedays.semall.utils.SnowFlake;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        //插入平台属性
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

        Long skuId = pmsSkuInfo.getId();

        //插入平台base属性关联
        List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : pmsSkuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        //插入商品spu销售属性关联
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : pmsSkuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        //插入图片信息
        List<PmsSkuImage> imageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : imageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }


        return "success";
    }

    public PmsSkuInfo getSkuByIdFromDB(Long skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(skuId);
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);

        List<PmsSkuImage> imageList = pmsSkuImageMapper.select(pmsSkuImage);
        //添加图片列表
        pmsSkuInfo.setSkuImageList(imageList);

        return pmsSkuInfo;
    }

    @Override
    public PmsSkuInfo getSkuById(Long skuId) {
        PmsSkuInfo pmsSkuInfo;

        //连接redis缓存
        Jedis jedis = redisUtil.getJedis();

        //查询缓存
        String skuKey = "sku:" + skuId + ":info";
        String skuJson = jedis.get(skuKey);
        if (StringUtils.isNotBlank(skuJson)) {
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        } else {

            //缓存中如果没有 查询mysql
            //访问数据库之前 先设置分布式锁--防止缓存穿透
            SetParams setParams = new SetParams();
            setParams.ex(10);
            setParams.nx();
            SnowFlake snowFlake = new SnowFlake(3L,3L);
            String lockId =  String.valueOf(snowFlake.nextId());
            String oK = jedis.set("sku:" + skuId + ":lock", lockId, setParams);

            if (StringUtils.isNotBlank(oK) && oK.equals("OK")) {

                pmsSkuInfo = getSkuByIdFromDB(skuId);

                //mysql查询结果存入redis
                if (null != pmsSkuInfo) {
                    jedis.set(skuKey, JSON.toJSONString(pmsSkuInfo));
                } else {
                    // 数据库中不存在该sku  为了防止缓存穿透 将null值或一个空字符串设置给redis
                    //将这个空串的过期时间设置为5min
                    jedis.setex(skuKey, 60 * 5, "");
                }

                //访问到mysql并执行完操作之后 将分布式锁释放
                // 用lockToken确认删除的是自己的sku锁 避免因自己的锁过期而错删其他线程的分布式锁
                String lockToken = jedis.get("sku:" + skuId + ":lock");
                if(StringUtils.isNotBlank(lockToken) && lockToken.equals(lockId)) {
                    // jedis.eval("lua"); 可以使用lua脚本 在查询到key的同时删除该key 防止高并发下的意外发生
                    jedis.del("sku:" + skuId + ":lock");
                }

            } else {
                // 未获取到锁，自旋（该线程在睡眠几秒后，重新尝试访问本方法）
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuById(skuId);
            }
        }

        //关闭redis连接
        jedis.close();

        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(Long spuId) {
        return pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(spuId);
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            Long skuId = pmsSkuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);

            pmsSkuInfo.setSkuAttrValueList(select);

        }
        return pmsSkuInfoList;
    }

    @Override
    public boolean checkPrice(Long productSkuId, BigDecimal productPrice) {
        boolean b = false;

        PmsSkuInfo pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(productSkuId);
        BigDecimal price = pmsSkuInfo.getPrice();
        if (price.compareTo(productPrice) == 0){
            b = true;
        }

        return b;
    }
}
