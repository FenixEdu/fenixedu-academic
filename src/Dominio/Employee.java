/*
 * Created on 2/Out/2003
 */
package Dominio;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author  Tânia Pousão
 */
public class Employee extends DomainObject implements IEmployee {

	private IPessoa person = null;
	private Integer keyPerson = null;

	private Integer employeeNumber = null;
	private Integer workingHours = null;
	private Date antiquity = null;

	private EmployeeHistoric employeeHistoric = null;
	private List historicList = null;
	
	private Boolean active;

	public Employee() {
	}

	public Employee(Integer idInternal) {
		setIdInternal(idInternal);
	}

	public Employee(IPessoa person, Integer employeeNumber) {
		setPerson(person);
		setEmployeeNumber(employeeNumber);
	}

	public Employee(IPessoa person, Integer employeeNumber, Integer workingHours, Date antiquity) {
		setPerson(person);
		setEmployeeNumber(employeeNumber);
		setWorkingHours(workingHours);
		setAntiquity(antiquity);
	}

	public Integer getEmployeeNumber() {
		return employeeNumber;
	}
	public Integer getWorkingHours() {
		return workingHours;
	}
	public Date getAntiquity() {
		return antiquity;
	}
	public Integer getKeyPerson() {
		return keyPerson;
	}
	public IPessoa getPerson() {
		return person;
	}

	public void setEmployeeNumber(Integer number) {
		employeeNumber = number;
	}
	public void setWorkingHours(Integer workingHours) {
		this.workingHours = workingHours;
	}
	public void setAntiquity(Date antiquity) {
		this.antiquity = antiquity;
	}
	public void setKeyPerson(Integer keyPerson) {
		this.keyPerson = keyPerson;
	}
	public void setPerson(IPessoa person) {
		this.person = person;
	}

	public EmployeeHistoric getEmployeeHistoric() {
		return employeeHistoric;
	}
	public void setEmployeeHistoric(EmployeeHistoric employeeHistoric) {
		this.employeeHistoric = employeeHistoric;
	}

	public List getHistoricList() {
		return historicList;
	}
	public void setHistoricList(List historicList) {
		this.historicList = historicList;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IEmployee) {
			resultado = getEmployeeNumber().equals(((IEmployee) obj).getEmployeeNumber());
		}
		return resultado;
	}

	public String toString() {
		String result = "[Dominio.Employee ";
		result += ", employeeNumber=" + getEmployeeNumber();
		result += ", person=" + getPerson();
		result += "]";
		return result;
	}

	public void fillEmployeeHistoric() {
		if (getHistoricList() != null) {
			ListIterator iterator = getHistoricList().listIterator();
			EmployeeHistoric historic = null;
			
			setEmployeeHistoric(new EmployeeHistoric());
			while (iterator.hasNext()) {
				historic = (EmployeeHistoric) iterator.next();

				if (historic.getKeyResponsableEmployee() != null) {
					employeeHistoric.setKeyResponsableEmployee(historic.getKeyResponsableEmployee());
					employeeHistoric.setResponsableEmployee(historic.getResponsableEmployee());
				}
				if (historic.getKeyMailingCostCenter() != null) {
					employeeHistoric.setKeyMailingCostCenter(historic.getKeyMailingCostCenter());
					employeeHistoric.setMailingCostCenter(historic.getMailingCostCenter());
				}
				if (historic.getKeyWorkingPlaceCostCenter() != null) {
					employeeHistoric.setKeyWorkingPlaceCostCenter(historic.getKeyWorkingPlaceCostCenter());
					employeeHistoric.setWorkingPlaceCostCenter(historic.getWorkingPlaceCostCenter());
				}
				if (historic.getKeySalaryCostCenter() != null) {
					employeeHistoric.setKeySalaryCostCenter(historic.getKeySalaryCostCenter());
					employeeHistoric.setSalaryCostCenter(historic.getSalaryCostCenter());
				}
				if (historic.getCalendar() != null) {
					employeeHistoric.setCalendar(historic.getCalendar());
				}
				if (historic.getKeyStatus() != null) {
					employeeHistoric.setKeyStatus(historic.getKeyStatus());
					employeeHistoric.setStatus(historic.getStatus());
				}
			}
		}
	}
    /**
     * @return Returns the active.
     */
    public Boolean getActive()
    {
        return this.active;
    }
    /**
     * @param active The active to set.
     */
    public void setActive(Boolean active)
    {
        this.active = active;
    }
}
