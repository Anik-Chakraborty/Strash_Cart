package com.example.strash_cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class feedback extends AppCompatActivity {
    ImageView btn_back_feedback;
    Button btn_feedback_send;
    EditText edit_text_feedback;
    String recieverEmail, feedback_msg, feedback_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btn_back_feedback = findViewById(R.id.btn_back_feedback);
        btn_feedback_send = findViewById(R.id.btn_feedback_send);
        edit_text_feedback = findViewById(R.id.edit_text_feedback);

        recieverEmail = "canik104@gmail.com,devchat000@gmail.com";

        feedback_subject = "Feedback On StrashCart";


        btn_back_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btn_feedback_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback_msg = edit_text_feedback.getText().toString();
                if(TextUtils.isEmpty(feedback_msg)){
                    edit_text_feedback.setError("Message Box is Empty");
                    edit_text_feedback.requestFocus();
                }
                else{
                    sendMail();
                }
            }
        });


    }


    private void sendMail() {
        String recipientList = recieverEmail.toString();
        String[] recipients = recipientList.split(",");

        String subject = feedback_subject.toString();
        String message = feedback_msg.toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
}

}