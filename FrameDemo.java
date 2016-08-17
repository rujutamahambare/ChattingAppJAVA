import java.awt.*;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.filechooser.*;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;

public class FrameDemo extends JFrame implements Runnable{
	String uName;
    ChatPanel cp;
	DrawPanel dp;
	ListDemo ld;
	ControlPanel conp;
	boolean connected;

	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	//public FrameDemo(){}
	public FrameDemo(String s, String Name){
		super(s);
		uName = Name;
		connect();
		if (!isConnected())
			setTitle("Chat System with Shared Whiteboard - Unable to log in");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cp = new ChatPanel(this);
		dp = new DrawPanel(this);
		ld = new ListDemo();
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(cp, BorderLayout.WEST);
		container.add(ld, BorderLayout.CENTER);
		container.add(dp, BorderLayout.EAST);
        getContentPane().add(container, BorderLayout.CENTER);
		conp = new ControlPanel(this);
		getContentPane().add(conp, BorderLayout.SOUTH);

		//Display the window.
        //frame.pack();
		setSize(1000,500);
		setLocationRelativeTo(null);

		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				close();
			}
		});

        setVisible(true);
        try{
			StringMessage sm=new StringMessage();
			sm.setMessage(uName+" Joined");
			System.out.println(sm.getMessage());
			oos.writeObject(sm);
			UserMessage um=new UserMessage();
			um.setMessage(uName);
			System.out.println("UM msg: "+um.getMessage());
			oos.writeObject(um);

		}catch(IOException e)
		{
			System.out.println(e.getMessage());
		}

	}
	public void setConnected(boolean c){
		connected = c;
	}
	public boolean isConnected(){
		return connected;
	}
	public String getName(){
		return uName;
	}
    public void save()
	{
		JFileChooser fc = new JFileChooser("./");
		//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = fc.showSaveDialog(FrameDemo.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File file = new File(fc.getSelectedFile() + "");
        	if(file == null)
                return;
            if(file.exists())
            {
                returnVal = JOptionPane.showConfirmDialog(this,
                                   "Replace existing file?");
                if (returnVal == JOptionPane.NO_OPTION)
                    return;
            }
            //This is where a real application would save the file.
            try
            {
            	BufferedWriter o = new BufferedWriter(new FileWriter(file));

            	o.write(cp.ta.getText());
            	o.close();
            }
            catch(IOException e){
            	System.out.println(e.getMessage());
            }
            cp.ta.append("Saving: " + file.getName() + "." + uName + "\n"+cp.ta.getText());

        }
        else
        {
            cp.ta.append("Save command cancelled by user." + uName + "\n");
    	}
    	cp.ta.setCaretPosition(cp.ta.getDocument().getLength());
	}
	public void saveDrawing()
	{
		JFileChooser fc = new JFileChooser("./");
		//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


		int returnVal = fc.showSaveDialog(FrameDemo.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File file = new File(fc.getSelectedFile() + "");
        	if(file == null)
                return;
            if(file.exists())
            {
                returnVal = JOptionPane.showConfirmDialog(this,
                                   "Replace existing file?");
                if (returnVal == JOptionPane.NO_OPTION)
                    return;
            }
            //This is where a real application would save the file.
            try
            {
            	BufferedImage i = new BufferedImage(450, 450,BufferedImage.TYPE_INT_RGB);
            	Graphics2D ig2 = i.createGraphics();
            	Iterator<Line> it = dp.linelist.iterator();
				while(it.hasNext()){
					Line current = it.next();
					ig2.drawLine(current.getStartX(), current.getStartY(),
							current.getEndX(), current.getEndY());
				}
				//LineMessage lm = new LineMessage();
				//lm.setMessage(linelist);
				//container.sendMessage(lm);
				ig2.drawString("Painted on JPanel",100,100);

            	ImageIO.write(i,"PNG",file);
            }
            catch(IOException e){
            	System.out.println(e.getMessage());
            }
            cp.ta.append("Saving: " + file.getName() + "." + uName + "\n");

        }
        else
        {
            cp.ta.append("Save command cancelled by user." + uName + "\n");
    	}
    	cp.ta.setCaretPosition(cp.ta.getDocument().getLength());
	}
	public void connect(){
		System.out.println("Should connect now . . . ");
		try{
			s = new Socket("afsaccess1.njit.edu", 2001);
			//s = new Socket("127.0.0.1", 3001);
			setConnected(true);
			System.out.println("connected ");
			oos = new ObjectOutputStream(s.getOutputStream());
			//oos.writeObject(new DataObject(uName+" Joined"));
			//sendMessage(sm);
			new Thread(this).start();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	public void close(){
		try{
			if (connected){
				StringMessage sm=new StringMessage();
				sm.setMessage(uName+" left");
				oos.writeObject(sm);
				rmUser ru = new rmUser();
				ru.setMessage(uName);
				oos.writeObject(ru);

				oos.close();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		finally
		{
			setConnected(false);	//should send last message so others can update user list
									// and remove self from shared arralist of handlers
			Login.main(new String[2]);
			dispose();
			//System.exit(0);
		}
	}



	public void load(){
		new Thread(this).start();
	}
	public void run(){
		try{
			ois = new ObjectInputStream(s.getInputStream());
			for(;;){
				Object o = receiveMessage();
				if(o != null){
					if(o instanceof StringMessage){
						StringMessage sm = (StringMessage)o;
						String s = sm.getMessage();
						System.out.println(s);
						cp.appendMessage(s);
					}else if(o instanceof LineMessage){
						LineMessage lm = (LineMessage)o;
						ArrayList<Line> linelist = (ArrayList)lm.getMessage();
						dp.linelist = linelist;
						dp.repaint();
					}else if(o instanceof ArrayList){
						ld.refreshList(o);
					}
				}else{
					break;
				}
			}
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(IOException e){
			System.out.println("IO Exception: " + e.getMessage());
		}finally{
			try{
				ois.close();
			}catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
	}
	public void sendMessage(Object o){

		if(isConnected()){
			if(o instanceof LineMessage) System.out.println("LineMessage written to stream");
			//if(o instanceof DataObject) System.out.println("DataObject written to stream");
			try{
				oos.writeObject(o);
			}catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
	}
	public Object receiveMessage(){

		Object obj = null;
		try{
			obj = ois.readObject();
			//if(obj instanceof StringMessage){
			//	StringMessage sm = (StringMessage)obj;
			//	String s = (String)sm.getMessage();
			//	System.out.println("Message is: " + s);
			//}
		}catch(IOException e){
			System.out.println("End of stream.");
		}catch(ClassNotFoundException e){
			System.out.println(e.getMessage());
		}
		return obj;
	}

	public void clear(){
		dp.linelist = new ArrayList<Line>();
		dp.repaint();
		cp.ta.setText("");
	}

    public static void createAndShowGUI(String uName) {
        FrameDemo frame = new FrameDemo("Chat System with Shared Whiteboard - logged in as "+uName, uName);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI("Sagar");
            }
        });
    }
}
