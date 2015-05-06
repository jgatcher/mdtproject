package com.walkwithme.activity;
import java.util.List;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ArrayAdapter;

/**Custom ArrayAdapter to display items
 * The main work is done in the getView method where data stored in array is welded into the layout for the individaul rows 
 * @author User
 *
 *
 */
public class ListAdapter extends BaseAdapter {
    int resourceId;
    List<Address> listItems;
    LayoutInflater layoutInflater;

    //This constructor expects activity as context,
    public ListAdapter(Context context, List<Address> objects) {
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
        int addressLines=listItems.get(position).getMaxAddressLineIndex();
        holder.primaryText.setText(listItems.get(position).getAddressLine(0));
        if(addressLines>=2){
            holder.secondaryText.setText(listItems.get(position).getAddressLine(1));
        }
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
        return listItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return listItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

}
