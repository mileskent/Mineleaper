import java.io.*;
import java.util.Scanner;
public class FileManager
{
    public static String getTxt(String filename)
    {
        String end = "";
        String path = new File(filename).getAbsolutePath();
        try
        {
            Scanner sc = new Scanner(new File(path));
            while (sc.hasNextLine())
                end += sc.nextLine() + "\n";
            end = end.substring(0, end.length() - 1);
        }
        catch (Exception e)
        {
            ;
        }
        finally
        {
            return end;
        }
    }

    public static void writeTxt(String filename, String text) 
    {
        String path = new File(filename).getAbsolutePath();
        try 
        {
            FileWriter wr = new FileWriter(new File(filename));
            wr.write(text);
            wr.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

}
