/*
 * InfoItem.java
 * Mar 11, 2003
 */
package DataBeans.gesdis;

import java.io.Serializable;

import Dominio.ISection;

/**
 * @author Ivo Brandão
 */
public class InfoItem implements Serializable {

	private String information;
	private String name;
	private Integer order;
	private ISection section;
	private Boolean urgent;

	/**
	 * Constructor
	 */
	public InfoItem() {
	}

	/**
	 * Constructor
	 */
	public InfoItem(String information, String name, Integer order, 
		ISection section, Boolean urgent) {

		this.information = information;
		this.name = name;
		this.order = order;
		this.section = section;
		this.urgent = urgent;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoItem) {
			InfoItem infoItem = (InfoItem) obj;
			resultado =  getInformation().equals(infoItem.getInformation())
				&& getName().equals(infoItem.getName())
				&& getOrder().equals(infoItem.getOrder())
				&& getSection().equals(infoItem.getSection())
				&& getUrgent().equals(infoItem.getUrgent());
		}
		return resultado;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[INFOITEM";
		result += ", name=" + name;
		result += ", order=" + order;
		result += ", section=" + section;
		result += ", urgent=" + urgent;		
		result += "]";
		return result;
	}

	/**
	 * @return String
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Integer
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @return ISection
	 */
	public ISection getSection() {
		return section;
	}

	/**
	 * @return Boolean
	 */
	public Boolean getUrgent() {
		return urgent;
	}

	/**
	 * Sets the information.
	 * @param information The information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the order.
	 * @param order The order to set
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * Sets the section.
	 * @param section The section to set
	 */
	public void setSection(ISection section) {
		this.section = section;
	}

	/**
	 * Sets the urgent.
	 * @param urgent The urgent to set
	 */
	public void setUrgent(Boolean urgent) {
		this.urgent = urgent;
	}

}
