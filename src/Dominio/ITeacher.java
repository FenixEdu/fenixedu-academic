/*
 * IDocente.java
 *
 * Created on 26 de Novembro de 2002, 23:02
 */
package Dominio;
/**
 *
 * @author  EP15
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


//
//TODO:FIXME teacher does not own sites neither lectures sites. The teacher is responsible for or lectures  execution courses  
