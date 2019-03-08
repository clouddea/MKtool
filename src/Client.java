import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.omg.CORBA.portable.OutputStream;
import javax.swing.*;
public class Client {

	
	public static void main(String[] args) throws UnknownHostException, IOException {

		new MainWindow().setVisible(true);
		
	}

}



class BeatingThread extends Thread
{
	Socket socket;
	public BeatingThread(Socket socket)
	{
		this.socket = socket;
	}
	@Override
	public void run() 
	{
		
		
		
	}
	public void start() 
	{
		super.start();
	}
}