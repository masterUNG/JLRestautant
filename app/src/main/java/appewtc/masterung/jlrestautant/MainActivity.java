package appewtc.masterung.jlrestautant;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private UserTABLE objUserTABLE;
    private FoodTABLE objFoodTABLE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connected SQLite
        connectedSQLite();

        //Test Add Value
        //testAddValue();

        //Delete All Data
        deleteAllData();

        //Synchronize JSON to SQLite
        synJSONtoSQLite();

    }   // onCreate

    private void synJSONtoSQLite() {

        //Connected Protocol
        StrictMode.ThreadPolicy objThreadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(objThreadPolicy);

        int intTimes = 0;
        while (intTimes <=1) {


            InputStream objInputStream = null;
            String strJSON = "";
            String strUserURL = "http://swiftcodingthai.com/jl/php_get_data_master.php";
            String strFoodURL = "http://swiftcodingthai.com/jl/php_get_data_restaurant.php";
            HttpPost objHttpPost;

            //1. Create Input Stream
            try {

                HttpClient objHttpClient = new DefaultHttpClient();

                if (intTimes != 1) {
                    objHttpPost = new HttpPost(strUserURL);
                } else {
                    objHttpPost = new HttpPost(strFoodURL);
                }

                HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
                HttpEntity objHttpEntity = objHttpResponse.getEntity();
                objInputStream = objHttpEntity.getContent();

            } catch (Exception e) {
                Log.d("jl", "InputStream ==> " + e.toString());
            }


            //2. Create JSON String
            try {

                BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
                StringBuilder objStringBuilder = new StringBuilder();
                String strLine = null;

                while ((strLine = objBufferedReader.readLine()) != null) {
                    objStringBuilder.append(strLine);
                }   // while

                objInputStream.close();
                strJSON = objStringBuilder.toString();

            } catch (Exception e) {
                Log.d("jl", "strJSON ==> " + e.toString());
            }


            //3. Update SQLite
            try {

                final JSONArray objJsonArray = new JSONArray(strJSON);

                for (int i = 0; i < objJsonArray.length(); i++) {

                    JSONObject myJsonObject = objJsonArray.getJSONObject(i);

                    if (intTimes != 1) {

                        //for userTABLE
                        String strUser = myJsonObject.getString("User");
                        String strPassword = myJsonObject.getString("Password");
                        String strName = myJsonObject.getString("Name");

                        objUserTABLE.addUser(strUser, strPassword, strName);


                    } else {

                        //for foodTABLE
                        String strFood = myJsonObject.getString("Food");
                        String strPrice = myJsonObject.getString("Price");

                        objFoodTABLE.addFood(strFood, strPrice);

                    }

                }   //for


            } catch (Exception e) {
                Log.d("jl", "Update ==>" + e.toString());
            }


            //Increase
            intTimes += 1;

        }   // while

    }   // synJSONtoSQLite

    private void deleteAllData() {

        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("jl.db", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("userTABLE", null, null);
        objSqLiteDatabase.delete("foodTABLE", null, null);

    }

    private void testAddValue() {
        objUserTABLE.addUser("testUser", "testPass", "มาสเตอร์");
        objFoodTABLE.addFood("ข้าวขาหมู", "45");
    }

    private void connectedSQLite() {
        objUserTABLE = new UserTABLE(this);
        objFoodTABLE = new FoodTABLE(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}   // Main Class
