package ru.serv_techno.sandwichclub03;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private UserProfile userProfile;

    EditText ProfileNameEditText;
    EditText ProfilePhoneInputLayout;
    EditText ProfileAddressEditText;
    Button ProfileBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Ваш профиль");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ProfileNameEditText = (EditText)findViewById(R.id.ProfileNameEditText);
        ProfilePhoneInputLayout = (EditText)findViewById(R.id.ProfilePhoneInputLayout);
        ProfileAddressEditText = (EditText)findViewById(R.id.ProfileAddressEditText);
        ProfileBtnSave = (Button)findViewById(R.id.ProfileBtnSave);
        ProfileBtnSave.setOnClickListener(this);

        loadData();
    }

    private void loadData(){
        List<UserProfile> userProfiles = UserProfile.listAll(UserProfile.class);
        if(userProfiles.size()!=0){
            userProfile = userProfiles.get(0);
            if(userProfile!=null){
                ProfileNameEditText.setText(userProfile.name);
                ProfilePhoneInputLayout.setText(userProfile.phone);
                ProfileAddressEditText.setText(userProfile.address);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ProfileBtnSave:
                String nameProfile = ProfileNameEditText.getText().toString();
                String phoneProfile = ProfilePhoneInputLayout.getText().toString();
                String addressProfile = ProfileAddressEditText.getText().toString();

                if(nameProfile.equals("")){
                    MySnackbar.ShowMySnackbar(v, "Укажите имя и повторите попытку!", R.color.SnackbarBg);
                    break;
                }
                if(phoneProfile.equals("")){
                    MySnackbar.ShowMySnackbar(v, "Укажите телефон и повторите попытку!", R.color.SnackbarBg);
                    break;
                }
                if(addressProfile.equals("")){
                    MySnackbar.ShowMySnackbar(v, "Укажите адрес доставки и повторите попытку!", R.color.SnackbarBg);
                    break;
                }
                if(userProfile!=null){
                    userProfile.name = nameProfile;
                    userProfile.phone =  phoneProfile;
                    userProfile.address = addressProfile;
                }else{
                    userProfile = new UserProfile(nameProfile, phoneProfile, addressProfile);
                }
                try{
                    userProfile.save();
                    MySnackbar.ShowMySnackbar(v, "Профиль успешно сохранен!", R.color.SnackbarBg);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                catch (Exception e){
                    Log.e(String.valueOf(R.string.app_name), e.getMessage());
                    e.printStackTrace();
                }
            default:
                break;
        }
    }
}
