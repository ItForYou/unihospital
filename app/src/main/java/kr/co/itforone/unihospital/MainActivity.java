package kr.co.itforone.unihospital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kakao.ad.common.json.CompleteRegistration;
import com.kakao.ad.tracker.KakaoAdTracker;

import butterknife.BindView;
import butterknife.ButterKnife;
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.webview)    WebView webView;
    @BindView(R.id.refreshlayout)  SwipeRefreshLayout refreshlayout;
    private long backPrssedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!KakaoAdTracker.isInitialized()) {
            KakaoAdTracker.getInstance().init(getApplicationContext(), getString(R.string.kakao_ad_track_id));
        }

        CompleteRegistration event = new CompleteRegistration();
        event.tag = "Tag"; // 분류
        KakaoAdTracker.getInstance().sendEvent(event);

        Intent splash = new Intent(MainActivity.this,SplashActivity.class);
        startActivity(splash);

        webView.addJavascriptInterface(new WebviewJavainterface(this),"Android");
        webView.setWebViewClient(new ClientManager(this));
        webView.setWebChromeClient(new ChoromeManager(this, this));
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("D", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        webView.loadUrl("javascript:set_token("+token+")");
                        // Log and toast
                    }
                });


        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.clearCache(true);
                webView.reload();
                refreshlayout.setRefreshing(false);
            }
        });

        refreshlayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(webView.getScrollY() == 0){
                    refreshlayout.setEnabled(true);
                }
                else{
                    refreshlayout.setEnabled(false);
                }
            }
        });

        webView.loadUrl(getString(R.string.index));
        webView.clearHistory();
        webView.clearCache(true);
    }

    //뒤로가기이벤트
    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPrssedTime;
            if (0 <= intervalTime && 2000 >= intervalTime){
                finish();
            }
            else
            {
                backPrssedTime = tempTime;
                Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누를시 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
