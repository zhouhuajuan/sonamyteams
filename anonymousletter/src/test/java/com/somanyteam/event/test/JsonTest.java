package com.somanyteam.event.test;

import com.somanyteam.event.dto.result.report.UserIdJsonResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @program: somanyteams
 * @description:
 * @author: 周华娟
 * @create: 2021-11-10 20:09
 **/
public class JsonTest {

    @Test
    public void test(){
//        String str = "['123','456']";
//        JSONArray jsonarray = JSONArray.fromObject(str);
//        System.out.println(jsonarray);
//
//        List list = (List)net.sf.json.JSONArray.toCollection(jsonarray, String.class);
//        Iterator it = list.iterator();
//        while(it.hasNext()){
//            String defendant = (String) it.next();
//            System.out.println(defendant);
//        }


//        List list = (List)net.sf.json.JSONArray.toCollection(jsonarray, UserIdJsonResult.class);
//
//        Iterator it = list.iterator();
//        while(it.hasNext()){
//            UserIdJsonResult defendant = (UserIdJsonResult) it.next();
//
//            System.out.println(defendant);
//            System.out.println(defendant.getUserId());
//        }
//
//        String content = "dajuan: \r\n"+"  hello，您由于"+
//                "在xxx的相关内容中涉嫌违规，现对您进行举报。\r\n"+new Date();
//        System.out.println(content);

        List<String> list2 = new ArrayList<>();
        list2.add("123");
        list2.add("456");
        String json = com.alibaba.fastjson.JSONArray.toJSONString(list2);
        System.out.println(json);
    }
}
