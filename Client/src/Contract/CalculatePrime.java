package Contract;

import java.io.Serializable;

import javafx.scene.control.TextInputDialog;

public class CalculatePrime implements Task, Serializable {

	private String result = "Primes : ";
	private int num;

	public CalculatePrime() {
		TextInputDialog tid = new TextInputDialog();
		tid.setContentText("Enter num :");
		tid.setHeaderText("Calculate Prime");
		tid.showAndWait();
		this.num = Integer.parseInt(tid.getResult());
		tid.close();
		
	}

	public boolean isPrime(int number) {
		for (int i = 2; i < number; i++) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}

	public void executeTask() {
		for(int i=2 ; i<=num ; i++) {
			if(isPrime(i)) {
				result += " "+i; 
			}
		}
	}

	public Object getResult() {
		return result;
	}
}
