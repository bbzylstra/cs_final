public class AStar {

    private GameBoard game;

    AStar(GameBoard g){
        game = g;
    }

    public void solve(){
        if (!(game.hasStart() && game.hasEnd())){
            return;
        }

        Point start = game.getStart();
        Point end = game.getEnd();

    }
}
