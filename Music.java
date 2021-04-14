package tech.codingclub.entity;

public class Music {
    public Music(String parent_link, String link, String album, String duration, String singers, String lyricist, String music_director, String download_128, String download_256) {
        this.parent_link = parent_link;
        this.link = link;
        this.album = album;
        this.duration = duration;
        this.singers = singers;
        this.lyricist = lyricist;
        this.music_director = music_director;
        this.download_128 = download_128;
        this.download_256 = download_256;
    }

   public Music (){

    }
    String parent_link;
    String link;
    String album;
    String duration;
    String singers;
    String lyricist;
    String music_director;
    String download_128;
    String download_256;
}
