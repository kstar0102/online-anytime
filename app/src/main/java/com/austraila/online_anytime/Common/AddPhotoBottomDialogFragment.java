package com.austraila.online_anytime.Common;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.austraila.online_anytime.R;
import com.austraila.online_anytime.activitys.FormActivity;
import com.austraila.online_anytime.activitys.cameraActivity.CameraActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.austraila.online_anytime.activitys.cameraActivity.CameraActivity.Image_Capture_Code;

public class AddPhotoBottomDialogFragment extends BottomSheetDialogFragment {
    TextView photoIcon, localIcon;
    String strtext,formDes,formtitle, elementId;
    private Uri imageUri;

    public static AddPhotoBottomDialogFragment newInstance() {
        return new AddPhotoBottomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        strtext = getArguments().getString("id");
        formDes = getArguments().getString("formDes");
        formtitle = getArguments().getString("formtitle");
        elementId = getArguments().getString("elementId");
        View view = inflater.inflate(R.layout.layout_photo_bottom_sheet, container,false);
        photoIcon = view.findViewById(R.id.tv_btn_add_photo_camera);
        localIcon = view.findViewById(R.id.tv_btn_add_photo_gallery);

        photoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "photo", Toast.LENGTH_LONG).show();
                try {Intent intent = new Intent(getActivity(), FormActivity.class);
                intent.putExtra("camera", "camera");
                intent.putExtra("id", strtext);
                intent.putExtra("des", formDes);
                intent.putExtra("title", formtitle);
                intent.putExtra("elementid", elementId);
                Toast.makeText(getContext(), elementId, Toast.LENGTH_LONG).show();
                startActivity(intent);
                dismiss();}catch (Exception e){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Catch the error");
                    alertDialog.setMessage("The camera url add the Photo error:" + e.getMessage());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        localIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 7);
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
                    Uri uri = data.getData();
                    String src = uri.getPath();

                    Uri PathHolder = data.getData();
                    File file = new File(PathHolder.getPath());
                    String path = file.getAbsolutePath();
                    Intent intent = new Intent(getActivity(), FormActivity.class);
                    intent.putExtra("filepath", src);
                    intent.putExtra("id", strtext);
                    intent.putExtra("des", formDes);
                    intent.putExtra("title", formtitle);
                    startActivity(intent);
                }
                break;
        }
    }

}