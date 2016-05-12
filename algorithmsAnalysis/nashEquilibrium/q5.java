
//Collaborators: Gaurav Bishnoi, Sushant Mittal

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class q5 {

	
	static double[] lossVectorP = new double[5];
	static double[] lossVectorQ = new double[5];
	static double[] pVector = new double[5];
	static double[] qVector = new double[5];
	static double[] wget = new double[5];
	static double[] wget1 = new double[5];
	


	public static void main(String[] args) {
		
		String SID ="103620281";
		double[] pt = new double[5];
		double[] qt_avg = new double[5];
		double[] sumP = new double[5];
		double[] sumQ = new double[5];
		double[][] id = new double[5][5];
		double totalSum = 0.0f;
		
		Random rg = new Random();

		for (int row = 0; row < 5; row++) {
			for (int m = 0; m < 5; m++) {
				char c =SID.charAt((m+5*row)%SID.length());
				id[row][m] =Character.getNumericValue(c) ;
				
			}
			
		}
		System.out.println("Payoff Matrix");
		for (int l = 0; l < 5; l++) {
			System.out.print("[");
			for (int m = 0; m < 5; m++) {

				System.out.print(id[l][m] + "  ");
			}
			System.out.print("]");
			System.out.println();

		}


		for (int s = 0; s < 5; s++) {
			lossVectorP[s] = 0;
		}
		for (int s = 0; s < 5; s++) {
			wget[s] = 1;
			wget1[s] = 1;

			
		}

		for (int i = 0; i < 200; i++) {
			int user_input;
			user_input = 1;
			sumP = hedgeP(id);
			sumQ = hedgeQ(id);
		}
		
		for (int i = 0; i < 5; i++) {

			pt[i] = sumP[i]/200 ;
			qt_avg[i] = sumQ[i]/200 ;
		}
		
		System.out.println("p average = ");
		printArray(pt);
		System.out.println("pT = ");
		printArray(pVector);
		System.out.println("q average = ");
		printArray(qt_avg);
		System.out.println("qT = ");
		printArray(qVector);
		double lastp = multiplyThree(pVector, id, qt_avg);
		double avgp = multiplyThree(pt, id, qt_avg);
		double lastq =multiplyThree(pt, id, qVector);
		double avgq =multiplyThree(pt, id, qt_avg);
		System.out.println(" lastp " +(lastp));
		System.out.println(" avgp " + avgp);
		System.out.println(" lastq " +(lastq));
		System.out.println(" avgq " + avgq);
		System.out.println(" regret p " +(lastp -avgp));
		System.out.println(" regret q " +(lastq -avgq));
	}

	public static void printArray(double[] arr) {

		System.out.print("[");
		for (int m = 0; m < arr.length; m++) {
			System.out.print(arr[m] + "  ");
		}
		
		System.out.print("]");
		System.out.println();
	}
	
	// below function
	// Ref:-http://stackoverflow.com/questions/9330394/how-to-pick-an-item-by-its-probability
	public static int numby_replacement(double[] pVector) {
		int index = 0;
		double p = Math.random();
		double cumulativeProbability = 0.0;
		for (int item = 0; item < pVector.length; item++) {
			cumulativeProbability += pVector[item];
			if (p <= cumulativeProbability) {
				index = item;
				break;
			}
		}
		return index;

	}

	public static double[] hedgeP(double[][] id) {
		double lr = 1 / 5;
		double sum_of_weight = 0;
		for (int i = 0; i < 5; i++) {
			sum_of_weight = sum_of_weight + wget[i];
		}
		double max = 0;
		int index = 0;
		double[] sum = new double[5];
		for (int i = 0; i < 5; i++) {
			pVector[i] = wget[i] / sum_of_weight;
			sum[i] = sum[i] + pVector[i];
		}
		index = numby_replacement(pVector);
		for (int action = 0; action < 5; action++) {
			lossVectorP[action] += multiply(id)[action];

		}
		for (int i = 0; i < 5; i++) {
			wget[i] = (double) (Math.exp(-(double) (0.25 * lossVectorP[i])));

		}
		return sum;
	}

	public static double[] hedgeQ(double[][] id) {
		double lr = 1 / 5;
		double sum_of_weight = 0;
		double[] sum = new double[5];
		for (int i = 0; i < 5; i++) {
			sum_of_weight = sum_of_weight + wget1[i];

		}
		double max = 0;
		int index = 0;
		for (int i = 0; i < 5; i++) {
			qVector[i] = wget1[i] / sum_of_weight;
			sum[i] = sum[i] + qVector[i];

		}
		index = numby_replacement(qVector);
		for (int action = 0; action < 5; action++) {
			lossVectorQ[action] += multiplyTwo(id)[action];
		
			

		}
		for (int i = 0; i < 5; i++) {
			wget1[i] = (double) (Math.exp(-(double) (0.25 * lossVectorQ[i])));

		}


		return sum;

	}

	public static double[] multiply(double[][] id) {
		double[] gives = new double[5];
		for (int l = 0; l < 5; l++) {

			for (int m = 0; m < 5; m++) {
				gives[l] = gives[l] + pVector[m] * id[m][l];

			}

		}
		return gives;
	}
	
	public static double multiplyThree(double [] p1,double[][] id,double []q1) {
		double[] gives = new double[5];
		double sum_q=0.0;
		for (int l = 0; l < 5; l++) {

			for (int m = 0; m < 5; m++) {
				gives[l] = gives[l] + q1[m] * id[l][m];
			}
			sum_q +=gives[l]*p1[l];

		}
		
		return sum_q;
	}
	
	public static double[] multiplyTwo(double[][] id) {
		double[] gives = new double[5];
		for (int l = 0; l < 5; l++) {

			for (int m = 0; m < 5; m++) {
				gives[l] = gives[l] + qVector[m] * id[l][m];

			}

		}
		
		return gives;
	}
	
}