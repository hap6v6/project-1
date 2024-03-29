package com.example.studyproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class WeeklyFragmentTmp extends Fragment {
    private View weeklyView;
    private RecyclerView myWeeklyList;
    private DatabaseReference WeeklyRef, WeekRef;
    private Uri imgUri, photoUri;
    private String mCurrentPhotoPath, downloadUrl;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private int flag = 0;
    private ImageView iv_photo;
    private FirebaseStorage storage;


    public WeeklyFragmentTmp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        weeklyView = (View) inflater.inflate(R.layout.fragment_weekly_tmp, container, false);

        myWeeklyList = (RecyclerView) weeklyView.findViewById(R.id.recyclerviewWeekly);
        myWeeklyList.setLayoutManager(new LinearLayoutManager(getContext()));

        WeeklyRef = FirebaseDatabase.getInstance().getReference().child("weekly");
        WeekRef = FirebaseDatabase.getInstance().getReference().child("weekly");

        iv_photo = weeklyView.findViewById(R.id.imageViewTempGallery); //수정
/*
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        TedPermission.with(getContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission] ")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
*/
        return weeklyView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<WeeklyDB> options = new FirebaseRecyclerOptions.Builder<WeeklyDB>()
                .setQuery(WeeklyRef, WeeklyDB.class).build();

        FirebaseRecyclerAdapter<WeeklyDB, WeeklyViewHolder> adapter
                = new FirebaseRecyclerAdapter<WeeklyDB, WeeklyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WeeklyViewHolder holder, int position, @NonNull WeeklyDB model) {
                String week = getRef(position).getKey();

                WeekRef.child(week).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("weekly")) {
                            String week_num = snapshot.child("week").getValue().toString();
                            String week_todo = snapshot.child("todo").getValue().toString();

                            holder.tv_week.setText(week_num);
                            holder.tv_todo.setText(week_todo);
                        } else {
                            String week_num = snapshot.child("week").getValue().toString();
                            String week_todo = snapshot.child("todo").getValue().toString();

                            holder.tv_week.setText(week_num);
                            holder.tv_todo.setText(week_todo);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_layout, parent, false);
                WeeklyViewHolder viewHolder = new WeeklyViewHolder(view);
                return viewHolder;
            }
        };

        myWeeklyList.setAdapter(adapter);
        adapter.startListening();
    }

//    public void takePhoto() {
//
//        // 촬영 후 이미지 가져옴
//
//        String state = Environment.getExternalStorageState();
//        if(Environment.MEDIA_MOUNTED.equals(state)){
//
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if(intent.resolveActivity(getActivity().getPackageManager())!=null){
//                File photoFile = null;
//                try{
//                    photoFile = createImageFile();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//                if(photoFile!=null){
//                    Uri providerURI = FileProvider.getUriForFile(getContext(), getActivity().getPackageName(),photoFile);
//                    imgUri = providerURI;
//                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);
//                    startActivityForResult(intent, FROM_CAMERA);
//                }
//            }
//        } else {
//            Log.v("알림", "저장공간에 접근 불가능");
//            return;
//        }
//    }
//
//    public File createImageFile() throws IOException{
//        String imgFileName = System.currentTimeMillis() + ".jpg";
//        File imageFile= null;
//        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "");
//
//        if(!storageDir.exists()){
//            //없으면 만들기
//            Log.v("알림","storageDir 존재 x " + storageDir.toString());
//            storageDir.mkdirs();
//        }
//        Log.v("알림","storageDir 존재함 " + storageDir.toString());
//        imageFile = new File(storageDir,imgFileName);
//        mCurrentPhotoPath = imageFile.getAbsolutePath();
//        return imageFile;
//    }
//
//    public void galleryAddPic(){
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        getActivity().sendBroadcast(mediaScanIntent);
//        Toast.makeText(getActivity(),"사진이 저장되었습니다",Toast.LENGTH_SHORT).show();
//    }
//
//    public void selectAlbum(){
//        //앨범에서 이미지 가져옴
//        //앨범 열기
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        intent.setType("image/*");
//        startActivityForResult(intent, FROM_ALBUM);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        switch (requestCode) {
//            case FROM_ALBUM: {
//                //앨범에서 가져오기
//                if (data.getData() != null) {
//                    try {
//                        photoUri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
//                        iv_photo.setImageBitmap(bitmap);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            }
//
//            case FROM_CAMERA: {
//                //카메라 촬영
//                try {
//                    Log.v("알림", "FROM_CAMERA 처리");
//                    galleryAddPic();
//                    iv_photo.setImageURI(imgUri);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//        }
//    }
//
//    public void makeConfirmDialog() {
//        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext()); //R.style.추가
//        alt_bld.setTitle("작성 완료").setMessage("글을 게시하시겠습니까?").setCancelable(
//                false).setPositiveButton("네",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //DB에 등록하기
////                        final String cu = mAuth.getUid();
//                        //1. 사진을 storage에 저장하고 그 url을 알아내야 함
//                        String filename = "" + System.currentTimeMillis(); //cu + "_" +
//                        StorageReference storageRef = storage.getReferenceFromUrl("https://memberdb-27335-default-rtdb.firebaseio.com/").child("Gallery/" + filename);
//                        UploadTask uploadTask;
//                        Uri file = null;
//                        if(flag ==0){
//                            //사진촬영
//                            file = Uri.fromFile(new File(mCurrentPhotoPath));
//                        } else if(flag==1){
//                            //앨범선택
//                            file = photoUri;
//                        }
//                        uploadTask = storageRef.putFile(file);
//                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//                        progressDialog.setMessage("업로드중...");
//                        progressDialog.show();
//
//                        // Register observers to listen for when the download is done or if it fails
//                        uploadTask.addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle unsuccessful uploads
//                                Log.v("알림", "사진 업로드 실패");
//                                exception.printStackTrace();
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                                downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                                Log.v("알림", "사진 업로드 성공 " + downloadUrl);
//                            }
//                        });
//                    }
//                }).setNegativeButton("아니오",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // 아니오 클릭. dialog 닫기.
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = alt_bld.create();
//        alert.show();
//    }

    public class WeeklyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_week, tv_todo;
        ImageButton ibt_camera, ibt_timer;
        TimerFragment fragment_timer;

        public WeeklyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_week = (TextView) itemView.findViewById(R.id.textViewWeek);
            tv_todo = (TextView) itemView.findViewById(R.id.textViewTodo);
            fragment_timer = new TimerFragment();

            ibt_camera = (ImageButton)itemView.findViewById(R.id.imageButtonCamera);
            ibt_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            flag = 0;
                            //takePhoto();
                        }
                    };

                    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            flag = 1;
                            //selectAlbum();
                        }
                    };

                    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    };

                    new AlertDialog.Builder(getContext())
                            .setTitle("업로드할 이미지 선택")
                            .setPositiveButton("사진촬영", cameraListener)
                            .setNeutralButton("앨범선택", albumListener)
                            .setNegativeButton("취소", cancelListener)
                            .show();
                }
            });

            ibt_timer = (ImageButton)itemView.findViewById(R.id.imageButtonTimer);
            ibt_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerTabs, fragment_timer).commit();
                }
            });
        }
    }
}