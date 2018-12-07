import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Pathfinding {
    int _height;
    int _width;
    int _level;
    Coordinates playerPos;
    Coordinates enenemyPos;
    Coordinates originPos;
    ArrayList<Coordinates> path;
    Coordinates[][] map;

    public void loadLevel(int level) {
        path = new ArrayList<Coordinates>();
        String path = "Level" + level +".txt";
        File filePath = new File(path);
        try {
            URL absPath = filePath.toURI().toURL();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(absPath.openStream()));
            String data = in.readLine();
            String[] Int = data.split(" ");
            try {
                _level = Integer.parseInt(Int[0]);
                _height = Integer.parseInt(Int[1]);
                _width = Integer.parseInt(Int[2]);
            }
            catch (NumberFormatException e){
                System.out.println("error in converting");
                _level = level;
                _height = Integer.parseInt(Int[1]);
                _width = Integer.parseInt(Int[2]);

            }
            map = new Coordinates[_height][_width];
            for(int j = 0; j < _height; j++){
                data = in.readLine();
                for(int i = 0; i < _width; i++){
                    char symbol = data.charAt(i);
                    switch (symbol){
                        case 'p':
                            map[j][i] = new Coordinates(symbol, i, j, true);
                            playerPos = map[j][i];
                            break;
                        case '1':
                            map[j][i] = new Coordinates(symbol, i, j, true);
                            enenemyPos = map[j][i];
                            originPos = enenemyPos;
                            break;
                        case '#':
                            map[j][i] = new Coordinates(symbol, i, j, false);
                            break;
                        case '*':
                            map[j][i] = new Coordinates(symbol, i, j, false);
                            break;
                            default:
                                map[j][i] = new Coordinates(symbol, i, j, true);
                            break;
                    }
                }
            }

            in.close();
        } catch (IOException e) {
            System.out.println("Error loading level ");
        }
    }

    public Coordinates findShortestDistance(ArrayList<Coordinates> openList, ArrayList<Coordinates> closedList){
        Coordinates shortest = null;
        if(openList.size() > 0) {
            shortest = openList.get(0);
            for (int i = 0; i < openList.size(); i++) {
                if(!closedList.contains(openList.get(i))) {
                    if (openList.get(i).point < shortest.point) {
                        shortest = openList.get(i);
                    }
                }
            }
        }
        return shortest;
    }

    public void findPath(){
        ArrayList<ArrayList<Coordinates>> shortestToOriginalPos = new ArrayList<ArrayList<Coordinates>>();
        ArrayList<Coordinates> openList = new ArrayList<Coordinates>();
        ArrayList<Coordinates> closedList = new ArrayList<Coordinates>();
        openList.add(originPos);
        while (!openList.isEmpty()){
            enenemyPos = findShortestDistance(openList, closedList);
            closedList.add(enenemyPos);
            openList.remove(enenemyPos);

            if(closedList.contains(playerPos)){
                System.out.println("enemy is at player pos");
                break;
            }

            //array for all adjacent tile
            ArrayList<Coordinates> adjacentTiles = new ArrayList<Coordinates>();

            //up tile
            if(enenemyPos._y > 0) {
                Coordinates upTile = map[enenemyPos._y - 1][enenemyPos._x];
                if(upTile.isWalkable()) {
                    adjacentTiles.add(upTile);
                }
            }

            //right tile
            if(enenemyPos._x < _width - 1) {
                Coordinates rightTile = map[enenemyPos._y][enenemyPos._x + 1];
                if(rightTile.isWalkable()) {
                    adjacentTiles.add(rightTile);
                }
            }

            //down tile
            if(enenemyPos._y < _height - 1) {
                Coordinates downTile = map[enenemyPos._y + 1][enenemyPos._x];
                if(downTile.isWalkable()) {
                    adjacentTiles.add(downTile);
                }
            }

            //left tile
            if(enenemyPos._x > 0) {
                Coordinates leftTile = map[enenemyPos._y][enenemyPos._x - 1];
                if(leftTile.isWalkable()) {
                    adjacentTiles.add(leftTile);
                }
            }

            for(int i = 0; i < adjacentTiles.size(); i++){
                Coordinates adjacent = adjacentTiles.get(i);
                if(closedList.contains(adjacent)) {
                    continue;
                }
                if(!openList.contains(adjacent)) {
                    int costToOriginPos = Math.abs(adjacent._x - originPos._x) + Math.abs(adjacent._y - originPos._y);
                    int costToDestination = Math.abs(adjacent._x - playerPos._x) + Math.abs(adjacent._y - playerPos._y);
                    adjacent.setCostToOriginPos(costToOriginPos);
                    adjacent.setCostToDestination(costToDestination);
                    adjacent.setParents(enenemyPos);
                    adjacent.calculatePoint();
                    openList.add(adjacent);
                }
                else {
                    int costToOriginPos = Math.abs(adjacent._x - originPos._x) + Math.abs(adjacent._y - originPos._y);
                    int costToDestination = Math.abs(adjacent._x - playerPos._x) + Math.abs(adjacent._y - playerPos._y);
                    int temp_point = costToOriginPos + costToDestination;
                    if(temp_point < adjacent.point){
                        adjacent.setCostToOriginPos(costToOriginPos);
                        adjacent.setCostToDestination(costToDestination);
                        adjacent.setParents(enenemyPos);
                        adjacent.calculatePoint();
                    }
                }
            }
        }
    }

    public void makePath(){
        while (enenemyPos.parents != null){
            if(enenemyPos != null) {
                path.add(enenemyPos);
                if (enenemyPos._x < enenemyPos.parents._x) {
                    map[enenemyPos._y][enenemyPos._x].symbol = '>';
                } else if (enenemyPos._y < enenemyPos.parents._y) {
                    map[enenemyPos._y][enenemyPos._x].symbol = 'V';
                } else if (enenemyPos._x > enenemyPos.parents._x) {
                    map[enenemyPos._y][enenemyPos._x].symbol = '<';
                } else if (enenemyPos._y > enenemyPos.parents._y) {
                    map[enenemyPos._y][enenemyPos._x].symbol = '^';
                }
            }
            enenemyPos = enenemyPos.parents;
        }
    }

    public void printMap(){
        System.out.println();
        for(int i = 0; i < _height; i++){
            for(int j = 0; j < _width; j++){
                System.out.print(map[i][j].symbol);
            }
            System.out.println();
        }
    }

    public void printPath(){
        for (Coordinates p: path) {
            System.out.println(p._y + " " + p._x);
        }
    }

    public static void main(String[] args){
        Pathfinding m = new Pathfinding();
        m.loadLevel(1);
        m.findPath();
        m.makePath();
        m.printMap();
        m.printPath();
    }
}
