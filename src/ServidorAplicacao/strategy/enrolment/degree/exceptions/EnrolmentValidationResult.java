package ServidorAplicacao.strategy.enrolment.degree.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrolmentValidationResult {

	private Map message;
	private boolean sucess;

	public EnrolmentValidationResult() {
		this.message = new HashMap();
		this.sucess = true;
	}

	public EnrolmentValidationResult(Map errorMessage) {
		this.message = errorMessage;
		this.sucess = true;
	}

	public void setSucessMessage(String key) {
		message.put(key, new ArrayList());
		this.sucess = true;
	}

	public void setMessage(String key) {
		message.put(key, new ArrayList());
		this.sucess = false;
	}

	public void setMessage(String key, Object value) {
		List valueList = new ArrayList(4);
		valueList.add(0, value);
		message.put(key, valueList);
		this.sucess = false;
	}

	public void setMessage(String key, Object value0, Object value1) {
		List valueList = new ArrayList(4);
		valueList.add(0, value0);
		valueList.add(1, value1);
		message.put(key, valueList);
		this.sucess = false;
	}

	public void setMessage(String key, Object value0, Object value1, Object value2) {
		List valueList = new ArrayList(4);
		valueList.add(0, value0);
		valueList.add(1, value1);
		valueList.add(2, value2);
		message.put(key, valueList);
		this.sucess = false;
	}

	public void setMessage(String key, Object value0, Object value1, Object value2, Object value3) {
		List valueList = new ArrayList(4);
		valueList.add(0, value0);
		valueList.add(1, value1);
		valueList.add(2, value2);
		valueList.add(3, value3);
		message.put(key, valueList);
		this.sucess = false;
	}

	public void setMessage(String key, Object[] values) {
		message.put(key, values);
		this.sucess = false;
	}

	/**
	 * Sets the message.
	 * @param message The message to set
	 */
	public void setMessage(Map errorMessage) {
		this.message = errorMessage;
		this.sucess = false;
	}

	/**
	 * @return Map
	 */
	public Map getMessage() {
		return message;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "\n";
		result += "message " + this.getMessage()+ "\n";
		result += "sucess " + this.isSucess() + "\n";
		result += "]";
		return result;
	}

	/**
	 * @return boolean
	 */
	public boolean isSucess() {
		return sucess;
	}

	/**
	 * Sets the sucess.
	 * @param sucess The sucess to set
	 */
	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}
}