package com.b1610701.tuvantuyensinh.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.b1610701.tuvantuyensinh.MainActivity;
import com.b1610701.tuvantuyensinh.R;

public class ChuandauraFragment extends Fragment {

    private WebView webView;
    private String URL;
    private ProgressDialog progDailog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chuandaura, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = view.findViewById(R.id.webview);

        switch (MainActivity.NGANH){
            case "cntt":
                URL = "http://www.lieutri.tk/congnghethongtin.html";
                break;
            case "kdnn":
                URL = "http://www.lieutri.tk/kinhdoanhnongnghiep.html";
                break;
            case "ktnn":
                URL = "http://www.lieutri.tk/kinhtenongnghiep.html";
                break;
            case "ktxd":
                URL = "http://www.lieutri.tk/kythuatxaydung.html";
                break;
            case "lhc":
                URL = "http://www.lieutri.tk/luat.html";
                break;
            case "nna":
                URL = "http://www.lieutri.tk/ngonnguanh.html";
                break;
            case "qtkd":
                URL = "http://www.lieutri.tk/quantrikinhdoanh.html";
                break;
            case "vnh":
                URL = "http://www.lieutri.tk/vietnamhoc.html";
                break;
            default: URL = "http://www.lieutri.tk/";
        }

        progDailog = ProgressDialog.show(getActivity(), "Đang tải","Vui lòng đợi...", true);
        progDailog.setCancelable(false);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
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
        webView.loadUrl(URL);
    }
}