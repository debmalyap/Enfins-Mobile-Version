package com.qbent.enfinsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qbent.enfinsapp.CollectionPointDetailActivity;
import com.qbent.enfinsapp.LoanHouseVisitActivity;
import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.model.CollectionPoint;
import com.qbent.enfinsapp.model.LoanHouseVisitDetail;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HouseVisitQuestionAdapter extends
        RecyclerView.Adapter<HouseVisitQuestionAdapter.HouseVisitQuestionViewHolder>  {

    private final List<LoanHouseVisitDetail> detail;
    private Context mContext;

    public HouseVisitQuestionAdapter(List<LoanHouseVisitDetail> detail, Context context) {
        this.detail = detail;
        this.mContext = context;
    }

    @NonNull
    @Override
    public HouseVisitQuestionAdapter.HouseVisitQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_house_visit_questions, viewGroup, false);
        return new HouseVisitQuestionAdapter.HouseVisitQuestionViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final HouseVisitQuestionAdapter.HouseVisitQuestionViewHolder houseVisitQuestionViewHolder, final int i) {

        getTotalPrice();
        houseVisitQuestionViewHolder.mItem = detail.get(i);
        houseVisitQuestionViewHolder.question.setText(detail.get(i).getQuestion());
        houseVisitQuestionViewHolder.score.setText(String.valueOf(detail.get(i).getTotalScore()));
        if(detail.get(i).getTotalScore() == detail.get(i).getScore1())
        {
            houseVisitQuestionViewHolder.answer1.setChecked(true);
        }
        else if(detail.get(i).getTotalScore() == detail.get(i).getScore2())
        {
            houseVisitQuestionViewHolder.answer2.setChecked(true);
        }
        else if(detail.get(i).getTotalScore() == detail.get(i).getScore3())
        {
            houseVisitQuestionViewHolder.answer3.setChecked(true);
        }
        else if(detail.get(i).getTotalScore() == detail.get(i).getScore4())
        {
            houseVisitQuestionViewHolder.answer4.setChecked(true);
        }
        else if(detail.get(i).getTotalScore() == detail.get(i).getScore5())
        {
            houseVisitQuestionViewHolder.answer5.setChecked(true);
        }
        else if(detail.get(i).getTotalScore() == detail.get(i).getScore6())
        {
            houseVisitQuestionViewHolder.answer6.setChecked(true);
        }


//        houseVisitQuestionViewHolder.answer1.setOnClickListener();

 //       int count = getItemCount();




        houseVisitQuestionViewHolder.answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(houseVisitQuestionViewHolder.answer1.isChecked())
                {
                    houseVisitQuestionViewHolder.answer1.setChecked(true);
                    houseVisitQuestionViewHolder.score.setText(String.valueOf(detail.get(i).getScore1()));
                    detail.get(i).setTotalScore(detail.get(i).getScore1());
                    notifyDataSetChanged();
                    getTotalPrice();
                    //tsum = tsum +  Integer.parseInt(houseVisitQuestionViewHolder.score.getText().toString());
                    //notifyDataSetChanged();
                }
            }
        });

        houseVisitQuestionViewHolder.answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(houseVisitQuestionViewHolder.answer2.isChecked())
                {
                    houseVisitQuestionViewHolder.answer2.setChecked(true);
                    houseVisitQuestionViewHolder.score.setText(String.valueOf(detail.get(i).getScore2()));
                    detail.get(i).setTotalScore(detail.get(i).getScore2());
                    notifyDataSetChanged();
                    getTotalPrice();
                    //notifyDataSetChanged();
                }
            }
        });

        houseVisitQuestionViewHolder.answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(houseVisitQuestionViewHolder.answer3.isChecked())
                {
                    houseVisitQuestionViewHolder.answer3.setChecked(true);
                    houseVisitQuestionViewHolder.score.setText(String.valueOf(detail.get(i).getScore3()));
                    detail.get(i).setTotalScore(detail.get(i).getScore3());
                    notifyDataSetChanged();
                    getTotalPrice();
                    //notifyDataSetChanged();
                }
            }
        });

        houseVisitQuestionViewHolder.answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(houseVisitQuestionViewHolder.answer4.isChecked())
                {
                    houseVisitQuestionViewHolder.answer4.setChecked(true);
                    houseVisitQuestionViewHolder.score.setText(String.valueOf(detail.get(i).getScore4()));
                    detail.get(i).setTotalScore(detail.get(i).getScore4());
                    notifyDataSetChanged();
                    getTotalPrice();
                    //notifyDataSetChanged();
                }
            }
        });

        houseVisitQuestionViewHolder.answer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(houseVisitQuestionViewHolder.answer5.isChecked())
                {
                    houseVisitQuestionViewHolder.answer5.setChecked(true);
                    houseVisitQuestionViewHolder.score.setText(String.valueOf(detail.get(i).getScore5()));
                    detail.get(i).setTotalScore(detail.get(i).getScore5());
                    notifyDataSetChanged();
                    getTotalPrice();
                    //notifyDataSetChanged();
                }
            }
        });

        houseVisitQuestionViewHolder.answer6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(houseVisitQuestionViewHolder.answer6.isChecked())
                {
                    houseVisitQuestionViewHolder.answer6.setChecked(true);
                    houseVisitQuestionViewHolder.score.setText(String.valueOf(detail.get(i).getScore6()));
                    detail.get(i).setTotalScore(detail.get(i).getScore6());
                    notifyDataSetChanged();
                    getTotalPrice();
                    //notifyDataSetChanged();
                }
            }
        });






        if(detail.get(i).getAnswer1().equals("null"))
        {
            houseVisitQuestionViewHolder.answer1.setVisibility(View.GONE);
        }
        else
        {
            houseVisitQuestionViewHolder.answer1.setVisibility(View.VISIBLE);
            houseVisitQuestionViewHolder.answer1.setText(detail.get(i).getAnswer1());
        }

        if(detail.get(i).getAnswer2().equals("null"))
        {
            houseVisitQuestionViewHolder.answer2.setVisibility(View.GONE);
        }
        else
        {
            houseVisitQuestionViewHolder.answer2.setVisibility(View.VISIBLE);
            houseVisitQuestionViewHolder.answer2.setText(detail.get(i).getAnswer2());
        }

        if(detail.get(i).getAnswer3().equals("null"))
        {
            houseVisitQuestionViewHolder.answer3.setVisibility(View.GONE);
        }
        else
        {
            houseVisitQuestionViewHolder.answer3.setVisibility(View.VISIBLE);
            houseVisitQuestionViewHolder.answer3.setText(detail.get(i).getAnswer3());
        }

        if(detail.get(i).getAnswer4().equals("null"))
        {
            houseVisitQuestionViewHolder.answer4.setVisibility(View.GONE);
        }
        else
        {
            houseVisitQuestionViewHolder.answer4.setVisibility(View.VISIBLE);
            houseVisitQuestionViewHolder.answer4.setText(detail.get(i).getAnswer4());
        }

        if(detail.get(i).getAnswer5().equals("null"))
        {
            houseVisitQuestionViewHolder.answer5.setVisibility(View.GONE);
        }
        else
        {
            houseVisitQuestionViewHolder.answer5.setVisibility(View.VISIBLE);
            houseVisitQuestionViewHolder.answer5.setText(detail.get(i).getAnswer5());
        }

        if(detail.get(i).getAnswer6().equals("null"))
        {
            houseVisitQuestionViewHolder.answer6.setVisibility(View.GONE);
        }
        else
        {
            houseVisitQuestionViewHolder.answer6.setVisibility(View.VISIBLE);
            houseVisitQuestionViewHolder.answer6.setText(detail.get(i).getAnswer6());
        }

        //houseVisitQuestionViewHolder.score.setText("-1");



