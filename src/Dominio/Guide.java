package Dominio;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class Guide implements IGuide {
  protected Integer internalCode;
  protected Integer keyPerson;
  protected Integer keyContributor;
  
  
  protected Integer number;
  protected Integer year;
  protected Double total;
  protected String remarks;
  
  protected IPessoa person;
  protected IContributor contributor;

  public Guide() { }
    
  public Guide(Integer number,Integer year,Double total, String remarks,IPessoa person,IContributor contributor) {
	this.contributor = contributor;
	this.number = number;
	this.person = person;
	this.remarks = remarks;
	this.total = total;
	this.year = year;

  }
  
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof IGuide) {
      IGuide guide = (IGuide)obj;

      resultado = getNumber().equals(guide.getNumber()) &&
                  getYear().equals(guide.getYear());
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[GUIDE";
    result += ", codInt=" + internalCode;
    result += ", number=" + number;
    result += ", year=" + year;
    result += ", person=" + person;
    result += ", contributor=" + contributor;
	result += ", total=" + total;
	result += ", remarks=" + remarks;
    result += "]";
    return result;
  }

	/**
	 * @return
	 */
	public IContributor getContributor() {
		return contributor;
	}
	
	/**
	 * @return
	 */
	public Integer getInternalCode() {
		return internalCode;
	}
	
	/**
	 * @return
	 */
	public Integer getKeyContributor() {
		return keyContributor;
	}
	
	/**
	 * @return
	 */
	public Integer getKeyPerson() {
		return keyPerson;
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
	public IPessoa getPerson() {
		return person;
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
	public void setContributor(IContributor contributor) {
		this.contributor = contributor;
	}
	
	/**
	 * @param integer
	 */
	public void setInternalCode(Integer integer) {
		internalCode = integer;
	}
	
	/**
	 * @param integer
	 */
	public void setKeyContributor(Integer integer) {
		keyContributor = integer;
	}
	
	/**
	 * @param integer
	 */
	public void setKeyPerson(Integer integer) {
		keyPerson = integer;
	}
	
	/**
	 * @param integer
	 */
	public void setNumber(Integer integer) {
		number = integer;
	}
	
	/**
	 * @param pessoa
	 */
	public void setPerson(IPessoa pessoa) {
		person = pessoa;
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
