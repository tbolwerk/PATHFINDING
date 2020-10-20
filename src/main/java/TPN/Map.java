package TPN;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Map {
    private Point[][] grid;
    private Pathfinding pathfinding;
    private ArrayList<Point> reconstructPath;
    public int walls = 1;
    public Map(Pathfinding pathfinding){
        this.pathfinding=pathfinding;
        this.grid= new Point[Pathfinding.rows][Pathfinding.cols];
    }

    public void setup(){
        for (int i = 0; i < Pathfinding.rows; i++) {
            for (int j = 0; j <Pathfinding.cols ; j++) {
                grid[i][j]= new Point(i,j,calculateWidthOfPoints(),calculateHeihtOfPoints(),setWall(walls));
            }
        }
        for (int i = 0; i < Pathfinding.rows; i++) {
            for (int j = 0; j < Pathfinding.cols; j++) {
                grid[i][j].addNeighbors(this);
            }
        }

    }
    public boolean setWall(int spawnRate){
        Random random = new Random();
        return random.nextInt(spawnRate) > 6;
    }

    public void draw(){
        for (int i = 0; i < Pathfinding.rows; i++) {
            for (int j = 0; j < Pathfinding.cols; j++) {
                if (grid[i][j] != null && grid[i][j].isWall()) {
                    grid[i][j].show(pathfinding, Color.BLACK);
                } else if (grid[i][j] != null && grid[i][j].isClicked()) {
                    grid[i][j].show(pathfinding, Color.MAGENTA);
                }else if(reconstructPath != null && reconstructPath.size()>0 && reconstructPath.contains(grid[i][j])){
                    grid[i][j].show(pathfinding,Color.red);
                }else if(pathfinding.getOpenSet() !=null && pathfinding.getOpenSet().size()>0 && pathfinding.getOpenSet().contains(grid[i][j])) {
                    grid[i][j].show(pathfinding, Color.green);
                }else if(pathfinding.getClosedSet() !=null && pathfinding.getClosedSet().size()>0 && pathfinding.getClosedSet().contains(grid[i][j])){
                    grid[i][j].show(pathfinding, Color.blue);
                }else{
                    grid[i][j].show(pathfinding,Color.white);
                }
            }
        }
    }

    public Point[][] getGrid() {
        return grid;
    }

    public void resetGrid() {
        for (int i = 0; i < Pathfinding.rows; i++) {
            for (int j = 0; j < Pathfinding.cols; j++) {
                grid[i][j].setF(0);
                grid[i][j].setH(0);
                grid[i][j].setG(0);
                grid[i][j].setClicked(false);
            }
        }
    }

    private int calculateWidthOfPoints(){
        return Pathfinding.width/Pathfinding.rows;
    }
    private int calculateHeihtOfPoints(){
        return Pathfinding.height/Pathfinding.cols;
    }

    public ArrayList<Point> getReconstructPath() {
        return reconstructPath;
    }

    public void setReconstructPath(ArrayList<Point> reconstructPath) {
        this.reconstructPath = reconstructPath;
    }
}
