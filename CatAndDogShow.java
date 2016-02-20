
import java.util.*;

public class CatAndDogShow {

	private int[] results;
	
	public static void main(String[] args) {
		CatAndDogShow show = new CatAndDogShow();
		Scanner in = new Scanner(System.in);
		int numberOfTests = in.nextInt();
		show.results = new int[numberOfTests];		
		for (int test=0; test<numberOfTests; test++) {
			show.doTest(test, in);
		}
		in.close();		
		show.printResults();
	}
		
	private void doTest(int test, Scanner in) {
		int catCount = in.nextInt();
		int dogCount = in.nextInt();			
		int numberOfVotes = in.nextInt();
		ArrayDeque<Vote> votes = new ArrayDeque<Vote>(numberOfVotes);			
		for (int index=0; index<numberOfVotes; index++) {
			String accept = in.next();
			String reject = in.next();
			Vote vote = new Vote(accept, reject);
			votes.add(vote);
		}
		results[test] = maxViewers(votes);	
	}

	private class Vote {
		private String accept;
		private String reject;
		
		public Vote(String accept,String reject) {
			this.accept = accept;
			this.reject = reject;
		}
	}

	private int maxViewers(ArrayDeque<Vote> remainingVotes) {
		if (remainingVotes.size() == 0) {
			return 0;
		}
		TreeSet<VoteCount> sortedVotes = calculateVotes(remainingVotes);
		VoteCount minAnimal = sortedVotes.first();
		VoteCount maxAnimal = sortedVotes.last();

		if (minAnimal.id.equals(maxAnimal.id)) {
			return Math.abs(minAnimal.voteCount);
		}

		ArrayDeque<Vote> aux = new ArrayDeque<Vote>();
		int satisfiedViewerCounter = 0;
		for (Vote vote : remainingVotes) {
			if ((vote.accept.equals(maxAnimal.id)) || (vote.accept.equals("OK"))) {
				if ((vote.reject.equals(minAnimal.id)) || (vote.reject.equals("OK"))) {
					satisfiedViewerCounter++;
				} else {
					vote.accept = "OK";
					aux.add(vote);
				}
			} else if (vote.reject.equals(minAnimal.id)) {
				if (vote.reject.equals(minAnimal.id)) {
					vote.reject = "OK";
				}
				aux.add(vote);
			} else if ((!vote.accept.equals(minAnimal.id)) && (!vote.reject.equals(maxAnimal.id))) {
				aux.add(vote);
			}
		}
		return satisfiedViewerCounter + maxViewers(aux);
	}
	
	private TreeSet<VoteCount> calculateVotes(ArrayDeque<Vote> remainingVotes) {
		Map<String, VoteCount> votes = new HashMap<String, VoteCount>();
		for (Vote vote : remainingVotes) {
			VoteCount voteCountInHash = null;
			if (!vote.accept.equals("OK")) {
				voteCountInHash = votes.get(vote.accept);
				if (voteCountInHash != null) {
					voteCountInHash.incrementVote();
				} else {
					votes.put(vote.accept, new VoteCount(vote.accept, 1));
				}
			}			
			if (!vote.reject.equals("OK")) {
				voteCountInHash = votes.get(vote.reject);
				if (voteCountInHash != null) {
					voteCountInHash.decrementVote();
				} else {
					votes.put(vote.reject, new VoteCount(vote.reject, -1));
				}
			}
		}
		TreeSet<VoteCount> sortedVotes = new TreeSet<VoteCount>(votes.values());
		return sortedVotes;	
	}
	
	private class VoteCount implements Comparable<VoteCount> {
		private String id;
		private int voteCount;
	
		public VoteCount(String id, int voteCount) {
			this.id = id;
			this.voteCount = voteCount;
		}
	
		public void incrementVote() {
			voteCount++;
		}
	
		public void decrementVote() {
			voteCount--;
		}	
	
		public int compareTo(VoteCount other) {
			final int BEFORE = -1;
			final int EQUAL = 0;
			final int AFTER = 1;					
			if (this == other) {
				return EQUAL;
			}
			if (this.voteCount < other.voteCount) {
				return BEFORE;
			}
			if (this.voteCount > other.voteCount) {
				return AFTER;
			}
			return EQUAL;
		}
	}
	
	private void printResults() {
		int numberOfTests = results.length;
		StringBuilder sb = new StringBuilder();
		for (int test=0; test<numberOfTests; test++) {
			sb.append(results[test]);
			sb.append("\n");
		}
		System.out.println(sb.toString().trim());
	}
}
