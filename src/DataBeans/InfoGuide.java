/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;

import java.io.Serializable;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoGuide implements Serializable{
	
	private Integer number;
	private Integer year;
	private Double total;
	private String remarks;
	private InfoPerson infoPerson;
	private InfoContributor infoContributor;

	public InfoGuide() {} 
		
	public InfoGuide(Integer number, Integer year, Double total, String remarks, InfoPerson infoPerson, InfoContributor infoContributor){	 		
		this.number = number;
		this.year = year;
		this.total = total;
		this.remarks = remarks;
		this.infoPerson = infoPerson;
		this.infoContributor = infoContributor;
	}
	
	public boolean equals(Object obj) {
	  boolean resultado = false;
	  if (obj instanceof InfoGuide) {
		InfoGuide guide = (InfoGuide)obj;

		resultado = getNumber().equals(guide.getNumber()) &&
					getYear().equals(guide.getYear());
	  }

	  return resultado;
	}

	public String toString() {
	  String result = "[INFO_GUIDE";
	  result += ", number=" + number;
	  result += ", year=" + year;
	  result += ", person=" + infoPerson;
	  result += ", contributor=" + infoContributor;
	  result += ", total=" + total;
	  result += ", remarks=" + remarks;
	  result += "]";
	  return result;
	}
    



	/**
	 * @return
	 */
	public InfoContributor getInfoContributor() {
		return infoContributor;
	}

	/**
	 * @return
	 */
	public InfoPerson getInfoPerson() {
		return infoPerson;
	}

	/**
	 * @return
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @return
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @return
	 */
	public Double getTotal() {
		return total;
	}

	/**
	 * @return
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param contributor
	 */
	public void setInfoContributor(InfoContributor contributor) {
		infoContributor = contributor;
	}

	/**
	 * @param person
	 */
	public void setInfoPerson(InfoPerson person) {
		infoPerson = person;
	}

	/**
	 * @param integer
	 */
	public void setNumber(Integer integer) {
		number = integer;
	}

	/**
	 * @param string
	 */
	public void setRemarks(String string) {
		remarks = string;
	}

	/**
	 * @param double1
	 */
	public void setTotal(Double double1) {
		total = double1;
	}

	/**
	 * @param integer
	 */
	public void setYear(Integer integer) {
		year = integer;
	}

}
