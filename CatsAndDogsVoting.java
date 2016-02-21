
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CatsAndDogsVoting {

  public static void main(String[] args) {
    CatsAndDogsVoting voting = new CatsAndDogsVoting();
    Scanner in = new Scanner(System.in);
    int numberOfTests = in.nextInt();
    int[] results = new int[numberOfTests];		
    for (int test=0; test<numberOfTests; test++) {
      results[test] = voting.doTest(test, in);
    }
    in.close();		
    voting.printResults(results);
  }

  private int doTest(int test, Scanner in) throws IllegalArgumentException {
    in.nextInt();	// won't use cat count
    in.nextInt();	// won't use dog count
    int numberOfVotes = in.nextInt();
    VoteInteractionGraph voteInteractions = new VoteInteractionGraph(numberOfVotes);
    for (int index=0; index<numberOfVotes; index++) {
      String accept = in.next();
      String reject = in.next();
      Vote vote = new Vote(index, accept, reject);
      voteInteractions.add(vote);
    }
    // 		VERBOSE ON
    // 		voteInteractions.showConflictingVotes();
    // 		VERBOSE OFF		
    return voteInteractions.maxViewers();
  }

  private class VoteInteractionGraph {
    private int numberOfVotes;
    private int conflictingGroupCount;
    private Set<Vote> allVotes;
    private Set<Vote> isConsidered;
    private Map<Vote, Boolean> selection;
    private Map<Vote, ConflictingGroup> conflictingVoteGroups;

    public VoteInteractionGraph(int numberOfVotes) {
      this.numberOfVotes = numberOfVotes;		
      allVotes = new HashSet<Vote>(numberOfVotes);
      isConsidered = new HashSet<Vote>(numberOfVotes);
      selection = new HashMap<Vote, Boolean>(numberOfVotes);
      conflictingVoteGroups = new HashMap<Vote, ConflictingGroup>();
    }

    public void add(Vote vote) {
      findConflictingVotesWith(vote);
      allVotes.add(vote);
    }

    private void findConflictingVotesWith(Vote vote) {
      for (Vote prevVote : allVotes) {
        if ((vote.accept.equals(prevVote.reject)) 
            || (prevVote.accept.equals(vote.reject))) {
          addConflict(prevVote, vote);
            }
      }
    }

    private void addConflict(Vote fromVote, Vote toVote) {
      fromVote.conflicts.add(toVote);
      toVote.conflicts.add(fromVote);
    }

    // 		public void showConflictingVotes() {
    // 			System.out.println("CONFLICTS:");
    // 			for (Vote vote : allVotes) {
    // 				StringBuilder sb = new StringBuilder();
    // 				sb.append(vote.id);
    // 				sb.append(" (");
    // 				sb.append(vote.conflicts.size());
    // 				sb.append("): ");
    // 				for (Vote conflictingVote : vote.conflicts) {
    // 					sb.append(conflictingVote.id);
    // 					sb.append(" ");
    // 				}
    // 				System.out.println(sb.toString().trim());
    // 			}
    // 		}

    public int maxViewers() throws IllegalArgumentException {
      for (Vote vote : allVotes) {
        if (!isConsidered.contains(vote)) {
          dfs(vote, null);
        }
      }
      //	 		VERBOSE ON
      // 			showConflictingGroups();
      // 			showSelectedVotes();
      //	 		VERBOSE OFF			
      int maxSatisfiedViewerCount = 0;
      Set<ConflictingGroup> uniqueGroups = getUniqueGroups();
      for (ConflictingGroup conflictingGroup : uniqueGroups) {
        maxSatisfiedViewerCount += conflictingGroup.getMaxSatisfiedSelectionCount();
      }
      return maxSatisfiedViewerCount;
    }

    private void dfs(Vote vote, Vote parentVote) throws IllegalArgumentException {		
      if (!isConsidered.contains(vote)) {
        isConsidered.add(vote);
        ConflictingGroup conflictingGroup = null;
        if (parentVote == null) {
          conflictingGroupCount++;
          conflictingGroup = new ConflictingGroup(conflictingGroupCount);
          conflictingGroup.incTrue();
          selection.put(vote, Boolean.TRUE);
        } else {
          conflictingGroup = conflictingVoteGroups.get(parentVote);
          selection.put(vote, new Boolean(!selection.get(parentVote).booleanValue()));
          if (selection.get(vote).booleanValue()) {
            conflictingGroup.incTrue();
          } else {
            conflictingGroup.incFalse();
          }				
        }
        conflictingVoteGroups.put(vote, conflictingGroup);
        for (Vote conflictingVote : vote.conflicts) {
          if (!isConsidered.contains(conflictingVote)) {
            dfs(conflictingVote, vote);
          } else {
            if (selection.get(vote).booleanValue() == selection.get(conflictingVote).booleanValue()) {
              throw new IllegalArgumentException();
            }
          }
        }
      }
    }

    // 		private void showConflictingGroups(){
    // 			StringBuilder sb = new StringBuilder("GROUPS:\n");
    // 			for (Vote vote : allVotes) {
    // 				sb.append(vote.id);
    // 				sb.append(": ");
    // 				sb.append(conflictingVoteGroups.get(vote).id);
    // 				sb.append("\n");
    // 			}
    // 			System.out.println(sb.toString().trim());			
    // 		}

    // 		private void showSelectedVotes(){
    // 			StringBuilder sb = new StringBuilder("SELECTIONS:\n");
    // 			for (Vote vote : allVotes) {
    // 				if (conflictingVoteGroups.get(vote).selectionTrueCount 
    // 					> conflictingVoteGroups.get(vote).selectionFalseCount) {
    // 					if (selection.get(vote).booleanValue()) {
    // 						sb.append(vote.id);
    // 						sb.append("\n");
    // 					}
    // 				} else {
    // 					if (!selection.get(vote).booleanValue()) {
    // 						sb.append(vote.id);
    // 						sb.append("\n");
    // 					}				
    // 				}
    // 			}
    // 			System.out.println(sb.toString().trim());			
    // 		}		

    private Set<ConflictingGroup> getUniqueGroups () {
      Set<ConflictingGroup> uniqueGroups = new HashSet<ConflictingGroup>(numberOfVotes);
      for (ConflictingGroup conflictingGroup : conflictingVoteGroups.values()) {
        uniqueGroups.add(conflictingGroup);
      }
      return uniqueGroups;
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

    public int hashCode() {
      return (new Integer(id)).hashCode();
    }
  }

  private class ConflictingGroup {
    private int id;
    private int selectionTrueCount;
    private int selectionFalseCount;

    public ConflictingGroup(int id) {
      this.id = id;
    }

    public void incTrue() {
      selectionTrueCount++;
    }

    public void incFalse() {
      selectionFalseCount++;
    }

    public int getMaxSatisfiedSelectionCount() {
      return Math.max(selectionTrueCount, selectionFalseCount);
    }
  }

  private void printResults(int[] results) {
    int numberOfTests = results.length;
    StringBuilder sb = new StringBuilder();
    for (int test=0; test<numberOfTests; test++) {
      sb.append(results[test]);
      sb.append("\n");
    }
    System.out.println(sb.toString().trim());
  }
}
