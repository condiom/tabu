package tratrafe2.condiom.tambu;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Test extends Activity {
    View bt1, bt2, bt3, bt4;
    FrameLayout fr;
    CountDownTimer cdt;
    View tx;
    View b;
    SeekBar sk;
    TextView txtSpeed;
    private float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 150;
    static final int MIN_NEG_DISTANCE = -150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        bt1 = (View) findViewById(R.id.btnTest1);
        bt2 = (View) findViewById(R.id.btnTest2);
        bt3 = (View) findViewById(R.id.btnTest3);
        bt4 = (View) findViewById(R.id.btnTest4);
        sk = (SeekBar) findViewById(R.id.seekBar);
        fr = (FrameLayout) findViewById(R.id.frLayout);
        txtSpeed = (TextView) findViewById(R.id.txtSpeed);


        fr.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fr.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float pos = fr.getWidth() / 4;
                float space = (pos - bt1.getWidth()) / 2;
                bt1.setX(0 + space);
                bt2.setX(pos + space);
                bt3.setX(2 * pos + space);
                bt4.setX(3 * pos + space);

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showT(View v) {
        // previously invisible view
        View myView = findViewById(R.id.img1);

        // get the center for the clipping circle
        int cx = (int) (v.getX() + v.getWidth() / 2);
        int cy = (int) (v.getY() + v.getHeight() / 2);

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.setDuration(1000);
        anim.start();
    }

    public void btnClick(View v) {
        b = v;
        if (cdt != null)
            cdt.cancel();
        tx = (View) findViewById(R.id.txtTestTarget);

        cdt = new CountDownTimer(100000, 1) {
            float speedX = sk.getProgress();
            float speed = sk.getProgress();

            @Override
            public void onTick(long l) {
                speed += 0.1;
                txtSpeed.setText(speed + " " + speedX);
                if (b.getX() + b.getWidth() / 2 != tx.getX() + tx.getWidth() / 2) {
                    if (b.getX() + b.getWidth() / 2 > tx.getX() + tx.getWidth() / 2) {
                        b.setX(b.getX() - speedX);
                    } else {
                        b.setX(b.getX() + speedX);
                    }
                    b.setY(b.getY() + speed);
                    if (Math.abs((b.getX() + b.getWidth() / 2) - (tx.getX() + tx.getWidth() / 2)) < speed) {
                        b.setX((tx.getX() + tx.getWidth() / 2) - b.getWidth() / 2);
                    }

                } else {

                    if (b.getY() + b.getHeight() / 2 < tx.getY() + tx.getHeight() / 2)
                        b.setY(b.getY() + speed);
                    else {
                        b.setY(-b.getHeight() / 2 + tx.getY() + tx.getHeight() / 2);
                        cdt.cancel();
                    }
                }


            }

            @Override
            public void onFinish() {

            }
        };

        cdt.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if ((deltaX > MIN_DISTANCE) && (Math.abs(deltaY) < MIN_DISTANCE)) {
                    Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();
                } else if ((deltaX < MIN_NEG_DISTANCE) && (Math.abs(deltaY) < MIN_DISTANCE)) {
                    Toast.makeText(this, "right2left swipe", Toast.LENGTH_SHORT).show();
                } else if ((Math.abs(deltaX) < MIN_DISTANCE) && (deltaY > MIN_DISTANCE)) {
                    Toast.makeText(this, "up2down swipe", Toast.LENGTH_SHORT).show();
                } else if ((Math.abs(deltaX) < MIN_DISTANCE) && (deltaY < MIN_NEG_DISTANCE)) {
                    Toast.makeText(this, "down2up swipe", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onTouchEvent(event);
    }
}
