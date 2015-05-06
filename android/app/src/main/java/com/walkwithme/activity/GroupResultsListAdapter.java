package com.walkwithme.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by User on 4/28/2015.
 */


/**Custom ArrayAdapter to display items
 * The main work is done in the getView method where data stored in array is welded into the layout for the individaul rows
 * @author User
 *
 *
 */
public class GroupResultsListAdapter  extends BaseAdapter {
    int resourceId;
   List<Group> listItems;
    LayoutInflater layoutInflater;

    //This constructor expects activity as context,
    public GroupResultsListAdapter(Context context, List<Group> objects) {
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
            viewToRender=layoutInflater.inflate(R.layout.single_group, parent,false);
            holder=new ViewHolder();//create a temp obj to hold references to the layout,and avoid having ot call findViewByid
            holder.primaryText=(TextView)viewToRender.findViewById(R.id.group_name);
            holder.secondaryText=(TextView)viewToRender.findViewById(R.id.group_size);
            holder.tertiaryText=(TextView)viewToRender.findViewById(R.id.group_time);

            viewToRender.setTag(holder);//store the temp obj in the view instead of creating another data structure
        }else{

            holder=(ViewHolder)viewToRender.getTag();
        }
        Group group=listItems.get(position);

        holder.primaryText.setText(group.groupId);
        holder.tertiaryText.setText(group.time);
        holder.secondaryText.setText("Group of "+String.valueOf(group.latLongs.size())+" leaving at ");

        return viewToRender;
    }

    //A temp obj that stores references to the layout
    class ViewHolder{
        TextView primaryText;
        TextView secondaryText;
        TextView tertiaryText;
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
