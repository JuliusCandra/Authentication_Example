package com.dev_candra.aplication_authentication_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInAccount account;
    private GoogleSignInOptions options;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private SignInButton button_google;
    private GoogleSignInClient client;
    private static final int RC_SIGN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       button_google = findViewById(R.id.buttonGoogle);
       auth = FirebaseAuth.getInstance();
       Aksi();
    }

    private void initToolbar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Candra Julius Sinaga");
            getSupportActionBar().setSubtitle("Home Firebase Authentication");
        }
    }

    private void Aksi(){
        requestCrendtail();
        button_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        initToolbar();
    }

    private void requestCrendtail(){
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .build();
        client = GoogleSignIn.getClient(this,options);
    }

    private void signIn() {
        Intent enterToWithGoogle = client.getSignInIntent();
        startActivityForResult(enterToWithGoogle,RC_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RC_SIGN == requestCode){
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = accountTask.getResult(ApiException.class);
                FirebsaeWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(this, "Gagal login", Toast.LENGTH_SHORT).show();
        }
    }

    private void FirebsaeWithGoogle(GoogleSignInAccount account) {
        final AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user1 = auth.getCurrentUser();
                    UpdateUi(user1);
                }else{
                    UpdateUi(null);
                }
            }
        });
    }

    private void UpdateUi(FirebaseUser user1) {
        if (user1 != null){
            String username = user1.getDisplayName();
            String emailUser = user1.getEmail();
            String photoUrl = Objects.requireNonNull(user1.getPhotoUrl()).toString();
            Intent toMaintUtama = new Intent(MainActivity.this,DetailActivity.class);
            toMaintUtama.putExtra("nama",username);
            toMaintUtama.putExtra("email",emailUser);
            toMaintUtama.putExtra("photo",photoUrl);
            startActivity(toMaintUtama);
        }else{
            Toast.makeText(MainActivity.this, "Anda belum login", Toast.LENGTH_SHORT).show();
        }
    }


}
