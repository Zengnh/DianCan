package com.king.android.ui.my;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityPersonInfoBinding;
import com.king.android.model.User;
import com.king.base.activity.BaseActivity;
import com.king.base.dialog.LoadingDialog;
import com.king.base.utils.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 个人资料
 */
public
class PersonInfoActivity extends BaseActivity<ActivityPersonInfoBinding> {

    private boolean selectBoy = true;
    private User user;
    private LoadingDialog dialog;
    private String cover_img;

    @Override
    public void init() {
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("基本資料");
        ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.avatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.chooseImage(thisAtv,11);
            }
        });
        binding.boyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSex(true);
            }
        });
        binding.girlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSex(false);
            }
        });
        binding.brithdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String birthday = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        user.setBirthday(birthday);
                        binding.setUser(user);
                    }
                };
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                DatePickerDialog date = new DatePickerDialog(thisAtv,
                        dateListener, year, month, day);
                date.show();

            }
        });

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });

        user = User.getInstance();
        selectSex("1".equals(user.getSex()));
        binding.setUser(user);
        Glide.with(thisAtv).load(user.getCover()).into(binding.avatarIv);

        dialog = new LoadingDialog(thisAtv);

    }

    private void selectSex(boolean isBoy){
        selectBoy = isBoy;
        if (isBoy){
            user.setSex("1");
            binding.boyV.setBackgroundResource(R.drawable.shape_select_check);
            binding.girlV.setBackgroundResource(R.drawable.shape_line_c6c6c6_radlis_100);
        }else {
            user.setSex("0");
            binding.boyV.setBackgroundResource(R.drawable.shape_line_c6c6c6_radlis_100);
            binding.girlV.setBackgroundResource(R.drawable.shape_select_check);
        }
    }

    private void updateInfo(){
        String sex = selectBoy ? "1" : "2";
        String mobile = binding.editPhone.getText().toString();
        String birthday = binding.brithdayTv.getText().toString();
        String name = binding.editName.getText().toString();
        if (name.isEmpty()){
            toast("請輸入姓名");
            return;
        }
        if (mobile.isEmpty()){
            toast("請輸入手機號碼");
            return;
        }
        if (birthday.isEmpty()){
            toast("請輸選擇出生日期");
            return;
        }
        dialog.show();
        User user = User.getInstance();
        Map<String,Object> map  =new HashMap<>();
        map.put("access_token",user.getAccess_token());
        map.put("sex",sex);
        map.put("mobile",mobile);
        map.put("birthday",birthday);
        map.put("name",name);

        if (!TextUtils.isEmpty(cover_img )){
            map.put("cover_img",cover_img );
        }

        ApiHttp.getApiService().updateMemberInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                    if (data.isSuccess()){
                        toast("儲存成功");

                        user.setSex(sex);
                        user.setName(name);
                        user.setMobile(mobile);
                        user.setBirthday(birthday);
                        user.setCover(cover_img);
                        user.save();
                        finish();
                    }else {
                        toast(data.getMsg());
                    }
                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            String s = FileUtil.uriToPath(data.getData(), thisAtv);
            System.out.println("ssscccc_"+s);
            Bitmap bitmap = BitmapFactory.decodeFile(s);
            binding.avatarIv.setImageBitmap(bitmap);
            Glide.with(thisAtv).load(new File(s)).into(binding.avatarIv);

            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(new File(s));
                }
            }).start();
        }
    }

    private void upload(File file){

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int quality = 100;
//        byte[] datas = null;
//        do {
//            baos.reset();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//            datas = baos.toByteArray();
//            quality -= 10;
//        } while (datas.length / 1024 > 500);

//        final byte[] bs = datas;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap b = BitmapFactory.decodeByteArray(bs,0,bs.length);
//                binding.avatarIv.setImageBitmap(b);
//            }
//        });

//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

//        String descriptionString = "This is a description";
//        RequestBody description =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), descriptionString);
//
//        String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg";
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), datas);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", name, requestBody);

        User user = User.getInstance();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("access_token",user.getAccess_token())
                .build();
        ApiHttp.getApiService().upload(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();

                    if (data.isSuccess()){
                        cover_img = data.getData().getUrl();
                        RequestOptions options = new RequestOptions().placeholder(R.mipmap.logo);
                        Glide.with(thisAtv).load(cover_img).apply(options).into(binding.avatarIv);
                    }

                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                });
    }
}
