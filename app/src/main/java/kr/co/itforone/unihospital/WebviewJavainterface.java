package kr.co.itforone.unihospital;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

class WebviewJavainterface {

    Activity activity;
    MainActivity mainActivity;


    WebviewJavainterface (MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }
    WebviewJavainterface(Activity activity){
        this.activity=activity;
    }

    @JavascriptInterface
    public void call_app() {
        String number ="";
          number = "tel:051-891-0088";
        mainActivity.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(number)));
    }
    @JavascriptInterface
    public void share(int idx) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://unihospital.itforone.co.kr/bbs/board.php?bo_table=clinic&wr_id="+idx);
        Intent chooser = Intent.createChooser(intent, "공유하기");
        mainActivity.startActivity(chooser);
    }
    @JavascriptInterface
    public void share2(int idx) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://unihospital.itforone.co.kr/bbs/board.php?bo_table=event&wr_id="+idx);
        Intent chooser = Intent.createChooser(intent, "공유하기");
        mainActivity.startActivity(chooser);
    }
    @JavascriptInterface
    public void testback(String token) {
        Toast.makeText(mainActivity.getApplicationContext(),token,Toast.LENGTH_LONG).show();
    }



}
