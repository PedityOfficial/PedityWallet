package com.example.pwallet.peditywallet;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<AccountData> accountDataList;

    public MyAdapter(Context context, List<AccountData> accountDataList) {
        this.context = context;
        this.accountDataList = accountDataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardview;
        public TextView amount, accountid, type;

        public ViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            accountid = itemView.findViewById(R.id.accountid);
            type = itemView.findViewById(R.id.type);
            mCardview = itemView.findViewById(R.id.item_cardview);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.history_item,parent,false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.amount.setText(accountDataList.get(position).getAmount());
        holder.accountid.setText(accountDataList.get(position).getAcountid());
        holder.type.setText(accountDataList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return accountDataList.size();
    }

}