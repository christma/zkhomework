package nx.zk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import nx.zk.DataNode;
import org.omg.CORBA_2_3.portable.InputStream;

import java.io.*;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GsonUtils {

    /**
     * 将object对象转成json格式字符串
     */
    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.serializeNulls().create();
        return gson.toJson(object);

    }

    /**
     * 将json 文件中的数据读出
     */

    public static ConcurrentHashMap<String, Object> readJsonFile(String path) {
        ConcurrentHashMap<String, Object> nodes = new ConcurrentHashMap<String, Object>();
        try {
            nodes = JSON.parseObject(readJson(path), ConcurrentHashMap.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return nodes;
    }

    //读取json文件
    public static String readJson(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        ConcurrentHashMap<String, Object> stringDataNodeConcurrentHashMap = readJsonFile("./zkSource/log.json");
        for (String key : stringDataNodeConcurrentHashMap.keySet()) {
            System.out.println(stringDataNodeConcurrentHashMap.get(key));
        }

    }
}
