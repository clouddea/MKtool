import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Input {
	public static byte[]IntToByte(int num){
		byte[]bytes=new byte[4];
		bytes[0]=(byte) ((num>>24)&0xff);
		bytes[1]=(byte) ((num>>16)&0xff);
		bytes[2]=(byte) ((num>>8)&0xff);
		bytes[3]=(byte) (num&0xff);
		return bytes;
	}
	public static int ByteToInt(byte[] length) throws IOException 
	{
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(length));
		return dis.readInt();
	}
}
