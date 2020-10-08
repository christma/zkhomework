package nx.zk;


import nx.zk.utils.GsonUtils;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.omg.CORBA_2_3.portable.InputStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataTree implements DataTreeProtocol, SnapShot {

    private DataNode rootNode = new DataNode();

    private final String rootZooKeeper = "./zkSource";

    public final ConcurrentHashMap<String, DataNode> nodes = new ConcurrentHashMap<String, DataNode>();


    public DataTree() {

        nodes.put(rootZooKeeper, rootNode);


    }


    public void createNode(String path, DataNode node) {
        int lastSlash = path.lastIndexOf('/');
        String parentName = path.substring(0, lastSlash);
        node.setParent(parentName);
        if (nodes.contains(parentName)) {
            throw new IllegalArgumentException("没有父类节点，请先创建。");
        }
        DataNode fatherNode = nodes.get(parentName);
        fatherNode.addChild(path);

        nodes.put(path, node);
    }


    public boolean deleteNode(String path) {

        int lastSlash = path.lastIndexOf('/');
        String parentName = path.substring(0, lastSlash);
        String childName = path.substring(lastSlash + 1);
        DataNode delNode = nodes.get(path);
        if (delNode.getChildren() != null) {
            throw new IllegalArgumentException("该节点有子节点，不能被删除");
        }

        nodes.remove(path);

        DataNode parent = nodes.get(parentName);
        if (parent == null) {
            throw new IllegalArgumentException("该节点不存在");
        }
        synchronized (parent) {
            return parent.removeChild(path);
        }


    }

    public DataNode getNode(String path) {
        return nodes.get(path);
    }

    public Set<String> getChildren(String path) {
        DataNode dataNode = nodes.get(path);
        return dataNode.getChildren();
    }

    public DataNode setNode(String path, String data) {
        if (!nodes.containsKey(path)) {
            throw new IllegalArgumentException("该节点不存在");
        }
        DataNode needChange = nodes.get(path);
        needChange.setData(data);
        return needChange;
    }


    public void treeShow(DataTree dataTree) {
        Queue<String> queue = new LinkedList<String>();
        queue.add(rootZooKeeper);
        while (!queue.isEmpty()) {
            String poll = queue.poll();
            DataNode dn = dataTree.nodes.get(poll);
            System.out.println(dn);
            if (dn.getChildren() != null) {
                for (String child : dn.getChildren()) {
                    queue.add(child);
                }
            }
        }

    }


    public void serialize(DataTree dataTree) {

        String s = GsonUtils.toJson(nodes);

        try {
            writeFileContext(s, rootZooKeeper + "/log.json");
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

    public DataTree deserialize(String fliePath) {
        ConcurrentHashMap<String, DataNode> map = GsonUtils.readJsonFile(fliePath);

        for (String key : map.keySet()) {
            nodes.put(key, map.get(key));
        }

        return this;
    }

    public void close() throws IOException {

    }
}
