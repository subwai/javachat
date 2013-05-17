package client;

 import java.io.*;
 import java.io.File;
 import java.util.*;
 import javax.swing.filechooser.FileFilter; 
 
 class jpgSaveFilter extends FileFilter
 { 
    public boolean accept(File f)
   {
        if (f.isDirectory())
          {
            return false;
          }

         String s = f.getName();

        return s.endsWith(".jpg")||s.endsWith(".JPG");
   }

   public String getDescription() 
  {
       return "*.jpg,*.JPG";
  }

}