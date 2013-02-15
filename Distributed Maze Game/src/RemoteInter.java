

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInter extends Remote{
	
	int joinGame()throws RemoteException;
	int[][] getMaze()throws RemoteException;
	int[][] move(int id , String direction) throws RemoteException;
	int[] gettreasures() throws RemoteException;
	int[][] getupdates()throws RemoteException;
	}
