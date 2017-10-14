/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg170.mp1.arbuis.butas.erasmo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author TRISHA NICOLE
 */
public class CMSC170MachineProblem {

    private static final String TEXT_FILE = "C:\\Users\\User\\Documents\\CMSC 170\\Machine Problems\\Machine Problem 1\\Mazes\\bigMaze.lay.txt";
    // private static final String TEXT_FILE = "D:\\College\\4thyear-1stsem\\CMSC 170\\Mazes\\smallMaze.lay.txt";

    public static void main(String[] args) {
        Maze maze = new Maze(TEXT_FILE);
       
        maze.single_goal(false);
    }
}

class Maze {
    Tile[][] maze;
    int start_row = 0, start_col = 0, end_row = 0, end_col = 0;
    Tile start = null;
    ArrayList<Tile> goal = new ArrayList();
    
    Maze(String path) {
        BufferedReader br = null;
        FileReader fr = null;
        String line;
        ArrayList<Tile[]> temp_maze = new ArrayList();
        
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
                
            for(int i = 0;(line = br.readLine()) != null; i++) {
                Tile[] arr = new Tile[line.length()];
               
                for(int j = 0; j < line.length(); j++){
                    Tile newTile = new Tile(i, j, line.charAt(j));
                    arr[j] = newTile;
       
                    if(line.charAt(j) == 'P') {
                        start = newTile;
                        this.start_row = i;
                        this.start_col = j;
                    } else if(line.charAt(j) == '.') {
                        goal.add(newTile);
                        this.end_row = i;
                        this.end_col = j;
                    }
                } 
                temp_maze.add(arr);
            }
            this.maze = temp_maze.toArray(new Tile[][] {});
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void doSomeProcess(Tile child, OpenListEntry current, ArrayList<OpenListEntry> open_list, ArrayList<ParentListEntry> parent_list, boolean heuristics) {
        OpenListEntry dup = this.searchOpenList(open_list, child);
        int g = current.g + 1;
        int h;
        if(heuristics) {
            h = child.get_manhattan_dist(goal.get(0));
        } else {
            h = child.get_straight_dist(goal.get(0));
        }
        
        int fn = g+h;
        
        if(dup == null){
            open_list = this.addOpenListEntry(open_list,new OpenListEntry(child, g, h, fn));
            parent_list.add(new ParentListEntry(child, current.square));
        }
        else{
            if(g < dup.g){
                open_list.remove(dup);
                open_list = this.addOpenListEntry(open_list, new OpenListEntry(child, g, h, fn));
            }
        }
    }
   
    public void single_goal(boolean heuristics) {
        ArrayList<OpenListEntry> open_list = new ArrayList();
        ArrayList<Tile> closed_list = new ArrayList();
        ArrayList<ParentListEntry> parent_list = new ArrayList();
        OpenListEntry current = null;
        
        parent_list.add(new ParentListEntry(start, null));
        if(heuristics) {
            open_list.add(new OpenListEntry(start, 0, start.get_manhattan_dist(goal.get(0)), start.get_manhattan_dist(goal.get(0))));
        } else {
            open_list.add(new OpenListEntry(start, 0, start.get_straight_dist(goal.get(0)), start.get_straight_dist(goal.get(0))));
        }
        current = open_list.remove(0);
        
        while(!current.square.equals(goal.get(0))) {
            // System.out.println(current.square.x + "," + current.square.y + " " + current.g + " " + current.h + " " + current.fn);
            closed_list.add(current.square);
            
            Tile currSq = current.square;
            
            // upper left
            // if(currSq.x-1 >= 0 && currSq.y-1 >= 0 && !closed_list.contains(maze[currSq.x-1][currSq.y-1]) && maze[currSq.x-1][currSq.y-1].type != '%') {
            //     doSomeProcess(maze[currSq.x-1][currSq.y-1], current, open_list, parent_list, heuristics);
            // }
            
            //upper mid
            if(currSq.x-1 >= 0 && !closed_list.contains(maze[currSq.x-1][currSq.y]) && maze[currSq.x-1][currSq.y].type != '%') {
                doSomeProcess(maze[currSq.x-1][currSq.y], current, open_list, parent_list, heuristics);
            }
            
            // upper right
            // if(currSq.x-1 >= 0 && currSq.y+1 < maze[currSq.x-1].length && !closed_list.contains(maze[currSq.x-1][currSq.y+1]) && maze[currSq.x-1][currSq.y+1].type != '%') {
            //     doSomeProcess(maze[currSq.x-1][currSq.y+1], current, open_list, parent_list, heuristics);
            // }
            
            //mid left
            if(currSq.y-1 >= 0 && !closed_list.contains(maze[currSq.x][currSq.y-1]) && maze[currSq.x][currSq.y-1].type != '%') {
                doSomeProcess(maze[currSq.x][currSq.y-1], current, open_list, parent_list, heuristics);
            }
            
            //mid right
            if(currSq.y+1 < maze[currSq.x].length && !closed_list.contains(maze[currSq.x][currSq.y+1]) && maze[currSq.x][currSq.y+1].type != '%') {
                doSomeProcess(maze[currSq.x][currSq.y+1], current, open_list, parent_list, heuristics);
            }
            
            // lower left
            // if(currSq.x+1 < maze.length && currSq.y-1 >= 0 && !closed_list.contains(maze[currSq.x+1][currSq.y-1]) && maze[currSq.x+1][currSq.y-1].type != '%') {
            //     doSomeProcess(maze[currSq.x+1][currSq.y-1], current, open_list, parent_list, heuristics);
            // }
            
            // lower mid
            if(currSq.x+1 < maze.length && !closed_list.contains(maze[currSq.x+1][currSq.y]) && maze[currSq.x+1][currSq.y].type != '%') {
                doSomeProcess(maze[currSq.x+1][currSq.y], current, open_list, parent_list, heuristics);
            }
            
            // lower right
            // if(currSq.x+1 < maze.length && currSq.y+1 < maze[currSq.x+1].length && !closed_list.contains(maze[currSq.x+1][currSq.y+1]) && maze[currSq.x+1][currSq.y+1].type != '%') {
            //     doSomeProcess(maze[currSq.x+1][currSq.y+1], current, open_list, parent_list, heuristics);
            // }

            current = open_list.remove(0);
        }
        closed_list.add(current.square);

        tracePath(closed_list.get(closed_list.size()-1), parent_list);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("solution.txt", "UTF-8");
            for(int j = 0; j < this.maze.length; j++) {
                for(int k = 0; k < this.maze[j].length; k++) {
                    writer.print(this.maze[j][k].type);
                }
                writer.println();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    private void tracePath(Tile current, ArrayList<ParentListEntry> parent_list) {
        while(current!=null){
            for(int i = 0; i < parent_list.size(); i++){
                if(current.equals(parent_list.get(i).square)) {
                    // System.out.print(" -> "+current.x+","+current.y);

                    for(int j = 0; j < this.maze.length; j++) {
                        for(int k = 0; k < this.maze[j].length; k++) {
                            if(current.equals(this.maze[j][k])) {
                                this.maze[j][k].type = '.';
                            }
                        }
                    }

                    current = parent_list.get(i).parent;
                    break;
                }
            }
        }
    }
    
    private static OpenListEntry searchOpenList(ArrayList<OpenListEntry> arr, Tile tile) {
        for(int i = 0; i < arr.size(); i++) {
            if(arr.get(i).square.equals(tile))
                return arr.get(i);
        }
        
        return null;
    }
    
    private static ArrayList<OpenListEntry> addOpenListEntry(ArrayList<OpenListEntry> open_list, OpenListEntry newEntry) {
        if(open_list.isEmpty()){
            open_list.add(newEntry);
            return open_list;
        }
        for(int i = 0; i < open_list.size(); i++) {
            if(open_list.get(i).fn > newEntry.fn){
                open_list.add(i, newEntry);
                return open_list;
            }
            else if(open_list.get(i).fn == newEntry.fn){
                if(open_list.get(i).square.x > newEntry.square.x) {
                    open_list.add(i, newEntry);
                    return open_list;
                }
                else if(open_list.get(i).square.x == newEntry.square.x) {
                    if(open_list.get(i).square.y > newEntry.square.y){
                        open_list.add(i, newEntry);
                        return open_list;
                    }
                }
            }
        }
        open_list.add(newEntry);
        return open_list;
    }
}

class Tile {
    int x;
    int y;
    char type;
    
    Tile(int x, int y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
    } 
    
    int get_manhattan_dist(Tile goal) {
         return Math.abs(this.x - goal.x) + Math.abs(this.y - goal.y);
    }
    
    int get_straight_dist(Tile goal) {
        return Math.max(Math.abs(this.x - goal.x), Math.abs(this.y - goal.y));
    }

    public String toString() { 
        return "(" + this.x + "," + this.y + ")";
    }
}

class OpenListEntry {
    Tile square;
    int g;
    int h;
    int fn;
   
    OpenListEntry(Tile t, int g, int h, int fn) {
        this.square = t;
        this.g = g;
        this.h = h;
        this.fn = fn;
    }

    public String toString() { 
        return "(" + this.square + " = " + "g: " + this.g + ", h: " + this.h + ", fn: " + this.fn + ")";
    }
}

class ParentListEntry {
    Tile square;
    Tile parent;
    
    ParentListEntry(Tile t, Tile p) {
        this.square = t;
        this.parent = p;
    }
}

