public class Coordinates {
    char symbol;
    int _x, _y;
    int costToOriginPos;
    int costToDestination;
    int point;
    Coordinates parents;
    boolean walkable;
    public Coordinates(char s, int x, int y, boolean walkable){
        this.symbol = s;
        _x = x;
        _y = y;
        this.parents = parents;
        this.walkable = walkable;
        costToOriginPos = Integer.MAX_VALUE;
        costToDestination = Integer.MAX_VALUE;
        parents = null;
        calculatePoint();
    }

    public boolean isWalkable() {
        return walkable;
    }

    public boolean isTheSamePos(Coordinates other){
        if(other._x == _x && other._y == _y){
            return true;
        }
        return false;
    }

    public int get_x() {
        return _x;
    }

    public int get_y() {
        return _y;
    }

    public int getCostToOriginPos(){
        return costToOriginPos;
    }

    public int getCostToDestination() {
        return costToDestination;
    }

    public void setCostToOriginPos(int cost){
        costToOriginPos = cost;
    }

    public void setCostToDestination(int costToDestination) {
        this.costToDestination = costToDestination;
    }

    public void calculatePoint(){
        point = costToDestination + costToOriginPos;
    }

    public void set_x(int _x) {
        this._x = _x;
    }

    public void set_y(int _y) {
        this._y = _y;
    }

    public Coordinates getParents(){
        return parents;
    }

    public void setParents(Coordinates parents){
        this.parents = parents;
    }

    public boolean theSameAs(Coordinates other){
        return _x == other._x && _y == other._y;
    }

    public int getPoint(){
        return point;
    }
}
