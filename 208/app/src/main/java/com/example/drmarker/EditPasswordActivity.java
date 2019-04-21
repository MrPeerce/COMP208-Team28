package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drmarker.RealmModule.UserModule;
import com.example.drmarker.stepModel.SuccessTransaction;
import com.example.drmarker.stepModel.UserTransaction;
import com.example.drmarker.userModel.User;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class EditPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_newPassword;
    public static String uid;
    private RealmConfiguration userDB;
    private User loginUser;
    private Realm mRealm;
    RealmAsyncTask realmAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);
        getInfo();
        //Get the intent from the input activity
        Intent fromAbove = getIntent();
        uid= fromAbove.getStringExtra("uid");
        userDB = new RealmConfiguration.Builder()
                .name("user_db").schemaVersion(2).modules(new UserModule())
                .build();
        mRealm=Realm.getInstance(userDB);
        RealmResults<User> userlist = mRealm.where(User.class).equalTo("uid", uid).findAll();
        loginUser = userlist.get(0);
    }

    private void getInfo(){
        //Initialize the button
        Button button_edit = findViewById(R.id.bt_edit);
        Button button_back = findViewById(R.id.bt_back);
        button_edit.setOnClickListener(this);
        button_back.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_edit:
                //Initialize an intent to the receive class
                Intent intent_editPassword=new Intent(EditPasswordActivity.this, MeActivity.class);
                intent_editPassword.putExtra("uid",getIntent().getStringExtra("uid"));
                //Get the input from text view
                et_newPassword=findViewById(R.id.et_edit_password);
                String newPassword=et_newPassword.getText().toString();

                realmAsyncTask = mRealm.executeTransactionAsync(
                        new UserTransaction(loginUser.getName(), newPassword, loginUser.getUid()),
                        new SuccessTransaction(realmAsyncTask),
                        new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                error.printStackTrace();
                                Log.d("realm", "insert error");
                            }
                        }
                );
                mRealm.close();
                Toast.makeText(this, "Password successfully changed", Toast.LENGTH_LONG).show();
                startActivity(intent_editPassword);
                finish();
                break;

            case R.id.bt_back:
                Intent intent_back=new Intent(EditPasswordActivity.this, MeActivity.class);
                intent_back.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent_back);
                finish();
        }
    }
}