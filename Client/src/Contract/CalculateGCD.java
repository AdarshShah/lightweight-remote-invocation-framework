package Contract;

import java.io.Serializable;

import javafx.scene.control.TextInputDialog;

public class CalculateGCD implements Task, Serializable {

	private int a, b, result;

	public CalculateGCD() {
		TextInputDialog tid = new TextInputDialog();
		tid.setContentText("Enter a :");
		tid.setHeaderText("Calculate GCD");
		tid.showAndWait();
		this.a = Integer.parseInt(tid.getResult());
		tid.close();

		tid = new TextInputDialog();
		tid.setContentText("Enter b :");
		tid.setHeaderText("Calculate GCD");
		tid.showAndWait();
		this.b = Integer.parseInt(tid.getResult());
		tid.close();
	}

	public long CalculateGCD(long a, long b) {
		if (a == 0)
			return b;
		else {
			while (b != 0) {
				if (a > b)
					a = a - b;
				else
					b = b - a;
			}
			return a;
		}
	}

	public void executeTask() {
		result = (int) CalculateGCD(a,b);
	}

	public Object getResult() {
		return "GCD(" + a + "," + b + "):" + result;
	}
}
