package com.b1610701.tuvantuyensinh.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.b1610701.tuvantuyensinh.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class CNTTFragment extends Fragment {

    private TextView txt_title;
    private TextView txt_thongtinchung;
    private TextView txt_gioithieu;
    private TextView txt_noidung_gioithieu;
    private TextView txt_noidung_thongtinchung;
    private TextView txt_vitrivieclam;
    private TextView txt_noidung_vitrivieclam;
    private TextView txt_noilamviec;
    private TextView txt_noidung_noilamviec;
    private WebView webView;
    private TextView txt_chuandaura;

    private ProgressDialog progDailog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cntt, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_title = view.findViewById(R.id.txt_title);
        txt_thongtinchung = view.findViewById(R.id.txt_thongtinchung);
        txt_noidung_thongtinchung = view.findViewById(R.id.txt_noidung_thongtinchung);
        txt_noidung_gioithieu = view.findViewById(R.id.txt_noidung_gioithieu);
        txt_gioithieu = view.findViewById(R.id.txt_gioithieu);
        txt_vitrivieclam = view.findViewById(R.id.txt_vitrivieclam);
        txt_noidung_vitrivieclam = view.findViewById(R.id.txt_noidung_vitrivieclam);
        txt_noilamviec = view.findViewById(R.id.txt_noilamviec);
        txt_noidung_noilamviec = view.findViewById(R.id.txt_noidung_noilamviec);
        webView = view.findViewById(R.id.webview_video);
        txt_chuandaura = view.findViewById(R.id.txt_chuandaura);

        txt_title.setText(Html.fromHtml(getResources().getString(R.string.title_cntt)));
        txt_thongtinchung.setText(Html.fromHtml(getResources().getString(R.string.thongtinchung)));
        txt_noidung_thongtinchung.setText(Html.fromHtml(getResources().getString(R.string.thongtinchung_cntt)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txt_noidung_thongtinchung.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        txt_gioithieu.setText(Html.fromHtml(getResources().getString(R.string.gioithieu)));
        txt_noidung_gioithieu.setText(Html.fromHtml(getResources().getString(R.string.gioithieu_cntt)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txt_noidung_gioithieu.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        txt_vitrivieclam.setText(Html.fromHtml(getResources().getString(R.string.vitrivieclam)));
        txt_noidung_vitrivieclam.setText(Html.fromHtml(getResources().getString(R.string.vitrivieclam_cntt)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txt_noidung_vitrivieclam.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        txt_noilamviec.setText(Html.fromHtml(getResources().getString(R.string.noilamviec)));
        txt_noidung_noilamviec.setText(Html.fromHtml(getResources().getString(R.string.noilamviec_cntt)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txt_noidung_noilamviec.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        progDailog = ProgressDialog.show(getActivity(), "Đang tải","Vui lòng đợi...", true);
        progDailog.setCancelable(false);

        CookieManager.getInstance().setAcceptCookie(true);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });
        webView.loadUrl("https://www.youtube.com/embed/cCfpQpj47Bk");

        txt_chuandaura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChuandauraFragment();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

//    public String downloadUrl(String myurl) throws IOException {
//        InputStream is = null;
//        try {
//            URL url = new URL(myurl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//            conn.connect();
//            is = conn.getInputStream();
//            return readIt(is);
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
//    }
//
//    public String readIt(InputStream stream) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            if (line.contains("fmt_stream_map")) {
//                sb.append(line).append("\n");
//                break;
//            }
//        }
//        reader.close();
//        String result = decode(sb.toString());
//        String[] url = result.split("\\|");
//        return url[1];
//    }
//
//    public String decode(String in) {
//        String working = in;
//        int index;
//        index = working.indexOf("\\u");
//        while (index > -1) {
//            int length = working.length();
//            if (index > (length - 6)) break;
//            int numStart = index + 2;
//            int numFinish = numStart + 4;
//            String substring = working.substring(numStart, numFinish);
//            int number = Integer.parseInt(substring, 16);
//            String stringStart = working.substring(0, index);
//            String stringEnd = working.substring(numFinish);
//            working = stringStart + ((char) number) + stringEnd;
//            index = working.indexOf("\\u");
//        }
//        return working;
//    }
}