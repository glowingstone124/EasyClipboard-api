package org.glowingstone.easyclipboardapi;

import org.json.JSONObject;

import java.sql.*;

public class Funcs {
    public static void initdb(){
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection connection = null;
            ResultSet resultSet = null;
            Statement statement = null;
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS clipboards ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "deviceid TEXT,"
                    + "timestamp INTEGER,"
                    + "content TEXT"
                    + ")";
            statement.executeUpdate(createTableSQL);
            resultSet = statement
                    .executeQuery("SELECT timestamp FROM clipboards");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static String postHandler(String data){
        JSONObject jsonObject = new JSONObject(data);
        String deviceid = jsonObject.getString("deviceid");
        int timestamp = jsonObject.getInt("timestamp");
        String content = jsonObject.getString("content");
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection connection = null;
            ResultSet resultSet = null;
            Statement statement = null;
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.createStatement();
            String maxIdQuery = "SELECT MAX(id) FROM clipboards";
            ResultSet maxIdResult = statement.executeQuery(maxIdQuery);
            int maxId = 0;
            if (maxIdResult.next()) {
                maxId = maxIdResult.getInt(1);
            }
            String timestampQuery = "SELECT timestamp FROM clipboards WHERE id = " + maxId;
            ResultSet timestampResult = statement.executeQuery(timestampQuery);

            int maxTimestamp = 0;
            if (timestampResult.next()) {
                maxTimestamp = timestampResult.getInt("timestamp");
            }

            if (maxTimestamp < timestamp) {
                String insertQuery = "INSERT INTO clipboards (deviceid, timestamp, content) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, deviceid);
                preparedStatement.setInt(2, timestamp);
                preparedStatement.setString(3, content);
                preparedStatement.executeUpdate();

                maxIdResult.close();
                timestampResult.close();
                preparedStatement.close();
                statement.close();
                connection.close();
            } else {
                maxIdResult.close();
                timestampResult.close();
                statement.close();
                connection.close();
            }
            maxIdResult.close();
            timestampResult.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getHandler(){
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection connection = null;
            ResultSet resultSet = null;
            Statement statement = null;
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.createStatement();
            String maxIdQuery = "SELECT MAX(id) FROM clipboards";
            ResultSet maxIdResult = statement.executeQuery(maxIdQuery);
            int maxId = 0;
            if (maxIdResult.next()) {
                maxId = maxIdResult.getInt(1);
            }
            String timestampQuery = "SELECT timestamp FROM clipboards WHERE id = " + maxId;
            ResultSet timestampResult = statement.executeQuery(timestampQuery);

            int maxTimestamp = 0;
            if (timestampResult.next()) {
                int returnTimeStamp = timestampResult.getInt("timestamp");
                String returnContent = timestampResult.getString("content");
                JSONObject returnObj = new JSONObject();
                returnObj.put("code", 0);
                returnObj.put("content", returnContent);
                returnObj.put("timestamp", returnTimeStamp);
                return returnObj.toString();
            }
            maxIdResult.close();
            timestampResult.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gFailed("null");
    }
    public static String gFailed(String content){
        JSONObject returnObj = new JSONObject();
        returnObj.put("code", -1);
        returnObj.put("content", content);
        return returnObj.toString();
    }
}
