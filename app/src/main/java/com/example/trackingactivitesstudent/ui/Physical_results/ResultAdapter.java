package com.example.trackingactivitesstudent.ui.Physical_results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.trackingactivitesstudent.R;

import java.util.List;

public class ResultAdapter extends ArrayAdapter<Result> {
    private Context context;
    private List<Result> resultList;

    public ResultAdapter(@NonNull Context context, @NonNull List<Result> objects) {
        super(context, 0, objects);
        this.context = context;
        this.resultList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_result, parent, false);
        }

        Result result = resultList.get(position);

        TextView txvCourseName = convertView.findViewById(R.id.txvCourseName);
        TextView txvCourseCode = convertView.findViewById(R.id.txvCourseCode);
        TextView txvClassCode = convertView.findViewById(R.id.txvClassCode);
        TextView txvTest1 = convertView.findViewById(R.id.txvTest1);
        TextView txvMidtermScore = convertView.findViewById(R.id.txvMidtermScore);
        TextView txvExamScore = convertView.findViewById(R.id.txvExamScore);
        TextView txvTenScore = convertView.findViewById(R.id.txvTenScore);
        TextView txvFourScore = convertView.findViewById(R.id.txvFourScore);
        TextView txvLetterScore = convertView.findViewById(R.id.txvLetterScore);
        TextView txvRank = convertView.findViewById(R.id.txvRank);

        // Hiển thị thông tin
        txvCourseName.setText(result.getCourseName());
        txvCourseCode.setText(result.getCourseCode());
        txvClassCode.setText(result.getClassCode());
        txvTest1.setText(String.valueOf(result.getTest1()));
        txvMidtermScore.setText(String.valueOf(result.getMidtermScore()));
        txvExamScore.setText(String.valueOf(result.getExamScore()));

        // Tính toán điểm tổng kết
        float finalScore = result.getTest1() * 0.2f + result.getMidtermScore() * 0.3f + result.getExamScore() * 0.5f;
        txvTenScore.setText(String.format("%.2f", finalScore));

        // Tính điểm chữ
        String letterScore = calculateLetterScore(finalScore);
        txvLetterScore.setText(letterScore);

        // Tính điểm 4
        float fourScore = calculateFourScore(letterScore);
        txvFourScore.setText(String.format("%.2f", fourScore));

        // Tính xếp loại
        String rank = calculateRank(fourScore);
        txvRank.setText(rank);

        return convertView;
    }

    private String calculateLetterScore(float score) {
        if (score < 4) return "F";
        if (score <= 4.6) return "D";
        if (score <= 5.4) return "D+";
        if (score <= 6.1) return "C";
        if (score <= 6.9) return "C+";
        if (score <= 7.6) return "B";
        if (score <= 8.4) return "B+";
        return "A";
    }

    private float calculateFourScore(String letterScore) {
        switch (letterScore) {
            case "A": return 4;
            case "B+": return 3.5f;
            case "B": return 3;
            case "C+": return 2.5f;
            case "C": return 2;
            case "D+": return 1.5f;
            case "D": return 1;
            case "F": return 0;
            default: return 0;
        }
    }

    private String calculateRank(float fourScore) {
        if (fourScore < 1) return "Kém";
        if (fourScore <= 1.9) return "Yếu";
        if (fourScore <= 2.4) return "Trung bình";
        if (fourScore <= 3.1) return "Khá";
        if (fourScore <= 3.5) return "Giỏi";
        return "Xuất sắc";
    }
}

