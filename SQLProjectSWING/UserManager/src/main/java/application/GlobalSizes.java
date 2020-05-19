package application;

public class GlobalSizes {

    public static final double scale = 1.00;
    public static final int menuHeight = scaleInt(36);
    public static final int buttonWidth = scaleInt(120);
    public static final int buttonHeight = scaleInt(30);
    public static final int margin = 4;

    private static int scaleInt(int dimension){
        double temp = dimension*scale;
        return (int)temp;
    }
}
