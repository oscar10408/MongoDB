import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.Vector;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONArray;

public class GetData {

    static String prefix = "project3.";

    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;

    // You must refer to the following variables for the corresponding 
    // tables in your database
    String userTableName = null;
    String friendsTableName = null;
    String cityTableName = null;
    String currentCityTableName = null;
    String hometownCityTableName = null;

    // DO NOT modify this constructor
    public GetData(String u, Connection c) {
        super();
        String dataType = u;
        oracleConnection = c;
        userTableName = prefix + dataType + "_USERS";
        friendsTableName = prefix + dataType + "_FRIENDS";
        cityTableName = prefix + dataType + "_CITIES";
        currentCityTableName = prefix + dataType + "_USER_CURRENT_CITIES";
        hometownCityTableName = prefix + dataType + "_USER_HOMETOWN_CITIES";
    }

    // TODO: Implement this function
    @SuppressWarnings("unchecked")
    public JSONArray toJSON() throws SQLException {

        // This is the data structure to store all users' information
        JSONArray users_info = new JSONArray();
        HashMap<Integer, JSONObject> usersMap = new HashMap<>();
        
        try (Statement stmt = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            // Your implementation goes here....
            ResultSet rst_user = stmt.executeQuery(
                "SELECT User_ID, First_Name, Last_Name, Gender, Year_of_Birth, Month_of_Birth, Day_of_Birth " +
                "FROM " + userTableName);

            while (rst_user.next()) {
                JSONObject user_basic = new JSONObject();
                int user_id = rst_user.getInt(1);
                user_basic.put("user_id", user_id);
                user_basic.put("first_name", rst_user.getString(2));
                user_basic.put("last_name", rst_user.getString(3));
                user_basic.put("gender", rst_user.getString(4));
                user_basic.put("YOB", rst_user.getInt(5));
                user_basic.put("MOB", rst_user.getInt(6));
                user_basic.put("DOB", rst_user.getInt(7));
                // Store this individual user's information in the map
                usersMap.put(user_id, user_basic);
            }

            ResultSet rst_friends = stmt.executeQuery(
                "SELECT F1.USER1_ID AS FD1, F1.USER2_ID AS FD2 " + 
                "FROM " + friendsTableName + " F1 " + 
                "UNION " +
                "SELECT F2.USER2_ID AS FD1, F2.USER1_ID AS FD2 " + 
                "FROM " + friendsTableName + " F2");

            HashMap<Integer, JSONArray> friendsMap = new HashMap<>();

            while (rst_friends.next()) {
                int user1 = rst_friends.getInt(1);
                int user2 = rst_friends.getInt(2);
                if (user1 < user2) {
                    // User2 has larger id: Add user2 to user1's friend list
                    friendsMap.computeIfAbsent(user1, k -> new JSONArray()).put(user2);
                } else {
                    // User1 has larger id: Add user1 to user2's friend list
                    friendsMap.computeIfAbsent(user2, k -> new JSONArray()).put(user1);
                }
            }

            ResultSet rst_city = stmt.executeQuery(
                "SELECT U.User_ID AS ID, C1.City_Name AS CC_City, C1.State_Name AS CC_State, C1.Country_Name AS CC_Country, " +
                "C2.City_Name AS HC_City, C2.State_Name AS HC_State, C2.Country_Name AS HC_Country " +
                "FROM " + userTableName + " U, " + cityTableName + " C1, " + currentCityTableName + " CC, " +
                cityTableName + " C2, " + hometownCityTableName + " HC " +
                "WHERE U.User_ID = CC.User_ID AND U.User_ID = HC.User_ID AND " +
                "CC.Current_City_ID = C1.City_ID AND HC.Hometown_City_ID = C2.City_ID");

            HashMap<Integer, JSONObject> cityMap = new HashMap<>();

            while (rst_city.next()) {
                int user_id = rst_city.getInt(1);
                JSONObject city_info = new JSONObject();
                JSONObject curr_city_info = new JSONObject();
                curr_city_info.put("city", rst_city.getString(2));
                curr_city_info.put("state", rst_city.getString(3));
                curr_city_info.put("country", rst_city.getString(4));
                JSONObject home_city_info = new JSONObject();
                home_city_info.put("city", rst_city.getString(5));
                home_city_info.put("state", rst_city.getString(6));
                home_city_info.put("country", rst_city.getString(7));
                city_info.put("current_city", curr_city_info);
                city_info.put("hometown_city", home_city_info);
                // Store this individual user's city information in the map
                cityMap.put(user_id, city_info);
            }

            for (int user_id : usersMap.keySet()) {
                // Get the user's information
                JSONObject user_info = usersMap.get(user_id);
                // Get the user's friends (if any), put empty array if no friends
                JSONArray friends_array = friendsMap.getOrDefault(user_id, new JSONArray());
                user_info.put("friends", friends_array);
                // Get the user's city information
                JSONObject city_info = cityMap.getOrDefault(user_id, new JSONObject());
                if (city_info.length() == 0) {
                    city_info.put("current", new JSONObject());
                    city_info.put("hometown", new JSONObject());
                } else {
                    user_info.put("current", city_info.get("current_city"));
                    user_info.put("hometown", city_info.get("hometown_city"));
                }
                users_info.put(user_info);
            }
            
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return users_info;
    }

    // This outputs to a file "output.json"
    // DO NOT MODIFY this function
    public void writeJSON(JSONArray users_info) {
        try {
            FileWriter file = new FileWriter(System.getProperty("user.dir") + "/output.json");
            file.write(users_info.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}