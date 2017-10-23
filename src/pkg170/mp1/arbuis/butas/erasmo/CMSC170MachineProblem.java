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
     private static final String TEXT_FILE = "C:\\Users\\TRISHA NICOLE\\Desktop\\Mazes\\tinyMaze.lay.txt";
    // private static final String TEXT_FILE = "C:\\Users\\User\\Documents\\CMSC 170\\Machine Problems\\Machine Problem 1\\Mazes\\trickySearch.lay.txt";
//    private static final String TEXT_FILE = "D:\\College\\4thyear-1stsem\\CMSC 170\\Mazes\\mediumSearch.lay.txt"; //<- Celine

    public static void main(String[] args) {
        Maze maze = new Maze(TEXT_FILE);
       
        
        // false = straight line dist; true = manhattan dist
         maze.single_goal_driver(false);
//        maze.multiple_goal_driver(false);
        
    }
}

class Maze {
    Tile[][] maze;
    int start_row = 0, start_col = 0, end_row = 0, end_col = 0;
    Tile start = null;
    ArrayList<Tile> goal = new ArrayList();
    int path_cost = 0;
    int frontier_size = 0;
    
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

    private void processNextTile(Tile child, OpenListEntry current, ArrayList<OpenListEntry> open_list, ArrayList<ParentListEntry> parent_list, boolean heuristics) {
        OpenListEntry dup = this.searchOpenList(open_list, child);
        int g = current.g + 1;
        int h;
        Tile removed;

        if(heuristics) {
            h = child.get_manhattan_dist(goal.get(0));
        } else {
            h = child.get_straight_dist(goal.get(0));
        }
        
        int fn = g+h;

        // System.out.println(fn);
        
        if(dup == null){
            open_list = this.addOpenListEntry(open_list, new OpenListEntry(child, g, h, fn));
            parent_list.add(new ParentListEntry(child, current.square));
            frontier_size++;
        }
        else{
            if(g < dup.g){
                open_list.remove(dup);
                open_list = this.addOpenListEntry(open_list, new OpenListEntry(child, g, h, fn));
            }
        }
    }
   
