package appewtc.masterung.jlrestautant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 6/29/15 AD.
 */
public class FoodTABLE {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeDatabase, readDatabase;

    public static final String TABLE_FOOD = "foodTABLE";
    public static final String COLUMN_ID_FOOD = "_id";
    public static final String COLUMN_FOOD = "Food";
    public static final String COLUMN_PRICE = "Price";

    public FoodTABLE(Context context) {
        objMyOpenHelper = new MyOpenHelper(context);
        writeDatabase = objMyOpenHelper.getWritableDatabase();
        readDatabase = objMyOpenHelper.getReadableDatabase();
    }   // Constructor

    //Read All Price
    public String[] readAllPrice() {

        String strPrice[] = null;
        Cursor objCursor = readDatabase.query(TABLE_FOOD,
                new String[]{COLUMN_ID_FOOD, COLUMN_PRICE},
                null, null, null, null, null);

        if (objCursor != null) {

            objCursor.moveToFirst();
            strPrice = new String[objCursor.getCount()];
            for (int i = 0; i < objCursor.getCount(); i++) {
                strPrice[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PRICE));
                objCursor.moveToNext();
            }
        }
        objCursor.close();
        return strPrice;
    }

    //Read All Food
    public String[] readAllFood() {

        String strFood[] = null;

        Cursor objCursor = readDatabase.query(TABLE_FOOD,
                new String[]{COLUMN_ID_FOOD, COLUMN_FOOD},
                null, null, null, null, null);

        if (objCursor != null) {

            objCursor.moveToFirst();
            strFood = new String[objCursor.getCount()];
            for (int i = 0; i < objCursor.getCount(); i++) {

                strFood[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_FOOD));
                objCursor.moveToNext();

            } //for
        }   //if1
        objCursor.close();
        return strFood;
    }



    public long addFood(String strFood, String strPrice) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_FOOD, strFood);
        objContentValues.put(COLUMN_PRICE, strPrice);

        return writeDatabase.insert(TABLE_FOOD, null, objContentValues);
    }

}   // Main Class
