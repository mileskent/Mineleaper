public class Color
{
    private static final String RESET = "\u001B[0m";
    private String[] numbers = {"", "\u001B[36m", "\u001B[33m", "\u001B[32m", "\u001B[36m", "\u001B[36m", "\u001B[33m", "\u001B[32m", "\u001B[36m"};
    private String text;
    private String code;
    
   
    public Color(String text, String code)
    {
        this.text = text;
        this.code = code;
    }
    
    public Color(String number)
    {
        int num = Integer.parseInt(number);
        this.text = num + "";
        this.code = numbers[num];
    }
    
    public Color(String text, int i)
    {
        this.text = text;
        this.code = numbers[i];
    }
    
    public void setText(String text)
    {
        this.text = text;
    }
   
    public String getText()
    {
        return text;
    }
    
    public void print() 
    {
        System.out.print(code + text + RESET);
    }
}