//        houseVisitQuestionViewHolder.answer2.setText(detail.get(i).getAnswer2());
//        houseVisitQuestionViewHolder.answer3.setText(detail.get(i).getAnswer3());
//        houseVisitQuestionViewHolder.answer4.setText(detail.get(i).getAnswer4());
//        houseVisitQuestionViewHolder.answer5.setText(detail.get(i).getAnswer5());
//        houseVisitQuestionViewHolder.answer6.setText(detail.get(i).getAnswer6());



//        houseVisitQuestionViewHolder.gridLayout.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                //Toast.makeText(view.getContext(),"You Clicked "+members.get(i).getFullName(), Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(view.getContext(), CollectionPointDetailActivity.class);
//                intent.putExtra("collection_id",collectionPoints.get(i).getId());
//                view.getContext().startActivity(intent);
//
//            }
//        });
    }


    public void getTotalPrice() {
        int total = 0;
        for (int i = 0; i < detail.size(); i++) {
            total+=detail.get(i).getTotalScore();
        }
        ((LoanHouseVisitActivity)mContext).totalScore(total);
        //return total;
    }

    public List<LoanHouseVisitDetail> totalData()
    {
        return detail;
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class HouseVisitQuestionViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final RadioButton answer1;
        public final RadioButton answer2;
        public final RadioButton answer3;
        public final RadioButton answer4;
        public final RadioButton answer5;
        public final RadioButton answer6;
        public final TextView question;
        public final TextView score;

        public LoanHouseVisitDetail mItem;
        public GridLayout gridLayout;

        public HouseVisitQuestionViewHolder(View view) {
            super(view);
            mView = view;
            question = (TextView) view.findViewById(R.id.houseVisitQuestion);
            score = (TextView) view.findViewById(R.id.houseVisitScore);
            answer1 = (RadioButton) view.findViewById(R.id.houseVisitAnswer1);
            answer2 = (RadioButton) view.findViewById(R.id.houseVisitAnswer2);
            answer3 = (RadioButton) view.findViewById(R.id.houseVisitAnswer3);
            answer4 = (RadioButton) view.findViewById(R.id.houseVisitAnswer4);
            answer5 = (RadioButton) view.findViewById(R.id.houseVisitAnswer5);
            answer6 = (RadioButton) view.findViewById(R.id.houseVisitAnswer6);

            gridLayout = (GridLayout) view.findViewById(R.id.HouseVisitGridLayout);
        }

    }

}
