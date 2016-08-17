import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class ControlPanel extends JPanel{
	JButton b1, b2, b3, b4, b5;
	JButton saveDrawing;
	String b2Text;
	FrameDemo container;
	public ControlPanel(FrameDemo fd){
		container = fd;
		b1 = new JButton("Save Chat History");
		saveDrawing = new JButton("Save Drawing");
		if (container.isConnected()) b2Text="Sign Out";
		else b2Text="Close & TryAgain";
		b2 = new JButton(b2Text);


		b4 = new JButton("Clear");
		b5 = new JButton("Send Drawing");
		b1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent ae){
				container.save();
			}
		});
saveDrawing.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				container.saveDrawing();
			}
		});
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				container.close();
			}
		});

		b4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				container.clear();
			}
		});
		b5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				LineMessage lm = new LineMessage();
				lm.setMessage(container.dp.linelist);
				container.sendMessage(lm);
			}
		});

		add(b1);
		add(b2);
		add(saveDrawing);
		add(b4);
		//add(b5);
	}
}

