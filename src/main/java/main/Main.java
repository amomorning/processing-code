package main;

import io.socket.client.IO;
import processing.core.PApplet;

import java.net.URISyntaxException;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Main {
   public static void main(String[] args) {
      try {
         if (args.length > 0) {
            Show.socket = IO.socket(args[0]);
         } else {
            Show.socket = IO.socket("http://127.0.0.1:2601");
         }
         PApplet.main("main.Show");
      } catch (URISyntaxException e) {
         e.printStackTrace();
      }
   }

}
