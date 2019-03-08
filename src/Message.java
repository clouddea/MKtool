import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Message {
	public int type;
	public String content;
	public Message(int type, String content) 
	{
		this.type = type;
		this.content = content;
	}
	public String toString() 
	{
		return "type: " + type + ", content: " + content;
	}
	static Message read(InputStream is) throws IOException 
	{
		byte[] type = new byte[4];
		if(is.read(type) != -1)
		{
			byte[] length = new byte[4];
			is.read(length);
			byte[] data = new byte[Input.ByteToInt(length)];
			is.read(data);
			String msgInfo = new String(data);
			return new Message(Input.ByteToInt(type), msgInfo);
		}
		return new Message(-1, "error");
	}
	static void send(OutputStream os,Message msg) throws IOException 
	{
		byte[] data = msg.content.getBytes();
		os.write(Input.IntToByte(msg.type));
		os.write(Input.IntToByte(data.length));
		os.write(data);
	}
}