    public void single_goal_driver(boolean heuristics){
        ArrayList<Tile> closed_list = new ArrayList();
        ArrayList<ParentListEntry> parent_list = new ArrayList();
        parent_list.add(new ParentListEntry(start, null));
        single_goal(heuristics, new ArrayList(), closed_list, parent_list, null);
        System.out.println("SINGLE");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("solutionSingleGoal.txt", "UTF-8");
            for(int j = 0; j < this.maze.length; j++) {
                for(int k = 0; k < this.maze[j].length; k++) {
                    writer.print(this.maze[j][k].type + " ");
                }
                writer.println();
            }
            
            writer.println("\nPath Cost: " + this.path_cost(parent_list));
            writer.println("Nodes Expanded: " + closed_list.size());
            writer.println("Frontier size: " + frontier_size);
            
            for(int x = 0; x < closed_list.size(); x++){
                System.out.println(closed_list.get(x).x + "," + closed_list.get(x).y);
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
    
    public void single_goal(boolean heuristics, ArrayList<OpenListEntry> open_list,  ArrayList<Tile> closed_list, ArrayList<ParentListEntry> parent_list,  OpenListEntry current) {
        frontier_size++;
        if(heuristics) {
            open_list.add(new OpenListEntry(start, 0, start.get_manhattan_dist(goal.get(0)), start.get_manhattan_dist(goal.get(0))));
        } else {
            open_list.add(new OpenListEntry(start, 0, start.get_straight_dist(goal.get(0)), start.get_straight_dist(goal.get(0))));
        }
        current = open_list.remove(0);
        System.out.println(current.square.x + "  ,  " + current.square.y);;
        
        while(!current.square.equals(goal.get(0))) {
            closed_list.add(current.square);
            
            Tile currSq = current.square;
            
            //upper mid
            if(currSq.x-1 >= 0 && !closed_list.contains(maze[currSq.x-1][currSq.y]) && maze[currSq.x-1][currSq.y].type != '%') {
                processNextTile(maze[currSq.x-1][currSq.y], current, open_list, parent_list, heuristics);
            }

            //mid left
            if(currSq.y-1 >= 0 && !closed_list.contains(maze[currSq.x][currSq.y-1]) && maze[currSq.x][currSq.y-1].type != '%') {
                processNextTile(maze[currSq.x][currSq.y-1], current, open_list, parent_list, heuristics);
            }
            
            //mid right
            if(currSq.y+1 < maze[currSq.x].length && !closed_list.contains(maze[currSq.x][currSq.y+1]) && maze[currSq.x][currSq.y+1].type != '%') {
                processNextTile(maze[currSq.x][currSq.y+1], current, open_list, parent_list, heuristics);
            }
           
            // lower mid
            if(currSq.x+1 < maze.length && !closed_list.contains(maze[currSq.x+1][currSq.y]) && maze[currSq.x+1][currSq.y].type != '%') {
                processNextTile(maze[currSq.x+1][currSq.y], current, open_list, parent_list, heuristics);
            }
           
            current = open_list.remove(0);
            System.out.println(current.square.x + "  ,  " + current.square.y);;

            Tile closest = goal.get(0);
            for(int i = 1; i < goal.size(); i++){
                if(heuristics){
                    if(current.square.get_manhattan_dist(goal.get(i)) < current.square.get_manhattan_dist(closest)){
                        closest = goal.get(i);
                    }
                }
                else{
                    if(current.square.get_straight_dist(goal.get(i)) < current.square.get_straight_dist(closest)){
                        closest = goal.get(i);
                    }
                }
            }
            goal.remove(closest);
            goal.add(0, closest);
        }
        closed_list.add(current.square);
        tracePath(closed_list.get(closed_list.size()-1), parent_list);

        
    }

    /**
            MULTIPLE GOALS
    **/
    public void multiple_goal_driver(boolean heuristics){
        ArrayList<Tile> closed_list = new ArrayList();
        ArrayList<OpenListEntry> open_list = new ArrayList();
        ArrayList<ParentListEntry> parent_list = new ArrayList();
        parent_list.add(new ParentListEntry(start, null));
        ArrayList<Tile> ordered_goal = new ArrayList();
        ArrayList<Tile> closed_list_accumulate = new ArrayList();
         ArrayList<OpenListEntry> open_list_accumulate = new ArrayList();
        System.out.println("MULTIPLE");
       
        while(!goal.isEmpty()){
            Tile closest = goal.get(0);
            for(int i = 1; i < goal.size(); i++){
                if(heuristics){
                    if(start.get_manhattan_dist(goal.get(i)) < start.get_manhattan_dist(closest)){
                        closest = goal.get(i);
                    }
                }
                else{
                    if(start.get_straight_dist(goal.get(i)) < start.get_straight_dist(closest)){
                        closest = goal.get(i);
                    }
                }
            }
            goal.remove(closest);
            goal.add(0, closest);
            closed_list = new ArrayList(); 
            open_list = new ArrayList();
            single_goal(heuristics, open_list, closed_list, parent_list, null);
            
            System.out.println("OL " + open_list.size());
            open_list_accumulate.addAll(open_list);
//            for(int x = 0; x < closed_list.size(); x++){
//                System.out.print(closed_list.get(x).x + "," + closed_list.get(x).y + "  ");
//            }
            
            System.out.println("\n");
            closed_list_accumulate.addAll(closed_list);
            start = goal.remove(0);
            ordered_goal.add(start);
        }
        for(int x = 0; x < closed_list_accumulate.size(); x++){
            System.out.println(closed_list_accumulate.get(x).x + " " + closed_list_accumulate.get(x).y);
        }
        
//        for(int x = 0; x < closed_list_accumulate.size() - 1; x++){
//            for(int y = x + 1; y < closed_list_accumulate.size();){
//                if(closed_list_accumulate.get(x).equals(closed_list_accumulate.get(y))){
//                    closed_list_accumulate.remove(y);
//                }
//                else{
//                     y++;
//                }
//            }
//        }

        int temp = 0;

        for(int l = 0; l < ordered_goal.size(); l++){
            ordered_goal.get(l).order = l+1;
        }

        PrintWriter writer = null;
        int spacer = 1;
        for(int l=10; (ordered_goal.size() + 1) / l > 0; l*=10) {
            spacer ++;
        }
        try {
            writer = new PrintWriter("solutionMultipleGoal.txt", "UTF-8");
            for(int j = 0; j < this.maze.length; j++) {
                for(int k = 0; k < this.maze[j].length; k++) {
                    if(this.maze[j][k].order == 0) {
                        writer.print(this.maze[j][k].type);
                        writer.print(new String(new char[spacer]).replace("\0", " "));
                    }
                    else {
                        writer.print(this.maze[j][k].order);
                        int ind_space = spacer;
                        for(int l=10; this.maze[j][k].order / l > 0; l*=10) {
                            ind_space --;
                        }
                        writer.print(new String(new char[ind_space]).replace("\0", " "));
                    }
                }
                writer.println();
            }
            
            writer.println("\nPath Cost: " + this.path_cost(parent_list));
            writer.println("Nodes Expanded: " + closed_list_accumulate.size());
            writer.println("Frontier size: " + frontier_size);
            
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
    
    private void tracePath(Tile current, ArrayList<ParentListEntry> parent_list) {
        int count = -1;
        while(current!=null){
            for(int i = 0; i < parent_list.size(); i++){
                if(current.equals(parent_list.get(i).square)) {
                    count++;
                    for(int j = 0; j < this.maze.length; j++) {
                        for(int k = 0; k < this.maze[j].length; k++) {
                            if(current.equals(this.maze[j][k]) && this.maze[j][k].type != 'P') {
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
    
    private static int path_cost(ArrayList<ParentListEntry> parent_list){
        Tile traverse = parent_list.get(parent_list.size()-1).parent;
        int path_cost = 1;
        for(int x = parent_list.size()-2; x > 0; x--){
            if(parent_list.get(x).square.x == traverse.x && parent_list.get(x).square.y == traverse.y){
                path_cost++;
                traverse = parent_list.get(x).parent;
            }
        }
        
        return path_cost;
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
    int order;
    
    Tile(int x, int y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.order = 0;
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

