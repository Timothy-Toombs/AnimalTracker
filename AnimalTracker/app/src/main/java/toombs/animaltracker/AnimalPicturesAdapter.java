package toombs.animaltracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnimalPicturesAdapter extends RecyclerView.Adapter<AnimalPicturesAdapter.AnimalPictureViewHolder> {
    private ArrayList<AnimalPictureItem> animalPicturesList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class AnimalPictureViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView infoDate;

        public AnimalPictureViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.animal_picture_page);
            infoDate = itemView.findViewById(R.id.animal_info_date_picture_page);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public AnimalPicturesAdapter(ArrayList<AnimalPictureItem> animalPicturesList) {
        this.animalPicturesList = animalPicturesList;
    }

    @NonNull
    @Override
    public AnimalPictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_picture_item, parent, false);
        AnimalPictureViewHolder apvh = new AnimalPictureViewHolder(v, listener);
        return apvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalPictureViewHolder holder, int position) {
        AnimalPictureItem currentItem = animalPicturesList.get(position);

        byte[] byteArray = currentItem.getImageResource();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        holder.imageView.setImageBitmap(bitmap);
        holder.infoDate.setText(R.string.info_date_place_holder);
    }

    @Override
    public int getItemCount() {
        return animalPicturesList.size();
    }
}
