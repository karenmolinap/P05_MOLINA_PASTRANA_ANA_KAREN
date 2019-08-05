package com.upvictoria.dispositivos_moviles_may_ago_2019.p05_molina_pastrana_ana_karen;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class leaderboard_listviewAdapter extends BaseAdapter {
    public ArrayList<Score> scoreList;
    Activity activity;

    public leaderboard_listviewAdapter(Activity activity, ArrayList<Score> scoreList) {
        super();
        this.activity = activity;
        this.scoreList = scoreList;
    }

    @Override
    public int getCount() {
        return scoreList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mId;
        TextView mNombre;
        TextView mAciertos;
        TextView mFecha;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row_leaderboard, null);
            holder = new ViewHolder();
            holder.mId = (TextView) convertView.findViewById(R.id.listview_row_id);
            holder.mNombre = (TextView) convertView.findViewById(R.id.listview_row_nombre);
            holder.mAciertos = (TextView) convertView.findViewById(R.id.listview_row_aciertos);
            holder.mFecha = (TextView) convertView.findViewById(R.id.listview_row_fecha);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Score score = scoreList.get(position);
        holder.mId.setText(score.getId());
        holder.mNombre.setText(score.getNombre());
        holder.mAciertos.setText(score.getAciertos());
        holder.mFecha.setText(score.getFecha());

        return convertView;
    }
}
