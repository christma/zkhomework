package nx.zk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import nx.zk.utils.GsonUtils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileSnap implements SnapShot {

    @Override
    public void serialize(DataTree dataTree) {


        String s = GsonUtils.toJson(dataTree.nodes);

        try {
            writeFileContext(s, "./zkSource/log.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(s);


    }


    public static void writeFileContext(String strings, String path) throws Exception {
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        writer.write(strings);
        writer.flush();
        writer.close();
    }

    @Override
    public DataTree deserialize() {
        String fliePath = "./zkSource/log.json";
        ConcurrentHashMap<String, Object> map = GsonUtils.readJsonFile(fliePath);
        DataTree dataTree = new DataTree();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            DataNode dataNode = JSON.parseObject(entry.getValue().toString(), DataNode.class);
            dataTree.createNode(entry.getKey(), dataNode);
        }

        return dataTree;
    }

    @Override
    public void close() throws IOException {

    }

    public static void main(String[] args) {
        new FileSnap().deserialize().treeShow();

    }

}
