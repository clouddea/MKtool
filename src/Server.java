
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

import javax.swing.JFrame;

import java.io.*;
import org.omg.CORBA.portable.InputStream;
public class Server{

	public static void main(String[] args) throws IOException {
		/*
		 msg:
		 	1 : msg length                int 4 bytes
		 	2 : msg content               multi bytes
		 */
		
		/*
		 	msg content :
		 		{
		 			type : 0 greeting, 1 heartbeating, 2 mouse press, 3 mouse release  4 mouseWheel
		 			  5 key press 6 key release, 7 mouse move,  8 mouse drag
		 			
		 			value : "null",      "null",     ...  keycode  ...
		 		}
		 */
		
		/*
		    .................................2019\3\5..............................................................
		 */
		
		//set windows
		JFrame jframe = new JFrame();
		jframe.setTitle("Becontroller");
		jframe.setSize(new Dimension(640, 480));
		jframe.setLayout(null);
		Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
		String addresses = "\n";
		while(e.hasMoreElements()) 
		{
			NetworkInterface ni = e.nextElement();
			Enumeration<InetAddress> ei = ni.getInetAddresses();
			while(ei.hasMoreElements()) 
			{
				InetAddress ia = ei.nextElement();
				if(ia != null && ia instanceof Inet4Address) 
				{

					addresses += ia.getHostAddress() + "|\n";
				}
			}
		}
		System.out.println(addresses);
		Label label = new Label("plese connect to one of available ip: " + addresses);
		
		label.setBounds(0, 0, 640, 100);
		Label state = new Label("idle");
		state.setBounds(0, 110, 640, 30);
		jframe.getContentPane().add(label);
		jframe.getContentPane().add(state);
		jframe.setVisible(true);
		//set socket server
		ServerSocket server = new ServerSocket(6666);
		
		while(true) 
		{
			Socket socket = server.accept();
			if(!ControlManager.used) 
			{
				new CustomThread(socket, state).start();
				ControlManager.used = true;
				state.setText("used");
			}else 
			{
				socket.close();
			}
		}
		
	}
	
	

}


class CustomThread extends Thread
{
	private Socket socket;
	private Label label;
	private Robot robot = null;
	public CustomThread(Socket socket, Label label)
	{
		this.socket = socket;
		this.label = label;
	}
	@Override
	public void run() 
	{
		
		System.out.println("come a people");
		try {
			robot = new Robot();
		} catch (AWTException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		byte[] data = (screenSize.width + "," + screenSize.height).getBytes();
		try {
			socket.getOutputStream().write(Input.IntToByte(data.length));
			socket.getOutputStream().write(data);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			java.io.InputStream is = socket.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		while(true) 
		{
			try {
				Message msg = Message.read(socket.getInputStream());
				System.out.println(msg);
				if(robot != null)
				{
					if(msg.type == 7 || msg.type == 2 || msg.type == 3) 
					{
						String[] s = msg.content.split(",");
						if(s != null) 
						{
							int x = Integer.parseInt(s[0]);
							int y = Integer.parseInt(s[1]);
							if(msg.type == 7) 
							{
								robot.mouseMove(x, y);
								System.out.println("move");
							}else 
							{
								int b = Integer.parseInt(s[2]);
								int button = 0;
								switch(b) 
								{
								case 1:button = InputEvent.BUTTON1_DOWN_MASK;break;
								case 2:button = InputEvent.BUTTON2_DOWN_MASK;break;
								case 3:button = InputEvent.BUTTON3_DOWN_MASK;break;
								}
								if(msg.type == 2) 
								{
									robot.mousePress(button);
									System.out.println("click");
								}else 
								{
									robot.mouseRelease(button);
									System.out.println("release");
								}
							}
						}
						
					}else if(msg.type == 4)  //wheel
					{
						int rotation = Integer.parseInt(msg.content);
						robot.mouseWheel(rotation);
						System.out.println("wheel");
					}else if(msg.type == 5) 
					{
						int keycode = Integer.parseInt(msg.content);
						robot.keyPress(keycode);
						System.out.println("keypress");
					}else if(msg.type == 6) 
					{
						int keycode = Integer.parseInt(msg.content);
						robot.keyRelease(keycode);
						System.out.println("keyrelease");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//¡¨Ω”÷–∂œ
				ControlManager.used = false;
				label.setText("idle");
				break;
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		


		/*try {
			Robot robot = new Robot();
			robot.mouseMove(814, 329);
			robot.mousePress(KeyEvent.BUTTON1_MASK);
			robot.mouseRelease(KeyEvent.BUTTON1_MASK);
			robot.mouseWheel(1);
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_SHIFT);
			//robot.mousePress(1);
			//robot.mouseRelease(1);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	public void start() 
	{
		super.start();
	}
}





















