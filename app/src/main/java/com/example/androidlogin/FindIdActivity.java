package com.example.androidlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FindIdActivity extends AppCompatActivity {

    // 파이어베이스 인증 객체 생성
    private FirebaseFirestore firebaseFirestore;

    // 작성한 이름 값과 전화번호 값을 저장할 객체 생성
    private EditText editTextName;
    private EditText editTextPhone;

    private String sendname = "";
    private String sendphone = "";

    // 회원의 이메일을 보여줄 textview 객체 생성
    private TextView textshowid;
    private  TextView textshowtext;

    // 로그인 화면으로 돌아가기 위한 버튼 객체 생성
    private Button btngotologin;

    // 사용자에게 실시간 진행상태를 알려주는 ProgressDialog 객체 생성
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findid);

        // id가 write_email인 editText에 대한 메서드 저장
        editTextName = findViewById(R.id.write_name);
        // id가 write_phone인 editText에 대한 메서드 저장
        editTextPhone = findViewById(R.id.write_phone);
        // id가 tv_showid인 textview에 대한 메서드 저장
        textshowid = findViewById(R.id.tv_showid);
        // id가 tv_showtext인 textview에 대한 메서드 저장
        textshowtext = findViewById(R.id.tv_showtext);

        // 로그인 화면으로 돌아가는 버튼에 대한 메서드 저장
        btngotologin = findViewById(R.id.btn_gotologin);

        // progressDialog 객체 선언
        progressDialog = new ProgressDialog(this);

        btngotologin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 이메일 찾기 버튼을 누르면 이메일 찾기 레이아웃으로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void findEmail(View view)
    {
        // editText에 작성한 내용을 String으로 변환하여 객체에 저장
        sendname = editTextName.getText().toString();
        sendphone = editTextPhone.getText().toString();
        // findid 메서드 실행
        findid(sendname, sendphone);
    }



    // 아이디 찾기 메서드
    private void findid(final String sendname, final String sendphone) {
        progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
        progressDialog.show();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                // doument의 id와 입력한 이름이 같고 저장된 휴대폰번호와 입력한 휴대폰 번호가 같으면 입력한 이름의 저장된 이메일을 보여줌
                                if (sendname.equals(document.getId()) && sendphone.equals(document.getData().get("phone"))) {
                                    textshowtext.setText("회원님의 이메일은 다음과 같습니다.");
                                    textshowid.setText(document.getData().get("email").toString());
                                    btngotologin.setVisibility(View.VISIBLE);
                                }
                                else{
                                    textshowtext.setText("일치하는 회원정보가 없습니다.");
                                }
                            }

                        }

                        // Dialog 사라짐
                        progressDialog.dismiss();
                    }
                });

    }


}
