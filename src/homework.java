import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

class inputtestcase{
    int boardsize;
    int gamedepth;
    String algorithm;
    String curr_player;
    String opposite_player;
    int[][] board;
    char[][] state;
    char[][] nextstate;
    String move;
    int[] pos;
    public inputtestcase(int boardsize,int gamedepth,String algorithm,String curr_player,int[][] board,char[][] state) {
        this.boardsize = boardsize;
        this.gamedepth = gamedepth;
        this.algorithm = algorithm;
        this.curr_player = curr_player;
        if(curr_player.equals("X")){
            this.opposite_player = "O";
        }
        else{
            this.opposite_player = "X";
        }
        this.board = board;
        this.state = state;
    }
}

public class homework {
    public boolean compare(String file1,String file2){
        try {
            File filename1 = new File(file1);
            File filename2 = new File(file2);
            Scanner input1 = new Scanner(filename1);
            Scanner input2 = new Scanner(filename2);
            while(input1.hasNextLine() && input2.hasNextLine()){
                String line1 = input1.nextLine();
                String line2 = input2.nextLine();
                if(!(line1.equals(line2))){
                    return false;
                }
            }
            while(input1.hasNextLine()){
                return false;
            }
            while(input2.hasNextLine()){
                return false;
            }
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public inputtestcase readinputtestcase(){
        try{
                    String dataset = System.getProperty("user.dir") + "/input.txt";
                    File file = new File(dataset);
                    Scanner input = new Scanner(file);
                    int boardsize = Integer.parseInt(input.nextLine());
                    String algorithm = input.nextLine();
                    algorithm =algorithm.trim();
                    String curr_player = input.nextLine();
                    curr_player = curr_player.trim();
                    int game_depth = Integer.parseInt(input.nextLine());
                    int counter =0;
                    int[][] board = new int[boardsize][boardsize];
                    while(input.hasNextLine() && counter<boardsize) {
                        String line = input.nextLine();
                        String[] arr = line.trim().split(" ");
                        for (int i = 0; i < boardsize; i++) {
                            board[counter][i] = Integer.parseInt(arr[i]);
                        }
                        counter++;
                    }
                    int counter2 =0;
                    char[][] state = new char[boardsize][boardsize];
                    while(input.hasNextLine() && counter2<boardsize){
                        String line = input.nextLine();
                        for (int i = 0; i < boardsize; i++) {
                            state[counter2][i] = line.charAt(i);
                        }
                        counter2++;
                    }
                    inputtestcase itestcase = new inputtestcase(boardsize,game_depth,algorithm,curr_player,board,state);
                    input.close();
                    return itestcase;

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return new inputtestcase(0,0,"","",new int[0][0],new char[0][0]);
    }
    public static void main(String[] args) {
       // File folder = new File("/Users/admin/Downloads/artifical_intelligence-master/USC/HW2/testcases");
       // File[] listOfFiles = folder.listFiles();

      //  for (int m = 0; m < listOfFiles.length; m++) {
      //      File file = listOfFiles[m];
     //       if (file.isFile() && file.getName().endsWith(".in")) {
                homework hw = new homework();
                inputtestcase ip = hw.readinputtestcase();
                if (ip.algorithm.equals("MINIMAX")) {
                    hw.dominmax(ip);
                } else {
                    hw.doalphabetapruning(ip);
                }
            //    String str = file.toString();
            //    str = str.replaceAll("\\.in", "\\.out");
                // str = str.replaceAll("INPUT", "OUTPUT");
            //    String dataset = System.getProperty("user.dir") + "/output.txt";
           //     if (hw.compare(str, dataset)) {
           //         System.out.println(true);
           //     } else {
           //         System.out.println(str);
           //     }
        //    }
      //  }
    }
    public int countemtpyposition(char[][] state){
        int m = state.length;
        int n = state[0].length;
        int count =0;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(state[i][j]=='.'){
                    count++;
                }
            }
        }
        return count;
    }
    public void dominmax(inputtestcase ip){
       int count = countemtpyposition(ip.state);
       if(ip.gamedepth>count){
           ip.gamedepth = count;
       }
        max_score(ip.state,0,ip.curr_player,ip);
        printoutput(ip);
    }
    public void printoutput(inputtestcase ip){

        try {
            PrintWriter writer = new PrintWriter("output.txt");
            char col = (char)(ip.pos[1]+65);
            int row =ip.pos[0]+1;
            writer.println(col + ""+ row + " " + ip.move);
            char[][] statenext = arraycopy(ip.state);
            statenext[ip.pos[0]][ip.pos[1]] = ip.curr_player.charAt(0);
            if(ip.move.equals("Raid")) {
                statenext = updatestateraid(statenext,ip.pos[0], ip.pos[1],ip.curr_player,ip);
            }
            int m = statenext.length;
            int n = statenext[0].length;
            for(int i=0;i<m;i++){
                String str ="";
                for(int j=0;j<n;j++){
                    str = str + statenext[i][j];
                }
                writer.println(str);
            }
            writer.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public int game_score(char[][] state,inputtestcase ip){
        int curr_player_sum =0;
        int opp_player_sum =0;
        int m = state.length;
        int n = state[0].length;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(state[i][j]== ip.curr_player.charAt(0)){
                    curr_player_sum = curr_player_sum + ip.board[i][j];
                }
                else if(state[i][j] == ip.opposite_player.charAt(0)){
                    opp_player_sum = opp_player_sum + ip.board[i][j];
                }
            }
        }
        int game_score = curr_player_sum - opp_player_sum;
        return game_score;
    }
    public char[][] arraycopy(char[][] temp){
        int m = temp.length;
        int n = temp[0].length;
        char[][] temparray = new char[m][n];
        for(int i=0;i<m;i++){
           System.arraycopy(temp[i],0,temparray[i],0,n);
        }
        return temparray;
    }
    public boolean stake(char[][] state,int i,int j){
        if(state[i][j] == '.'){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean raid(char[][] state,int i,int j,String curr_player){
       if(stake(state,i,j)){
           int counter =1;
           for(int asd =0;asd<10;asd++){
               counter = counter +1;
           }
           int m = state.length;
           int n = state[0].length;
           int rowless = i-1;
           int rowmore = i+1;
           int colmore = j+1;
           int colless = j-1;
           for(int asd =0;asd<10;asd++){
               counter = counter +1;
           }
           if (rowless >= 0 && state[rowless][j] == curr_player.charAt(0))
               return true;
           if(rowmore< m && state[rowmore][j] == curr_player.charAt(0)){
               return true;
           }
           if(colless>=0 && state[i][colless] == curr_player.charAt(0)){
               return true;
           }
           if(colmore <n && state[i][colmore] == curr_player.charAt(0)) {
               return true;
           }
       }
       return false;
    }
    public char[][] updatestateraid(char[][] state,int i,int j,String curr_player,inputtestcase ip){
        int m = state.length;
        int n = state[0].length;
        int rowless = i-1;
        int rowmore = i+1;
        int colmore = j+1;
        int colless = j-1;
        int counter =1;
        for(int asd =0;asd<10;asd++){
            counter = counter +1;
        }
        String oppplayer = "";
        if(curr_player.equals(ip.curr_player)){
            oppplayer = ip.opposite_player;
        }
        else{
            oppplayer = ip.curr_player;
        }
        if (rowless >= 0 && state[rowless][j] == oppplayer.charAt(0))
            state[rowless][j] = curr_player.charAt(0);
        if(rowmore< m && state[rowmore][j] == oppplayer.charAt(0)){
            state[rowmore][j] = curr_player.charAt(0);
        }
        if(colless>=0 && state[i][colless] == oppplayer.charAt(0)){
            state[i][colless] = curr_player.charAt(0);
        }
        if(colmore <n && state[i][colmore] == oppplayer.charAt(0)){
            state[i][colmore] = curr_player.charAt(0);
        }
       return state;
    }
    public void savestate(char[][] temp,String moveval,int i,int j,inputtestcase ip){
        ip.nextstate = arraycopy(temp);
        ip.move =moveval;
        int[] newpos ={i,j};
        ip.pos = newpos;
    }
    public int max_score(char[][] state,int currdepth,String curr_player,inputtestcase ip){
        if(currdepth == ip.gamedepth){
           int score = game_score(state,ip);
            return score;
        }
        int m = state.length;
        int n = state[0].length;
        int val = Integer.MIN_VALUE;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(stake(state,i,j)){
                char[][] temp = arraycopy(state);
                temp[i][j] = curr_player.charAt(0);
                String player = "";
                if(curr_player.equals(ip.curr_player)){
                    player = ip.opposite_player;
                }
                else{
                    player = ip.curr_player;
                }
                int result = min_score(temp,currdepth+1,player,ip);
                if(result>val){
                    if(currdepth==0){
                        savestate(temp,"Stake",i,j,ip);
                    }
                   val = result;
                }
            }
                else{

                }
            }
        }
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(raid(state,i,j,curr_player)){
                    char[][] temp = arraycopy(state);
                    temp[i][j] = curr_player.charAt(0);
                    temp = updatestateraid(temp,i,j,curr_player,ip);
                    String player = "";
                    if(curr_player.equals(ip.curr_player)){
                        player = ip.opposite_player;
                    }
                    else{
                        player = ip.curr_player;
                    }
                    int result = min_score(temp,currdepth+1,player,ip);
                    if(result>val){
                        if(currdepth==0){
                            savestate(temp,"Raid",i,j,ip);
                        }
                        val = result;
                    }
                }
                else{

                }
            }
        }
        return val;
    }
    public int min_score(char[][] state,int currdepth,String curr_player,inputtestcase ip){
        if(currdepth == ip.gamedepth){
            int score = game_score(state,ip);
            return score;
        }
        int m = state.length;
        int n = state[0].length;
        int val = Integer.MAX_VALUE;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(stake(state,i,j)){
                    char[][] temp = arraycopy(state);
                    temp[i][j] = curr_player.charAt(0);
                    String player = "";
                    if(curr_player.equals(ip.curr_player)){
                        player = ip.opposite_player;
                    }
                    else{
                        player = ip.curr_player;
                    }
                    int result = max_score(temp,currdepth+1,player,ip);
                    if(result<val){
                        val = result;
                    }
                }
                else{

                }
            }
        }
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(raid(state,i,j,curr_player)){
                    char[][] temp = arraycopy(state);
                    temp[i][j] = curr_player.charAt(0);
                    temp = updatestateraid(temp,i,j,curr_player,ip);
                    String player = "";
                    if(curr_player.equals(ip.curr_player)){
                        player = ip.opposite_player;
                    }
                    else{
                        player = ip.curr_player;
                    }
                    int result = max_score(temp,currdepth+1,player,ip);
                    if(result<val){
                        val = result;
                    }
                }
                else{

                }
            }
        }
        return val;
    }
    public int max_scorealphabeta(char[][] state,int currdepth,String curr_player,inputtestcase ip,int alpha,int beta){
        if(currdepth == ip.gamedepth){
            int score = game_score(state,ip);
            return score;
        }
        int m = state.length;
        int n = state[0].length;
        int val = Integer.MIN_VALUE;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(stake(state,i,j)){
                    char[][] temp = arraycopy(state);
                    temp[i][j] = curr_player.charAt(0);
                    String player = "";
                    if(curr_player.equals(ip.curr_player)){
                        player = ip.opposite_player;
                    }
                    else{
                        player = ip.curr_player;
                    }
                    int result = min_scorealphabeta(temp,currdepth+1,player,ip,alpha,beta);
                    if(result>val){
                        if(currdepth==0){
                            savestate(temp,"Stake",i,j,ip);
                        }
                        val = result;
                    }
                    if(val>=beta)
                        return val;
                    alpha = Math.max(alpha,val);
                }
                else{

                }
            }
        }
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(raid(state,i,j,curr_player)){
                    char[][] temp = arraycopy(state);
                    temp[i][j] = curr_player.charAt(0);
                    temp = updatestateraid(temp,i,j,curr_player,ip);
                    String player = "";
                    if(curr_player.equals(ip.curr_player)){
                        player = ip.opposite_player;
                    }
                    else{
                        player = ip.curr_player;
                    }
                    int result = min_scorealphabeta(temp,currdepth+1,player,ip,alpha,beta);
                    if(result>val){
                        if(currdepth==0){
                            savestate(temp,"Raid",i,j,ip);
                        }
                        val = result;
                    }
                    if(val>=beta)
                        return val;
                    alpha = Math.max(alpha,val);
                }
                else{

                }
            }
        }
        return val;
    }
    public int min_scorealphabeta(char[][] state,int currdepth,String curr_player,inputtestcase ip,int alpha,int beta){
        if(currdepth == ip.gamedepth){
            int score = game_score(state,ip);
            return score;
        }
        int m = state.length;
        int n = state[0].length;
        int val = Integer.MAX_VALUE;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(stake(state,i,j)){
                    char[][] temp = arraycopy(state);
                    temp[i][j] = curr_player.charAt(0);
                    String player = "";
                    if(curr_player.equals(ip.curr_player)){
                        player = ip.opposite_player;
                    }
                    else{
                        player = ip.curr_player;
                    }
                    int result = max_scorealphabeta(temp,currdepth+1,player,ip,alpha,beta);
                    if(result<val){
                        val = result;
                    }
                    if(val <= alpha)
                      return val;
                    beta = Math.min(beta, val);
                }
                else{

                }
            }
        }
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(raid(state,i,j,curr_player)){
                    char[][] temp = arraycopy(state);
                    temp[i][j] = curr_player.charAt(0);
                    temp = updatestateraid(temp,i,j,curr_player,ip);
                    String player = "";
                    if(curr_player.equals(ip.curr_player)){
                        player = ip.opposite_player;
                    }
                    else{
                        player = ip.curr_player;
                    }
                    int result = max_scorealphabeta(temp,currdepth+1,player,ip,alpha,beta);
                    if(result<val){
                        val = result;
                    }
                    if(val <= alpha)
                        return val;
                    beta = Math.min(beta, val);
                }
                else{

                }
            }
        }
        return val;
    }
    public void doalphabetapruning(inputtestcase ip){
        int count = countemtpyposition(ip.state);
        if(ip.gamedepth>count){
            ip.gamedepth = count;
        }
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        max_scorealphabeta(ip.state,0,ip.curr_player,ip,alpha,beta);
        printoutput(ip);
    }
}
