package com.wds.codebook.datainsert.contorller;

import com.wds.codebook.datainsert.util.RandomValue;
import cn.hutool.core.util.IdUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcBatch {

    //起始id
    private long begin = 0;
    //每次循环插入的数据量
    private long end = begin+10000;
    private String url = "jdbc:mysql://10.1.1.202:3306/chaken-sms-log?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
    private String user = "root";
    private String password = "Vvtech@2021";


    public static void main(String[] args) {
        new JdbcBatch().insertBigData();
    }

    public void insertBigData() {
        //定义连接、statement对象
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            //加载jdbc驱动
            Class.forName("com.mysql.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //将自动提交关闭
            // conn.setAutoCommit(false);
            //编写sql
            String sql = "INSERT INTO charging_log VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            //预编译sql
            pstm = conn.prepareStatement(sql);
            //开始总计时
            long bTime1 = System.currentTimeMillis();

            //循环10次，每次1万数据，一共1000万
            for(int i=0;i<1000;i++) {

                //开启分段计时，计1W数据耗时
                long bTime = System.currentTimeMillis();
                //开始循环
                while (begin < end) {
                    //赋值
                    pstm.setString(1,IdUtil.getSnowflake().nextIdStr());
                    pstm.setInt(2, 13);
                    pstm.setString(3, RandomValue.getRandomChar(10));
                    pstm.setInt(4, RandomValue.getRandomInt());
                    pstm.setString(5, RandomValue.getRandomChar(4, 15));
                    pstm.setString(6, RandomValue.getRandomChar());
                    pstm.setString(7, RandomValue.getRandomChar());
                    pstm.setInt(8, RandomValue.getRandomInt());
                    pstm.setInt(9, RandomValue.getRandomInt());
                    pstm.setInt(10, RandomValue.getRandomInt());
                    pstm.setString(11, RandomValue.getRandomChar());
                    pstm.setInt(12, RandomValue.getRandomInt());
                    pstm.setString(13, "1");
                    pstm.setString(14, RandomValue.getRandomChar());
                    pstm.setInt(15, RandomValue.getRandomInt());
                    pstm.setDate(16, RandomValue.getRandomDate());
                    pstm.setDate(17, RandomValue.getRandomDate());
                    pstm.setDate(18, RandomValue.getRandomDate());
                    pstm.setInt(19, RandomValue.getRandomInt());
                    pstm.setInt(20, RandomValue.getRandomInt(1));
                    pstm.setString(21, RandomValue.getRandomChar());
                    pstm.setDate(22, RandomValue.getRandomDate());
                    pstm.setDate(23, RandomValue.getRandomDate());
                    pstm.setDate(24, RandomValue.getRandomDate());
                    pstm.setDate(25, RandomValue.getRandomDate());
                    pstm.setDate(26, RandomValue.getRandomDate());
                    pstm.setDate(27, RandomValue.getRandomDate());
                    pstm.setDate(28, RandomValue.getRandomDate());
                    pstm.setString(29, RandomValue.getRandomChar());
                    pstm.setInt(30, RandomValue.getRandomInt());
                    pstm.setString(31, RandomValue.getRandomChar());
                    pstm.setString(32, RandomValue.getRandomChar());
                    pstm.setString(33, RandomValue.getRandomChar());
                    pstm.setInt(34, RandomValue.getRandomInt());
                    pstm.setString(35, RandomValue.getRandomChar());
                    pstm.setString(36, RandomValue.getRandomChar());
                    pstm.setInt(37, RandomValue.getRandomInt());
                    pstm.setString(38, RandomValue.getRandomChar());
                    pstm.setInt(39, RandomValue.getRandomInt());
                    pstm.setInt(40, RandomValue.getRandomInt());
                    pstm.setString(41, RandomValue.getRandomChar());
                    pstm.setString(42, RandomValue.getRandomChar());
                    pstm.setString(43, RandomValue.getRandomChar());
                    pstm.setString(44, RandomValue.getRandomChar());
                    pstm.setInt(45, RandomValue.getRandomInt());
                    pstm.setString(46, RandomValue.getRandomChar());
                    pstm.setInt(47, RandomValue.getRandomInt());
                    pstm.setInt(48, RandomValue.getRandomInt());
                    pstm.setInt(49, RandomValue.getRandomInt());
                    pstm.setInt(50, RandomValue.getRandomInt());
                    //添加到同一个批处理中
                    pstm.addBatch();
                    begin++;
                }
                //执行批处理
                pstm.executeBatch();
                //提交事务
                //        conn.commit();
                //边界值自增1W
                end += 10000;
                //关闭分段计时
                long eTime = System.currentTimeMillis();
                //输出
                System.out.println("成功插入1W条数据耗时："+(eTime-bTime));
            }
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("插入10W数据共耗时："+(eTime1-bTime1));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

}
