import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class Login
{
	public static void main(String args[])
	{
		LoginFrame f=new LoginFrame();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}


class LoginFrame extends JFrame
{
	JTextField tf=new JTextField(20);
	Container c;
	JPanel main;
	int width;
	int height;
	Dimension screenSize;
	LoginFrame()
	{
		Toolkit kits=Toolkit.getDefaultToolkit();
		screenSize=kits.getScreenSize();
		width=screenSize.width/2;
		height=screenSize.height/2;
		setSize(500,500);
		setResizable(false);
		setLocation(screenSize.width/3,screenSize.height/4);
		LoginFrame.setDefaultLookAndFeelDecorated(true);
		setTitle("Login Page");
		//Image img=Toolkit.getDefaultToolkit().getImage("Gradien.jpg");
		//setIconImage(img);
		c=getContentPane();
		tf.setBorder(BorderFactory.createBevelBorder(1, new Color(200, 200, 200), new Color(100, 100, 100)));
		tf.setMaximumSize(tf.getPreferredSize());
		JButton ok=new JButton("Login");
		
 		main=new JPanel()
 		{
			public void paintComponent(Graphics g)
			{
				Toolkit kit=Toolkit.getDefaultToolkit();
				Image img=kit.getImage("Chat.png");
				MediaTracker t=new MediaTracker(this);
				t.addImage(img,1);
				while(true)
				{
					try
					{
						t.waitForID(1);
						break;
					}
					catch(Exception e){}
				}
				g.drawImage(img,0,0,width,height+20,null);

			}
		};

		JLabel a= new JLabel("WELCOME!!!!");
		a.setFont(a.getFont().deriveFont(25f));
		JLabel b= new JLabel("Username:");
		b.setFont(b.getFont().deriveFont(20f));
		ok.setFont(ok.getFont().deriveFont(20f));
		main.setLayout(new GridBagLayout());
		GridBagConstraints cons=new GridBagConstraints();
		cons.fill=GridBagConstraints.CENTER;
		cons.anchor=GridBagConstraints.CENTER;
		cons.weightx=00;
		cons.weighty=00;
		cons.insets = new Insets(10, 10, 10, 10);
		add(a,cons,1,0,1,1);
		add(b,cons,1,2,1,1);
		cons.fill=GridBagConstraints.CENTER;
		cons.weightx=00;
		cons.insets = new Insets(10, 10, 10, 10);
		add(tf,cons,0,1,0,0);
		cons.insets = new Insets(10, 10, 10, 10);
		add(ok,cons,1,4,1,1);


		
	
		
		tf.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if((e.getKeyCode())==KeyEvent.VK_ENTER)
				{
					String uName=tf.getText();
					//FrameDemo.main(uName);
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							FrameDemo.createAndShowGUI(uName);
						}
        			});

					dispose();
				}
			}
		});

		ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String uName=tf.getText();
				//FrameDemo.main(new String[1]);
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						FrameDemo.createAndShowGUI(uName);
					}
				});

				dispose();
			}
		});
		
	}
public void add(Component comp,GridBagConstraints cons,int x,int y,int w,int h)
	{
		cons.gridx=x;
		cons.gridy=y;
		cons.gridwidth=w;
		cons.gridheight=h;
		comp.setPreferredSize(comp.getPreferredSize());
		main.add(comp,cons);
		c.add(main);
	}

}