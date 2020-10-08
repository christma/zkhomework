package nx.zk;


import java.util.Set;

public interface DataTreeProtocol {

    long versionID = 1L;

    void createNode(String path, DataNode data);

    boolean deleteNode(String path);

    DataNode getNode(String path);

    Set<String> getChildren(String path);

    DataNode setNode(String path, String data);

    void treeShow(DataTree dataTree);
}
