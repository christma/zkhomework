package nx.zk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import nx.zk.DataNode;
import org.omg.CORBA_2_3.portable.InputStream;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
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

    public static ConcurrentHashMap<String, DataNode> readJsonFile(String path) {
        BufferedReader reader = null;
        ConcurrentHashMap<String, DataNode> nodes = new ConcurrentHashMap<String, DataNode>();
        List<DataNode> list = new LinkedList<DataNode>();
        try {
            reader = new BufferedReader(new FileReader(path));
            Gson gson = new GsonBuilder().create();
            nodes = gson.fromJson(reader, ConcurrentHashMap.class);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return nodes;
    }


    public static void main(String[] args) {
        ConcurrentHashMap<String, DataNode> stringDataNodeConcurrentHashMap = readJsonFile("./zkSource/log.json");
        for (String key : stringDataNodeConcurrentHashMap.keySet()){
            System.out.println(stringDataNodeConcurrentHashMap.get(key));
        }

    }
}
