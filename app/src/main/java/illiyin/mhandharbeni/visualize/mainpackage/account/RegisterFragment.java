package illiyin.mhandharbeni.visualize.mainpackage.account;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import illiyin.mhandharbeni.databasemodule.AdapterModel;
import illiyin.mhandharbeni.sessionlibrary.Session;
import illiyin.mhandharbeni.sessionlibrary.SessionListener;
import illiyin.mhandharbeni.utilslibrary.SnackBar;
import illiyin.mhandharbeni.utilslibrary.SnackBarListener;
import illiyin.mhandharbeni.visualize.R;

/**
 * Created by root on 10/23/17.
 */

public class RegisterFragment extends Fragment implements SnackBarListener {
    private View v;
    private TextView tologin;
    private TextInputEditText nama, alamat, notelp, username, password;
    private Button do_register;

    private AdapterModel adapterModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.__mainactivty_layout_register, container, false);
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
        adapterModel = new AdapterModel(getActivity().getApplicationContext());
        Session session = new Session(getActivity().getApplicationContext(), new SessionListener() {
            @Override
            public void sessionChange() {

            }
        });
    }

    private void fetch_element(){
        nama = v.findViewById(R.id.nama);
        alamat = v.findViewById(R.id.alamat);
        notelp = v.findViewById(R.id.notelp);
        username = v.findViewById(R.id.username);
        password = v.findViewById(R.id.password);
        do_register = v.findViewById(R.id.do_register);
        tologin= v.findViewById(R.id.txttologin);
    }

    private void fetch_event(){
        do_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    /*do login*/
                    try {
                        do_register.setEnabled(false);
                        doLogin();
                    } catch (Exception e) {
                        do_register.setEnabled(true);
                        e.printStackTrace();
                    }
                }
            }
        });
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new LoginFragment());
            }
        });
    }

    private Boolean validate(){
        if(nama.getText().toString().isEmpty()){
            nama.setError(getString(R.string.placeholder_textkosong));
            nama.requestFocus();
            return false;
        }else if(alamat.getText().toString().isEmpty()){
            alamat.setError(getString(R.string.placeholder_textkosong));
            alamat.requestFocus();
            return false;
        }else if(notelp.getText().toString().isEmpty()){
            notelp.setError(getString(R.string.placeholder_textkosong));
            notelp.requestFocus();
            return false;
        }else if (username.getText().toString().isEmpty()){
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

    private void doLogin() throws Exception {
        String response = adapterModel.register(nama.getText().toString(), alamat.getText().toString(), notelp.getText().toString(), username.getText().toString(), password.getText().toString());
        if (response.equalsIgnoreCase(getString(R.string.placeholder_loginberhasil))){
            /* set state login */
            do_register.setEnabled(true);
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
        do_register.setEnabled(true);
    }
    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }
}
