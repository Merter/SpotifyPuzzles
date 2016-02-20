
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class CatAndDogVoting {

	private int[] results;

	public static void main(String[] args) {
		CatAndDogVoting voting = new CatAndDogVoting();
		Scanner in = new Scanner(System.in);
		int numberOfTests = in.nextInt();
		voting.results = new int[numberOfTests];		
		for (int test=0; test<numberOfTests; test++) {
			voting.doTest(test, in);
		}
		in.close();		
		voting.printResults();
	}
		
	private void doTest(int test, Scanner in) {
		int catCount = in.nextInt();
		int dogCount = in.nextInt();			
		int numberOfVotes = in.nextInt();
		VoteInteractionGraph voteInteractions = new VoteInteractionGraph();
		for (int index=0; index<numberOfVotes; index++) {
			String accept = in.next();
			String reject = in.next();
			Vote vote = new Vote(index+1, accept, reject);
			voteInteractions.add(vote);
		}
		results[test] = maxViewers(voteInteractions);	
	}

	private class VoteInteractionGraph {
		private PriorityQueue<VoteConflictCount> maxVoteQueue;
		private HashMap<Integer, VoteConflictCount> allVotes;
		
		public VoteInteractionGraph() {
			maxVoteQueue = new PriorityQueue<VoteConflictCount>();
			allVotes = new HashMap<Integer, VoteConflictCount>();
		}
		
		public void add(Vote vote) {
			VoteConflictCount voteConflictCount = new VoteConflictCount();
			voteConflictCount.vote = vote;
			findConflictingVotesWith(voteConflictCount);
			allVotes.put(vote.id, voteConflictCount);
		}
		
		private void findConflictingVotesWith(VoteConflictCount voteConflictCount) {
			Vote currentVote = voteConflictCount.vote;
			for (Integer prevVoteId : allVotes.keySet()) {
				VoteConflictCount prevVoteCount = allVotes.get(prevVoteId);
				Vote prevVote = prevVoteCount.vote;
				if ((currentVote.accept.equals(prevVote.reject)) 
					|| (prevVote.accept.equals(currentVote.reject))) {
					addConflict(prevVoteCount, voteConflictCount);
				}
			}
		}
		
		private void addConflict(VoteConflictCount fromVote, VoteConflictCount toVote) {
			fromVote.vote.conflicts.add(toVote.vote);
			fromVote.increaseTempConflictCount();
			toVote.vote.conflicts.add(fromVote.vote);
			toVote.increaseTempConflictCount();
		}
		
		public void findMaxConflictedVote() {
			for (Integer voteId : allVotes.keySet()) {
				VoteConflictCount voteCount = allVotes.get(voteId);
				maxVoteQueue.add(voteCount);
			}		
		}
		
		public void removeMaxConflictedVote() {
			VoteConflictCount maxConflictedVote = maxVoteQueue.poll();
			for (Vote conflictedVote : maxConflictedVote.vote.conflicts) {
				VoteConflictCount voteInHash = allVotes.get(conflictedVote.id);
				if (voteInHash != null) {
					maxVoteQueue.remove(conflictedVote);
					voteInHash.decreaseTempConflictCount();
					maxVoteQueue.add(voteInHash);
				}
			}
			allVotes.remove(maxConflictedVote.vote.id);		
		}

		public int getNumberOfConflictsOfMaxVote() {
			if (maxVoteQueue.size() > 0) {
				return maxVoteQueue.peek().tempConflictCount;
			}
			return 0;
		}
		
		public int remainingVotes() {
			return allVotes.size();
		}
	}

	private class VoteConflictCount implements Comparable<VoteConflictCount> {
		private Vote vote;
		private int tempConflictCount;
		
		public void increaseTempConflictCount() {
			tempConflictCount++;
		}		
		
		public void decreaseTempConflictCount() {
			tempConflictCount--;
		}
		
		public int compareTo(VoteConflictCount other) {
			final int BEFORE = -1;
			final int EQUAL = 0;
			final int AFTER = 1;
			
			if (this == other) {
				return EQUAL;
			}
			if (this.tempConflictCount > other.tempConflictCount) {
				return BEFORE;
			}
			if (this.tempConflictCount < other.tempConflictCount) {
				return AFTER;
			}
			return EQUAL;
		}
	}
	
	private class Vote {
		private int id;
		private String accept;
		private String reject;
		private ArrayDeque<Vote> conflicts;
		
		public Vote(int id, String accept, String reject) {
			this.id = id;
			this.accept = accept;
			this.reject = reject;
			conflicts = new ArrayDeque<Vote>();
		}		
	}

	private int maxViewers(VoteInteractionGraph voteInteractions) {		
		voteInteractions.findMaxConflictedVote();
		while (voteInteractions.getNumberOfConflictsOfMaxVote() > 0) {
			voteInteractions.removeMaxConflictedVote();
		}
		return voteInteractions.remainingVotes();
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
