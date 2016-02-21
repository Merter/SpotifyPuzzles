
import java.util.*;

public class SongSelector {

  private PriorityQueue<Song> songs;
  private int selectionCount;
  private Song[] bestSongs;
  private static long firstSongCount;	

  private class Song implements Comparable<Song> {
    private long songCount;
    private int order;
    private String name;
    private double quality;

    public Song(long songCount, int order, String name) {
      this.songCount = songCount;
      this.order = order;
      this.name = name;
      if (order == 1) {
        SongSelector.firstSongCount = songCount;
      }
      this.quality = (songCount * order) / (double) SongSelector.firstSongCount;
    }

    public int compareTo(Song other) {
      final int BEFORE = -1;
      final int EQUAL = 0;
      final int AFTER = 1;

      if (this == other) {
        return EQUAL;
      }
      if (this.quality < other.quality) {
        return AFTER;
      }
      if (this.quality > other.quality) {
        return BEFORE;
      }
      if (this.order < other.order) {
        return BEFORE;
      }
      return AFTER;
    }
  }

  public static void main(String[] args) {
    SongSelector songSelector = new SongSelector();
    songSelector.getInput();		
    songSelector.extractBestSongs();
    songSelector.show();
  }

  private void getInput() {
    Scanner in = new Scanner(System.in);
    int count = in.nextInt();
    songs = new PriorityQueue<Song>(count);
    selectionCount = in.nextInt();
    for (int index=0; index<count; index++) {
      long songCount = in.nextLong();
      String name = in.next();
      Song newSong = new Song(songCount, index+1, name);
      songs.add(newSong);
    }
    in.close();
  }

  private void extractBestSongs() {
    bestSongs = new Song[selectionCount];
    for (int index=0; index<selectionCount; index++) {
      bestSongs[index] = songs.poll();	
    }
  }

  private void show() {
    StringBuilder sb = new StringBuilder();
    for (Song song : bestSongs) {
      sb.append(song.name);
      sb.append("\n");
    }
    System.out.println(sb.toString().trim());
  }
}
