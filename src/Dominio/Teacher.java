/*
 * Teacher.java
 *
 */
package Dominio;
/**
 *
 * @author  EP15
 * @author Ivo Brandão
 */
import java.util.ArrayList;
import java.util.List;
public class Teacher extends Pessoa implements ITeacher {
    private Integer teacherNumber;
    private List professorShipsExecutionCourses;
    private List responsableForExecutionCourses;
    /** Creates a new instance of Teacher */
    public Teacher() {
    }
    public Teacher(String user, String password, Integer teacherNumber){
		super.setUsername(user);
		super.setPassword(password);
        setTeacherNumber(teacherNumber);
        setProfessorShipsExecutionCourses(new ArrayList());
        setResponsableForExecutionCourses(new ArrayList());

    }
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ITeacher) {
            resultado = getTeacherNumber().equals(((ITeacher) obj).getTeacherNumber());
        }
        return resultado;
    }
	/**
	 * @return List
	 */
	public List getProfessorShipsExecutionCourses() {
		return professorShipsExecutionCourses;
	}

	/**
	 * @return List
	 */
	public List getResponsableForExecutionCourses() {
		return responsableForExecutionCourses;
	}

	/**
	 * @return Integer
	 */
	public Integer getTeacherNumber() {
		return teacherNumber;
	}

	/**
	 * Sets the professorShipsExecutionCourses.
	 * @param professorShipsExecutionCourses The professorShipsExecutionCourses to set
	 */
	public void setProfessorShipsExecutionCourses(List professorShipsExecutionCourses) {
		this.professorShipsExecutionCourses = professorShipsExecutionCourses;
	}

	/**
	 * Sets the responsableForExecutionCourses.
	 * @param responsableForExecutionCourses The responsableForExecutionCourses to set
	 */
	public void setResponsableForExecutionCourses(List responsableForExecutionCourses) {
		this.responsableForExecutionCourses = responsableForExecutionCourses;
	}

	/**
	 * Sets the teacherNumber.
	 * @param teacherNumber The teacherNumber to set
	 */
	public void setTeacherNumber(Integer teacherNumber) {
		this.teacherNumber = teacherNumber;
	}

}
