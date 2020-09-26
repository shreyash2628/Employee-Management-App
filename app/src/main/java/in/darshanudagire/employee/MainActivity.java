package in.darshanudagire.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {


    Animation topanim,botanim;
    ImageView splash_image;
    TextView splash_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        topanim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botanim = AnimationUtils.loadAnimation(this,R.anim.bot_animation);

        splash_image= findViewById(R.id.splash_image_id);

        splash_image.setAnimation(topanim);
        // splash_text.setAnimation(botanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,LoginAcitivity.class);
                startActivity(intent);
                finish();
            }
        },5000);

    }
}
