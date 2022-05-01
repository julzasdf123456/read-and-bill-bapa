package com.lopez.julz.readandbillbapa.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.lopez.julz.readandbillbapa.R;
import com.lopez.julz.readandbillbapa.ReadingFormActivity;
import com.lopez.julz.readandbillbapa.dao.DownloadedPreviousReadings;

import java.util.List;

public class AccountsListAdapter extends RecyclerView.Adapter<AccountsListAdapter.ViewHolder> {

    public List<DownloadedPreviousReadings> downloadedPreviousReadingsList;
    public Context context;
    public String servicePeriod, userId, bapaName;

    public AccountsListAdapter(List<DownloadedPreviousReadings> downloadedPreviousReadingsList, Context context, String servicePeriod, String userId, String BAPAName) {
        this.downloadedPreviousReadingsList = downloadedPreviousReadingsList;
        this.context = context;
        this.servicePeriod = servicePeriod;
        this.userId = userId;
        bapaName = BAPAName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_accounts_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DownloadedPreviousReadings downloadedPreviousReadings = downloadedPreviousReadingsList.get(position);

        holder.accountName.setText(downloadedPreviousReadings.getServiceAccountName());
        holder.accountNumber.setText((downloadedPreviousReadings.getOldAccountNo() != null ? downloadedPreviousReadings.getOldAccountNo() : "-" ) + " (" + downloadedPreviousReadings.getId() + ") | " + downloadedPreviousReadings.getAccountType());

        if (downloadedPreviousReadings.getAccountStatus().equals("ACTIVE")) {
            if (downloadedPreviousReadings.getStatus() != null) {
                if (downloadedPreviousReadings.getStatus().equals("READ")) {
                    holder.accountStatus.setBackgroundResource(R.drawable.ic_baseline_check_circle_18);
                } else {
                    holder.accountStatus.setBackgroundResource(R.drawable.ic_baseline_check_circle_outline_18);
                }
            } else {
                holder.accountStatus.setBackgroundResource(R.drawable.ic_baseline_check_circle_outline_18);
            }
        } else {
            if (downloadedPreviousReadings.getStatus() != null) {
                if (downloadedPreviousReadings.getStatus().equals("READ")) {
                    holder.accountStatus.setBackgroundResource(R.drawable.ic_baseline_check_circle_red_18);
                } else {
                    holder.accountStatus.setBackgroundResource(R.drawable.ic_baseline_error_outline_18);
                }
            } else {
                holder.accountStatus.setBackgroundResource(R.drawable.ic_baseline_error_outline_18);
            }

        }

        holder.accountParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReadingFormActivity.class);
                intent.putExtra("ID", downloadedPreviousReadings.getId());
                intent.putExtra("SERVICEPERIOD", servicePeriod);
                intent.putExtra("USERID", userId);
                intent.putExtra("BAPANAME", bapaName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadedPreviousReadingsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public MaterialCardView accountParent;
        public TextView accountName, accountNumber;
        public ImageView accountStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            accountParent = itemView.findViewById(R.id.accountParent);
            accountName = itemView.findViewById(R.id.consumerName);
            accountNumber = itemView.findViewById(R.id.accountNo);
            accountStatus = itemView.findViewById(R.id.accountStatus);
        }
    }
}
