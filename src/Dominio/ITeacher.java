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
public interface ITeacher extends IPessoa{
    public Integer getTeacherNumber();

//	overseenExecutionCourses
	public List getResponsableForExecutionCourses();
	public List getProfessorShipsExecutionCourses();

    public void setTeacherNumber(Integer number);

	public void setResponsableForExecutionCourses(List executionCourses);
	public void setProfessorShipsExecutionCourses(List executionCourses);
}