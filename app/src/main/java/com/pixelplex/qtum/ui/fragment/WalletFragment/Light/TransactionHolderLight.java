package com.pixelplex.qtum.ui.fragment.WalletFragment.Light;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.ui.fragment.WalletFragment.TransactionClickListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public class TransactionHolderLight extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_value)
    TextView mTextViewValue;
    @BindView(R.id.tv_date)
    TextView mTextViewDate;
    @BindView(R.id.tv_id)
    TextView mTextViewID;
    @BindView(R.id.iv_icon)
    ImageView mImageViewIcon;
    @BindView(R.id.ll_transaction)
    LinearLayout mLinearLayoutTransaction;

    Date date = new Date();
    long currentTime = date.getTime() / 1000L;

    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM");


    TransactionHolderLight(View itemView, final TransactionClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTransactionClick(getAdapterPosition());
            }
        });
        ButterKnife.bind(this, itemView);
    }

    void bindTransactionData(History history) {

        mLinearLayoutTransaction.setBackgroundResource(android.R.color.transparent);

        if (history.getChangeInBalance().doubleValue() > 0) {
            mImageViewIcon.setImageResource(R.drawable.ic_received_light);
        } else {
            mImageViewIcon.setImageResource(R.drawable.ic_sended_light);
        }

        if(history.getBlockTime() != null) {
            long transactionTime = history.getBlockTime();
            long delay = currentTime - transactionTime;
            String dateString;
            if(delay<60){
                dateString = delay + " sec ago";
            }else
            if (delay < 3600) {
                dateString = delay / 60 + " min ago";
            } else {
                Calendar calendarNow = Calendar.getInstance();
                calendarNow.set(Calendar.HOUR_OF_DAY, 0);
                calendarNow.set(Calendar.MINUTE, 0);
                calendarNow.set(Calendar.SECOND, 0);
                date = calendarNow.getTime();
                Date dateTransaction = new Date(transactionTime * 1000L);
                dateString = ((transactionTime - date.getTime() / 1000L) > 0)? timeFormat.format(dateTransaction) : dateFormat.format(dateTransaction);
            }
            mTextViewDate.setText(dateString);
        } else {
            mImageViewIcon.setImageResource(R.drawable.ic_confirmation_loader);
            mTextViewDate.setText(R.string.confirmation);
            mLinearLayoutTransaction.setBackgroundResource(R.color.bottom_nav_bar_color_light);
        }

        mTextViewID.setText(history.getTxHash());
        mTextViewValue.setText(history.getChangeInBalance().toString() + " QTUM");
    }
}
