/*
 * Created on Jul 5, 2004
 *
 */
package Util.renderer.container;


/**
 * @author Luis Cruz
 *  
 */
public class RequestEntry {
	private String requestPath = null;

	private int executionTime = 0;

	private int numberCalls = 0;

	public RequestEntry() {
		super();
	}

	public int getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}

	public int getNumberCalls() {
		return numberCalls;
	}

	public void setNumberCalls(int numberCalls) {
		this.numberCalls = numberCalls;
	}

	public int getAverageExecutionTime() {
		if (numberCalls != 0) {
			return executionTime / numberCalls;
		} 
			return 0;
		
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}
}