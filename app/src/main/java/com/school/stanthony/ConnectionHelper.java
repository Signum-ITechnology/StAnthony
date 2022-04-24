package com.school.stanthony;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;////////////////////////////////////l,

public class ConnectionHelper {

    String ip,db,DBUserNameStr,DBPasswordStr;

    @SuppressLint("NewApi")
    public Connection connectionclasss()
    {
        ip = "103.150.187.212:1444";
        db = "stanthony_edusofterp2_stanthony";
        DBUserNameStr = "stanthony_edusofterp2_stanthony";
        DBPasswordStr = "ri36W8z?";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        String ConnectionURL;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip +";databaseName="+ db + ";user=" + DBUserNameStr+ ";password=" + DBPasswordStr + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
         Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException ce)
        {
         Log.e("error here 2 : ", ce.getMessage());
        }
        catch (Exception e)
        {
         Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}