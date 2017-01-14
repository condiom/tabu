import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
 ************************************************************************
 ************************************************************************
 ************************************************************************
 ******* Gia na dixni elinikus xaraktires to console tis eclipse*********
 **Piene run->run configurations->common->encoding ke 8kialekse UTF-8****
 ************************************************************************
 ************************************************************************
 ************************************************************************
 ************************************************************************
 */

public class CheckInputs {

	public static void main(String[] args) {
		BufferedReader sc = null;
		try {
			File file = new File("words_gr.txt");
			sc = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(file), "UTF8"));
			//sc = new Scanner(file);
		} catch (Exception e) {
			System.out.println("file not found");
		}
		int number=0;
		try {
			number = Integer.parseInt(sc.readLine()) - 1;
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean check = true;
		List<String> list = new ArrayList<>();
		try {
		while (true) {
			Card temp = new Card();
			
				temp.word = sc.readLine();
				if(temp.word==null)
					break;
			for (int i = 0; i < number; i++) {
				try{
					temp.apagorevmenes[i] = sc.readLine();
				}catch(Exception e)
				 {
					check = false;
				}
			}
			for (int i = number; i < 5; i++) {
				temp.apagorevmenes[i] = "";
			}
			if (check)
				list.add(temp.word);
		}
			}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
