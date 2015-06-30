package appewtc.masterung.jlrestautant;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class MenuRestaurantListView extends AppCompatActivity {

    //Explicit
    TextView officerTextView;
    Spinner deskSpinner;
    ListView foodListView;
    String officerString, deskString, foodString, itemString;
    FoodTABLE objFoodTABLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_restaurant_list_view);

        //Initial Widget
        initialWidget();

        //Show Officer
        showOfficer();

        //Create Spinner
        createSpinner();

        //Create ListView
        createListView();

    }   // onCreate

    private void createListView() {

        objFoodTABLE = new FoodTABLE(this);
        String strFood[] = objFoodTABLE.readAllFood();
        String strPrice[] = objFoodTABLE.readAllPrice();
        int intFoodImage[] = {R.drawable.food1, R.drawable.food2, R.drawable.food3,
                R.drawable.food4, R.drawable.food5, R.drawable.food6, R.drawable.food7,
                R.drawable.food8, R.drawable.food9, R.drawable.food10, R.drawable.food11,
                R.drawable.food12, R.drawable.food13, R.drawable.food14, R.drawable.food15,
                R.drawable.food16, R.drawable.food17, R.drawable.food18, R.drawable.food19,
                R.drawable.food20, R.drawable.food21, R.drawable.food22, R.drawable.food23,
                R.drawable.food24, R.drawable.food24, R.drawable.food26, R.drawable.food27,
                R.drawable.food28, R.drawable.food29, R.drawable.food30, R.drawable.food31,
                R.drawable.food32, R.drawable.food33, R.drawable.food34, R.drawable.food35,
                R.drawable.food36, R.drawable.food37, R.drawable.food38, R.drawable.food39,
                R.drawable.food40, R.drawable.food41, R.drawable.food42, R.drawable.food43,
                R.drawable.food44, R.drawable.food45, R.drawable.food46, R.drawable.food47,
                R.drawable.food48, R.drawable.food49, R.drawable.food50};
        MyAdapter objMyAdapter = new MyAdapter(MenuRestaurantListView.this, strFood, strPrice, intFoodImage);
        foodListView.setAdapter(objMyAdapter);

    }   //createListView

    private void createSpinner() {

        //Create Spinner
        final String strShowSpinner[] = getResources().getStringArray(R.array.my_desk);
        ArrayAdapter<String> deskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strShowSpinner);
        deskSpinner.setAdapter(deskAdapter);


        //Selected Spinner
        deskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deskString = strShowSpinner[i];

                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.YELLOW);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                deskString = strShowSpinner[0];
            }
        });


    }   //createSpinner



    private void showOfficer() {

        officerString = getIntent().getExtras().getString("Officer");
        officerTextView.setText(officerString);

    }   //showOfficer

    private void initialWidget() {
        officerTextView = (TextView) findViewById(R.id.txtShowOfficer);
        deskSpinner = (Spinner) findViewById(R.id.spinner);
        foodListView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_restaurant_list_view, menu);
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
