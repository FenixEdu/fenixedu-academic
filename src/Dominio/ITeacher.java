/*
 * ITeacher.java
 */
package Dominio;

import java.util.List;

import Dominio.teacher.ICategory;


/**
 *
 * @author  EP15
 * @author Ivo Brandão
 */
public interface ITeacher extends IDomainObject {
    public Integer getTeacherNumber();

	public IPessoa getPerson();
	public ICategory getCategory();
	

    public void setTeacherNumber(Integer number);
	public void setPerson(IPessoa person);
	public void setCategory(ICategory category);

	/**
	 * @return
	 */
	public List getTeacherPublications();
	public void setTeacherPublications(List teacherPublications);
	
}