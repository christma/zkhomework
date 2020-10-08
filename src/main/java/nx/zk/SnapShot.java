package nx.zk;


import java.io.IOException;
import java.util.Map;


public interface SnapShot {

    void serialize(DataTree dataNode);

    DataTree deserialize(String fliePath);

    void close() throws IOException;
}