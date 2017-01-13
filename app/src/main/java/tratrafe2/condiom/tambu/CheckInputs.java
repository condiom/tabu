package tratrafe2.condiom.tambu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Trexi se eclipse, en gia na tsiakariskoume an oules oi le3is mas en diaforetikes
 * DEN tha trexi sto kanoniko app, en gia emas, prin kamoume publish mia lista p lexis
 * //TODO na mporoume na valoume multiple files, gia pio megali lista.
 * Eskeftika na to kamo na tis diagrafi aftomata, alla mpori na iparxi kapou misalign me tis le3is,
 * j na mas diagrapsi le3is p en theloume na diagrapsi.
 * Created by tratrafe2 on 13/01/2017.
 */

public class CheckInputs {

	public static void main(String[] args) {
		Scanner sc = null;
		try {
			File file = new File("words.txt");
			sc = new Scanner(file);
		} catch (Exception e) {
			System.out.println("file not found");
		}
		int number = Integer.parseInt(sc.nextLine()) - 1;
		boolean check = true;
		List<String> list = new ArrayList<>();
		while (sc.hasNext()) {
			Card temp = new Card();
			temp.word = sc.nextLine();
			for (int i = 0; i < number; i++) {
				if (sc.hasNext()) {
					temp.apagorevmenes[i] = sc.nextLine();
				} else {
					check = false;
				}
			}
			for (int i = number; i < 5; i++) {
				temp.apagorevmenes[i] = "";
			}
			if (check)
				list.add(temp.word);
		}
		System.out.println(list.toString());
		list.sort(null);
		System.out.println(list.toString());
		for(int i=0;i<list.size()-1;i++){
			if(list.get(i).equals(list.get(i+1))){
				System.out.println(list.get(i));
			}
		}
		System.out.println("Done");
	}
}
