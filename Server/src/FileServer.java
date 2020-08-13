import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Contract.CSMessage;
import Contract.Task;

public class FileServer implements Runnable {

	public static ServerSocket serverSocket;
	private Socket s;

	public static void main(String[] args) {
		try {
			FileServer.serverSocket = new ServerSocket(6790);
			ObjectServer.serverSocket = new ServerSocket(6789);
			System.out.println("Listening for file upload on " + FileServer.serverSocket);
			System.out.println("Listening for objects on " + ObjectServer.serverSocket);

				new Thread(new FileServer()).start();
				new Thread(new ObjectServer()).start();
		} catch (IOException e) {
		}
	}

	public FileServer() {

	}

	public void run() {
		
			while (true) {
				try {
				s = serverSocket.accept();
				System.out.println("file upload");
				InputStream in = s.getInputStream();
				// Construct data input stream to receive class files
				DataInputStream clientData = new DataInputStream(in);
				// Receive the class file name
				String ClassName = clientData.readUTF();
				// Receive the class file length
				int size = clientData.readInt();
				// Construct a byte array to receive the class file
				byte[] buffer = new byte[size];
				int bytesRead = clientData.read(buffer, 0, buffer.length);
				// Construct a file output stream to save the class file
				File f = new File("bin\\Contract", ClassName + ".class");
				FileOutputStream fo = new FileOutputStream(f);
				BufferedOutputStream bos = new BufferedOutputStream(fo);
				Task t=null;
				bos.write(buffer, 0, bytesRead);
				bos.flush();
				t = new CSMessage(ClassName+".class uploaded successfully");
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(t);
				oos.flush();
				oos.close();
				bos.close();
				
				s.close();
				
			}
				 catch (Exception e) {
						e.printStackTrace();
					}
		}
	}

}

class ObjectServer implements Runnable {

	public static ServerSocket serverSocket;
	private Socket s;

	public ObjectServer() {

	}

	@Override
	public void run() {
		while (true) {
			try {
				s = serverSocket.accept();
				System.out.println("object execution");
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

				try {
					Task t = (Task) ois.readObject();
					t.executeTask();
					System.out.println(t.getResult().toString());
					oos.writeObject(t);
					oos.flush();
				} catch (ClassNotFoundException e) {
					Task t = new CSMessage("Class not found. Please upload class file");
					oos.writeObject(t);
					oos.flush();
				}
				oos.close();
				ois.close();
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
