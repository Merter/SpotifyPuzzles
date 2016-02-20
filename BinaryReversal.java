
import java.util.Scanner;

public class BinaryReversal {

	private int number;
	private int reverseNumber;

	public static void main(String[] args) {
		BinaryReversal operation = new BinaryReversal();
		operation.getInput();
		operation.reverseInteger();
		System.out.println(operation.reverseNumber);
	}

	private void getInput() {
		Scanner in = new Scanner(System.in);
		number = in.nextInt();
		in.close();
	}

	private void reverseInteger() {
		int highOneMask = findHighestOneBitMask();
		reverseByAddingFromRight(highOneMask);
	}
	
	private int findHighestOneBitMask() {
		int mask = 1 << Integer.SIZE-1;
		while (true) {
			if ((mask & number) != 0) {
				break;
			}
			mask >>>= 1;
		}
		return mask;
	}
	
	private void reverseByAddingFromRight(int mask) {
		int reverseMask = 1;
		while (mask != 0) {
			if ((mask & number) != 0) {
				reverseNumber |= reverseMask;
			}
			reverseMask <<= 1;			
			mask >>>= 1;
		}
	}
}
