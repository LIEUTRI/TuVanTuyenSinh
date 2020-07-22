package com.b1610701.tuvantuyensinh.fragments;

import android.annotation.SuppressLint;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.b1610701.tuvantuyensinh.HomeActivity;
import com.b1610701.tuvantuyensinh.R;
import com.b1610701.tuvantuyensinh.model.Nganh;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private DatabaseReference reference;

    private TextView txt_title;
    private TableLayout tableLayout;
    private TextView txt_TT, txt_MaNganh, txt_TenNganh, txt_ToHopMon, txt_ChiTieu, txt_1, txt_2, txt_3;
    private TextView txt_year_1, txt_year_2, txt_year_3;
    private TextView txt_ghichu, txt_footer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_title = view.findViewById(R.id.home_title);
        tableLayout = view.findViewById(R.id.mytable);
//        tableRow = view.findViewById(R.id.row_2);
        txt_TT = view.findViewById(R.id.txt_TT);
        txt_MaNganh = view.findViewById(R.id.txt_MaNganh);
        txt_TenNganh = view.findViewById(R.id.txt_TenNganh);
        txt_ToHopMon = view.findViewById(R.id.txt_ToHopMon);
        txt_ChiTieu = view.findViewById(R.id.txt_ChiTieu);
        txt_1 = view.findViewById(R.id.txt_1);
        txt_2 = view.findViewById(R.id.txt_2);
        txt_3 = view.findViewById(R.id.txt_3);
        txt_year_1 = view.findViewById(R.id.title_year_1);
        txt_year_2 = view.findViewById(R.id.title_year_2);
        txt_year_3 = view.findViewById(R.id.title_year_3);
        txt_ghichu = view.findViewById(R.id.txt_ghichu);
        txt_footer = view.findViewById(R.id.txt_footer);


        txt_title.setText(Html.fromHtml(getResources().getString(R.string.home_title)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txt_title.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        int this_year = Calendar.getInstance().get(Calendar.YEAR);
        txt_year_1.setText(String.valueOf(this_year - 1));
        txt_year_2.setText(String.valueOf(this_year - 2));
        txt_year_3.setText(String.valueOf(this_year - 3));

        ///////////////////////////////////////
        reference = FirebaseDatabase.getInstance().getReference("Nganh");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int tt = 1;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Nganh nganh = dataSnapshot.getValue(Nganh.class);
                    txt_TT.append(tt+"\n"); tt++;
                    txt_MaNganh.append(nganh.getMaNganh()+"\n");
                    txt_TenNganh.append(nganh.getTenNganh()+"\n");
                    txt_ToHopMon.append(nganh.getToHopMon()+"\n");
                    txt_ChiTieu.append(nganh.getChiTieu()+"\n");
                    txt_1.append(nganh.getDiemChuan().get_1()+"\n");
                    txt_2.append(nganh.getDiemChuan().get_2()+"\n");
                    txt_3.append(nganh.getDiemChuan().get_3()+"\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        txt_ghichu.setText(Html.fromHtml(getResources().getString(R.string.ghichu)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txt_ghichu.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        txt_footer.setText(Html.fromHtml(getResources().getString(R.string.footer)));
    }
}