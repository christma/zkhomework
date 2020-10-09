package nx.zk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataNode {
    private String data;

    private String parent;

    private Set<String> children;

    public DataNode() {

    }

    public DataNode(String data) {
        this.data = data;
        parent = "./zkSource";
        children = null;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Set<String> getChildren() {
        return children;
    }

    public void setChildren(Set<String> children) {
        this.children = children;
    }

    public synchronized boolean addChild(String child) {
        if (children == null) {
            children = new HashSet<String>(8);
        }
        return children.add(child);
    }

    public synchronized boolean removeChild(String child) {
        if (children == null) {
            return false;
        }
        return children.remove(child);
    }

    @Override
    public String toString() {
        return "DataNode{" +
                "data='" + data + '\'' +
                ", parent='" + parent + '\'' +
                ", children=" + children +
                '}';
    }
}
