package sample;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends Main {

    public static void main(String args[]) {
        try {
            /**
            * @param obj Instantiating the implementation class
            */
            Calculator obj = new Calculator();
            /** 
             * This method exports the object of implementation class to the stub 
            */
            Interface stub = (Interface) UnicastRemoteObject.exportObject(obj, 0);
            /**
             *  Binding of the remote object in the registry
             */
            Registry registry = LocateRegistry.getRegistry();

            registry.bind("Interface", stub);
            /**
             *   Server response when Executed
             */
            System.err.println("Server ready");
        } catch (Exception e) {
            /**
             * Exception Handling
             */
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }


}
