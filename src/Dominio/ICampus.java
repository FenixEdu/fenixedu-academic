package Dominio;

import java.util.List;

/**
 * @author Tânia Pousão
 * Create on 10/Nov/2003
 */
public interface ICampus extends IDomainObject {
	public String getName();
	public List getExecutionDegreeList();
	
	public void setName(String name);
	public void setExecutionDegreeList(List executionDegreeList);
}
