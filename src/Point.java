
public class Point implements Comparable<Point>{
    public Integer x;
    public Integer y;
    Point(int n_x, int n_y){
        x = n_x;
        y=n_y;
    }

    public Integer getX(){
        return x;
    }

    public Integer getY(){
        return y;
    }

    public void setX(Integer X){
        x = X;
    }

    public void setY(Integer Y){
        y = Y;
    }

    public int compareTo(Point o){
        if(o.x.equals(x) && o.y.equals(y)){
            return 1;
        }
        else{
            return 0;
        }
    }
    public boolean equals(Object o){
        return (this.compareTo((Point) o)==1);
    }
    @Override
    public int hashCode(){
        return x*200 + y;
    }
}