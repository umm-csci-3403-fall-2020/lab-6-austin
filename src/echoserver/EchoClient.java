package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException {
		Socket socket = new Socket("localhost", PORT_NUMBER);
	// Output threading
		OutputStream outputStream = socket.getOutputStream();
		WriteToServer writer = new WriteToServer(outputStream, socket);
		Thread outThread = new Thread(writer);
		outThread.start();

		// input threading
		InputStream inputStream = socket.getInputStream();
		ReadServer reader = new ReadServer(inputStream, socket);
		Thread inThread = new Thread(reader);
		 inThread.start();
	}

	public static void ioError(IOException io){
		io.printStackTrace();
		System.out.println("Something went very wrong Godspeed in finding the error");	
	}

	public class ReadServer implements Runnable{
		InputStream inputStream;
		int read;
		Socket socket;
	
		public ReadServer(InputStream in, Socket sock) {
			this.inputStream = in;
			this.socket = sock;
		}
		@Override
		public void run(){
			try{
				while((read = inputStream.read())!= -1){//reads while data is sent in
					System.out.write(read);
					System.out.flush();
				}
				socket.shutdownInput();//shutdowns the soxket after reading
			}catch(IOException io){
				ioError(io);
			}
		}
	}

	public class WriteToServer implements Runnable{
		OutputStream outputStream;
		int send;
		Socket socket;

		public WriteToServer(OutputStream out, Socket sock){
			outputStream = out;
			socket = sock;
		}
		@Override
		public void run(){
			try{
				while((send = System.in.read())!= -1){//reads while data is sent in
					this.outputStream.write(send);
					this.outputStream.flush();
				}
				socket.shutdownOutput();//shutdowns the soxket after reading
			}catch(IOException io){
				ioError(io);
			}
		}
	}
}
