/*
 * ITeacher.java
 */
package Dominio;
/**
 *
 * @author  EP15
 * @author Ivo Brandão
 */
import java.util.List;
public interface ITeacher {
    public Integer getTeacherNumber();

	public IPessoa getPerson();
	public List getResponsibleForExecutionCourses();
	public List getProfessorShipsExecutionCourses();

    public void setTeacherNumber(Integer number);
	public void setPerson(IPessoa person);
	public void setResponsibleForExecutionCourses(List executionCourses);
	public void setProfessorShipsExecutionCourses(List executionCourses);
}