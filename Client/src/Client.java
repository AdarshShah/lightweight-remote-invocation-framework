import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import Contract.Task;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Client extends Application {

	Socket fileSocket;
	Socket objectSocket;

	@FXML
	TextField ipAddress;
	@FXML
	TextField portObject;
	@FXML
	TextField portFile;
	@FXML
	Button set;
	@FXML
	ComboBox<String> classFiles;
	@FXML
	Button upload;
	@FXML
	Button calculate;
	@FXML
	TextArea output;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Client.fxml"));
		primaryStage.setTitle("Registration Form FXML Application");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@FXML
	public void initiateSockets() {
		String[] classes = new String[] { "CalculatePi", "CalculateGCD", "CalculatePrime" };
		classFiles.setItems(FXCollections.observableArrayList(classes));
		output.setWrapText(true);
	}
	
	@FXML
	public void upload() {
		try {
			fileSocket = new Socket(ipAddress.getText().trim(), Integer.parseInt(portFile.getText().trim()));
			OutputStream out = fileSocket.getOutputStream();
            //Retrieve the compute class name
            String ClassName=classFiles.getValue();
            //Read the class file into a byte array
            File ClassFile = new File("bin\\Contract",ClassName+".class");
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ClassFile));
            DataInputStream dis = new DataInputStream(bis);   
            byte[] mybytearray = new byte[(int) ClassFile.length()]; 
            dis.readFully(mybytearray, 0, mybytearray.length);
            //Use a data output stream to send the class file
            DataOutputStream dos = new DataOutputStream(out);
            //Send the class file name
            dos.writeUTF(ClassName);
            //Send the class file length
            dos.writeInt(mybytearray.length);
            //Send the class file
            dos.write(mybytearray, 0, mybytearray.length);   
            dos.flush();
            ObjectInputStream ois = new ObjectInputStream(fileSocket.getInputStream());
            Task t = (Task)ois.readObject();
            display(t.getResult().toString());
            
            fileSocket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void calculate() {
		try {
			
			String s = classFiles.getValue();
			Task t = (Task)Class.forName("Contract."+s).newInstance();
			objectSocket = new Socket(ipAddress.getText().trim(), Integer.parseInt(portObject.getText().trim()));
			ObjectOutputStream oos = new ObjectOutputStream(objectSocket.getOutputStream());
			oos.writeObject(t);
			oos.flush();
			
			ObjectInputStream ois = new ObjectInputStream(objectSocket.getInputStream());
			t = (Task)ois.readObject();
			display(t.getResult().toString());
			objectSocket.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void display(String s) {
		output.appendText("\n---------------------------------------------------------------------------------------");
		output.appendText("\n"+s);
		output.appendText("\n---------------------------------------------------------------------------------------");
	}

}
