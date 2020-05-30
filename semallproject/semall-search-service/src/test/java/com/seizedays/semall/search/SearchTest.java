package com.seizedays.semall.search;

import com.seizedays.semall.beans.PmsSearchSkuInfo;
import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.services.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchTest {

    @Resource
    SkuService skuService;  //查询mysql

    @Autowired
    JestClient jestClient;

    @Test
    //用于向es中添加数据
    public void put() throws IOException {

        //查询mysql数据
        List<PmsSkuInfo> pmsSkuInfos = skuService.getAllSku();


        //转化为es的数据结构
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo, pmsSearchSkuInfo);

            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
        }


        //导入es
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {

            Index put = new Index.Builder(pmsSearchSkuInfo).index("semall0105pms").type("_doc").id(Long.toString(pmsSearchSkuInfo.getId())).build();
            jestClient.execute(put);
        }

    }

    @Test
    //用于在es中查询数据
    //用api执行复杂查询
    public void contextLoads() throws IOException {
        String queryStr = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"filter\":[\n" +
                "        {\n" +
                "             \n" +
                "          \"term\": {\n" +
                "            \"skuAttrValueList.valueId\": \"48\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"term\": {\n" +
                "            \"skuAttrValueList.valueId\": \"42\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "      , \"must\": [\n" +
                "        {\n" +
                "          \"match\":{\n" +
                "            \"skuName\": \"seizedays\"\n" +
                "          }\n" +
                "        }\n" +
                "          \n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

        Search search = new Search.Builder(queryStr).addIndex("semall0105pms").build();
        SearchResult searchResult = jestClient.execute(search);

        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;
            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
            System.out.println(pmsSearchSkuInfo);
        }
    }


    @Test
    //用于在es中查询数据
    //使用api来简化查询语句
    public void contextLoadsTwo() throws IOException {
        //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //filter
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", "48");
        TermQueryBuilder termQueryBuilder2 = new TermQueryBuilder("skuAttrValueList.valueId", "42");

        boolQueryBuilder.filter(termQueryBuilder);
        boolQueryBuilder.filter(termQueryBuilder2);

        //must
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", "seizedays");
        boolQueryBuilder.must(matchQueryBuilder);
        //query
        searchSourceBuilder.query(boolQueryBuilder);


        //from
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
        //highlight
        searchSourceBuilder.highlighter(null);

        String queryStr = searchSourceBuilder.toString();
        System.out.println("执行语句： " + queryStr);

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

        Search search = new Search.Builder(queryStr).addIndex("semall0105pms").build();
        SearchResult searchResult = jestClient.execute(search);

        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;
            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
            System.out.println(pmsSearchSkuInfo);
        }
    }

}
