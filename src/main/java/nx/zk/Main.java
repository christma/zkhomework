package nx.zk;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
//
        DataTree dataTree = new DataTree();
        DataNode dataNode = new DataNode("helloworld");
        dataTree.createNode("./zkSource/helloworld", dataNode);
//
        DataNode sparkNode = new DataNode("spark");
        dataTree.createNode("./zkSource/spark", sparkNode);

        DataNode hadoopNode = new DataNode("hadoop");
        dataTree.createNode("./zkSource/helloworld/hadoop", hadoopNode);

        dataTree.deleteNode("./zkSource/spark");


////
        DataNode setNode = dataTree.setNode("./zkSource/helloworld/hadoop", "hadoopname");
//
//
        Set<String> children = dataTree.getChildren("./zkSource");
        for (String str : children) {
            System.out.println(str);
        }


        dataTree.serialize(dataTree);


        DataTree tree = new DataTree().deserialize();
        DataNode kafkaNode = new DataNode("kafka");
        tree.createNode("./zkSource/kafka", kafkaNode);
        tree.treeShow();
    }
}
