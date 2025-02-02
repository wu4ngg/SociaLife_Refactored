package com.example.projectmain;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.projectmain.Database.DB;
import com.example.projectmain.Global.OnColorChangeListener;
import com.example.projectmain.Model.User;
import com.example.projectmain.Prototype.ItemPrototype;
import com.example.projectmain.Refactoring.Singleton.GlobalUser;
import com.example.projectmain.Refactoring.SingletonColorChange.ColorManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;

public class SettingActivity extends AppCompatActivity implements OnColorChangeListener {
    ImageButton btnExit, btnInfo;
    ImageButton btnLogout;
    ImageView img_Icon;
    TextView tv_title;

    LinearLayout btnUpdate;
    ShapeableImageView ivAvatar;
    TextView tvName, tvEmail;
    LinearLayout btnLogoff;

    LinearLayout btnListFollow;
    private static final String SHARED_PREF_NAME = "mypref";

    private static final String KEY_IMAGE_LINK = "linkImage";

    private static final String KEY_NAME = "name";

    private static final String KEY_EMAIL = "email";

    SharedPreferences sharedPreferences;
    DB db;

    User user;

    ShapeableImageView imageOfAvatar;
    ImageView crown;

    ImageView tickBlue;
    ScrollView wrap_parent;

    GlobalUser userInstance;
    @SuppressLint({"MissingInflatedId", "LongLogTag", "ResourceAsColor", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        DB db = new DB(this);
        RefactorPrototype();

        userInstance = GlobalUser.getInstance(this);

       // btnLogoff = findViewById(R.id.btnLogoff);
        btnExit = findViewById(R.id.btn_exit);
        tvName = findViewById(R.id.setting_userName);
        tvEmail = findViewById(R.id.setting_email);
        ivAvatar = findViewById(R.id.ivAvatar);
        wrap_parent = findViewById(R.id.wrap_parent);
        //btnListFollow = findViewById(R.id.btnFlolow);
        btnInfo = findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(v -> {
            Intent i = new Intent(SettingActivity.this, AppCreditsActivity.class);
            startActivity(i);
        });

        ColorManager colorManager = ColorManager.getInstance();
        wrap_parent.setBackgroundDrawable(ColorManager.getInstance().getBackgroundDrawable());


        tickBlue = findViewById(R.id.blueTick);

        imageOfAvatar = findViewById(R.id.ivAvatar);
        crown = findViewById(R.id.crownIcon);





//        btnListFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SettingActivity.this, FollowerActivity.class);
//                startActivity(i);
//            }
//        });


        db = new DB(getApplicationContext());

        // Tương tác với Decorator
        CheckBuyTick(this, userInstance.getUser().getId());
        CheckBuyCrownAndFrame(this, userInstance.getUser().getId());

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_NAME, null);
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String imgUrl = sharedPreferences.getString(KEY_IMAGE_LINK, null);
        Uri link = null;
        user = db.getUser(email);
        String strImageAvatar = db.getImagefor(user.getId());


        Log.d("IMG: ", String.valueOf(link));

//            ivAvatar.setImageResource(R.drawable.def);
        if (name != null) {
            if (strImageAvatar != null) {
                link = Uri.parse(strImageAvatar);
                ivAvatar.setImageURI(link);

            } else {
                ivAvatar.setImageResource(R.drawable.def);
            }
            tvName.setText(name);
            tvEmail.setText(email);
        }
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });

//        btnExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//        btnUpdate = findViewById(R.id.btnUpdateInfo);
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SettingActivity.this, EditInfoActivity.class);
//                startActivity(i);
//            }
//        });
    }

    public void CheckBuyTick(Context c, int idUser){
        db = new DB(c);
        Log.d("USER CURRENT ID: ", String.valueOf(idUser));
        Boolean checkTick = db.CheckTick(idUser);
        Log.d("KIEM TRA Mua: ", String.valueOf(checkTick));
        if(checkTick){
            tickBlue.setVisibility(View.VISIBLE);
        }
    }

    public void CheckBuyCrownAndFrame(Context c, int idUser){
        db = new DB(c);
        Boolean checkCrownAndFrame = db.CheckFrameAndCrown(idUser);
        if(checkCrownAndFrame){
            int strokeColor = ContextCompat.getColor(c, R.color.border_frame);
            imageOfAvatar.setStrokeColor(ColorStateList.valueOf(strokeColor));
            crown.setVisibility(View.VISIBLE);
        }
    }

    // class Prototype
    public void RefactorPrototype()
    {
        ItemPrototype info=new ItemPrototype(this);
        ItemPrototype follower=new ItemPrototype(this);
        ItemPrototype payment = new ItemPrototype(this);

        ItemPrototype exit=new ItemPrototype(this);
        payment.SetContent(R.drawable.baseline_payment_24, "Mua gói vip");
        follower.SetContent(R.drawable.star_line,"Danh sách người theo dõi");
        exit.SetContent(R.drawable.logout_box_r_line,"Đăng xuất");
        info.SetContent(R.drawable.user_3_line,"Chỉnh sửa thông tin cá nhân");

        View itemView= info.getView();
        View itemView1= follower.getView();
        View itemView2= exit.getView();
        View itemView3= payment.getView();

        itemView.setOnClickListener(v -> {
            Intent intent=new Intent(SettingActivity.this, EditInfoActivity.class);
            startActivity(intent);
        });
        itemView1.setOnClickListener(v -> {
            Intent intent=new Intent(SettingActivity.this, FollowerActivity.class);
            startActivity(intent);
        });
        itemView2.setOnClickListener(v -> {
            AlertDialog.Builder d = new AlertDialog.Builder(SettingActivity.this);
            d.setTitle("Đăng xuất");
            d.setMessage("Bạn có chắc là muốn đăng xuất không?");
            d.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    finish();
                    Intent j = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(j);
                }
            });
            d.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog a = d.create();
            a.show();
        });
        itemView3.setOnClickListener(v -> {
            Intent intent=new Intent(SettingActivity.this, PaymentActivity.class);
            startActivity(intent);
        });

        LinearLayout layoutCha=findViewById(R.id.linearLayout2);
        layoutCha.addView(itemView);
        layoutCha.addView(itemView1);
        layoutCha.addView(itemView2);
        layoutCha.addView(itemView3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new DB(SettingActivity.this);
        String strImageAvatar = db.getImagefor(user.getId());

        String name = sharedPreferences.getString(KEY_NAME, null);
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        //  String imgUrl = sharedPreferences.getString(KEY_IMAGE_LINK, null);
        Uri link = null;
        if (strImageAvatar != null) {
            link = Uri.parse(strImageAvatar);
        }
        if (name != null) {
            if (link == null) {
                ivAvatar.setImageResource(R.drawable.def);
            } else {
                ivAvatar.setImageURI(link);
            }
        }
        CheckBuyTick(SettingActivity.this, user.getId());
        CheckBuyCrownAndFrame(SettingActivity.this, user.getId());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        this.overridePendingTransition(R.anim.in_right, R.anim.out_right);
    }

    @Override
    public void onColorChanged(Drawable color) {
    }
}