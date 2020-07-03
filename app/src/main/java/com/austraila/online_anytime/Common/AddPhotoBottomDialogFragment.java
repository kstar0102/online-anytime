package com.austraila.online_anytime.Common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.austraila.online_anytime.R;
import com.austraila.online_anytime.activitys.FormActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class AddPhotoBottomDialogFragment extends BottomSheetDialogFragment {
    TextView photoIcon, localIcon;

    public static AddPhotoBottomDialogFragment newInstance() {
        return new AddPhotoBottomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_photo_bottom_sheet, container,
                false);
        photoIcon = view.findViewById(R.id.tv_btn_add_photo_camera);
        localIcon = view.findViewById(R.id.tv_btn_add_photo_gallery);

        photoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "photo", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), FormActivity.class);
                intent.putExtra("camera", "camera");
                startActivity(intent);
                dismiss();
            }
        });

        localIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);
            }
        });

        // get the views and attach the listener
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){
            case 7:
                if(resultCode==RESULT_OK){
                    Uri PathHolder = data.getData();
                    File file = new File(PathHolder.getPath());
                    String path = file.getAbsolutePath();
                    Intent intent = new Intent(getActivity(), FormActivity.class);
                    intent.putExtra("filepath", path);
                    startActivity(intent);
                }
                break;
        }
    }
}