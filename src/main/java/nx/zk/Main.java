package nx.zk;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {

        DataTree dataTree = new DataTree();
//        DataNode dataNode = new DataNode("helloworld");
//
//
//        DataNode sparkNode = new DataNode("spark");
//        dataTree.createNode("./zkSource/helloworld", dataNode);
//        dataTree.createNode("./zkSource/spark", sparkNode);
//
////        dataTree.deleteNode("./zkSource/spark");
//
//        DataNode hadoopNode = new DataNode("hadoop");
//
//        DataNode setNode = dataTree.setNode("./zkSource/helloworld", "hadoop");
//
//        Set<String> children = dataTree.getChildren("./zkSource");
//        for (String str : children) {
//            System.out.println(str);
//        }

//        dataTree.serialize(dataTree);

    }
}
