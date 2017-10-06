/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg170.mp1.arbuis.butas.erasmo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TRISHA NICOLE
 */
public class MP1ArbuisButasErasmo {

    private static final String TEXT_FILE = "C:\\Users\\TRISHA NICOLE\\Desktop\\Mazes\\TRIAL.txt";
    private static Tile[][] maze;
    
    public static void main(String[] args) {
        Maze maze = new Maze(TEXT_FILE);
        
        maze.single_goal_manhattan();
        
    }
    
}

class Maze{
    Tile[][] maze;
    int start_row = 0, start_col = 0, end_row = 0, end_col = 0;
    Tile start = null;
    ArrayList<Tile> goal =new ArrayList();
    
    Maze(String path){
        BufferedReader br = null;
        FileReader fr = null;
        String line;
        ArrayList<Tile[]> temp_maze = new ArrayList();
//        int manhattan_dist = 0, straight_dist = 0;
        
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
                
            for(int i = 0;(line = br.readLine()) != null; i++){
                Tile[] arr = new Tile[line.length()];
               
                for(int j = 0; j < line.length(); j++){
                    Tile newTile = new Tile(i, j, line.charAt(j));
                    arr[j] = newTile;
       
                    if(line.charAt(j) == 'P'){
                        start = newTile;
                        this.start_row = i;
                        this.start_col = j;
                    } else if(line.charAt(j) == '.'){
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
   
    void single_goal_manhattan(){
        ArrayList<OpenListEntry> open_list = new ArrayList();
        ArrayList<Tile> closed_list = new ArrayList();
        ArrayList<ParentListEntry> parent_list = new ArrayList();
        OpenListEntry current = null;
        
        parent_list.add(new ParentListEntry(start, null));
        open_list.add(new OpenListEntry(start, 0, start.get_manhattan_dist(goal.get(0)), start.get_manhattan_dist(goal.get(0))));
        current = open_list.remove(0);
        
        while(!current.equals(goal.get(0))){
            System.out.println(current.square.x + "," + current.square.y + " " + current.g + " " + current.h + " " + current.fn);
            closed_list.add(current.square);
            
            Tile currSq = current.square;
            
            //upper left
            if(currSq.x-1 >= 0 && currSq.y-1 >= 0 && !closed_list.contains(maze[currSq.x-1][currSq.y-1]) && maze[currSq.x-1][currSq.y-1].type == ' '){
                Tile upperLeft = maze[currSq.x-1][currSq.y-1];
                OpenListEntry dup = this.searchOpenList(open_list, upperLeft);
                int g = current.g + 1;
                int h = upperLeft.get_manhattan_dist(goal.get(0));
                int fn = g+h;
                
                if(dup == null){
                    open_list = this.addOpenListEntry(open_list,new OpenListEntry(upperLeft, g, h, fn));
                    parent_list.add(new ParentListEntry(upperLeft, current.square));
                }
                else{
                    if(g < dup.g){
                        open_list.remove(dup);
                        open_list = this.addOpenListEntry(open_list, new OpenListEntry(upperLeft, g, h, fn));
                    }
                }
            }
            
            //upper mid
            if(currSq.x-1 >= 0 && !closed_list.contains(maze[currSq.x-1][currSq.y]) && maze[currSq.x-1][currSq.y].type == ' '){
                Tile upperMid = maze[currSq.x-1][currSq.y];
                OpenListEntry dup = this.searchOpenList(open_list, upperMid);
                int g = current.g + 1;
                int h = upperMid.get_manhattan_dist(goal.get(0));
                int fn = g+h;
                
                if(dup == null){
                    open_list = this.addOpenListEntry(open_list,new OpenListEntry(upperMid, g, h, fn));
                    parent_list.add(new ParentListEntry(upperMid, current.square));
                }
                else{
                    if(g < dup.g){
                        open_list.remove(dup);
                        open_list = this.addOpenListEntry(open_list, new OpenListEntry(upperMid, g, h, fn));
                    }
                }
            }
            
            //upper right
            if(currSq.x-1 >= 0 && currSq.y+1 < maze[currSq.x-1].length && !closed_list.contains(maze[currSq.x-1][currSq.y+1]) && maze[currSq.x-1][currSq.y+1].type == ' '){
                Tile upperRight = maze[currSq.x-1][currSq.y+1];
                OpenListEntry dup = this.searchOpenList(open_list, upperRight);
                int g = current.g + 1;
                int h = upperRight.get_manhattan_dist(goal.get(0));
                int fn = g+h;
                
                if(dup == null){
                    open_list = this.addOpenListEntry(open_list,new OpenListEntry(upperRight, g, h, fn));
                    parent_list.add(new ParentListEntry(upperRight, current.square));
                }
                else{
                    if(g < dup.g){
                        open_list.remove(dup);
                        open_list = this.addOpenListEntry(open_list, new OpenListEntry(upperRight, g, h, fn));
                    }
                }
            }
            
            //mid left
            if(currSq.y-1 >= 0 && !closed_list.contains(maze[currSq.x][currSq.y-1]) && maze[currSq.x][currSq.y-1].type == ' '){
                Tile midLeft = maze[currSq.x][currSq.y-1];
                OpenListEntry dup = this.searchOpenList(open_list, midLeft);
                int g = current.g + 1;
                int h = midLeft.get_manhattan_dist(goal.get(0));
                int fn = g+h;
                
                if(dup == null){
                    open_list = this.addOpenListEntry(open_list,new OpenListEntry(midLeft, g, h, fn));
                    parent_list.add(new ParentListEntry(midLeft, current.square));
                }
                else{
                    if(g < dup.g){
                        open_list.remove(dup);
                        open_list = this.addOpenListEntry(open_list, new OpenListEntry(midLeft, g, h, fn));
                    }
                }
            }
            
            //mid right
            if(currSq.y+1 < maze[currSq.x].length && !closed_list.contains(maze[currSq.x][currSq.y+1]) && maze[currSq.x][currSq.y+1].type == ' '){
                Tile midRight = maze[currSq.x][currSq.y+1];
                OpenListEntry dup = this.searchOpenList(open_list, midRight);
                int g = current.g + 1;
                int h = midRight.get_manhattan_dist(goal.get(0));
                int fn = g+h;
                
                if(dup == null){
                    open_list = this.addOpenListEntry(open_list,new OpenListEntry(midRight, g, h, fn));
                    parent_list.add(new ParentListEntry(midRight, current.square));
                }
                else{
                    if(g < dup.g){
                        open_list.remove(dup);
                        open_list = this.addOpenListEntry(open_list, new OpenListEntry(midRight, g, h, fn));
                    }
                }
            }
            
            //lower left
            if(currSq.x+1 < maze.length && currSq.y-1 >= 0 && !closed_list.contains(maze[currSq.x+1][currSq.y-1]) && maze[currSq.x+1][currSq.y-1].type == ' '){
                Tile lowerLeft = maze[currSq.x+1][currSq.y-1];
                OpenListEntry dup = this.searchOpenList(open_list, lowerLeft);
                int g = current.g + 1;
                int h = lowerLeft.get_manhattan_dist(goal.get(0));
                int fn = g+h;
                
                if(dup == null){
                    open_list = this.addOpenListEntry(open_list,new OpenListEntry(lowerLeft, g, h, fn));
                    parent_list.add(new ParentListEntry(lowerLeft, current.square));
                }
                else{
                    if(g < dup.g){
                        open_list.remove(dup);
                        open_list = this.addOpenListEntry(open_list, new OpenListEntry(lowerLeft, g, h, fn));
                    }
                }
            }
            
            //lower mid
            if(currSq.x+1 < maze.length && !closed_list.contains(maze[currSq.x+1][currSq.y]) && maze[currSq.x+1][currSq.y].type == ' '){
                Tile lowerMid = maze[currSq.x+1][currSq.y];
                OpenListEntry dup = this.searchOpenList(open_list, lowerMid);
                int g = current.g + 1;
                int h = lowerMid.get_manhattan_dist(goal.get(0));
                int fn = g+h;
                
                if(dup == null){
                    open_list = this.addOpenListEntry(open_list,new OpenListEntry(lowerMid, g, h, fn));
                    parent_list.add(new ParentListEntry(lowerMid, current.square));
                }
                else{
                    if(g < dup.g){
                        open_list.remove(dup);
                        open_list = this.addOpenListEntry(open_list, new OpenListEntry(lowerMid, g, h, fn));
                    }
                }
            }
            
             //lower right
            if(currSq.x+1 < maze.length && currSq.y+1 < maze[currSq.x+1].length && !closed_list.contains(maze[currSq.x+1][currSq.y+1]) && maze[currSq.x+1][currSq.y+1].type == ' '){
                Tile lowerRight = maze[currSq.x+1][currSq.y+1];
                OpenListEntry dup = this.searchOpenList(open_list, lowerRight);
                int g = current.g + 1;
                int h = lowerRight.get_manhattan_dist(goal.get(0));
                int fn = g+h;
                
                if(dup == null){
                    open_list = this.addOpenListEntry(open_list,new OpenListEntry(lowerRight, g, h, fn));
                    parent_list.add(new ParentListEntry(lowerRight, current.square));
                }
                else{
                    if(g < dup.g){
                        open_list.remove(dup);
                        open_list = this.addOpenListEntry(open_list, new OpenListEntry(lowerRight, g, h, fn));
                    }
                }
            }
      
            current = open_list.remove(0);
        }
        
//        for(int i = 0; i < open_list.size(); i++){
//            System.out.println(open_list.get(i).square.x + "," + open_list.get(i).square.y + "  " + open_list.get(i).g + " " + open_list.get(i).h + " " + open_list.get(i).fn);
//        }
    }
    
    static OpenListEntry searchOpenList(ArrayList<OpenListEntry> arr, Tile tile){
        for(int i = 0; i < arr.size(); i++){
            if(arr.get(i).square.equals(tile)){
                return arr.get(i);
            }
        }
        
        return null;
    }
    
    static ArrayList<OpenListEntry> addOpenListEntry(ArrayList<OpenListEntry> open_list, OpenListEntry newEntry){
        if(open_list.isEmpty()){
            open_list.add(newEntry);
            return open_list;
        }
        for(int i = 0; i < open_list.size(); i++){
            if(open_list.get(i).fn > newEntry.fn){
                open_list.add(i, newEntry);
                return open_list;
            }
            else if(open_list.get(i).fn == newEntry.fn){
                if(open_list.get(i).square.x > newEntry.square.x){
                    open_list.add(i, newEntry);
                    return open_list;
                }
                else if(open_list.get(i).square.x == newEntry.square.x){
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

class Tile{
    int x;
    int y;
    char type;
    
    Tile(int x, int y, char type){
        this.x = x;
        this.y = y;
        this.type = type;
    } 
    
    int get_manhattan_dist(Tile goal){
         return Math.abs(this.x - goal.x) + Math.abs(this.y - goal.y);
    }
    
    int get_straight_dist(Tile goal){
        return Math.max(Math.abs(this.x - goal.x), Math.abs(this.y - goal.y));
    }
}

class OpenListEntry{
    Tile square;
    int g;
    int h;
    int fn;
   
    OpenListEntry(Tile t, int g, int h, int fn){
        this.square = t;
        this.g = g;
        this.h = h;
        this.fn = fn;
    }
}

class ParentListEntry{
    Tile square;
    Tile parent;
    
    ParentListEntry(Tile t, Tile p){
        this.square = t;
        this.parent = p;
    }
}

