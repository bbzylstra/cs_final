import java.util.ArrayList;
import java.util.List;

public class AStar {

    private GameBoardSquare game;

    AStar(GameBoardSquare g){
        game = g;
    }

    public List<Point> solve(){
        if (!(game.hasStart() && game.hasEnd())){
            return null;
        }

        Point start = game.getStart();
        Point end = game.getEnd();

        List<Point> checked_list = new ArrayList<>();
        boolean found = false;
        Heap pointQ = new Heap(game.getX_size() * game.getY_size());
        Node startN = new Node(game.get_distance(start, end), null, start);
        Node nNode = startN;
        pointQ.insert(startN);
        while(!found && !pointQ.isEmpty()){
            Node current = pointQ.remove();
            checked_list.add(current.getPoint());
            List<Point> open_list  = game.get_accessible_tiles(current.getPoint());
            for(Point i : open_list){
                if(!checked_list.contains(i)){
                    nNode = new Node(game.get_distance(i,end) + current.getKey(), current, i);
                    pointQ.insert(nNode);
                    if(game.get_distance(i,end)==0) {
                        found = true;
                        break;
                    }
                }
            }
        }
        if(pointQ.isEmpty()){
            return null;
        }
        List<Point> solved_list = new ArrayList<>();
        while(nNode.getLast() != null){
            solved_list.add(nNode.getPoint());
            nNode = nNode.getLast();
        }
        return solved_list;





    }
}
