import java.io.IOException;

public class Start {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String arg = args[0].trim();
		if(arg.equals("server")) 
		{
			Server.main(args);
		}else if(arg.equals("client")) 
		{
			Client.main(args);
		}else 
		{
			System.out.println("参数不正确");
			System.out.println("你输入的是：" + arg);
		}
	}

}
