/*
 * Created on 23/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Util;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EnrolmentPolicyType {
	
	public static final int ATOMIC = 1;
	public static final int INDIVIDUAL = 2;

	private Integer type;

	/** Creates a new instance of EnrolmentPolicyType */
	public EnrolmentPolicyType() {
	}

	public EnrolmentPolicyType(int type) {
		this.type = new Integer(type);
	}

	public EnrolmentPolicyType(Integer type) {
		this.type = type;
	}

	/** Getter for property type.
	* @return Value of property type.
	*
	*/
	
	public java.lang.Integer getType() {
		return type;
	}

	/** Setter for property type.
	* @param type New value of property type.
	*
	*/
	
	public void setType(Integer type) {
		this.type = type;
	}

	public boolean equals(Object o) {
			if (o instanceof EnrolmentPolicyType) {
				EnrolmentPolicyType aux = (EnrolmentPolicyType) o;
				return this.type.equals(aux.getType());
			} else {
				return false;
			}
		}

	public String toString() {

			int value = this.type.intValue();
			String stringValue = null;

			switch (value) {
				case ATOMIC :
					stringValue = "ATOMIC";
					break;
				case INDIVIDUAL :
					stringValue = "INDIVIDUAL";
					break;
				default:
					break;
			}

			return "[" + this.getClass().getName() + ": " + stringValue + "]";
		}
}
