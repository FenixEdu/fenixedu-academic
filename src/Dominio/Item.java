/*
 * Item.java
 *
 * Created on 19 de Agosto de 2002, 13:21
 */

package Dominio;

/**
 *
 * @author  ars
 */

public class Item implements IItem {

	private Integer internalCode;
	private String information;
	private String name;
	private Integer itemOrder;
	private ISection section;
	private Boolean urgent;

	private Integer keySection;

	/** 
	 * Construtor 
	 */
	public Item() {
	}

	/** 
	 * Construtor 
	 */
	public Item(String name, ISection section, Integer itemOrder, String information, Boolean urgent) {
		setName(name);
		setSection(section);
		setItemOrder(itemOrder);
		setInformation(information);
		setUrgent(urgent);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof Item) {
			Item item = (Item) obj;
			resultado = (getName().equals(item.getName()))
				&& getSection().equals(item.getSection());
		}
		return resultado;
	}

	public String toString() {
		String result = "[ITEM";
		result += ", codInt=" + internalCode;
		result += ", nome=" + name;
		result += ", seccao=" + section;
		result += ", ordem=" + itemOrder;
		result += ", informacao=" + information;
		result += ", urgente=" + urgent;
		result += ", chaveSeccao=" + keySection;
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
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * @return Integer
	 */
	public Integer getKeySection() {
		return keySection;
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
	public Integer getItemOrder() {
		return itemOrder;
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
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * Sets the keySection.
	 * @param keySection The keySection to set
	 */
	public void setKeySection(Integer keySection) {
		this.keySection = keySection;
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
	public void setItemOrder(Integer order) {
		this.itemOrder = order;
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
