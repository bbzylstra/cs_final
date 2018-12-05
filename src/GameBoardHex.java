import java.util.ArrayList;
import java.util.List;

public class GameBoardHex extends GameBoardSquare {

    GameBoardHex(int x, int y){
        super(x , y);
    }

    @Override
    public List<Point> get_accessible_tiles(Point s){
        List<Point> al = new ArrayList<>();

        return al;
    }
}
