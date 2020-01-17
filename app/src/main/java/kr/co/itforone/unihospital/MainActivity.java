package kr.co.itforone.unihospital;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kakao.ad.common.json.CompleteRegistration;
import com.kakao.ad.tracker.KakaoAdTracker;
import com.zoyi.channel.plugin.android.ChannelIO;
import com.zoyi.channel.plugin.android.ChannelLauncherView;
import com.zoyi.channel.plugin.android.ChannelPluginCompletionStatus;
import com.zoyi.channel.plugin.android.ChannelPluginSettings;
import com.zoyi.channel.plugin.android.Guest;
import com.zoyi.channel.plugin.android.OnBootListener;


import butterknife.BindView;
import butterknife.ButterKnife;
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.webview)    WebView webView;
    //@BindView(R.id.refreshlayout)    SwipeRefreshLayout refreshlayout;
    private long backPrssedTime = 0;
    private ChannelPluginSettings chsettings = new ChannelPluginSettings("ad5e697f-5f64-4803-805a-f88f6ee45e60");
    Context context = this;
    private String chboot_id = "";
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
        webView.setWebViewClient(new ClientManager(this, this));
        webView.setWebChromeClient(new ChoromeManager(this, this));
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);//HTML5에서 DOM 사용여부
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        
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
                        ChannelIO.initPushToken(token);
                       // webView.loadUrl("javascript:set_token("+token+")");
                        // Log and toast
                    }
                });

//        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                webView.clearCache(true);
//                webView.reload();
//                refreshlayout.setRefreshing(false);
//            }
//        });
//
//        refreshlayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                if(webView.getScrollY() == 0){
//                    refreshlayout.setEnabled(true);
//                }
//                else{
//                    refreshlayout.setEnabled(false);
//                }
//            }
//        });
        webView.loadUrl(getString(R.string.index));
        ChannelIO.initialize(this.getApplication());
        ChannelIO.handlePushNotification(this);
    }
    //채널톡 오픈

    OnBootListener onBootListener = new OnBootListener() {

        @Override
        public void onCompletion(ChannelPluginCompletionStatus status, @Nullable Guest guest) {
            if (status == ChannelPluginCompletionStatus.SUCCESS) {
                ChannelIO.open(MainActivity.this);
                chboot_id = guest.getId();
            }
        }
    };

        public void openChannel() {
            ChannelIO.boot(chsettings, onBootListener);
//        ChannelIO.boot(chsettings, (status, user) -> {
//            if (status == ChannelPluginCompletionStatus.SUCCESS) {
//                ChannelIO.open(MainActivity.this);
//            }
//        });
        }

        //뒤로가기이벤트
        @Override
        public void onBackPressed() {

            if (webView.canGoBack()) {
                WebBackForwardList historyList = webView.copyBackForwardList();
                String backUrl = historyList.getItemAtIndex(historyList.getCurrentIndex() - 1).getUrl();
                if (backUrl.equals(getString(R.string.realindex))) {
                    webView.clearHistory();
                } else
                    webView.goBack();
            } else {
                long tempTime = System.currentTimeMillis();
                long intervalTime = tempTime - backPrssedTime;
                if (0 <= intervalTime && 2000 >= intervalTime) {
                    finish();
                } else {
                    backPrssedTime = tempTime;
                    Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누를시 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

