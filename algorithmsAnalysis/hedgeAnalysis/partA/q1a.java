//Collaborators: Gaurav Bishnoi, Sushant Mittal

import java.util.*;

public class q1a {
	static float[] wgt = new float[4];
	static float[] p_vector = new float[4];
	static float[] l_vector = new float[4];
	static int userChoice = 0;

	public static void main(String[] args) {
		float user_score = 0;
		float AI_output = 0;
		float[][] payoffMat = new float[4][4];
		Random rg = new Random();
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				payoffMat[row][col] = 5 - rg.nextInt(10);
			}

		}
		System.out.println("Game Matrix");
		for (int row = 0; row < 4; row++) {
			
			for (int col = 0; col < 4; col++) {

				System.out.print(payoffMat[row][col] + ",");
			}
			
			System.out.println();

		}
		Scanner scan = new Scanner(System.in);
		l_vector = intialisevector(l_vector);
		for (int s = 0; s < 4; s++) {
			wgt[s] = 1;
		}
		for (int i = 0; i < 20; i++) {

			System.out.println("Turn " + i);

			System.out.println("User,Enter the choice ");
			userChoice = scan.nextInt();
			int hedgeChoice = hedge(payoffMat);

			System.out.println("AI choose" + hedgeChoice);
			System.out.println(" weight Vector");
			System.out.print("[");
			for (int m = 0; m < wgt.length; m++) {

				System.out.print(wgt[m] + ",");
			}
			
			System.out.println();

			System.out.println("updated loss Vector");
			System.out.print("[");
			for (int m = 0; m < l_vector.length; m++) {

				System.out.print(l_vector[m] + ",");
			}
		
			System.out.println();

			user_score += payoffMat[userChoice][hedgeChoice];
			System.out.println("New User Score " + user_score);

			AI_output += -payoffMat[userChoice][hedgeChoice];
			System.out.println("New AI Score " + AI_output);

		}

	}

	public static float[] intialisevector(float[] l_vector) {
		for (int s = 0; s < 4; s++) {
			l_vector[s] = 0;
		}
		return l_vector;
	}

	// below function
	// Ref:-http://stackoverflow.com/questions/9330394/how-to-pick-an-item-by-its-probability
	public static int numby_replacement(float[] p_vector) {
		int index = 0;
		double p = Math.random();
		double cumulativeProbability = 0.0;
		for (int item = 0; item < p_vector.length; item++) {
			cumulativeProbability += p_vector[item];
			if (p <= cumulativeProbability) {
				index = item;
				break;
			}
		}
		return index;

	}
	// end

	public static int hedge(float[][] payoffMat) {

		float totalWeight = 0;
		int p=0;
		while(p<4) {
			totalWeight = totalWeight + wgt[p];
			p++;
		}
	
		int item = 0;
		int q=0;
		while(q<4) { 
			p_vector[q] = wgt[q] / totalWeight;
			q++;
		}
		System.out.print("[");
		for (int m = 0; m < p_vector.length; m++) {

			System.out.print(p_vector[m] + ",");
		}
	
		System.out.println();
		item = numby_replacement(p_vector);
		System.out.println("updated Probablity  ");
		
		for (int m = 0; m < p_vector.length; m++) {

			System.out.print(p_vector[m] + ",");
		}
		System.out.print("]");
		System.out.println();
		for (int i = 0; i < 4; i++) {
			l_vector[i] += payoffMat[userChoice][i];

		}
		for (int i = 0; i < 4; i++) {
			wgt[i] = (float) (Math.exp(-(float) (0.3 * l_vector[i])));

		}

		return item;

	}

}