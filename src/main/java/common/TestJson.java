package common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.util.*;

/**
 * Created by yxcui on 2017/8/17.
 */
public class TestJson {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json;
        System.out.println("字符串_____________");
        json = mapper.writeValueAsString("asad");
        System.out.println(json);
        System.out.println(mapper.readValue(json,String.class));

        System.out.println("Map___________");
        Map<String,Object> map = new HashMap<>();
        map.put("11","1");
        map.put("22",2);
        json = mapper.writeValueAsString(map);
        System.out.println(json);
        System.out.println(mapper.readValue(json,new TypeReference<HashMap<String,Object>>(){}).toString());
        System.out.println(mapper.readValue(json,Map.class).toString());

        System.out.println("MapList___________");
        List<Map<String,Object>> mapList = new ArrayList<>();
        mapList.add(map);
        mapList.add(map);
        json = mapper.writeValueAsString(mapList);
        System.out.println(json);
        System.out.println(mapper.readValue(json,new TypeReference<ArrayList<HashMap<String,Object>>>(){}).toString());
        List<LinkedHashMap<String,Object>> mapList2 = mapper.readValue(json,List.class);
        System.out.println(mapList2.toString());
        System.out.println(mapper.readValue(json,List.class).toString());


        System.out.println("自定义对象测试___________");
        A aa = new A();
        aa.setName("张大宝");
        json = mapper.writeValueAsString(aa);
        System.out.println(json);
        System.out.println(mapper.readValue(json, A.class).toString());

        System.out.println("自定义对象List测试___________");
        List<A> list = new ArrayList<>();
        list.add(aa);
        list.add(aa);
        json = mapper.writeValueAsString(list);
        System.out.println(json);
        System.out.println(mapper.readValue(json, new TypeReference<List<A>>(){}).toString());
        System.out.println(mapper.readValue(json,List.class).toString());


    }

    public static class A{
        private String name;
        A(){};

        public A(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "A{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}

