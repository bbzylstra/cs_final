import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;



public class GameBoardSquare implements Iterable<Integer> {

    private Integer [][] Board;
    private int x_size;
    private int y_size;
    private Point start;
    private Point end;
    GameBoardSquare(int x, int y){
        x_size = x;
        y_size = y;
        Board = new Integer[y][x];

        for(int y_lim=0;y_lim<y;y_lim++) {
            for (int x_lim = 0; x_lim < x; x_lim++) {
                Board[y_lim][x_lim] = 0;
            }
        }

    }

    public Point getStart(){
        return start;
    }

    public Point getEnd(){
        return end;
    }

    public boolean hasStart(){
        return (start!=null);
    }

    public boolean hasEnd(){
        return (end!=null);
    }

    public void set_tile(int x, int y, Integer type){
        /*
        Set the x,y tile to type. The types are the following:
            0: Open tile
            1: Closed tile
            2: Start Point
            3: End Point
         */
        Board[y][x] = type;
        Point n = new Point(x,y);
        if(hasStart()){
            if(n.compareTo(start)==1){
                if(type==0 || type==1 || type==3)
                    start=null;
            }
        }

        if(hasEnd()){
            if(n.compareTo(end)==1){
                if(type==0 || type==1 || type==2)
                    end=null;
            }
        }

        if(type == 2){
            if(hasStart())
                Board[start.getY()][start.getX()]=0;
            start = new Point(x,y);
        }

        if(type == 3){
            if(hasEnd())
                Board[end.getY()][end.getX()]=0;
            end = new Point(x,y);
        }
    }

    public int getX_size(){
        return x_size;
    }

    public int getY_size(){
        return y_size;
    }

    public Integer get_tile(int x, int y){
        /*
        Returns the type of the tile selected or -1 if the tile doesnt exist
         */

        if(x >= x_size || y >= y_size || x<0 || y<0){
            return -1;
        }
        else{
            return Board[y][x];
        }
    }

    public List<Point> get_accessible_tiles(Point s){
        List<Point> al = new ArrayList<>();

        Integer tile = get_tile(s.getX(), s.getY()+1);
        if(tile!=-1 && tile!=1)
            al.add(new Point(s.getX(),s.getY()+1));

        tile = get_tile(s.getX(), s.getY()-1);
        if(tile!=-1 && tile!=1)
            al.add(new Point(s.getX(),s.getY()-1));

        tile = get_tile(s.getX()+1, s.getY());
        if(tile!=-1 && tile!=1)
            al.add(new Point(s.getX()+1,s.getY()));

        tile = get_tile(s.getX()-1, s.getY());
        if(tile!=-1 && tile!=1)
            al.add(new Point(s.getX()-1,s.getY()));
        return al;

    }

    public int get_distance(Point a, Point b){
        /*
        Simple euclidean distance between the two points
         */
        return Math.abs(b.getX() - a.getX()) + Math.abs(b.getY() - a.getY());
    }

    public Iterator<Integer> iterator(){
        return new GameIterator();
    }

    private class GameIterator implements Iterator<Integer>{
        private int cursorx;
        private int cursory;

        GameIterator(){
            cursorx = 0;
            cursory = 0;
        }

        public boolean hasNext(){
            return (cursorx < x_size && cursory < y_size);
        }

        public Integer next(){
            if(hasNext()){
                int current = Board[cursory][cursorx];
                if(cursorx==x_size-1){
                    cursorx = 0;
                    cursory++;
                }
                else{
                    cursorx++;
                }
                return current;
            }
            throw new NoSuchElementException();
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }
    }


}
