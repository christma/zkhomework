package nx.zk;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import nx.zk.utils.GsonUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileSnap implements SnapShot {


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

    public DataTree deserialize() {
        String fliePath = "./zkSource/log.json";
        ConcurrentHashMap<String, Object> map = GsonUtils.readJsonFile(fliePath);
        DataTree dataTree = new DataTree();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            //            dataTree.createNode(key, new DataNode(map.get(key).getData()));
            DataNode dataNode = (DataNode) entry.getValue();
            System.out.println(dataNode instanceof DataNode);
        }

        return dataTree;
    }

    public void close() throws IOException {

    }

    public static void main(String[] args) {
        new FileSnap().deserialize();

    }

}
