/*
 * Created on Sep 6, 2003
 *
 */
package Util;


/**
 * @author Luis Cruz
 *
 */
public class AdvisoryRecipients extends FenixUtil {

	public static final int STUDENTS = 1;
	public static final int TEACHERS = 2;
	public static final int EMPLOYEES = 3;

	private Integer recipients;

	/**
	 * 
	 */
	public AdvisoryRecipients() {
		super();
		recipients = null;
	}

	public AdvisoryRecipients(Integer recipients) {
		super();
		this.recipients = recipients;
	}	

	/**
	 * @return
	 */
	public Integer getRecipients() {
		return recipients;
	}

	/**
	 * @param integer
	 */
	public void setRecipients(Integer integer) {
		recipients = integer;
	}

	/**
	 * @param i
	 * @return
	 */
	public boolean equals(int i) {
		if (recipients != null && recipients.intValue() == i) {
			return true;
		}

		return false;
	}

}
