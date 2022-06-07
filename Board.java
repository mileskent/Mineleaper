import java.util.*;

public class Board
{
    public final String FLAG = "F";
    public final String UNCHECKED = "+";
    public final String BOMB = "M";
    public final String EMPTY = ".";
    private int width;
    private int height;
    private static String[][] board;
    private static Position[] bombs;
    private static String[][] under; 

    public Board(int width, int height)
    {
        this.width = width;
        this.height = height;
        init();
    }

    public Board()
    {
        try
        {
            String[] v = new FileManager().getTxt("boardsize.txt").split(",", -1);
            this.width = Integer.parseInt(v[0]);
            this.height = Integer.parseInt(v[1]);
        } catch (Exception e)
        {
            this.width = 26;
            this.height = 20;
        }
        init();
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }      

    public void showBombs()
    {
        for (Position coord : bombs)
        {
            System.out.print("(" + coord.x + "," + coord.y + ")");
        }
    }

    public void setSquare(int x, int y, String str)
    {
        board[x][y] = str;
    }

    public String[][] getBoard()
    {
        return board;
    }

    public void setBoardSize(int x, int y)
    {
        this.width = x;
        this.height = y;
    }

    public String getSquare(Position pos)
    {
        return board[pos.x][pos.y];
    }

    public String[][] getUnder()
    {
        return under;
    }

    public boolean checkWin()
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (!under[x][y].equals(BOMB) && !under[x][y].equals(board[x][y]))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void generateBombs(int amount, Cursor cursor)
    {
        int i = 0;
        bombs = new Position[amount];
        while (i < amount)
        {   
            int[] coords = randCoords();
            Position rand = new Position (coords[0], coords[1]);
            if (cursor.x != rand.x && cursor.y != rand.y && !inBombs(rand) )
            {
                bombs[i] = rand;
                i++;
            }
        }
        initUnder();
    }

    public void initUnder()
    {
        under = new String[width][height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                under[x][y] = EMPTY;
            }
        }

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int result = checkRadius(x, y);
                if (result > 0)
                {
                    under[x][y] = result + "";
                }
                else
                {
                    under[x][y] = EMPTY;
                }
            }
        }

        for (Position bomb : bombs)
        {
            under[bomb.x][bomb.y] = BOMB;
        }
    }

    public void emptyClick(Position pos)
    {
        ArrayList<Position> empties = emptyClickUncover(pos);

        for (Position empty : empties)
        {
            setSquare(empty.x, empty.y, under[empty.x][empty.y]);
        }
    }

    public ArrayList<Position> emptyClickUncover(Position pos)
    {
        ArrayList<Position> display = new ArrayList<>();
        ArrayList<Position> empties = new ArrayList<>();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (under[x][y].equals("."))
                {
                    empties.add(new Position(x, y));
                }
            }
        }

        display.add(pos);
        int j = 0;

        while (display.size() != j)
        {
            j = display.size();
            for (int k = 0; k < display.size(); k++)
            {
                for (int i = 0; i < empties.size(); i++)
                {
                    if (isAdjacentTo(display.get(k), empties.get(i)))
                    {
                        display.add(empties.get(i));
                        empties.remove(i--);
                    }
                }
            }
        }

        ArrayList<Position> temp = new ArrayList<>();
        for (int k = 0; k < display.size(); k++)
        {
            temp = concat(temp, getNeighbors(display.get(k)));
        }

        return removeDups(concat(display, temp));
    }

    public ArrayList<Position> concat(ArrayList<Position> L1, ArrayList<Position> L2)
    {
        for (Position p : L2)
        {
            L1.add(p);
        }
        return L1;
    }

    public ArrayList<Position> removeDups(ArrayList<Position> arrList)
    {
        ArrayList<Position> dupeless = new ArrayList();
        dupeless.add(arrList.get(0));
        for (Position pos : arrList)
        {
            if (!inArrList(pos, dupeless)) // not in dupeless then add to dupeless
            {
                dupeless.add(pos);
            }
        }
        return dupeless;
    }

    public ArrayList<Position> getNeighbors(Position pos)
    {
        ArrayList<Position> neighbors = new ArrayList<Position>(); 
        for (int i = pos.x - 1; i < pos.x + 2; i++)
        {
            for (int j = pos.y - 1; j < pos.y + 2; j++)
            {
                if ((i >= 0 && i < width) && (j >= 0 && j < height) && (pos.x != i && pos.y != j))
                {
                    if (isNumeric(under[i][j]))
                    {
                        neighbors.add(new Position(i, j));
                    }
                }
            }
        }
        return neighbors;
    }

    public boolean isAdjacentTo(Position pos1, Position pos2)
    {
        return Math.abs(pos1.x - pos2.x) <= 1 && Math.abs(pos1.y - pos2.y) <= 1;
    }

    public boolean inArrList(Position pos, ArrayList<Position> arrList)
    {
        for(Position p : arrList)
        {
            if(p.equals(arrList))
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Position> findEmpties(Position pos)
    {
        return new ArrayList<Position>();
    }

    public int checkRadius(int x, int y)
    {
        int bombCount = 0;
        for (int i = x - 1; i < x + 2; i++)
        {
            for (int j = y - 1; j < y + 2; j++)
            {
                if (inBombs(new Position(i, j)))
                {
                    bombCount++;
                }
            }
        }
        return bombCount;
    }

    private boolean inBombs(Position target)
    {
        for (Position pos : bombs)
        {
            if (pos != null && pos.equals(target))
            {
                return true;
            }
        }
        return false;
    }

    private int[] randCoords()
    {
        int x = (int)(Math.random() * width);
        int y = (int)(Math.random() * height);
        return new int[] {x, y};
    }

    public void init()
    {
        board = new String[width][height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                board[x][y] = UNCHECKED;
            }
        }
    }

    public void print(Cursor cursor)
    {
        print(board, cursor);
    }

    public void print(String[][] arr, Cursor cursor)
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (x == 0)
                {
                    if (height - y < 10)
                    {
                        System.out.print(" " + (height - y) + " - ");
                    }
                    else
                    {
                        System.out.print(height - y + " - ");
                    }
                }
                if (cursor.equals(x, y))
                {
                    cursor.getColor().print();
                    System.out.print(" ");
                }
                else if (isNumeric(arr[x][y]))
                {
                    new Color(arr[x][y]).print();
                    System.out.print(" ");
                }
                else if (arr[x][y].equals(FLAG))
                {
                    new Color(arr[x][y], "\u001B[31m").print();
                    System.out.print(" ");
                }
                else
                {
                    System.out.print(arr[x][y] + " ");
                }
            }
            System.out.println();
        }
        System.out.print("     ");
        for (int i = 0; i < width; i++)
        {
            System.out.print("| ");
        }
        System.out.print("\n     ");
        for (int i = 0; i < width; i++)
        {
            System.out.print((char) (i + 97) + " ");
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

    public Position[] checkEmpty(Position[] checked, Position square)
    {
        return new Position[] {};
    }

    public int[] inputToCoords(String input)
    {
        char xCoord = input.charAt(0);
        int x = (int)(xCoord) - 97;
        int y = 20 - Integer.parseInt(input.substring(1));
        return new int[]{x, y};
    }

}
