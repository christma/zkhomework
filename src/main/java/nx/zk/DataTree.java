package nx.zk;


import nx.zk.utils.GsonUtils;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.mortbay.util.StringUtil;
import org.omg.CORBA_2_3.portable.InputStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataTree implements DataTreeProtocol, SnapShot {

    private static DataNode rootNode = new DataNode("");

    private static final String rootZooKeeper = "./zkSource";

    public static final Map<String, DataNode> nodes = new HashMap<String, DataNode>();


    public DataTree() {
        nodes.put(rootZooKeeper, rootNode);
    }


    @Override
    public void createNode(String path, DataNode node) {
        int lastIndex = path.lastIndexOf('/');
        String parentName = path.substring(0, lastIndex);
        node.setParent(parentName);
        if (null != nodes.get(parentName) && nodes.get(parentName).equals(node)) {
            throw new IllegalArgumentException("没有父类节点，请先创建。");
        }
        DataNode fatherNode = nodes.get(parentName);
        if (fatherNode != null) {
            fatherNode.addChild(path);
            nodes.put(path, node);
        }
    }

    @Override
    public boolean deleteNode(String path) {

        int lastSlash = path.lastIndexOf('/');
        String parentName = path.substring(0, lastSlash);
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

    @Override
    public DataNode getNode(String path) {
        return nodes.get(path);
    }

    @Override
    public Set<String> getChildren(String path) {
        DataNode dataNode = nodes.get(path);
        return dataNode.getChildren();
    }

    @Override
    public DataNode setNode(String path, String data) {
        if (!nodes.containsKey(path)) {
            throw new IllegalArgumentException("该节点不存在");
        }
        DataNode needChange = nodes.get(path);
        needChange.setData(data);
        return needChange;
    }

    @Override
    public void treeShow() {

        for (Map.Entry<String, DataNode> entry : nodes.entrySet()) {
            System.out.println(entry.getValue());
        }

//        Queue<String> queue = new LinkedList<String>();
//        queue.add(rootZooKeeper);
//        while (!queue.isEmpty()) {
//            String poll = queue.poll();
//            DataNode dn = dataTree.nodes.get(poll);
//            System.out.println(dn);
//            if (dn.getChildren() != null) {
//                for (String child : dn.getChildren()) {
//                    queue.add(child);
//                }
//            }
//        }

    }

    @Override
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

    @Override
    public DataTree deserialize() {
        String fliePath = "./zkSource/log.json";
        ConcurrentHashMap<String, Object> map = GsonUtils.readJsonFile(fliePath);
        DataTree dataTree = new DataTree();
        for (String key : map.keySet()) {
            dataTree.createNode(key, new DataNode());
            System.out.println(key + "  " + map.get(key));
        }

        return dataTree;
    }

    @Override
    public void close() throws IOException {

    }
}
