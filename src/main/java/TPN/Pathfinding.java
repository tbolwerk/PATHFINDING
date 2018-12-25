package TPN;

import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Pathfinding extends PApplet implements Runnable {
    public static int rows = 50;
    public static int cols = 50;
    public static int width, height;
    public static boolean diagonal = true;
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
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(mouseOverRect(map.getGrid()[i][j].getStartX(),map.getGrid()[i][j].getWidth(),map.getGrid()[i][j].getStartY(),map.getGrid()[i][j].getHeight())){
                    if(startAndEndPoint.size()==0){
                        startAndEndPoint.add(map.getGrid()[i][j]);
                    }else if(startAndEndPoint.size()==1){
                        startAndEndPoint.add(map.getGrid()[i][j]);
                    }
                }
            }
        }
    }

    private void pathFinding(Point start, Point goal) {
        start.setG(heuristics(start,start.getNeighbors().get(0)));
        start.setH(heuristics(start,goal));
        start.setF(start.getG()+start.getH());
        Point current = start;
        Point tempCurrent = start;
        openSet.add(start);
        Iterator<Point> iterator = openSet.iterator();
        while (iterator.hasNext()) {
                Point point= iterator.next();

            tempCurrent = current;
                if (point.getF() < tempCurrent.getF()) {
                    tempCurrent = point;
                }
                if (point.getF() == tempCurrent.getF()) {
                    if (point.getG() > tempCurrent.getG()) {
                        tempCurrent = point;
                    }
                }


            current = tempCurrent;



            if (current.equals(goal)) {
                System.out.println("done");
                drawPathfindingLine(current);


            }

            openSet.remove(current);
            closedSet.add(current);


            for (Point neighbor : current.getNeighbors()) {
                if (!closedSet.contains(neighbor)) {
                    int tempG = current.getG() +heuristics(neighbor, current);//needs to be checked again to make sure its the lowest G
                    if (!openSet.contains(neighbor) && !neighbor.isWall()) {
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
        return (int)abs(a.getX() - b.getX()) + abs(a.getY() - b.getY());
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
        System.out.println(score);
    }


}
