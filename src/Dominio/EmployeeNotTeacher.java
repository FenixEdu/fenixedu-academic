/*
 * Created on 1/Mar/2004
 *
 */
package Dominio;

/**
 * @author Tânia Pousão
 *
 */
public class EmployeeNotTeacher extends DomainObject implements IEmployeeNotTeacher
{
	private IEmployee employee;
	private Integer employeeKey;
	/**
	 * @return Returns the employee.
	 */
	public IEmployee getEmployee()
	{
		return employee;
	}

	/**
	 * @param employee The employee to set.
	 */
	public void setEmployee(IEmployee employee)
	{
		this.employee = employee;
	}

	/**
	 * @return Returns the employeeKey.
	 */
	public Integer getEmployeeKey()
	{
		return employeeKey;
	}

	/**
	 * @param employeeKey The employeeKey to set.
	 */
	public void setEmployeeKey(Integer employeeKey)
	{
		this.employeeKey = employeeKey;
	}

}
