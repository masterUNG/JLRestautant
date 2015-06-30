package appewtc.masterung.jlrestautant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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
    private EditText userEditText, passEditText;
    private String userString, passString, nameString;

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

    public void clickLogin(View view) {

        //Initial Widget
        userEditText = (EditText) findViewById(R.id.edtUser);
        passEditText = (EditText) findViewById(R.id.edtPassword);

        //Get Value From EditText
        userString = userEditText.getText().toString().trim();
        passString = passEditText.getText().toString().trim();

        //Check Zero
        if (userString.equals("") || passString.equals("") ) {

            myAlertDialog("มีช่องว่าง", "กรุณากรอกทุกช่อง ด้วยคะ");

        } else {

            //Call searchUser
            callSearchUser();

        }   //if

    }   //clickLogin

    private void callSearchUser() {

        try {

            String strMyResult[] = objUserTABLE.searchUser(userString);
            nameString = strMyResult[3];

            //Check Password
            checkPassword(strMyResult[2]);

        } catch (Exception e) {
            myAlertDialog("No This User", "No This " + userString + " in my Database");
        }

    }   //callSearchUser

    private void checkPassword(String strTruePassword) {

        if (passString.equals(strTruePassword)) {

            wellcomOfficer();

        } else {
            myAlertDialog("Password False", "Please Try Again Password False");
        }

    }   //checkPassword

    private void wellcomOfficer() {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.restaurant);
        objBuilder.setTitle("Wellcome to Restaurant");
        objBuilder.setMessage("Wellcom " + nameString + "\n" + "To Our Restaurant");
        objBuilder.setCancelable(false);
        objBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                userEditText.setText("");
                passEditText.setText("");
                dialogInterface.dismiss();

            }   // event
        });

        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                intentToMenuRestaurant();
                dialogInterface.dismiss();

            }
        });
        objBuilder.show();

    } //wellcomOfficer

    private void intentToMenuRestaurant() {

        Intent objIntent = new Intent(MainActivity.this, MenuRestaurantListView.class);
        objIntent.putExtra("Officer", nameString);
        startActivity(objIntent);
        finish();

    }

    private void myAlertDialog(String strTitle, String strMessage) {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.icon_question);
        objBuilder.setTitle(strTitle);
        objBuilder.setMessage(strMessage);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }   // event
        });
        objBuilder.show();

    }   //myAlertDialog


    private void synJSONtoSQLite() {

        //Connected Protocol
        StrictMode.ThreadPolicy objThreadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(objThreadPolicy);

        int intTimes = 0;
        while (intTimes <= 1) {


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
