package kr.co.itforone.unihospital;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Message;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

class ChoromeManager extends WebChromeClient {
    MainActivity mainActivity;
    Activity activity;

    ChoromeManager(MainActivity mainActivity, Activity activity){
        this.mainActivity = mainActivity;
        this.activity = activity;
    }

    ChoromeManager(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    ChoromeManager(){}
    //팝업처리
    @Override
    public boolean onCreateWindow(WebView view, boolean dialog,
                                  boolean userGesture, Message resultMsg) {
        // TODO Auto-generated method stub

        return true;
    }
    //어럴트 창 처리
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        new AlertDialog.Builder(view.getContext())
                .setTitle("")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                .create()
                .show();
        return true;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
    {
        new AlertDialog.Builder(view.getContext())
                .setTitle("")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                result.confirm();
                            }
                        })
                .setCancelable(false)
                .create()
                .show();

        return true;
    }
}
