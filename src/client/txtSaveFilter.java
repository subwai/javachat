package client;

 import java.io.*;
 import java.io.File;
 import java.util.*;
 import javax.swing.filechooser.FileFilter; 
 
 class txtSaveFilter extends FileFilter
 { 
    public boolean accept(File f)
   {
        if (f.isDirectory())
          {
            return false;
          }

         String s = f.getName();

        return s.endsWith(".txt");
   }

   public String getDescription() 
  {
       return "*.txt";
  }

}