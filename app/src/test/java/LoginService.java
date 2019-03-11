import android.content.Context;
import android.content.SharedPreferences;

public class LoginService {

    private Context context;

    public LoginService(Context context){
        this.context =  context;
    }

    public boolean saveInfo(String username,String password){
        boolean flag = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        flag = editor.commit();
        return flag;
    }
}
