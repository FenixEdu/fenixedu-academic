package Dominio;

import java.util.List;

import Util.GuideRequester;
import Util.PaymentType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class Guide implements IGuide {
  protected Integer internalCode;
  protected Integer keyPerson;
  protected Integer keyContributor;
  protected Integer keyExecutionDegree;
  
  
  protected Integer number;
  protected Integer year;
  protected Double total;
  protected String remarks;
  
  protected IPessoa person;
  protected IContributor contributor;
  protected ICursoExecucao executionDegree;
  protected PaymentType paymentType;
    
  protected GuideRequester guideRequester;
  
  protected List guideEntries;
  protected List guideSituations;
  

  public Guide() { }
    
  public Guide(Integer number,Integer year,Double total, String remarks,IPessoa person,IContributor contributor,
  			 	GuideRequester guideRequester, ICursoExecucao executionDegree, PaymentType paymentType) {
	this.contributor = contributor;
	this.number = number;
	this.person = person;
	this.remarks = remarks;
	this.total = total;
	this.year = year;
	this.guideRequester = guideRequester;
	this.executionDegree = executionDegree;
	this.paymentType = paymentType;
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
    result += ", contributor=" + contributor;
	result += ", total=" + total;
	result += ", remarks=" + remarks;
	result += ", guide Requester=" + guideRequester;
	result += ", execution Degree=" + executionDegree;
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
	public ICursoExecucao getExecutionDegree() {
		return executionDegree;
	}
	
	/**
	 * @return
	 */
	public List getGuideEntries() {
		return guideEntries;
	}
	
	/**
	 * @return
	 */
	public GuideRequester getGuideRequester() {
		return guideRequester;
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
	public Integer getKeyExecutionDegree() {
		return keyExecutionDegree;
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
	 * @param execucao
	 */
	public void setExecutionDegree(ICursoExecucao execucao) {
		executionDegree = execucao;
	}
	
	/**
	 * @param list
	 */
	public void setGuideEntries(List list) {
		guideEntries = list;
	}
	
	/**
	 * @param requester
	 */
	public void setGuideRequester(GuideRequester requester) {
		guideRequester = requester;
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
	public void setKeyExecutionDegree(Integer integer) {
		keyExecutionDegree = integer;
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
	
	/**
	 * @return
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}
	
	/**
	 * @param type
	 */
	public void setPaymentType(PaymentType type) {
		paymentType = type;
	}

	/**
	 * @return
	 */
	public List getGuideSituations() {
		return guideSituations;
	}
	
	/**
	 * @param list
	 */
	public void setGuideSituations(List list) {
		guideSituations = list;
	}

}
