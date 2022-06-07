public class Cursor extends Position
{
    private Board board;
    private Color color;
    
    public Cursor(int x, int y, Board board, Color color)
    {
        super(x, y);
        if (this.board == null)
        {
            this.board = board;
        }
        this.color = color;
    }
    
    public Color getColor()
    {
        return this.color;
    }
    
    public void move(int x, int y)
    {
        int width = board.getWidth();
        int height = board.getHeight();        
        if (this.x + x < 0) {setX(width - 1);}
        else if (this.x + x >= width) {setX(0);}
        else {setX(this.x + x);}
        if (this.y + y < 0) {setY(height - 1);}
        else if (this.y + y >= height) {setY(0);}
        else {setY(this.y + y);}
    }
    
    public String toString()
    {
        return this.x + ", " + this.y;
    }
}