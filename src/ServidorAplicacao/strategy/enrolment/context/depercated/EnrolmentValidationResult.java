package ServidorAplicacao.strategy.enrolment.context.depercated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrolmentValidationResult {
	public static final String PRECEDENCE_DURING_ENROLMENT = "message.precedence.during.enrolment";
	public static final String ACUMULATED_CURRICULAR_COURSES="message.acumulated.curricular.courses";
	public static final String MINIMUM_CURRICULAR_COURSES_TO_ENROLL = "message.minimum.number.curricular.courses.to.enroll";
	public static final String MAXIMUM_CURRICULAR_COURSES_TO_ENROLL = "message.maximum.number.curricular.courses.to.enroll";
	public static final String MUST_ENROLL_IN_EARLIER_CURRICULAR_COURSES="message.enrolment.earlier.curricular.courses";
	public static final String SUCCESS_ENROLMENT = "message.successful.enrolment";
	public static final String LEQ_UNIQUE_LABORATORY = "message.leq.unique.laboratory";
	public static final String NO_CURRICULAR_COURSES_TO_ENROLL = "error.no.curricular.courses.to.enroll";
	public static final String NO_OPTIONAL_CURRICULAR_COURSES_TO_ENROLL = "error.no.optional.curricular.courses.to.enroll";
	
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

	public void setErrorMessage(String key) {
		message.put(key, new ArrayList());
		this.sucess = false;
	}

	public void setErrorMessage(String key, Object value) {
		List valueList = new ArrayList(4);
		valueList.add(0, value);
		message.put(key, valueList);
		this.sucess = false;
	}

	public void setErrorMessage(String key, Object value0, Object value1) {
		List valueList = new ArrayList(4);
		valueList.add(0, value0);
		valueList.add(1, value1);
		message.put(key, valueList);
		this.sucess = false;
	}

	public void setErrorMessage(String key, Object value0, Object value1, Object value2) {
		List valueList = new ArrayList(4);
		valueList.add(0, value0);
		valueList.add(1, value1);
		valueList.add(2, value2);
		message.put(key, valueList);
		this.sucess = false;
	}

	public void setErrorMessage(String key, Object value0, Object value1, Object value2, Object value3) {
		List valueList = new ArrayList(4);
		valueList.add(0, value0);
		valueList.add(1, value1);
		valueList.add(2, value2);
		valueList.add(3, value3);
		message.put(key, valueList);
		this.sucess = false;
	}

	public void setErrorMessage(String key, Object[] values) {
		message.put(key, values);
		this.sucess = false;
	}

	/**
	 * Sets the message.
	 * @param message The message to set
	 */
	public void setErrorMessage(Map errorMessage) {
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
	
	public void reset(){
		this.message.clear();
		this.sucess = true;
	}
}