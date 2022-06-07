import java.util.*;
import java.io.*;
public class Main
{

    private static String title = new FileManager().getTxt("title.txt");
    private static Scanner input = new Scanner(System.in);   
    private static String status = new FileManager().getTxt("playintro.txt").replace("\n", "").replace("\r", "");
    private static boolean playIntro = status.equals("true");

    public static void main(String[] args)
    {
        menu();
    }

    public static void menu()
    {
        clearScreen();
        System.out.print(title + "\n\n");
        System.out.print("Play\n\n");
        System.out.print("Help\n\n");
        System.out.print("Settings\n\n");
        System.out.print("Quit\n\n>>> ");
        String cmd = input.nextLine().toLowerCase();
        if (cmd.equals("play"))
        {
            game();
        }
        else if (cmd.equals("help"))
        {
            help();
        }
        else if (cmd.equals("quit"))
        {
            ;
        }
        else if (cmd.equals("settings"))
        {
            settings();
        }
        else
        {
            menu();
        }
    }

    public static void editBoard()
    {
        String[] v = new FileManager().getTxt("boardsize.txt").split(",", -1);
        int x = Integer.parseInt(v[0]);
        int y = Integer.parseInt(v[1]);
        System.out.println("board size: (" + x + ", " + y + ")");
        System.out.println("Difficulties:\n\t1. Can I play daddy? (6 x 5)\n\t2. Don't hurt me (10 x 8)\n\t3. Bring it on (13 x 9)\n\t4. I know what I'm doing (26 x 20)");
        System.out.println("Enter the number corresponding to the difficulty you like.");
        System.out.print(">>> ");
        int dif = 4;
        try
        {
            dif = Integer.parseInt(input.nextLine()); 
        }
        catch (Exception e)
        {
            clearScreen();
            System.out.println("Error. Enter a number.");
            editBoard();
        }
        
        if (dif > 5 || dif < 1)
        {
            clearScreen();
            System.out.println("Error. Enter a valid number.");
            editBoard();
        }
        
        if (dif == 1)
        {
            new FileManager().writeTxt("boardsize.txt", "6,5");
        }
        else if (dif == 2)
        {
            new FileManager().writeTxt("boardsize.txt", "10,8");
        }
        else if (dif == 3)
        {
            new FileManager().writeTxt("boardsize.txt", "13,9");
        }
        else if (dif == 4)
        {
            new FileManager().writeTxt("boardsize.txt", "26,20");
        }
    
    }
    
    public static void editIntro()
    {
        clearScreen();
        System.out.println("true, false");
        System.out.print("Play intro = ");
        String c2 = input.nextLine().toLowerCase();
        if (!c2.equals("true") && !c2.equals("false")){editIntro();}
        else if (!c2.equals(status)){new FileManager().writeTxt("playintro.txt", c2);}
        status = new FileManager().getTxt("playintro.txt").replace("\n", "").replace("\r", "");
        playIntro = status.equals("true");
    }

    public static void settings()
    {
        clearScreen();
        System.out.println("Play intro = " + status);
        String[] v = new FileManager().getTxt("boardsize.txt").split(",", -1);
        int x = Integer.parseInt(v[0]);
        int y = Integer.parseInt(v[1]);
        System.out.println("board size: (" + x + ", " + y + ")");
        System.out.println("playintro, difficulty, menu");
        String c1 = input.nextLine().toLowerCase();
        if (c1.equals("playintro"))
        {
            editIntro();
            menu();
        }
        else if (c1.equals("difficulty"))
        {
            clearScreen();
            editBoard();
            menu();            
        }
        else if (c1.equals("menu"))
        {
            menu();            
        }
        else
        {
            clearScreen();
            System.out.println("Invalid command");
            System.out.print("\n>>> ");
            input.nextLine();
            settings();
        }
    }

    public static void intro()
    {
        clearScreen();
        String text = new FileManager().getTxt("intro.txt");
        String[] dlgs = text.split("<>", -1);
        for (String dlg : dlgs)
        {
            System.out.println(dlg);
            System.out.print("\n>>> ");
            input.nextLine();
        }
    }

    public static void help()
    {
        clearScreen();
        String text = new FileManager().getTxt("help.txt");
        System.out.print(text);
        System.out.print("\n>>> ");
        input.nextLine();
        menu();
    }

