package TPN;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Pathfinding extends PApplet implements Runnable {
    public static int rows = 40;
    public static int cols = 40;
    public static int width, height;
    public static boolean diagonal = false;
    private int score = 0;
    private List<Point> openSet = new CopyOnWriteArrayList<Point>();

    private List<Point> closedSet = new CopyOnWriteArrayList<Point>();

    private ArrayList<Point> startAndEndPoint = new ArrayList<>();
    private Map map = new Map(this);


    public static void main(String[] args) {
        PApplet.main("TPN.Pathfinding");
    }

    public void settings() {
        width = 800;
        height = 800;
        size(width, height);

    }

    public void setup() {
        map.walls = 10;
        map.setup();
        map.draw();
    }

    public void draw() {
        map.draw();
        if(map.getReconstructPath() != null) {
            fill(255, 0, 0);
            textSize(60);
            text(map.getReconstructPath().size(), 0, 60);
        }
        run();
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                if (mouseOverRect(map.getGrid()[i][j].getStartX(), map.getGrid()[i][j].getWidth(), map.getGrid()[i][j].getStartY(), map.getGrid()[i][j].getHeight())) {
//                    System.out.println("G=" + map.getGrid()[i][j].getG() + "H=" + map.getGrid()[i][j].getH() + "F=" + map.getGrid()[i][j].getF());
//                }
//            }
//        }

        interaction();

    }

    private boolean mouseOverRect(int x,int width,int y,int height){
        return mouseX>=x && mouseX<x+width && mouseY>=y && mouseY<y+height;
    }

    public void interaction() {
        if(keyPressed && keyCode == 82){
            resetMapState();
            setup();
        }

        if (mousePressed && getSelectedPoint() != null) {
            //Making walls when pressing ANY keyboard key
            if (keyPressed) {
                getSelectedPoint().setWall(true);
                //Setting start & endpoint from here..
            } else if (startAndEndPoint.size() == 0) {
                startAndEndPoint.add(getSelectedPoint());
                getSelectedPoint().setClicked(true);
            } else if (startAndEndPoint.size() == 1 && !startAndEndPoint.get(0).equals(getSelectedPoint())) {
                startAndEndPoint.add(getSelectedPoint());
                getSelectedPoint().setClicked(true);
            }
        }

    }


    private void resetMapState() {

            startAndEndPoint.clear();
            openSet.clear();
            closedSet.clear();
            map.resetGrid();


    }

    private Point getSelectedPoint() {
        Point selectedPoint = null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mouseOverRect(map.getGrid()[i][j].getStartX(), map.getGrid()[i][j].getWidth(), map.getGrid()[i][j].getStartY(), map.getGrid()[i][j].getHeight())) {
                    selectedPoint = map.getGrid()[i][j];
                }
            }
        }
        return selectedPoint;
    }

    private void pathFinding(Point start, Point goal) {
        start.setG(0);
        start.setH(heuristics(start,goal));
        start.setF(start.getG() + start.getH());
        Point current;

        Point point = start;

        openSet.add(start);
        Iterator<Point> iterator = openSet.iterator();
        while (iterator.hasNext()) {//while openset has points in it
            if (openSet.size() == 1) {//if one option is open just go with that no need for equation
                current = openSet.get(0);
            } else {//more than one option in the openset, it needs to be evaluted if its F value is actualy the lowest
                current = iterator.next();
                for (Point open : openSet) {
                    if (open.getF() < current.getF()) {
                        current = open;//not the lowest F values, so change it to the new lower value
                    }
                    }
                }
            if (current.equals(goal)) {
                System.out.println("done");
                resetMapState();
                drawPathfindingLine(current);//draw the path when its done
                break;
            }
            drawPathfindingLine(current);//draw the path its currently examining
            openSet.remove(current);//it determined its the best path so its added to the closedset and needs thusfore be removed from the closedset
            closedSet.add(current);


            for (Point neighbor : current.getNeighbors()) {
                if (!closedSet.contains(neighbor) && !neighbor.isWall()) {
                    int tempG = current.getG() + heuristics(neighbor, current);//needs to be checked again to make sure its the lowest G
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);//adding all the neighbors of the current, wich are not already in the openset
                    } else if (tempG >= neighbor.getG()) {//also cheching if this path is better or nog, if the g is higher or even
                        //no its not a better path
                        continue;
                    }
                    neighbor.setG(tempG);
                    neighbor.setH(heuristics(neighbor, goal));
                    neighbor.setF(neighbor.getG() + neighbor.getH());
                    neighbor.setPrevious(current);
                }
            }

        }
    }

    public void drawPathfindingLine(Point current){
        ArrayList<Point> path = new ArrayList<Point>();
        Point temp = current;
        path.add(temp);
        while (temp.getPrevious() != null) {
            path.add(temp.getPrevious());
            map.setReconstructPath(path);
            temp = temp.getPrevious();
        }

    }


    public int heuristics(Point a, Point b){
        if (diagonal)
            return (int) dist(a.getX(), a.getY(), b.getX(), b.getY());
        else
            return abs(a.getX() - b.getX()) + abs(a.getY() - b.getY());
    }

    public List<Point> getOpenSet() {
        return openSet;
    }

    public List<Point> getClosedSet() {
        return closedSet;
    }

    public void run() {
        if (startAndEndPoint.size() == 2) {
            pathFinding(startAndEndPoint.get(0), startAndEndPoint.get(1));
        }
    }


}
