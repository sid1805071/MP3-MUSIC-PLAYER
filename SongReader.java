package tech.codingclub.database;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.codingclub.Utility.FileUtility;
import tech.codingclub.Utility.HttpURLConnectionExample;
import tech.codingclub.Utility.TaskManager;
import tech.codingclub.entity.Music;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SongReader implements Runnable{
    static ArrayList<String> parent_url = new ArrayList<String>();
    static  ArrayList<String> link = new ArrayList<String>();

    public void run() {
        // reading the file
        ArrayList<String> parent= FileUtility.readAndPrintFile("C:\\Users\\KIIT\\IdeaProjects\\TechCodingMafia\\data\\practice\\file\\crawlsonglinks");
        for( String a : parent ){
          parent_url.add(a);
        }

        // getting links HTML
        String urlHTML="";
       for( String url : parent_url ){
           try {
               int i = 0;
//               System.setProperty("http.agent", "Mozilla/5.0");
               urlHTML = HttpURLConnectionExample.sendGet(url);
               Document document = Jsoup.parse(urlHTML, "https://songspk.mobi/");
               Elements childElements = document.body().select(".thumb-image > *").tagName("a href");
               for (Element childElement : childElements) {
                   String links = childElement.attr("href");
                   link.add(links);
                   i++;
               }
               String urlLink = "";
               for (String childUrl : link) {
                   String parent_link = url;
                   String link= childUrl;
                   String album="";
                   String duration="";
                   String singers="";
                   String lyricist="";
                   String music_director="";
                   String download_128="";
                   String download_256="";
                  try{
                       String ChildurlHTML = HttpURLConnectionExample.sendGet("https://songspk.mobi"+childUrl);
                       Document document1 = Jsoup.parse(ChildurlHTML);
                       Elements element1 =document1.body().select(".page-meta-wrapper .row .page-meta ul li .text-left");
                      Elements element2 =document1.body().select(".page-meta-wrapper .row .page-meta ul li .text-right");
                      Elements download =document.body().select(".page-zip-wrap .col-body .page-down-btns a");
                      download_128= document1.body().select(".page-down-btns a").get(0).attr("href");
                      download_256=document1.body().select(".page-down-btns a").get(1).attr("href");
                      for(int j=0; j<10;j++){
                          if(element2.get(j).text().trim().compareTo("Album")== 0){
                              album=element1.get(j).text();
                          }else if(element2.get(j).text().trim().compareTo("Duration")== 0){
                              duration=element1.get(j).text();
                          }else if(element2.get(j).text().trim().compareTo("Singers")== 0){
                              singers=element1.get(j).text();
                          }else if(element2.get(j).text().trim().compareTo("Lyricist")== 0){
                              lyricist =element1.get(j).text();
                          }else if(element2.get(j).text().trim().compareTo("Music Director")== 0){
                              music_director =element1.get(j).text();
                          }
                      }

                  }catch (Exception e){

                  }
                   Music music =new Music(parent_link,link,album,duration,singers,lyricist,music_director,download_128,download_256);
                   new GenericDB<Music>().addRow(tech.codingclub.tables.Music.MUSIC,music);
               }
           }catch (Exception e) {

           }
       }
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager(100);
        SongReader song = new SongReader();
        taskManager.waitTillQueueIsFreeAndAddTask(song);

    }
}
