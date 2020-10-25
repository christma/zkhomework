package nx.zk.learn;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
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

    public void addData(String rowkey, String tableName, String[] column, String[] value) throws IOException {

        // 设置rowkey
        Put put = new Put(Bytes.toBytes(rowkey));
        // HTable 负责跟记录相关的操作如增删改查等
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        // 获取所有的列簇
        HColumnDescriptor[] columnFamilies = table.getTableDescriptor().getColumnFamilies();
        for (int i = 0; i < column.length; i++) {
            // 获取列簇命
            String familyName = columnFamilies[0].getNameAsString();
            if (familyName.equals("f")) {
                put.add(Bytes.toBytes(familyName), Bytes.toBytes(column[i]), Bytes.toBytes(value[i]));
            }
        }
        table.put(put);
        System.out.println("add data Success");

    }

    public Result getResult(String tableName, String rowkey) throws Exception {
        Get get = new Get(Bytes.toBytes(rowkey));
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Result result = table.get(get);
        for (KeyValue kv : result.list()) {
            printKV(kv);
        }
        return result;
    }

    private void printKV(KeyValue kv) {
        System.out.println("rowkey : " + Bytes.toString(kv.getRow()));
        System.out.println("family : " + Bytes.toString(kv.getFamily()));
        System.out.println("qualifier : " + Bytes.toString(kv.getQualifier()));
        System.out.println("value : " + Bytes.toString(kv.getValue()));
        System.out.println("Timestamp : " + kv.getTimestamp());
        System.out.println("=================================");
    }


    public static void main(String[] args) {

        HbaseDemo hbaseDemo = new HbaseDemo();
        String tableName = "student";
        String[] family = {"f"};
        try {
            // TODO: 2020/10/25 创建表
            //hbaseDemo.createTable(tableName, family);
            String[] column = {"name", "age"};
            String[] value = {"nana", "18"};
            String[] v1 = {"ll", "19"};
            // TODO: 2020/10/25 添加信息
//            hbaseDemo.addData("rowkey1", "student", column, value);
//            hbaseDemo.addData("rowkey2", tableName, column, v1);

//            hbaseDemo.getResult(tableName, "rowkey1");
//            hbaseDemo.getResult(tableName, "rowkey2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
