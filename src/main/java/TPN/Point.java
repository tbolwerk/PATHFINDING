package TPN;

import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

public class Point {
    private int x,y;
    private int startX, startY;
    private int width,height;
    private int f,g,h; //pathfinding vars
    private ArrayList<Point> neighbors;
    private Point previous;
    private boolean wall;

    public Point(int x,int y, int width, int height,boolean wall){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.wall=wall;
        this.startX=x*width;
        this.startY=y*height;
        this.f=0;
        this.g=0;
        this.h=0;
        this.neighbors = new ArrayList<Point>();
    }
    public void show(PApplet app,Color color){
        app.fill(color.getRGB());
        app.rect(startX,startY,width,height);
    }
    public void addNeighbors(Map map){

                if(x>0)
                neighbors.add(map.getGrid()[this.x-1][this.y]);//left
                if(x < Pathfinding.rows-1)
                neighbors.add(map.getGrid()[this.x+1][this.y]);//right
                if(y>0)
                neighbors.add(map.getGrid()[this.x][this.y-1]);//up
                if(y < Pathfinding.cols-1)
                neighbors.add(map.getGrid()[this.x][this.y+1]);//down

        if(Pathfinding.diagonal){
            if(x>0 && y>0){
                neighbors.add(map.getGrid()[this.x-1][this.y-1]);//left up
            }
            if(x<Pathfinding.rows-1 && y<Pathfinding.cols-1){
                neighbors.add(map.getGrid()[this.x+1][this.y+1]);//right down
            }
            if(x>0 && y<Pathfinding.cols-1){
                neighbors.add(map.getGrid()[this.x-1][this.y+1]);//left down
            }
            if(x<Pathfinding.rows-1 && y>0){
                neighbors.add(map.getGrid()[this.x+1][this.y-1]);//right up
            }
        }

    }

    public ArrayList<Point> getNeighbors() {
        return neighbors;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public Point getPrevious() {
        return previous;
    }

    public void setPrevious(Point previous) {
        this.previous = previous;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isWall() {
        return wall;
    }
}


