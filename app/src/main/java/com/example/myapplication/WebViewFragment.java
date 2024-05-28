package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class WebViewFragment extends Fragment {
    MainActivity mainActivity;
    private MyWebView webD;
    private MyWebView webA;
    private WebSettings mWebSettings;
    private SwipeRefreshLayout refreshLayoutA;
    private SwipeRefreshLayout refreshLayoutD;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.web_view_layout, container, false);

        webD = rootView.findViewById(R.id.web_d);
        webA = rootView.findViewById(R.id.web_a);

        webD.loadUrl("http://203.255.81.72:10021/dustsensor_v2/sensingpage/");
        webA.loadUrl("http://203.255.81.72:10021/airquality/sensingpage/");


        for(int i=0; i<2; i++) {
            if(i==0) {
                webA.setWebViewClient(new WebViewClient());
                mWebSettings = webA.getSettings();  // 웹뷰에서 webSettings를 사용할 수 있도록 함.
            }else{
                webD.setWebViewClient(new WebViewClient());
                mWebSettings = webD.getSettings();
            }
            mWebSettings.setJavaScriptEnabled(true); //웹뷰에서 javascript를 사용하도록 설정
            mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); //멀티윈도우 띄우는 것
            mWebSettings.setAllowFileAccess(true); //파일 엑세스
            mWebSettings.setLoadWithOverviewMode(true); // 메타태그
            mWebSettings.setUseWideViewPort(true); //화면 사이즈 맞추기
            mWebSettings.setSupportZoom(true); // 화면 줌 사용 여부
            mWebSettings.setBuiltInZoomControls(true); //화면 확대 축소 사용 여부
            mWebSettings.setDisplayZoomControls(true); //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
            mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 사용 재정의 value : LOAD_DEFAULT, LOAD_NORMAL, LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, or LOAD_CACHE_ONLY
            mWebSettings.setDefaultFixedFontSize(14); //기본 고정 글꼴 크기, value : 1~72 사이의 숫자

        }

        refreshLayoutA = rootView.findViewById(R.id.swipe_a);
        refreshLayoutD = rootView.findViewById(R.id.swipe_d);

        refreshLayoutA.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webA.reload();
                refreshLayoutA.setRefreshing(false);
            }
        });
        refreshLayoutD.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webD.reload();
                refreshLayoutD.setRefreshing(false);
            }
        });


        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

}
