import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatPanel extends JPanel{
	JTextField tf;
	JTextArea ta;
	FrameDemo container;
	public ChatPanel(){
	}
	public ChatPanel(FrameDemo container){
		this.container = container;
		setLayout(new BorderLayout());
		tf = new JTextField();
		ta = new JTextArea();
		tf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String text = container.getName() + ": " + tf.getText();
				tf.setText("");
				StringMessage sm = new StringMessage();
				sm.setMessage(text);

				container.sendMessage(sm);
				//ta.setText(text + "\n");
			}
		});
		add(ta, BorderLayout.CENTER);
		add(tf, BorderLayout.SOUTH);
		container.addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		        tf.requestFocus();
		    }
		});
		ta.setFocusable(false);


	}
	public void appendMessage(String m){
		ta.append(m + "\n");
	}
	public Dimension getPreferredSize(){
		return new Dimension(450,100);
	}
	public Dimension getMinimumSize(){
		return new Dimension(450,100);
	}
}