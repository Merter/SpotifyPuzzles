
import java.util.Scanner;

public class SimplerBinaryReversal {

  public static void main(String[] args) {
    SimplerBinaryReversal operation = new SimplerBinaryReversal();
    int number = operation.getInput();
    int reverseNumber = operation.reverseInteger(number);
    System.out.println(reverseNumber);
  }

  private int getInput() {
    Scanner in = new Scanner(System.in);
    int number = in.nextInt();
    in.close();
    return number;
  }

  private int reverseInteger(int number) {
    int mask = 1;
    int reverse = 0;
    int numberOfBitsToReverse = Integer.SIZE-Integer.numberOfLeadingZeros(number);
    for (int i=0; i<numberOfBitsToReverse; i++) {
      reverse <<= 1;
      int bit = number & mask;
      reverse |= bit;
      number >>>= 1;
    }
    return reverse;
  }
}

