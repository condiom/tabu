package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Test extends Activity {
    View bt1,bt2,bt3,bt4;
   FrameLayout fr;
   CountDownTimer cdt;
    View tx;
    View b;
   SeekBar sk;
   TextView txtSpeed;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_test);
      bt1 = (View)findViewById(R.id.btnTest1);
      bt2 = (View)findViewById(R.id.btnTest2);
      bt3 = (View)findViewById(R.id.btnTest3);
      bt4 = (View)findViewById(R.id.btnTest4);
      sk=(SeekBar)findViewById(R.id.seekBar);
      fr=(FrameLayout)findViewById(R.id.frLayout);
      txtSpeed=(TextView)findViewById(R.id.txtSpeed);


      fr.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
         @Override
         public void onGlobalLayout() {
            fr.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            float pos=fr.getWidth()/4;
            float space=(pos-bt1.getWidth())/2;
            bt1.setX(0+ space);
            bt2.setX(pos+ space);
            bt3.setX(2*pos+ space);
            bt4.setX(3*pos+ space);

         }
      });




   }

   public void btnClick(View v){
      b=v;
       if(cdt!=null)
       cdt.cancel();
       tx = (View) findViewById(R.id.txtTestTarget);

       cdt = new CountDownTimer(100000,1) {
           float speed=sk.getProgress();
           @Override
           public void onTick(long l) {
               speed+=0.1;
               txtSpeed.setText(speed+"");
               if(b.getX()+b.getWidth()/2!=tx.getX()+tx.getWidth()/2){
                   if(b.getX()+b.getWidth()/2>tx.getX()+tx.getWidth()/2){
                       b.setX(b.getX()-speed);
                   }
                   else{
                       b.setX(b.getX()+speed);
                   }
                   b.setY(b.getY()+speed);
                   if(Math.abs((b.getX()+b.getWidth()/2)-(tx.getX()+tx.getWidth()/2))<speed){
                       b.setX((tx.getX()+tx.getWidth()/2)-b.getWidth()/2);
                   }

               }
               else{

                   if(b.getY()+b.getHeight()/2<tx.getY()+tx.getHeight()/2)
                       b.setY(b.getY()+speed);
                   else{
                       b.setY(-b.getHeight()/2+tx.getY()+tx.getHeight()/2);
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
}
