package com.lai.slinky;

/**
 * Created by Administrator on 2016/11/20.
 */

import android.util.Log;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Util {

    private Connection conn = null;
    private Statement ps;
    ResultSet rSet;
    public static void go(){
        //使用SSH隧道连接数据库
        String user = "teamuser";//SSH连接用户名
        String password = "qwertyuiop";//SSH连接密码
        String host = "181.214.62.239";//SSH服务器
        int lport = 33104;//本地端口
        String rhost = "181.214.62.239";//远程MySQL服务器
        int rport = 3306;//远程MySQL服务端口
        int port = 22;//SSH访问端口
        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession(user,host,port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            Log.e("=======>", "服务器连接成功");
            System.out.println(session.getServerVersion());//这里打印SSH服务器版本信息
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);//将服务器端口和本地端口绑定，这样就能通过访问本地端口来访问服务器
            System.out.println("---------localhost:" + assinged_port + " -> " + rhost + ":" + rport);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    //连接MySQL
    public void connSQL() {//连接数据库
        String url = "jdbc:mysql://181.214.62.239:3306/Slinky?useUnicode=true&characterEncoding=UTF-8";
//        String url = "jdbc:mysql://localhost:33104/Slinky";
        String username = "teamuser2";
        String password = "123";
        try {
            Class.forName("com.mysql.jdbc.Driver");//加载驱动，连接数据库
            conn = DriverManager.getConnection(url,username,password);
            Log.e("----------","连接数据库成功");
        }
        catch(SQLException ex){
            Log.e("----------","连接数据库失败");
            ex.printStackTrace();
        }
        catch(Exception clex){
            Log.e("----------","连接数据库失败");
            clex.printStackTrace();
        }
    }

    public void CloseConn(){//关闭数据库连接
        try{
            if(conn != null)
                conn.close();
        }
        catch(SQLException ex){
            Log.e("----------","关闭数据库连接失败");
            ex.printStackTrace();
        }
    }

    public ResultSet selectSQL(String sql){//查找操作
        ResultSet rs = null;
        try{
            ps = conn.createStatement();
            rs = ps.executeQuery(sql);
        }
        catch(SQLException ex){
            Log.e("----------","查找失败");///////////////
            ex.printStackTrace();
        }
        return rs;
    }

    public boolean insertSQL(String sql){//插入操作
        boolean b = false;
        try{
            ps = conn.createStatement();
            ps.executeUpdate(sql);
            return true;
        }
        catch(SQLException ex){
            Log.e("----------","插入数据库失败");
            ex.printStackTrace();
        }
        catch (Exception exx){
            Log.e("----------","插入失败");
            exx.printStackTrace();
        }
        return b;
    }
    public boolean updateSQL(String sql){//修改操作
        boolean b = false;
        try{
            ps = conn.createStatement();
            ps.executeUpdate(sql);
            return true;
        }
        catch(SQLException ex){
            Log.e("----------","修改数据库失败");
            ex.printStackTrace();
        }
        catch (Exception exx){
            Log.e("----------","修改失败");
            exx.printStackTrace();
        }
        return b;
    }
}