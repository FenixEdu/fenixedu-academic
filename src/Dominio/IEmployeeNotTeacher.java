/*
 * Created on 1/Mar/2004
 *
 */
package Dominio;

/**
 * @author Tânia Pousão
 *
 */
public interface IEmployeeNotTeacher extends IDomainObject
{
	public IEmployee getEmployee();
	public void setEmployee(IEmployee employee);	
}
