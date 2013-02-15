

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Client extends JPanel implements ActionListener,  KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int panel_size;
	int treasure_val =100;
	int no_players;
    int cell[]=null ;	
	int[][] response;	
	static int id=0;	
	String direction;
	RemoteInter stub;
	static Client clint;
	int gameover = 0;
	int winner=0;
	
	
	public Client(){
		super();
		
		JButton btn = new JButton("EXIT");
		setLayout(new BorderLayout());
		add(btn,BorderLayout.NORTH);
		btn.setOpaque(true);
		btn.setVisible(true);
		//btn.setSize(50, 30);
		btn.addActionListener(this);
		btn.setActionCommand("exit");
		this.requestFocus();
		addKeyListener(this);
		this.setFocusable(true);
	}
	
	
  public synchronized void paint(Graphics g) {
			int w = 0;
			int h = 0;
			int wide = 900;
			int tall = 580;
			 w= (wide-300) /(panel_size);
			 h = (tall-280) /(panel_size);
			g.setColor(Color.gray);
			g.fillRect(50, 50, wide, tall);
			Font font = new Font("Calibri", Font.BOLD, 13);
			g.setColor(Color.black);
			g.setFont(font);
			
			 if(id==0){
				 response=null;				 		
			 }
			 if(response!= null){
			 if(gameover==0){ 
						
			g.drawString("PLAYER "+id,350,40);
		    g.drawString("Plyr  TreasureCnt",670 , 90);
		    if(cell!=null){
		    	no_players=cell.length -1;
		    for(int i=1;i<=no_players;i++){
		    	g.drawString(""+i, 690, 80+i*30);	    	
				g.drawString(String.valueOf(cell[i]), 720, 80+i*30);
				//System.out.println("cell value "+cell[i][2]);
				}
		    }
			for(int i=0; i<panel_size;i++){
				for(int j=0;j<panel_size;j++){
					if(response[i][j] == treasure_val){
					  g.drawString("@",w*j+60, h*i+h+70);
				  }else if(response[i][j]==2*treasure_val){
					  g.setColor(Color.red);
					  g.drawString("&", w*j+60, h*i+h+70);
					  g.setColor(Color.black);
				  }else if(response[i][j]>2*treasure_val){
					  g.setColor(Color.blue);
					  g.drawString("B", w*j+60, h*i+h+70);
					  g.setColor(Color.black);
				  }else if(response[i][j]==0){
					  g.drawString(".", w*j+60, h*i+h+70);
				  }else if(response[i][j]>0 || response[i][j]<100){
					  String player_no = Integer.toString(response[i][j]);
					  g.setColor(Color.yellow);
					  g.drawString(player_no,w*j+60, h*i+h+70);
					  g.setColor(Color.black);
				  }
			 }
		}
			
				
			}else {				
				for(int i=1;i<=no_players;i++){
					if(cell[i]>cell[i-1]){
						winner=i;
					}
				}
						g.setColor(Color.blue);
						g.drawString("PLAYER "+id,350,40);
						g.drawString("Congratulations !!!!!",170,80);
						g.drawString("Player "+winner+" won the Maze Game", 150, 100);
						g.setColor(Color.black);
					
			}
			 }
			 else{
				g.setColor(Color.blue);
				g.drawString("Time is up... No more Players can JOIN", 150, 100);
				g.setColor(Color.black);
			}
	}
  
  	@Override
	public void keyPressed(KeyEvent evt) {
		int key = evt.getKeyCode();
		 if (key == KeyEvent.VK_LEFT) {
			direction = "left" ; 
		 }else if(key ==KeyEvent.VK_RIGHT){
			direction="right";
		 }else if(key ==KeyEvent.VK_UP){
			 direction ="up";
		 }else if(key ==KeyEvent.VK_DOWN){
			 direction="down";		 
		 }
		 try {
			response = stub.move(id, direction);
			
		
			} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

  	public class ThreadCall extends Thread{
		public synchronized void run(){
				
		try {
			while(true){
			int treasures_hint=0;
			Thread.sleep(100);
			response =stub.getupdates();			
			cell = stub.gettreasures();
			for(int i=0; i<panel_size;i++){
				for(int j=0;j<panel_size;j++){
					if(response[i][j]>=treasure_val){
						treasures_hint++;
					}
			
				}
			}
			if(treasures_hint==0){
				gameover=1;
			}
			clint.repaint();
			clint.validate();
			
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
		}
	
}
	public void startthread(){
		ThreadCall threadcall = new ThreadCall();
		threadcall.start();
	}
  	public void initialGame(String host){
  		try {
  			 Registry registry = LocateRegistry.getRegistry(host);
  	         stub = (RemoteInter)registry.lookup("Hello");
  	         id= stub.joinGame();
  	         System.out.println("id ="+id);
  	         if(id!=0){
  	         response = stub.getMaze();
  	         panel_size = response[0].length;
  	         }else{
  	        	 panel_size=15;
  	         }
  	        // no_players = (stub.gettreasures()).length;
  	       //  System.out.println("no of players ="+no_players);
  	         JFrame framegc = new JFrame();
			 framegc.getContentPane().add(clint);
			 framegc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 framegc.setSize(1000, 700);
			 framegc.setVisible(true);
  						          
  		}catch(Exception e){}
  	}
  	
    public static void main(String args[]) {
    				
		        if (System.getSecurityManager() == null) {
		            System.setSecurityManager(new SecurityManager());
		        }		        
		        String host = (args.length < 1) ? null : args[0];
		        try {
		        	clint = new Client();
		        	clint.initialGame(host);
		        	
		            clint.startthread();		                     
		             } catch (Exception e) {
		                System.err.println("exception occured");
		                e.printStackTrace();
		            }		          
		      
		  }


	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand()=="exit"){
			System.gc();
			System.exit(0);
		
	}

	} 
}
