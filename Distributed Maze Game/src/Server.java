

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Server implements RemoteInter{
	private int flag =0;
	static int panel_size;
	static int no_treasures;
	private int treasure_val =100;
	public int maze[][]=new int[panel_size][panel_size];	
	int no_players=0 ;
	int[][] plyrpos=new int[30][3];	
	static Server remoteobj;
	GenMaze gmaze = null;
	int left_treasure=0 ;
	int winner;
	
	public Server(){
		super();
	 }	
		
	public void framecall(){
		gmaze = new GenMaze(panel_size,no_treasures);
		maze = gmaze.maze();
		left_treasure = gmaze.treasures();
		
		}

	
	public int joinGame(){
		int seconds = 20;
		if(no_players==0){
			Timer timer = new Timer();
			Date timeToRun = new Date(System.currentTimeMillis()+ seconds*1000);
			
			timer.schedule(new TimerTask() {
				
				public void run() {
					flag = 1;
					System.out.println("Time is up. No more Players Allowed");
					this.cancel();
				}
			},timeToRun);
		}
			if(flag==0){ 
			       Random rangen = new Random();	   		   				
					no_players++;
				   			  plyrpos[no_players][0] = rangen.nextInt(panel_size);
				   			  plyrpos[no_players][1] = rangen.nextInt(panel_size);
				   			  while(maze[plyrpos[no_players][0]][plyrpos[no_players][1]]>=treasure_val){
				   				  plyrpos[no_players][0] = rangen.nextInt(panel_size);
					   			  plyrpos[no_players][1] = rangen.nextInt(panel_size);
				   			//	  left_treasure = left_treasure-(maze[plyrpos[no_players][0]][plyrpos[no_players][1]])/100;
				   			//	  System.out.println("left treasure refreshed : "+left_treasure);
				   			  }
				   			  maze[plyrpos[no_players][0]][plyrpos[no_players][1]] = no_players;
				   			  
				   		    System.out.println("players :" + no_players);						
	}else if(flag==1){		
		//no_players=0;
		System.out.println("no players allowed "+no_players);
		return 0;
	}
			 return no_players;
		}
	
	public int[][] move(int id, String direction) throws RemoteException {
		
		 int newXcordinates = 0;
		 int newYcordinates = 0;
		 int oldXcordinates = plyrpos[id][0];
		 int oldYcordinates = plyrpos[id][1]; 
		
		 int direc = 0;
		
		  if(direction.equals("left")){
			  direc = 1;
			  if(oldYcordinates == 0){
				  newXcordinates = oldXcordinates;
				  newYcordinates = oldYcordinates;
				  System.out.println("You are already extreame left");
			  }else{			 
			  newXcordinates = oldXcordinates;
			  newYcordinates = oldYcordinates-1;
			  }
		  }else if(direction.equals("right")){
			  direc =2;
			  if(oldYcordinates == panel_size-1){
				  newXcordinates = oldXcordinates;
				  newYcordinates = oldYcordinates;
				  System.out.println("You are already extreame right");
			  }else{
			  newXcordinates = oldXcordinates;
			  newYcordinates = oldYcordinates+1;
			  }
		  }else if(direction.equals("up")){
			  direc =3;
			  if(oldXcordinates == 0){
				  newXcordinates = oldXcordinates;
				  newYcordinates = oldYcordinates;
				  System.out.println("You are already extreame top");
			  }else{
			  newXcordinates = oldXcordinates-1;
			  newYcordinates = oldYcordinates;
			  }
		  }else if(direction.equals("down")){
			  direc =4;
			  if(oldXcordinates == panel_size-1){
				  newXcordinates = oldXcordinates;
				  newYcordinates = oldYcordinates;
				  System.out.println("You are already extreame bottom");
			  }else{
			  newXcordinates = oldXcordinates+1;
			  newYcordinates = oldYcordinates;
			  }
		  }		  
		  if(direc==1||direc==2||direc==3||direc==4){ 
			  if(left_treasure!=0){			
				if(maze[newXcordinates][newYcordinates]>=treasure_val){			
					plyrpos[id][2]= plyrpos[id][2]+maze[newXcordinates][newYcordinates];
					left_treasure = left_treasure - maze[newXcordinates][newYcordinates]/100 ;
					 maze[newXcordinates][newYcordinates]= id;
					 maze[oldXcordinates][oldYcordinates]=0;
					 
					 plyrpos[id][0]= newXcordinates;
					 plyrpos[id][1]= newYcordinates;
					// plyrpos[id][2]=treascount[id];
					 System.out.println("left  treasure :"+ left_treasure);					
				}else if(!(maze[newXcordinates][newYcordinates]>0 && maze[newXcordinates][newYcordinates]<treasure_val)){
					maze[newXcordinates][newYcordinates]= id;
					maze[oldXcordinates][oldYcordinates]=0;
					plyrpos[id][0]= newXcordinates;
					plyrpos[id][1]= newYcordinates;	
					
			}		 
			  }/*else{
				  for(int i=1;i<5;i++){
					if(plyrpos[i][2]>plyrpos[i-1][2]){
						winner =i;
					}
				  }
			  System.out.println("game over and player "+winner+" wins");
			  
			  }*/
		        }
		return maze;
			
	}
	@Override
	public int[][] getMaze() throws RemoteException {
		int seconds = 20;
		for(int i=0; i<1;i++){
			Timer timer = new Timer();
			Date timeToRun = new Date(System.currentTimeMillis()+ seconds*1000);			
			timer.schedule(new TimerTask() {				
				public void run() {
					flag = 1;					
					this.cancel();
				}
			},timeToRun);
		}
			if(flag==0){ 
		while(flag ==0){
         	try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
         }   	 
         						
	}/*else if(no_players==0){
		return null;
	}*/
			return maze;
			
	}
	

	@Override
	public int[][] getupdates() throws RemoteException {
					 
		return maze;
	}

	
	public int[] gettreasures() throws RemoteException {
		int treascount[] = new int[no_players+1];
		for(int i=1;i<=no_players;i++){
			treascount[i]= plyrpos[i][2];
		}
		return treascount;
	}	
	
	public static void main(String[] args) {
		panel_size= Integer.parseInt(args[0]);
		no_treasures = Integer.parseInt(args[1]);
		
		 if (System.getSecurityManager() == null) {
	            System.setSecurityManager(new SecurityManager());
	        }
		 RemoteInter stub = null;
		 Registry registry = null;
		 remoteobj =  new Server();
		 
		 try{	    
	            
	           stub = (RemoteInter) UnicastRemoteObject.exportObject(remoteobj, 0);
	    	   registry = LocateRegistry.getRegistry();
	    	   registry.bind("Hello", stub);
	    	   System.err.println("Server ready");
	    	   }catch(Exception e){			 
			 try{				
					registry.unbind("Hello");
					registry.bind("Hello",stub);
					System.err.println("Server ready1");
					 					 			
					    }catch(Exception ee){
						System.err.println("Server exception: " + ee.toString());
					    	ee.printStackTrace();
					    }
			  }
		 remoteobj.framecall();
		}

	}
