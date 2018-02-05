package illiyin.mhandharbeni.visualize.mainpackage.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.utilslibrary.SnackBar;
import illiyin.mhandharbeni.utilslibrary.SnackBarListener;
import illiyin.mhandharbeni.visualize.NavActivity;
import illiyin.mhandharbeni.visualize.R;

/**
 * Created by root on 10/23/17.
 */

public class LoginFragment extends Fragment implements SnackBarListener {
    private View v;
    private TextView toregister;
    private TextInputEditText username, password;
    private Button do_login;

    private AdapterModel adapterModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(illiyin.mhandharbeni.visualize.R.layout.__mainactivity_layout_login, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetch_modul();
        fetch_element();
        fetch_event();
    }

    private void fetch_modul(){
        MobileAds.initialize(this, getString(R.string.admobid));
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(R.string.admobunit);

        adapterModel = new AdapterModel(getActivity().getApplicationContext());
    }

    private void fetch_element(){
        username = v.findViewById(illiyin.mhandharbeni.visualize.R.id.username);
        password = v.findViewById(illiyin.mhandharbeni.visualize.R.id.password);
        do_login = v.findViewById(illiyin.mhandharbeni.visualize.R.id.do_login);
        toregister = v.findViewById(illiyin.mhandharbeni.visualize.R.id.txttoregister);
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
                    } catch (Exception e) {
                        do_login.setEnabled(true);
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
            username.setError(getString(illiyin.mhandharbeni.visualize.R.string.placeholder_textkosong));
            username.requestFocus();
            return false;
        }else if(password.getText().toString().isEmpty()){
            password.setError(getString(illiyin.mhandharbeni.visualize.R.string.placeholder_textkosong));
            password.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private void doLogin() throws Exception {
        String response = adapterModel.login(username.getText().toString(), password.getText().toString());
        showMessage(response);
        if (response.equalsIgnoreCase(getString(illiyin.mhandharbeni.visualize.R.string.placeholder_loginberhasil))){
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
        fragmentTransaction.replace(illiyin.mhandharbeni.visualize.R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }
}
