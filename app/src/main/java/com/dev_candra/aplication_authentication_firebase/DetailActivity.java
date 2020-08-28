package com.dev_candra.aplication_authentication_firebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("Registered")
public class DetailActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth auth;
    private GoogleSignInClient client;
    private GoogleSignInOptions options;
    private Button btn_google1;
    private TextView textnama,textemail;
    private ImageView gambar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        btn_google1 = findViewById(R.id.btn_sign_out);
        textnama = findViewById(R.id.name);
        textemail = findViewById(R.id.email);
        gambar = findViewById(R.id.gambar);
        auth.signOut();
        Aksi();

    }

    private void requestCredential(){
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this,options);
    }

    private void Aksi(){
        requestCredential();
        initToolbar();
        Intent ambildata = getIntent();
        String nama = ambildata.getStringExtra("nama");
        String email = ambildata.getStringExtra("email");
        String photoUrl = ambildata.getStringExtra("photo");
        Glide.with(this)
                .load(photoUrl)
                .into(gambar);
        textnama.setText(nama);
        textemail.setText(email);

        btn_google1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
    }

    private void initToolbar(){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Candra Julius Sinaga");
            getSupportActionBar().setSubtitle("Detail Firebase Authentication");
        }
    }


    private void Logout(){
        client.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(DetailActivity.this, "Terjadi kesalahan pada system", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
