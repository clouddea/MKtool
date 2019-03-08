import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainWindow extends JFrame implements MouseListener, KeyListener, MouseWheelListener, MouseMotionListener {

	private Socket socket = null;
	private static int aimWidth = 0;
	private static int aimHeight = 0;
	public static Boolean check(String s) 
	{
		return true;
	}
	public static int transformX(int x)
	{
		return x * aimWidth / Toolkit.getDefaultToolkit().getScreenSize().width;
	}
	public static int transformY(int y)
	{
		return y * aimHeight / Toolkit.getDefaultToolkit().getScreenSize().height;
	}
	public MainWindow()
	{
		this.setTitle("连接控制端");
		this.setSize(new Dimension(640, 480));
		final JPanel panel = new JPanel();
		final JTextField userText = new JTextField(20);
		final Label label = new Label("未连接");
        userText.setBounds(0,0,100,30);
        userText.setText("请输入受控端ip");
        final JButton button = new JButton("确定");
        this.add(panel);
        panel.add(userText);
        panel.add(button);
        panel.add(label);
		panel.setBackground(Color.cyan);
		this.setUndecorated(true);
		//this.setAlwaysOnTop(true);
		this.getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.addMouseWheelListener(this);
		this.addMouseMotionListener(this);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(check(userText.getText())) 
				{
					//socket
					try {
						socket = new Socket(userText.getText(), 6666);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					new BeatingThread(socket).start();
					InputStream is = null;
					try {
						is = socket.getInputStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					{
						byte[] length = new byte[4];
						try {
							is.read(length);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						DataInputStream dis = new DataInputStream(new ByteArrayInputStream(length));
						byte[] data = null;
						try {
							data = new byte[dis.readInt()];
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							is.read(data);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String[] serverDeviceInfo = new String(data).split(",");
						aimWidth = Integer.parseInt(serverDeviceInfo[0]);
						aimHeight = Integer.parseInt(serverDeviceInfo[1]);
						System.out.println("width:" + serverDeviceInfo[0]);
						System.out.println("height:" + serverDeviceInfo[1]);
						JOptionPane.showMessageDialog(null, "连接成功");
						button.setVisible(false);
						userText.setVisible(false);
						label.setText("已连接");
						//this.setResizable(false);
						//this.getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
						
						System.out.println("连接成功");
					}
				}
			}
		});
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode() == 27) 
		{
			System.exit(0);
		}
		//System.out.println(arg0.getKeyCode());
		//JOptionPane.showMessageDialog(this, arg0.getKeyCode() + ": " + arg0.getKeyChar());
		if(socket != null) 
		{
			try {
				Message.send(socket.getOutputStream(),new Message(5, arg0.getKeyCode() + ""));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

		if(socket != null) 
		{
			try {
				Message.send(socket.getOutputStream(),new Message(6, arg0.getKeyCode() + ""));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

		if(socket != null) 
		{
			try {
				Message.send(socket.getOutputStream(),new Message(2, transformX(arg0.getX()) + "," + transformY(arg0.getY()) + "," + arg0.getButton()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(socket != null) 
		{
			try {
				Message.send(socket.getOutputStream(),new Message(3, transformX(arg0.getX()) + "," + transformY(arg0.getY()) + "," + arg0.getButton()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		new MainWindow().setVisible(true);
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub

		if(socket != null) 
		{
			try {
				Message.send(socket.getOutputStream(),new Message(4, arg0.getWheelRotation() + ""));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseMoved(arg0);
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(socket != null) 
		{
			try {
				Message.send(socket.getOutputStream(),new Message(7, transformX(arg0.getX()) + "," + transformY(arg0.getY())));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
