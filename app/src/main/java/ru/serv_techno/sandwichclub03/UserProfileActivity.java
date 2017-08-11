package ru.serv_techno.sandwichclub03;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import ru.serv_techno.sandwichclub03.adapters.SpinnerAdapter;
import ru.serv_techno.sandwichclub03.models.Address;
import ru.serv_techno.sandwichclub03.models.UserProfile;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private UserProfile userProfile;

    EditText ProfileNameEditText;
    EditText ProfilePhoneEditText;
    Button ProfileBtnSave;

    Address DefaultAddress;

    Spinner spinner;
    TextView AddAddress;
    TextView EditAddress;
    List<Address> addressList;
    SpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Ваш профиль");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ProfileNameEditText = (EditText) findViewById(R.id.ProfileNameEditText);
        ProfilePhoneEditText = (EditText) findViewById(R.id.ProfilePhoneEditText);

        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("+7-___-___-__-__");
        FormatWatcher formatWatcher = new MaskFormatWatcher( // форматировать текст будет вот он
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(ProfilePhoneEditText);

        ProfileBtnSave = (Button) findViewById(R.id.ProfileBtnSave);
        ProfileBtnSave.setOnClickListener(this);
        AddAddress = (TextView) findViewById(R.id.AddAddress);
        AddAddress.setOnClickListener(this);
        EditAddress = (TextView) findViewById(R.id.EditAddress);
        EditAddress.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.SpinnerAddress);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DefaultAddress = addressList.get(position);
                setAddress(DefaultAddress);
                spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        loadData();
    }

    private void loadData() {
        List<UserProfile> userProfiles = UserProfile.listAll(UserProfile.class);
        if (userProfiles.size() != 0) {
            userProfile = userProfiles.get(0);
            if (userProfile != null) {
                ProfileNameEditText.setText(userProfile.name);
                ProfilePhoneEditText.setText(userProfile.phone);
                DefaultAddress = userProfile.address;
            }
        }

        addressList = Address.listAll(Address.class);
        spinnerAdapter = new SpinnerAdapter(getApplicationContext(), addressList);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        if (DefaultAddress != null) {
            setAddress(DefaultAddress);
        }
    }

    public void setAddress(Address address) {
        for(int i=0;i<spinnerAdapter.addressList.size();i++){
            if(spinnerAdapter.getItem(i).address.equals(address.address)){
                spinner.setSelection(i);
                spinner.setPrompt(address.address);
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
        switch (v.getId()) {
            case R.id.AddAddress:
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.address_dialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Создание адреса доставки")
                        .setCancelable(true)
                        .setIcon(R.mipmap.ic_launcher)
                        .setView(promptsView);

                final EditText AlertAddress = (EditText) promptsView.findViewById(R.id.AlertAddress);
                final EditText AlertDescription = (EditText) promptsView.findViewById(R.id.AlertDescription);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(AlertAddress.getText().equals("")){
                                    return;
                                }
                                if(AlertDescription.getText().equals("")){
                                    return;
                                }

                                Address newAddress = new Address(AlertDescription.getText().toString(), AlertAddress.getText().toString());
                                newAddress.save();

                                addressList = Address.listAll(Address.class);
                                spinnerAdapter = new SpinnerAdapter(getApplicationContext(), addressList);
                                spinner.setAdapter(spinnerAdapter);
                                spinnerAdapter.notifyDataSetChanged();
                                DefaultAddress = newAddress;
                                if (DefaultAddress != null) {
                                    setAddress(DefaultAddress);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;
            case R.id.EditAddress:
                if(DefaultAddress == null){
                    return;
                }

                LayoutInflater linflater = LayoutInflater.from(this);
                View promptsViewEdit = linflater.inflate(R.layout.address_dialog, null);

                AlertDialog.Builder builderEdit = new AlertDialog.Builder(this);
                builderEdit.setTitle("Редактирование адреса")
                        .setCancelable(true)
                        .setIcon(R.mipmap.ic_launcher)
                        .setView(promptsViewEdit);

                final EditText AlertAddressEdit = (EditText) promptsViewEdit.findViewById(R.id.AlertAddress);
                final EditText AlertDescriptionEdit = (EditText) promptsViewEdit.findViewById(R.id.AlertDescription);

                AlertAddressEdit.setText(DefaultAddress.address);
                AlertDescriptionEdit.setText(DefaultAddress.desc);

                builderEdit.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(AlertAddressEdit.getText().equals("")){
                            return;
                        }
                        if(AlertDescriptionEdit.getText().equals("")){
                            return;
                        }

                        DefaultAddress.address = AlertAddressEdit.getText().toString();
                        DefaultAddress.desc = AlertDescriptionEdit.getText().toString();
                        DefaultAddress.save();

                        addressList = Address.listAll(Address.class);
                        spinnerAdapter = new SpinnerAdapter(getApplicationContext(), addressList);
                        spinner.setAdapter(spinnerAdapter);
                        spinnerAdapter.notifyDataSetChanged();
                        if (DefaultAddress != null) {
                            setAddress(DefaultAddress);
                        }
                    }
                })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });

                AlertDialog alertDialogEdit = builderEdit.create();
                alertDialogEdit.show();

                break;
            case R.id.ProfileBtnSave:
                String nameProfile = ProfileNameEditText.getText().toString();
                String phoneProfile = ProfilePhoneEditText.getText().toString();

                if (nameProfile.equals("")) {
                    MySnackbar.ShowMySnackbar(v, "Укажите имя и повторите попытку!", R.color.SnackbarBg);
                    break;
                }
                if (phoneProfile.equals("")) {
                    MySnackbar.ShowMySnackbar(v, "Укажите телефон и повторите попытку!", R.color.SnackbarBg);
                    break;
                }
                if (DefaultAddress == null) {
                    MySnackbar.ShowMySnackbar(v, "Укажите адрес доставки и повторите попытку!", R.color.SnackbarBg);
                    break;
                }
                if (userProfile != null) {
                    userProfile.name = nameProfile;
                    userProfile.phone = phoneProfile;
                    userProfile.address = DefaultAddress;
                } else {
                    userProfile = new UserProfile(nameProfile, phoneProfile, DefaultAddress);
                }
                try {
                    userProfile.save();
                    MySnackbar.ShowMySnackbar(v, "Профиль успешно сохранен!", R.color.SnackbarBg);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    Log.e(String.valueOf(R.string.app_name), e.getMessage());
                    e.printStackTrace();
                    break;
                }
            default:
                break;
        }
    }
}