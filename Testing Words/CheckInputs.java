import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Trexi se eclipse, en gia na tsiakariskoume an oules oi le3is mas en
 * diaforetikes DEN tha trexi sto kanoniko app, en gia emas, prin kamoume
 * publish mia lista p lexis //TODO na mporoume na valoume multiple files, gia
 * pio megali lista. Eskeftika na to kamo na tis diagrafi aftomata, alla mpori
 * na iparxi kapou misalign me tis le3is, j na mas diagrapsi le3is p en theloume
 * na diagrapsi. Created by tratrafe2 on 13/01/2017.
 ************************************************************************
 ************************************************************************
 ************************************************************************
 ******* Gia na dixni elinikus xaraktires to console tis eclipse********* Piene
 * run->run configurations->common->encoding ke 8kialekse UTF-8****
 ************************************************************************
 ************************************************************************
 ************************************************************************
 ************************************************************************
 */

public class CheckInputs {

	public static void main(String[] args) {

		// String path = "E://Users/GamerMakrides/Documents/Creative
		// stuff/AndroidProjects/tabu/app/src/main/res/raw/Test1";
		String path = "Test12";
		String path2="Test14";
		BufferedReader sc = null;
		try {
			File file = new File(path);
			sc = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			// sc = new Scanner(file);
		} catch (Exception e) {
			System.out.println("file not found");
		}
		int number = 0;
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
			
				if (temp.word == null)
					break;
				temp.word = temp.word.replace('ά', 'α');
				temp.word = temp.word.replace('έ', 'ε');
				temp.word = temp.word.replace('ί', 'ι');
				temp.word = temp.word.replace('ό', 'ο');
				temp.word = temp.word.replace('ύ', 'υ');
				temp.word = temp.word.replace('ή', 'η');
				temp.word = temp.word.replace('ώ', 'ω');
				temp.word = temp.word.replace('Ά', 'α');
				temp.word = temp.word.replace('Έ', 'ε');
				temp.word = temp.word.replace('Ί', 'ι');
				temp.word = temp.word.replace('Ό', 'ο');
				temp.word = temp.word.replace('Ύ', 'υ');
				temp.word = temp.word.replace('Ή', 'η');
				temp.word = temp.word.replace('Ώ', 'ω');
				temp.word = temp.word.toUpperCase();

				for (int i = 0; i < number; i++) {
					try {
						temp.apagorevmenes[i] = sc.readLine();
						
					} catch (Exception e) {
						check = false;
					}
				}
				for (int i = number; i < 5; i++) {
					temp.apagorevmenes[i] = "";
				}
				if (check)
					list.add(temp.word);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(list.toString());
		list.sort(null);
		System.out.println(list.toString());
	
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i).equals(list.get(i + 1))) {
				System.out.println(list.get(i));
				
				File file = new File(path);
				try {
					PrintWriter writer = new PrintWriter(path2, "UTF-8");
				    writer.println("6");
					sc = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
					sc.readLine();
					while (true) {
						String temp = sc.readLine();
						if (temp == null)
							break;
						String temp2=new String(temp.toString());
						temp = temp.replace('ά', 'α');
						temp = temp.replace('έ', 'ε');
						temp = temp.replace('ί', 'ι');
						temp = temp.replace('ό', 'ο');
						temp = temp.replace('ύ', 'υ');
						temp = temp.replace('ή', 'η');
						temp = temp.replace('ώ', 'ω');
						temp = temp.replace('Ά', 'α');
						temp = temp.replace('Έ', 'ε');
						temp = temp.replace('Ί', 'ι');
						temp = temp.replace('Ό', 'ο');
						temp = temp.replace('Ύ', 'υ');
						temp = temp.replace('Ή', 'η');
						temp = temp.replace('Ώ', 'ω');
						temp = temp.toUpperCase();
						if (temp.compareTo(list.get(i)) == 0){
							for (int j = 0; j < number; j++) {
								sc.readLine();
							}
							break;
						}
						writer.println(temp2);
							for (int j = 0; j < number; j++) {
								temp2=sc.readLine();
								writer.println(temp2);
							}
					}
					String temp = sc.readLine();
					while(temp!=null){
						writer.println(temp);
						temp = sc.readLine();
					}
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		System.out.println("Done");
	}
}
