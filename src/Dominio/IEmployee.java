/*
 * Created on 2/Out/2003
 */
package Dominio;

import java.util.Date;
import java.util.List;

/**
 * @author Tânia Pousão
 */
public interface IEmployee extends IDomainObject{
	public Integer getEmployeeNumber();
	public Integer getWorkingHours();
	public Date getAntiquity();
	public IPessoa getPerson();
	public EmployeeHistoric getEmployeeHistoric();
	public List getHistoricList();
	public Integer getKeyPerson();
	public void setEmployeeNumber(Integer number);
	public void setWorkingHours(Integer workingHours);
	public void setAntiquity(Date antiquity);	
	public void setPerson(IPessoa person);
	public void setEmployeeHistoric(EmployeeHistoric employeeHistoric);
	public void setHistoricList(List historicList);
	public Boolean getActive();
	public void setActive(Boolean active);
	
	public void fillEmployeeHistoric();
}
