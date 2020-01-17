package kr.co.itforone.unihospital;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zoyi.channel.plugin.android.ChannelIO;
import com.zoyi.channel.plugin.android.ChannelPluginCompletionStatus;
import com.zoyi.channel.plugin.android.ChannelPluginSettings;

class ClientManager extends WebViewClient {
    Activity activity;
    MainActivity mainActivity;
    ClientManager(Activity activity){
        this.activity = activity;
    }
    ClientManager(Activity activity, MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(url.contains("test_chenneltalk.php")){
            mainActivity.openChannel();
        }

        else {
            view.loadUrl(url);
        }
        return true;
    }
}
