package tratrafe2.condiom.tambu;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by tratrafe2 on 11/01/2017.
 */

public class Card {
    String word;
    String[] apagorevmenes;

    public Card() {
        word = "";
        apagorevmenes = new String[4];
    }

    public static Card[] initArray(Context c) {
        Scanner sc = null;
        try {
            sc = new Scanner(c.getResources().openRawResource(R.raw.words));
        } catch (Exception e) {
        }
        boolean check=true;
        List<Card> list=new ArrayList<>();
        while(sc.hasNext()){
            Card temp=new Card();
            temp.word=sc.nextLine();
            for(int i=0;i<4;i++){
                if(sc.hasNext()) {
                    temp.apagorevmenes[i] = sc.nextLine();
                }else{
                    check=false;
                }
            }
           if(check)
               list.add(temp);
        }
        Card[] array=new Card[list.size()];
        for(int i=0;i<list.size();i++){
            array[i]=list.get(i);
        }
        return array;
    }
}