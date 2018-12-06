import java.util.ArrayList;
import java.util.List;

public class GameBoardHex extends GameBoardSquare {

    GameBoardHex(int x, int y){
        super(x , y);
    }

    @Override
    public int get_distance(Point a, Point b){
        int dx = b.x - a.x;
        int dy = b.y - a.y;

        if((int) Math.signum(dx) == (int) Math.signum(dy)){
            return(Math.max(Math.abs(dx), Math.abs(dy)));
        }
        else{
            return Math.abs(dx) + Math.abs(dy);
        }
    }

    @Override
    public List<Point> get_accessible_tiles(Point s){
        List<Point> al = new ArrayList<>();
        Integer tile = get_tile(s.getX(), s.getY()+1);
        if(tile!=-1 && tile!=1)
            al.add(new Point(s.getX(),s.getY()+1));

        tile = get_tile(s.getX()+1, s.getY());
        if(tile!=-1 && tile!=1)
            al.add(new Point(s.getX()+1,s.getY()));

        tile = get_tile(s.getX(), s.getY()-1);
        if(tile!=-1 && tile!=1)
            al.add(new Point(s.getX(),s.getY()-1));

        tile = get_tile(s.getX()-1, s.getY());
        if(tile!=-1 && tile!=1)
            al.add(new Point(s.getX()-1,s.getY()));




        if(s.getX()%2==0){

            tile = get_tile(s.getX()-1, s.getY()-1);
            if(tile!=-1 && tile!=1)
                al.add(new Point(s.getX()-1,s.getY()-1));

            tile = get_tile(s.getX()+1, s.getY()-1);
            if(tile!=-1 && tile!=1)
                al.add(new Point(s.getX()+1,s.getY()-1));


        }
        else{
            tile = get_tile(s.getX()+1, s.getY()+1);
            if(tile!=-1 && tile!=1)
                al.add(new Point(s.getX()+1,s.getY()+1));

            tile = get_tile(s.getX()-1, s.getY()+1);
            if(tile!=-1 && tile!=1)
                al.add(new Point(s.getX()-1,s.getY()+1));
        }
        return al;
    }
}
