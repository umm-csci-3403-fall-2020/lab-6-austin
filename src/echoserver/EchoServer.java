package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	
	// REPLACE WITH PORT PROVIDED BY THE INSTRUCTOR
	public static final int PORT_NUMBER = 6013; 
	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
		while (true) {
			Socket socket = serverSocket.accept();
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			ClientThread clientThread = new ClientThread(socket, input, output);
			Thread thread = new Thread(clientThread);
			thread.start();
		}
	}
	final class ClientThread implements Runnable{
		InputStream inputStream;
		OutputStream outputStream;
		Socket socket;
		public ClientThread(Socket sock, InputStream in, OutputStream out){
			this.inputStream = in;
			this.outputStream = out;
			this.socket = sock;
		}
		@Override
		public void run(){
			int line;
			try{
				while((line = inputStream.read()) != -1){
					outputStream.write(line);
					outputStream.flush();
				}
				socket.shutdownOutput();
				socket.shutdownInput();
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
}