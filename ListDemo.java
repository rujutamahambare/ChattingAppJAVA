import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
 import java.util.*;
/* ListDemo.java requires no other files. */
public class ListDemo extends JPanel
                      implements ListSelectionListener {
    private JList list;
    private DefaultListModel<String> listModel;

    private static final String hireString = "Hire";
    private static final String fireString = "Fire";
    private JButton fireButton;
    private JTextField employeeName;

    public ListDemo() {
        super(new BorderLayout());

        listModel = new DefaultListModel<String>();
        //listModel.addElement("Jane Doe");
        //listModel.addElement("John Smith");
        //listModel.addElement("Kathy Green");

        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);


        add(listScrollPane, BorderLayout.CENTER);
       
    }
	 public synchronized void refreshList(Object obj)
    {
		listModel.clear();
		ArrayList<String> onlineUsers = (ArrayList<String>) obj;
		ArrayList<String> copy = new ArrayList<String>();
		System.out.println("refreshing list");
		Iterator<String> it = onlineUsers.iterator();
		while(it.hasNext())
		{
			String user = it.next();
			System.out.println(user);
			listModel.addElement(user);
			copy.add(user);
		}
		onlineUsers = copy;
	}
   

    

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
 
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ListDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
