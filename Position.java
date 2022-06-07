public class Position
{
    public int x;
    public int y;
    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public void setPos(int x, int y)
    {
        setX(x);
        setY(y);
    }
    
    public void setPos(int[] coords)
    {
        setX(coords[0]);
        setY(coords[1]);
    }
    
    public boolean equals(Position other)
    {
        return this.x == other.x && this.y == other.y;
    }
    
    public boolean equals(int x, int y)
    {
        return this.x == x && this.y == y;
    }
    
    public String toString()
    {
        return "(" + this.x + "," + this.y + ")";
    }
}