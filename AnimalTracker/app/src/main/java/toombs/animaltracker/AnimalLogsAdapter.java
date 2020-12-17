package toombs.animaltracker;

import android.content.res.Resources;
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
import java.util.GregorianCalendar;

public class AnimalLogsAdapter extends RecyclerView.Adapter<AnimalLogsAdapter.AnimalLogViewHolder> {
    private ArrayList<AnimalLogItem> animalLogsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class AnimalLogViewHolder extends RecyclerView.ViewHolder {
        public TextView logMsg;

        public AnimalLogViewHolder (@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            logMsg = itemView.findViewById(R.id.animal_log_info_log_page);

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

    public AnimalLogsAdapter(ArrayList<AnimalLogItem> animalLogsList) {
        this.animalLogsList = animalLogsList;
    }

    @NonNull
    @Override
    public AnimalLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_log_item, parent, false);
        AnimalLogViewHolder alvh = new AnimalLogViewHolder(v, listener);
        return alvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalLogViewHolder holder, int position) {
        AnimalLogItem currentItem = animalLogsList.get(position);
        String calendar = DateUtil.logMsgInfoDateToString(currentItem.getInfoDate());
        String log = currentItem.getLogMsg();
        holder.logMsg.setText(holder.itemView.getContext().getString(R.string.animal_log_message, calendar, log));
    }

    @Override
    public int getItemCount() {
        return animalLogsList.size();
    }
}
