package com.example.carsrental;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link About#newInstance} factory method to
 * create an instance of this fragment.
 */
public class About extends Fragment {

    Intent intent = null;
    Button call,mail,sms;
    public About() {
        // Required empty public constructor
    }

    public static About newInstance(String param1, String param2) {
        return new About();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_about, container, false);
        sms=view.findViewById(R.id.message);
        call=view.findViewById(R.id.call);
        mail=view.findViewById(R.id.mail);
        sms.setOnClickListener(this::onClick);
        mail.setOnClickListener(this::onClick);
        call.setOnClickListener(this::onClick);
        return view;
    }


    public void showDialog(String type){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Message");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.message_layout, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {
            // send data from the AlertDialog to the Activity
            EditText editText = customLayout.findViewById(R.id.type_message);
            sendMessage(editText.getText().toString(),type);
        });
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {

        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void sendMessage(String message, String type){
        if (type.equals("sms")){
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "You don't have permission to make call", Toast.LENGTH_SHORT).show();
            }else {
                intent=new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:0644723038"));
                intent.putExtra("sms_body",message);
            }

        } else if (type.equals("mail")) {
            String[] recipients = {"abdourahmaneissoufou.ensa@uhp.ac.ma", "bado.ensa@uhp.ac.ma"};
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Renseignement");
            intent.putExtra(Intent.EXTRA_TEXT, message);
        }
        startActivity(intent);
    }

    public void onClick(View view) {
        if (view.getId()==R.id.call){
            Uri number = Uri.parse("tel:0644723038");
            intent=new Intent(Intent.ACTION_DIAL, number);
            startActivity(intent);
        }else if (view.getId()==R.id.mail){
            showDialog("mail");
        } else if (view.getId() == R.id.message) {
            showDialog("sms");
        }
    }
}