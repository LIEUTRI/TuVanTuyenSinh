package com.b1610701.tuvantuyensinh.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.b1610701.tuvantuyensinh.R;

public class QuestionFragment extends Fragment {

    TextView cau1, cau2, cau3;
    CheckBox mark, mark2, mark3;
    boolean expanded1 = false;
    boolean expanded2 = false;
    boolean expanded3 = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cau1 = view.findViewById(R.id.cau1);
        mark = view.findViewById(R.id.mark);
        cau2 = view.findViewById(R.id.cau2);
        mark2 = view.findViewById(R.id.mark2);
        cau3 = view.findViewById(R.id.cau3);
        mark3 = view.findViewById(R.id.mark3);

        cau3.setText(Html.fromHtml(getResources().getString(R.string.question3_answer)));

        collapse(cau1);
        collapse(cau2);
        collapse(cau3);

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expanded1){
                    collapse(cau1);
                    expanded1 = false;
                } else {
                    expand(cau1);
                    expanded1 = true;
                }
            }
        });

        mark2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expanded2){
                    collapse(cau2);
                    expanded2 = false;
                } else {
                    expand(cau2);
                    expanded2 = true;
                }
            }
        });

        mark3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expanded3){
                    collapse(cau3);
                    expanded3 = false;
                } else {
                    expand(cau3);
                    expanded3 = true;
                }
            }
        });
        cau3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tuyensinh.ctu.edu.vn/phuong-thuc-xet-tuyen/947-phuong-thuc-6.html"));
                startActivity(browserIntent);
            }
        });
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}