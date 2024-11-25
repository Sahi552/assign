package com.example.login_page.fragement;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.login_page.R;
import com.example.login_page.win_certificate;

public class CertificateFragment extends Fragment {

    Button wincertificate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_certificate, container, false);

        wincertificate = view.findViewById(R.id.winbutton);
        wincertificate.setOnClickListener(
                v -> {
                    Intent i  = new Intent(getActivity(), win_certificate.class);
                    startActivity(i);
                }
        );

        return view;
    }
}