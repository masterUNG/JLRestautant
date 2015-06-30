package appewtc.masterung.jlrestautant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by masterUNG on 6/30/15 AD.
 */
public class MyAdapter extends BaseAdapter{

    //Explicit
    private Context objContext;
    private String[] foodStrings, priceStrings;
    private int[] foodInts;

    public MyAdapter(Context objContext, String[] foodStrings, String[] priceStrings, int[] foodInts) {
        this.objContext = objContext;
        this.foodStrings = foodStrings;
        this.priceStrings = priceStrings;
        this.foodInts = foodInts;
    }

    @Override
    public int getCount() {
        return foodStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = objLayoutInflater.inflate(R.layout.my_listview, viewGroup, false);

        //Show Food Name
        TextView foodTextView = (TextView) view1.findViewById(R.id.txtShowFood);
        foodTextView.setText(foodStrings[i]);

        //Show Price Name
        TextView priceTextView = (TextView) view1.findViewById(R.id.txtShowPrice);
        priceTextView.setText(priceStrings[i]);

        //Show Food Image
        ImageView foodImageView = (ImageView) view1.findViewById(R.id.imvFood);
        foodImageView.setBackgroundResource(foodInts[i]);

        return view1;
    }   // getView

}   // Main Class
