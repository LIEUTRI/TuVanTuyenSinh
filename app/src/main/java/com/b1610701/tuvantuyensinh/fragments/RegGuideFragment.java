package com.b1610701.tuvantuyensinh.fragments;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.b1610701.tuvantuyensinh.R;

public class RegGuideFragment extends Fragment {

    TextView noidung_xettuyen, txt_taiday;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reg_guide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noidung_xettuyen = view.findViewById(R.id.noidung_dangkyxettuyen);
        txt_taiday = view.findViewById(R.id.txt_taiday);

        noidung_xettuyen.setText(Html.fromHtml(getResources().getString(R.string.noidung_dangkyxettuyen)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            noidung_xettuyen.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        txt_taiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tuyensinh.ctu.edu.vn/chuong-trinh-dai-tra/943-phuong-thuc-xet-tuyen.html"));
                startActivity(browserIntent);
            }
        });
    }
}