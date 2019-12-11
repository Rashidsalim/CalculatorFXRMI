import java.rmi.Remote;
import java.rmi.RemoteException;

/** Creating Remote Interface for The Calculator Application*/ 

public interface CalcInterface extends Remote {    
    void handle() throws RemoteException;
}
