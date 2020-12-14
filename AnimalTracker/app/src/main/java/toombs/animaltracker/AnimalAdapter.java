package toombs.animaltracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {
    private ArrayList<AnimalItem> mAnimalList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public AnimalViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.animalPicture);
            mTextView1 = itemView.findViewById(R.id.petName);
            mTextView2 = itemView.findViewById(R.id.commonName);

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

    public AnimalAdapter(ArrayList<AnimalItem> animalList) {
        mAnimalList = animalList;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_item, parent, false);
        AnimalViewHolder avh = new AnimalViewHolder(v, mListener);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        AnimalItem currentItem = mAnimalList.get(position);

        byte[] byteArray = currentItem.getImageResource();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        holder.mImageView.setImageBitmap(bitmap);
        holder.mTextView1.setText(currentItem.getAnimalPetName());
        holder.mTextView2.setText(currentItem.getAnimalCommonName());
    }

    @Override
    public int getItemCount() {
        return mAnimalList.size();
    }
}
