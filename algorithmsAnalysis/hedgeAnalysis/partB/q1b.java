//Collaborators: Gaurav Bishnoi, Sushant Mittal
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class q1b {
	static float[] wgt = new float[2];
	static float[] p_vector = new float[2];

	public static void main(String[] args) {

		Random rg = new Random();

		float[] l_vector = new float[2];

		float[] regret_vector = new float[2000];
		float[] time_vector = new float[2000];
		int p = 0;
		int turn = 0;

		for (int s = 0; s < 2; s++) {
			l_vector[s] = 0;
		}
		for (int s = 0; s < 2; s++) {
			wgt[s] = 1;
		}

		for (int i = 0; i < (2000); i++) {

			float[] lossAction = new float[4];
			float totalSum = 0.0f;

			int hedge_out = hedge(l_vector);

		
		
			for (int act = 0; act < 2; act++) {
				int x = rg.nextInt(2);
				l_vector[act] += x;

			}
			

			for (int j = 0; j < 2; j++) {

				totalSum = totalSum + l_vector[j] * p_vector[j];
				lossAction[j] += l_vector[j];
			}
			regret_vector[i] = (totalSum - minimum(lossAction)) / (i + 1);
			time_vector[i] = i;

			System.out.println("sum array" + totalSum);
			// printArray();
			System.out.println("loss array");
			// printArray(loss_vector1);
		}

		System.out.println("time_vector array");
		System.out.print("[");
		for (int m = 0; m < time_vector.length; m++) {

			System.out.print(time_vector[m] + ",");
		}
		System.out.print("]");
		System.out.println();
		System.out.println("regret_vector array");
		System.out.print("[");
		for (int m = 0; m < regret_vector.length; m++) {

			System.out.print(regret_vector[m] + ",");
		}
		System.out.print("]");
		System.out.println();

	}

	public static int hedge(float[] l_vector) {

		float sum_of_weight = 0;
		for (int i = 0; i < 2; i++) {
			sum_of_weight = sum_of_weight + wgt[i];

		}
		float max = 0;
		int index = 0;
		for (int i = 0; i < 2; i++) {
			p_vector[i] = wgt[i] / sum_of_weight;
			// below function
	// Ref:-http://stackoverflow.com/questions/9330394/how-to-pick-an-item-by-its-probability
			double p = Math.random();
			double cumulativeProbability = 0.0;
			for (int item = 0; item < p_vector.length; item++) {
				cumulativeProbability += p_vector[item];
				if (p <= cumulativeProbability) {
					index = item;
					break;
				}
			}

		}

		for (int i = 0; i < 2; i++) {

			wgt[i] = (float) (Math.exp(-(float) (0.001 * l_vector[i])));

		}

		return index;

	}

	public static float minimum(float[] a) {
		float minimum = 0;
		float[] a1 = new float[a.length - 1];
		a1 = Arrays.copyOf(a, a.length);

		Arrays.sort(a1);
		return a1[0];

	}

}