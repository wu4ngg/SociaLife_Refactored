package com.example.projectmain.Refactoring.Observer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.projectmain.Database.DB;
import com.example.projectmain.Model.Post;
import com.example.projectmain.Model.User;
import com.example.projectmain.Refactoring.Singleton.GlobalUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

// publisher người thông báo đến các follower
// không có subscriber vì người đăng kí đã follower ở trong chức năng khác
public class Publisher  {
    private DB db;
    private final Context context;

    public Publisher(Context context) {
        this.context = context;
    }

    // hàm thông báo của publisher để các follower
    public void thongBao(){
        db = new DB(context);
        User userMain = GlobalUser.getInstance(context).getUser();
        Date currentTime = Calendar.getInstance().getTime();
        List<Integer> followerIds = getAllMyFollower(userMain.getId());
        for (int i = 0; i < followerIds.size(); i++){
            db.insertNotify(userMain.getId(), userMain.getName() + " đã đăng 1 bài viết", String.valueOf(currentTime), 0, 0, 0, 0, followerIds.get(i));
        }
    }

    // hàm dùng để lấy danh sách follower (Subcriber)
    public List<Integer> getAllMyFollower(int idUser){
        db = new DB(context);
        return db.getFollowerIds(idUser);
    }

}
