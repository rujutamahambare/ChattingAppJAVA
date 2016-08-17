import java.util.*;
import java.io.*;
import java.net.*;

public class ThreadedObjectServer{
	public static void main(String[] args ){
		ArrayList<ThreadedObjectHandler> handlers = new ArrayList<ThreadedObjectHandler>();
		ArrayList<String> onlineUsers = new ArrayList<String>();
		try{
			ServerSocket s = new ServerSocket(2001);
			System.out.println("Server on.. waiting");
			for (;;){
				Socket incoming = s.accept( );
				System.out.println("Connected with client");
				new ThreadedObjectHandler(incoming, handlers, onlineUsers).start();
				System.out.println("Client Thread");
			}
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
}

class ThreadedObjectHandler extends Thread{
	Object myObject = null;
	private Socket incoming;
	ArrayList<ThreadedObjectHandler> handlers;
	ArrayList<String> onlineUsers;
	ObjectInputStream in;
	ObjectOutputStream out;

	public ThreadedObjectHandler(Socket incoming, ArrayList<ThreadedObjectHandler> handlers, ArrayList<String> onlineUsers){
		this.incoming = incoming;
		this.handlers = handlers;
		this.onlineUsers = onlineUsers;
		handlers.add(this);
	}

	public void broadcast(Object obj){
		Iterator<ThreadedObjectHandler> it = handlers.iterator();
		while(it.hasNext()){
			ThreadedObjectHandler current = it.next();
			try{
				current.out.writeObject(obj);
				current.out.reset();
			}catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
	}



	public void run(){
		try{
			in = new ObjectInputStream(incoming.getInputStream());

			out = new ObjectOutputStream(incoming.getOutputStream());

			for(;;){
				myObject = in.readObject();

				if (myObject instanceof UserMessage)
				{
					UserMessage um = (UserMessage)myObject;
					onlineUsers.add(um.getMessage());
					System.out.println("joined : "+um.getMessage());
					broadcast(onlineUsers);
				}
				else if (myObject instanceof rmUser)
				{
					rmUser rm = (rmUser)myObject;
					if (onlineUsers.remove(rm.getMessage()))
						System.out.println(rm.getMessage() + " Removed" );
					else
						System.out.println(rm.getMessage() + " not Removed" );
					System.out.println("left : "+rm.getMessage());
					broadcast(onlineUsers);
				}
				else
					broadcast(myObject);
			}
		}catch (Exception e){
			System.out.println(e);
		}finally{
			handlers.remove(this);
			try{
				in.close();
				out.close();
				incoming.close();
			}catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
	}
}

