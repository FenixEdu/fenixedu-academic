/*
 * Created on 4/Jul/2003 by jpvl
 *
 */
package DataBeans;

/**
 * @author jpvl
 */
public class InfoDeparment extends InfoObject {
	private String name;
	private String code;
	
	public InfoDeparment() {
	}
	
	/**
	 * @return
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
