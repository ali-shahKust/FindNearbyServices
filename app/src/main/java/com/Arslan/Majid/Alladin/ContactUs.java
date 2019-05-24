package com.Arslan.Majid.Alladin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;


public class ContactUs extends Fragment {
    private EditText name,email,message,subject;
    public ContactUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact_us, container, false);

        name= (EditText)view.findViewById(R.id.name);
        email= (EditText)view.findViewById(R.id.email);
        subject= (EditText)view.findViewById(R.id.subject);
        message= (EditText)view.findViewById(R.id.msg);

        Button fab = (Button) view.findViewById(R.id.send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTestEmail();
            }
        });

        return view;
    }
    private void sendTestEmail(){
        BackgroundMail.newBuilder(getActivity())
                .withUsername("mustafvi1122@gmail.com")
                .withPassword("Alberuni@3600")
                .withMailto("adeelhayat@gmail.com")
                .withSubject(subject.getText().toString())
                .withBody("Name:   "+name.getText().toString()+"\nEmail:  "+email.getText().toString()+"\nMessage:  "+message.getText().toString())
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
    }
    //feedback@pakistandoingbusiness.com

}
