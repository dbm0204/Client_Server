import java.net.*;
import java.io.*;
public class hallo
{
 public static void main(String[] args) {
	int readNr, MAXMSG=100;
	byte [] b = new byte[MAXMSG];
	try{
		System.out.println("Hello");
		System.out.flush();
		for(int k=0;k<3;k++){
			if(readNr=System.in/read(b)==-1)
				break;
			System.out.write(b,0,readNr);
			System.out.flush();
		}
		catch(Exception e){
			System.out.println("Exception:"+e);
			System.out.flush();
		}
	}	

	}
}