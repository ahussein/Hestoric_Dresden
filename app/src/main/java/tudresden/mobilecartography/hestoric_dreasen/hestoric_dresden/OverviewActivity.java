package tudresden.mobilecartography.hestoric_dreasen.hestoric_dresden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Overview");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Intent overview = getIntent();



    }

    public void mainmenubutton(View view) {

        Intent mainmenu = new Intent(this, MainActivity.class);

        startActivity(mainmenu);
    }
}