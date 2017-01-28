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
   Button bt1,bt2,bt3,bt4;
   FrameLayout fr;
   CountDownTimer cdt;
   Button b;
   SeekBar sk;
   TextView txtSpeed;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_test);
      bt1 = (Button)findViewById(R.id.btnTest1);
      bt2 = (Button)findViewById(R.id.btnTest2);
      bt3 = (Button)findViewById(R.id.btnTest3);
      bt4 = (Button)findViewById(R.id.btnTest4);
      sk=(SeekBar)findViewById(R.id.seekBar);
      fr=(FrameLayout)findViewById(R.id.frLayout);
      txtSpeed=(TextView)findViewById(R.id.txtSpeed);
      final TextView tx = (TextView) findViewById(R.id.txtTestTarget);

      cdt = new CountDownTimer(100000,1) {

         @Override
         public void onTick(long l) {
            int speed=sk.getProgress();
            txtSpeed.setText(speed+"");
            if(b.getX()+b.getWidth()/2!=tx.getX()+tx.getWidth()/2){
               if(b.getX()+b.getWidth()/2>tx.getX()+tx.getWidth()/2){
                  b.setX(b.getX()-speed);
               }
               else{
                  b.setX(b.getX()+speed);
               }
               if(Math.abs((b.getX()+b.getWidth()/2)-(tx.getX()+tx.getWidth()/2))<speed){
                  b.setX((tx.getX()+tx.getWidth()/2)-b.getWidth()/2);
               }

            }
            else{
               if(b.getY()+b.getHeight()/2<tx.getY()+tx.getHeight()/2)
                  b.setY(b.getY()+speed);
               else{
                  cdt.cancel();
               }
            }


         }

         @Override
         public void onFinish() {

         }
      };
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
      b=(Button)v;
      cdt.cancel();
      cdt.start();
   }
}
