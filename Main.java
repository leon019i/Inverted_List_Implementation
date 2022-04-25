/**
 * @author : loay fayaz
 * @group : s3
 * @id : 20190396
 * @TA : AhmedGalal
 */

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
public class Main {

	public static void main(String[] args) throws IOException {
		InvertedIndex index = new InvertedIndex();

		index.buildIndex(new String[] {
				"..\\Doc\\100.txt",//0
				"..\\Doc\\101.txt",//1
				"..\\Doc\\102.txt",//2
				"..\\Doc\\103.txt",//3
				"..\\Doc\\104.txt",//4
				"..\\Doc\\105.txt",//5
				"..\\Doc\\106.txt",//6
				"..\\Doc\\107.txt",//7
				"..\\Doc\\108.txt",//8
				"..\\Doc\\109.txt",//9
				"..\\Doc\\300.txt",//10
				"..\\Doc\\302.txt",//11
				"..\\Doc\\500.txt",//12
				"..\\Doc\\502.txt",//13
				"..\\Doc\\503.txt",//14
				"..\\Doc\\504.txt",//15
				"..\\Doc\\505.txt",//16
				"..\\Doc\\506.txt",//17
				"..\\Doc\\507.txt",//18
				"..\\Doc\\508.txt",//19
				"..\\Doc\\509.txt",//20
				"..\\Doc\\510.txt",//21
				"..\\Doc\\511.txt",//22
				"..\\Doc\\512.txt",//23
				"..\\Doc\\513.txt",//24
				"..\\Doc\\514.txt",//25
				"..\\Doc\\515.txt",//26
				"..\\Doc\\516.txt",//27
				"..\\Doc\\517.txt",//28
				"..\\Doc\\518.txt",//29
				"..\\Doc\\519.txt",//30
				"..\\Doc\\520.txt",//31
				"..\\Doc\\521.txt",//32
				"..\\Doc\\522.txt",//33
				"..\\Doc\\523.txt",//34
				"..\\Doc\\524.txt",//35
				"..\\Doc\\526.txt",//36
				"..\\Doc\\527.txt",//37
		});
		Scanner choice = new Scanner(System.in);  // Create a Scanner object
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("IR_Assignment_1");
		String phrase = "";
		do {
			System.out.println("1) Intersect_add_Operator");
			System.out.println("2) Ordinary Find");
			System.out.println("3) Find_01_add [1st Term and 2nd Term]");
			System.out.println("4) Find_02_add [3 Terms]");
			System.out.println("5) Intersect_or_Operator");
			System.out.println("6) Find_01_or [1st Term Or 2nd Term]");
			System.out.println("7) Find_02_or [1st Term Or 2nd Term Or 3rd Term]");
			System.out.println("8) Intersect_not_Operator");
			System.out.println("9) Find_01_not [1st Term not 2nd Term]");
			System.out.println("10) Find_02_not [1st Term Not 2nd Term Not 3rd Term]");
			System.out.println("11) Free Query");
			System.out.print(">:");

			int option = choice.nextInt();
			switch (option) {
				case 1:
					HashSet<Integer> pL1 = new HashSet<>();
					HashSet<Integer> pL2 = new HashSet<>();
					HashSet<Integer> intersect_result;
					pL1.add(1);
					pL1.add(2);
					pL1.add(3);
					pL1.add(4);
					pL2.add(1);
					pL2.add(3);
					pL2.add(4);
					intersect_result = index.intersect_add(pL1, pL2);
					System.out.println("Result = "+intersect_result);
					phrase="AND";
					break;
				case 2:
					System.out.println("Type a phrase: ");
					phrase = in.readLine();
					System.out.println(index.find(phrase.toLowerCase()));
					break;
				case 3:
					System.out.println("Type a phrase with 2-terms: ");
					phrase = in.readLine();
					System.out.println(index.find_01(phrase.toLowerCase()));
					break;
				case 4:
					System.out.println("Type a phrase with 3-terms: ");
					phrase = in.readLine();
					System.out.println(index.find_02(phrase.toLowerCase()));
					break;
				case 5:
					HashSet<Integer> pL1_or = new HashSet<>();
					HashSet<Integer> pL2_or = new HashSet<>();
					HashSet<Integer> intersect_or_result;
					pL1_or.add(1);
					pL1_or.add(2);
					pL1_or.add(3);
					pL1_or.add(4);
					pL2_or.add(1);
					pL2_or.add(3);
					pL2_or.add(4);
					intersect_or_result = index.intersect_or(pL1_or, pL2_or);
					System.out.println("Result = "+intersect_or_result);
					phrase="OR";
					break;
				case 6:
					System.out.println("Type a phrase with 2-terms: ");
					phrase = in.readLine();
					System.out.println(index.find_01_or(phrase.toLowerCase()));
					break;
				case 7:
					System.out.println("Type a phrase with 3-terms: ");
					phrase = in.readLine();
					System.out.println(index.find_02_or(phrase.toLowerCase()));
					phrase="N-phrase";
					break;
				case 8:
					HashSet<Integer> pL1_not = new HashSet<>();
					HashSet<Integer> pL2_not = new HashSet<>();
					HashSet<Integer> intersect_not_result;
					pL1_not.add(1);
					pL1_not.add(2);
					pL1_not.add(3);
					pL1_not.add(4);
					pL2_not.add(1);
					pL2_not.add(3);
					pL2_not.add(4);
					intersect_not_result = index.intersect_not(pL1_not, pL2_not);
					System.out.println("Result = "+intersect_not_result);
					phrase="NOT";
					break;
				case 9:
					System.out.println("Type a phrase with 2-terms: ");
					phrase = in.readLine();
					System.out.println(index.find_01_not(phrase.toLowerCase()));
					phrase="N-phrase";
					break;
				case 10:
					System.out.println("Type a phrase with 3-terms: ");
					phrase = in.readLine();
					System.out.println(index.find_02_not(phrase.toLowerCase()));
					phrase="N-phrase";
					break;
				case 11:
					System.out.println("Type Your Wanted Query: ");
					phrase = in.readLine();
					System.out.println(index.find_N_Terms(phrase.toLowerCase()));
					phrase="N-phrase";
					break;
				default:
					phrase= "";
					break;
			}
		} while (!phrase.isEmpty());
    }

//    		** [subgraphs,1] <1> =--> 5,
//			** [hazardous,1] <1> =--> 19,
//			** [downright,1] <1> =--> 17,
//			** [alarm,2] <2> =--> 24, 28,
//			** [instances,8] <9> =--> 17, 18, 3, 37, 7, 24, 29, 14,
//			** [appraisal,3] <25> =--> 23, 24, 28,
	//subgraphs or alarm or downright
	//alarm and appraisal
	//not alarm and appraisal
	//goal or calisce
	//not goal or alarm
}
