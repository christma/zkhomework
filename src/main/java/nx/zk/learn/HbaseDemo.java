package nx.zk.learn;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;


import java.io.IOException;

public class HbaseDemo {
    static Configuration conf = null;

    private static final String ZK_CONNECT_STR =
            "hadoop000:2181";

    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", ZK_CONNECT_STR);
    }


    public static void main(String[] args) {

        HbaseDemo hbaseDemo = new HbaseDemo();
        String tableName = "student";
        String[] family = {"f"};
        try {
            hbaseDemo.createTable(tableName, family);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable(String tableName, String[] family) throws IOException {
        HBaseAdmin admin = new HBaseAdmin(conf);
        HTableDescriptor descriptor = new HTableDescriptor(tableName);
        for (int i = 0; i < family.length; i++) {
            descriptor.addFamily(new HColumnDescriptor(family[i]));
        }
        if (admin.tableExists(tableName)) {
            System.out.println("table Exists!");
            System.exit(0);
        } else {
            admin.createTable(descriptor);
            System.out.println("create table Success!");
            admin.close();
        }
    }


}
