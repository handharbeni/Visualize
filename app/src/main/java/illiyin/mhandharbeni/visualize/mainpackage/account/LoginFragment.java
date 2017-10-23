package illiyin.mhandharbeni.visualize.mainpackage.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.utilslibrary.SnackBar;
import illiyin.mhandharbeni.utilslibrary.SnackBarListener;
import illiyin.mhandharbeni.visualize.NavActivity;
import illiyin.mhandharbeni.visualize.R;
import illiyin.mhandharbeni.visualize.SplashActivity;

/**
 * Created by root on 10/23/17.
 */

public class LoginFragment extends Fragment implements SnackBarListener {
    private static final String TAG = "LoginFragment";
    private View v;
    private TextView toregister;
    private TextInputEditText username, password;
    private Button do_login;

    private AdapterModel adapterModel;
    private Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fetch_modul();

        v = inflater.inflate(R.layout.__mainactivity_layout_login, container, false);

        fetch_element();
        fetch_event();
        return v;
    }

    private void fetch_modul(){
        adapterModel = new AdapterModel(getActivity().getApplicationContext());
        session = new Session(getActivity().getApplicationContext(), new SessionListener() {
            @Override
            public void sessionChange() {

            }
        });
    }

    private void fetch_element(){
        username = v.findViewById(R.id.username);
        password = v.findViewById(R.id.password);
        do_login = v.findViewById(R.id.do_login);
        toregister = v.findViewById(R.id.txttoregister);
    }

    private void fetch_event(){
        do_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    /*do login*/
                    try {
                        do_login.setEnabled(false);
                        doLogin();
                    } catch (JSONException e) {
                        do_login.setEnabled(true);
                        Log.d(TAG, "onClick: "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
        toregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new RegisterFragment());
            }
        });
    }

    private Boolean validate(){
        if (username.getText().toString().isEmpty()){
            username.setError(getString(R.string.placeholder_textkosong));
            username.requestFocus();
            return false;
        }else if(password.getText().toString().isEmpty()){
            password.setError(getString(R.string.placeholder_textkosong));
            password.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private void doLogin() throws JSONException {
        String response = adapterModel.login(username.getText().toString(), password.getText().toString());
        showMessage(response);
        if (response.equalsIgnoreCase(getString(R.string.placeholder_loginberhasil))){
            /* set state login */
            showMessage(response);
            do_login.setEnabled(true);
            startActivity(new Intent(getActivity().getApplicationContext(), NavActivity.class));
            getActivity().finish();
        }else{
            /* show error message */
            showMessage(response);
        }
    }

    private void showMessage(String message){
        new SnackBar(getActivity().getApplicationContext())
                .view(v)
                .message(message)
                .build()
                .listener(this)
                .show();
    }

    @Override
    public void onDismiss() {
        do_login.setEnabled(true);
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }
}