    public static void game()
    {
        if (playIntro){intro();}
        clearScreen();
        Board board = new Board();
        String command = "";
        String cursorTemp = board.getBoard()[0][0]; 
        Cursor cursor = new Cursor(0, 0, board, new Color(cursorTemp, "\u001B[42m"));
        boolean bombsGenerated = false;
        double bombPercentage = 0.1;

        do
        {
            if (bombsGenerated && board.checkWin())
            {
                clearScreen();
                System.out.println(title);
                System.out.print("\n\nYou won! Nice...");
                input.nextLine();
                board.print(board.getUnder(), cursor);
                input.nextLine();              
                break;
            }

            if(!board.getBoard()[cursor.x][cursor.y].equals(cursor.getColor())){cursorTemp = board.getBoard()[cursor.x][cursor.y];}
            cursor.getColor().setText(board.getBoard()[cursor.x][cursor.y]);
            board.setSquare(cursor.x, cursor.y, cursor.getColor().getText());
            System.out.println(title);
            board.print(cursor);
            System.out.print("\n>>> ");
            command = input.nextLine().toLowerCase()    ;
            if (command.equals("s"))
            {
                board.setSquare(cursor.x, cursor.y, cursorTemp);
                cursor.move(0, 1);
            }
            else if (command.equals("d"))
            {
                board.setSquare(cursor.x, cursor.y, cursorTemp);
                cursor.move(1, 0);
            }
            else if (command.equals("w"))
            {
                board.setSquare(cursor.x, cursor.y, cursorTemp);
                cursor.move(0, -1);
            }
            else if (command.equals("a"))
            {
                board.setSquare(cursor.x, cursor.y, cursorTemp);
                cursor.move(-1, 0);
            }
            else if ((command.equals("flag") || command.equals("f")))
            {
                if (board.getSquare(cursor).equals(board.UNCHECKED))
                {
                    board.setSquare(cursor.x, cursor.y, board.FLAG);
                    cursor.getColor().setText(board.FLAG);
                }
                else if (board.getSquare(cursor).equals(board.FLAG))
                {
                    board.setSquare(cursor.x, cursor.y, board.UNCHECKED);
                    cursor.getColor().setText(board.UNCHECKED);
                }
            }

            else if (command.equals("check") || command.equals("c"))
            {
                if (!bombsGenerated)
                {
                    board.generateBombs((int)(board.getWidth() * board.getHeight() * bombPercentage), cursor);
                    bombsGenerated = true;
                }

                /*
                 * If check a number, only reveal the number
                 * If check a bomb, game over
                 * If check an open space, reveal all open space as well as all numbers adjacent to the open space
                 */
                String u = board.getUnder()[cursor.x][cursor.y];
                if (isNumeric(u))
                {
                    board.setSquare(cursor.x, cursor.y, u);
                    cursor.getColor().setText(u);
                }
                else if (u.equals(board.BOMB))
                {
                    clearScreen();
                    cursor.getColor().setText(board.BOMB);
                    System.out.println(title);
                    board.print(board.getUnder(), cursor);
                    System.out.print("\n\nYou lost! What a shame...");
                    input.nextLine();
                    break;
                }
                else if (u.equals("."))
                {
                    board.emptyClick(cursor);
                }

            }
            else if (command.length() >= 2 && isAlpha(command.substring(0, 1)) && isNumeric(command.substring(1)))
            {
                board.setSquare(cursor.x, cursor.y, cursorTemp);
                cursor.setPos(board.inputToCoords(command));
            }
            else if (command.equals("menu")){break;}
            clearScreen();
        }   
        while(!command.equals("quit"));

        if (command.equals("menu")){menu();}
        else if (!command.equals("quit")){lose();}
        else if (board.checkWin()){win();}

    }

    public static void lose()
    {
        clearScreen();
        System.out.print(title + "\n\n");
        System.out.print("Play again?\n");
        String cmd = input.nextLine().toLowerCase();
        if (cmd.equals("yes") || cmd.equals("y"))
        {
            clearScreen();
            game();
        }
        else if (cmd.equals("no") || cmd.equals("n") || cmd.equals("quit"))
        {
            ;
        }
        else
        {
            lose();
        }
    }

    public static void win()
    {
        clearScreen();
        System.out.print(title + "\n\n");
        System.out.print("Play again?\n");
        String cmd = input.nextLine().toLowerCase();
        if (cmd.equals("yes") || cmd.equals("y"))
        {
            clearScreen();
            game();
        }
        else if (cmd.equals("no") || cmd.equals("n") || cmd.equals("quit"))
        {
            ;
        }
        else
        {
            win();
        }
    }

    public static boolean isNumeric(String str)
    {
        if (str == null) 
        {
            return false;
        }
        try 
        {
            int d = Integer.parseInt(str);
        } 
        catch (NumberFormatException nfe) 
        {
            return false;
        }
        return true;
    }

    public static boolean isAlpha(String str)
    {
        int val = (int) str.charAt(0);
        return 97 <= val && val <= 122;
    }

    public static void clearScreen()
    {
        //Clears Screen
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }


}