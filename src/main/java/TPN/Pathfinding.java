package TPN;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Pathfinding extends PApplet implements Runnable {
    public static int rows = 50;
    public static int cols = 50;
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
        map.setup();
        map.draw();
    }

    public void draw() {
        map.draw();
        run();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mouseOverRect(map.getGrid()[i][j].getStartX(), map.getGrid()[i][j].getWidth(), map.getGrid()[i][j].getStartY(), map.getGrid()[i][j].getHeight())) {
                    System.out.println("G=" + map.getGrid()[i][j].getG() + "H=" + map.getGrid()[i][j].getH() + "F=" + map.getGrid()[i][j].getF());
                }
            }
        }

    }

    private boolean mouseOverRect(int x,int width,int y,int height){
        return mouseX>=x && mouseX<x+width && mouseY>=y && mouseY<y+height;
    }

    @Override
    public void mouseClicked() {

        if(startAndEndPoint.size()>1){
            startAndEndPoint.clear();
            openSet.clear();
            closedSet.clear();
            map.resetGrid();
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(mouseOverRect(map.getGrid()[i][j].getStartX(),map.getGrid()[i][j].getWidth(),map.getGrid()[i][j].getStartY(),map.getGrid()[i][j].getHeight())){
                    System.out.println("G=" + map.getGrid()[i][j].getG() + "H=" + map.getGrid()[i][j].getH() + "F=" + map.getGrid()[i][j].getF());
                    if(startAndEndPoint.size()==0){
                        startAndEndPoint.add(map.getGrid()[i][j]);
                        map.getGrid()[i][j].setClicked(true);
                    }else if(startAndEndPoint.size()==1){
                        startAndEndPoint.add(map.getGrid()[i][j]);
                        map.getGrid()[i][j].setClicked(true);
                    }
                }
            }
        }
    }

    private void pathFinding(Point start, Point goal) {
        start.setG(0);
        start.setH(heuristics(start,goal));
        start.setF(start.getG() + start.getH());
        Point current;

        Point point = start;

        openSet.add(start);
        Iterator<Point> iterator = openSet.iterator();
        while (iterator.hasNext()) {
            if (openSet.size() == 1) {
                current = openSet.get(0);
            } else {
                current = iterator.next();
                for (Point open : openSet) {
                    if (open.getF() < current.getF()) {
                        current = open;
                    }
                    }
                }
            if (current.equals(goal)) {
                System.out.println("done");
                drawPathfindingLine(current);
                break;
            }
            drawPathfindingLine(current);
            openSet.remove(current);
            closedSet.add(current);


            for (Point neighbor : current.getNeighbors()) {
                if (!closedSet.contains(neighbor) && !neighbor.isWall()) {
                    int tempG = current.getG() + heuristics(neighbor, current);//needs to be checked again to make sure its the lowest G
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    } else if (tempG >= neighbor.getG()) {
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
            score++;
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
        if(startAndEndPoint.size()==2){
            pathFinding(startAndEndPoint.get(0),startAndEndPoint.get(1));
        }
    }


}
