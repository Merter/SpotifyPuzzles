
import java.util.Random;

public class CatsAndDogsTestGenerator {

	private	int catCount;
	private	int dogCount;
	private int voteCount;
	private static final int MAX_CAT_COUNT = 10;
	private static final int MAX_DOG_COUNT = 10;
	private static final int MAX_VOTE_COUNT = 50;

	public static void main(String[] args) {
		CatsAndDogsTestGenerator generator = new CatsAndDogsTestGenerator();
		String testText = generator.generateTest();
		System.out.println(testText);
	}

	private String generateTest() {
		StringBuilder sb = new StringBuilder();
		sb.append("1\n");
		Random rnd = new Random();
		catCount = rnd.nextInt(MAX_CAT_COUNT) + 1;
		dogCount = rnd.nextInt(MAX_DOG_COUNT) + 1;
		sb.append(catCount);
		sb.append(" ");
		sb.append(dogCount);
		sb.append(" ");
		voteCount = rnd.nextInt(MAX_VOTE_COUNT+1);
		sb.append(voteCount);
		sb.append("\n");		
		for (int i=0; i<voteCount; i++) {
			boolean isCat = rnd.nextBoolean();
			if (isCat) {
				sb.append(addCat());
				sb.append(" ");
				sb.append(addDog());				
				sb.append("\n");
			} else {
				sb.append(addDog());
				sb.append(" ");
				sb.append(addCat());				
				sb.append("\n");
			}
		}
		return sb.toString().trim();
	}

	private String addDog() {
		Random rnd = new Random();	
		StringBuilder sb = new StringBuilder();
		sb.append("D");		
		sb.append(rnd.nextInt(dogCount)+1);
		return sb.toString().trim();
	}
	
	private String addCat() {
		Random rnd = new Random();	
		StringBuilder sb = new StringBuilder();
		sb.append("C");
		sb.append(rnd.nextInt(catCount)+1);
		return sb.toString().trim();	
	}

}
