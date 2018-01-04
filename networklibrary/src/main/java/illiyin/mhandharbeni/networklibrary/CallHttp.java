package illiyin.mhandharbeni.networklibrary;

import android.content.Context;

import com.google.firebase.crash.FirebaseCrash;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import illiyin.mhandharbeni.httpcalllibrary.AndroidCall;
import okhttp3.RequestBody;

/**
 * Created by root on 17/07/17.
 */

public class CallHttp {
    private AndroidCall androidCall;

    public CallHttp(Context context) {
        Context context1 = context;
        androidCall = new AndroidCall(context1);
    }
    public String get(String url){
        if (isOnline()){
            try {
                return androidCall.get(url);
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }
        }else{
            return null;
        }
        return null;
    }
    public String post(String url, RequestBody requestBody){
        if (isOnline()){
            try {
                return androidCall.post(url, requestBody);
            } catch (Exception e) {
                FirebaseCrash.report(e);
            }
        }else{
            return null;
        }
        return null;
    }

    private boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("45.32.105.117", 2017);

            sock.connect(sockaddr, timeoutMs);
            sock.setKeepAlive(false);
            sock.setReuseAddress(false);
            sock.close();

            return true;
        } catch (Exception e) {
            FirebaseCrash.report(e);
            return false;
        }
    }
}
