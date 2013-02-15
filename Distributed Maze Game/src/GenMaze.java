import java.util.Random;



public  class GenMaze {
	int length ;
	int no_treasure;
	private int treasure_val =100;
	public int cell[][] ;
	public int treasure_count =0;
	
	public GenMaze(int length , int no_treasure){
		this.length = length;
		this.no_treasure = no_treasure;
		createmaze();
	}
	
	private void createmaze() {
		Random rangen = new Random();
		cell = new int[length][length];
		for(int k=0;k<no_treasure;k++){
			 int i = rangen.nextInt(length);
			 int j = rangen.nextInt(length);
			 if(cell[i][j]>=treasure_val){
				cell[i][j] = cell[i][j]+treasure_val;
			 }else{
				 cell[i][j]= treasure_val;
			 }
				
					
				}
		System.out.println("maze generated");
						
	}
	
	
	public int treasures(){
		for(int i=0;i<length;i++){
        	for(int j=0;j<length;j++){
        		if(cell[i][j]>=treasure_val){
        			treasure_count = treasure_count+cell[i][j]/100;
        		}
        	}    	
		}
		System.out.println("treasures:"+treasure_count);
		return treasure_count;
	}

	public int[][] maze(){
		return cell;
	}
	
}