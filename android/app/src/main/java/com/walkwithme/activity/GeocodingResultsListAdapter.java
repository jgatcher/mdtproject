package com.walkwithme.activity;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.maps.model.GeocodingResult;

import java.util.List;

/**Custom ArrayAdapter to display items
 * The main work is done in the getView method where data stored in array is welded into the layout for the individaul rows 
 * @author User
 *
 *
 */
public class GeocodingResultsListAdapter extends BaseAdapter {
    int resourceId;
    GeocodingResult[] listItems;
    LayoutInflater layoutInflater;

    //This constructor expects activity as context,
    public GeocodingResultsListAdapter(Context context, GeocodingResult[] objects) {
        //super();
        //this.resourceId=resource;
        this.listItems=objects;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View viewToRender=convertView;
        ViewHolder holder;
        if(viewToRender==null){
            viewToRender=layoutInflater.inflate(R.layout.single_address, parent,false);
            holder=new ViewHolder();//create a temp obj to hold references to the layout,and avoid having ot call findViewByid
            holder.primaryText=(TextView)viewToRender.findViewById(R.id.address_line);
            //holder.secondaryText=(TextView)viewToRender.findViewById(R.id.address_line2);
            viewToRender.setTag(holder);//store the temp obj in the view instead of creating another data structure
        }else{

            holder=(ViewHolder)viewToRender.getTag();
        }

        holder.primaryText.setText(listItems[position].formattedAddress);

        return viewToRender;
    }

    //A temp obj that stores references to the layout
    class ViewHolder{
        TextView primaryText;
        TextView secondaryText;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listItems.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return listItems[arg0];
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

}
