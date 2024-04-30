package com.example.happytailhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    private Context context;
    private List<HelperClass> dataList;
    private List<HelperClass> originalDataList;

    public MyAdapter(Context context, List<HelperClass> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.originalDataList = new ArrayList<>(dataList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recOwner.setText(dataList.get(position).getDataName());
        holder.recPet.setText(dataList.get(position).getDataPetName());
        holder.recSex.setText(dataList.get(position).getDataSex());
        holder.recAge.setText(dataList.get(position).getDataAge());
        holder.recType.setText(dataList.get(position).getDataType());
        holder.recColor.setText(dataList.get(position).getDataColor());
        holder.recSpec.setText(dataList.get(position).getDataSpec());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Name", dataList.get(holder.getAdapterPosition()).getDataName());
                intent.putExtra("PetName", dataList.get(holder.getAdapterPosition()).getDataPetName());
                intent.putExtra("Sex", dataList.get(holder.getAdapterPosition()).getDataSex());
                intent.putExtra("Age", dataList.get(holder.getAdapterPosition()).getDataAge());
                intent.putExtra("Type", dataList.get(holder.getAdapterPosition()).getDataType());
                intent.putExtra("Color", dataList.get(holder.getAdapterPosition()).getDataColor());
                intent.putExtra("Special Notes", dataList.get(holder.getAdapterPosition()).getDataSpec());
                intent.putExtra("key", dataList.get(holder.getAdapterPosition()).getKey());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase();
                List<HelperClass> filteredList = new ArrayList<>();

                if (searchText.isEmpty()) {
                    filteredList.addAll(originalDataList);
                } else {
                    for (HelperClass item : originalDataList) {
                        if (item.getDataName().toLowerCase().contains(searchText) ||
                                item.getDataType().toLowerCase().contains(searchText)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataList.clear();
                dataList.addAll((List<HelperClass>) filterResults.values);
                notifyDataSetChanged();

            }
        };
    }


    public void searchDataList(ArrayList<HelperClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recOwner, recType, recSex, recAge, recPet, recColor, recSpec;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recOwner = itemView.findViewById(R.id.recOwner);
        recType = itemView.findViewById(R.id.recType);
        recPet = itemView.findViewById(R.id.recPet);
        recSex = itemView.findViewById(R.id.recSex);
        recAge = itemView.findViewById(R.id.recAge);
        recColor = itemView.findViewById(R.id.recColor);
        recSpec = itemView.findViewById(R.id.recSpec);
    }
}