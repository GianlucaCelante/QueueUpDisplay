package it.pioppi.queueupdisplay.business;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.pioppi.queueupdisplay.MainActivity;
import it.pioppi.queueupdisplay.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    public static final String ZERO = "0";
    public static final String TWO_ZEROS = "00";
    public static final String THREE_ZEROS = "000";
    public static final Integer MAX_NUMBER = 999;

    private List<Integer> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public HistoryAdapter(Context context, List<Integer> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_history, parent, false);

        int height = parent.getMeasuredHeight();
        int width = parent.getMeasuredWidth();

        view.setLayoutParams(new RecyclerView.LayoutParams(width, height));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        Integer number = mData.get(position);
        String numberConverted = String.valueOf(number);
        numberConverted = formatNumberWithLeadingZeros(numberConverted);
        holder.myTextView.setText(numberConverted);
    }


    // total number of rows
    @Override
    public int getItemCount() {
        // 3 items limit to display
        return Math.min(mData.size(), 1);
    }


    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.itemNumber);
        }

    }

    private String formatNumberWithLeadingZeros(String input) {

        int number = Integer.parseInt(input);
        if (number < 0 || number > MAX_NUMBER) {
            return THREE_ZEROS;
        }

        if (number < 10) {
            return TWO_ZEROS + number;
        } else if (number < 100) {
            return ZERO + number;
        } else {
            return String.valueOf(number);
        }
    }
}
