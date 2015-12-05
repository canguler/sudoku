import java.util.Arrays;

/**
 * Created by can on 03.12.2015.
 */
public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid(3);
        grid.fillGrid("078000100" +
                "000200000" +
                "300600095" +
                "819060300" +
                "000000000" +
                "003050689" +
                "130009007" +
                "000005000" +
                "005000830");
        System.out.println(grid);
        System.out.println(grid.solve());
        System.out.println(grid);
    }
}