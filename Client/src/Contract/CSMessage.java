package Contract;

import java.io.Serializable;

import Contract.Task;

public class CSMessage implements Task, Serializable {
	
	private String finalResult;
	
	public CSMessage(String finalResult) {
		super();
		this.finalResult = finalResult;
	}

	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}

	public void executeTask() {
	}
	
	public Object getResult() {
		return finalResult;
	}
}
