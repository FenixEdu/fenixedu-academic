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
public class Teacher extends DomainObject implements ITeacher {
    private Integer teacherNumber;
    private List professorShipsExecutionCourses;
    private List responsibleForExecutionCourses;
    private IPessoa person;
    private Integer keyPerson;
    
    /** Creates a new instance of Teacher */
    public Teacher() {
    }
    public Teacher(IPessoa person,Integer teacherNumber){
		setPerson(person);
        setTeacherNumber(teacherNumber);
        setProfessorShipsExecutionCourses(new ArrayList());
        setResponsibleForExecutionCourses(new ArrayList());

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
	public List getResponsibleForExecutionCourses() {
		return responsibleForExecutionCourses;
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
	public void setResponsibleForExecutionCourses(List responsableForExecutionCourses) {
		this.responsibleForExecutionCourses = responsableForExecutionCourses;
	}

	/**
	 * Sets the teacherNumber.
	 * @param teacherNumber The teacherNumber to set
	 */
	public void setTeacherNumber(Integer teacherNumber) {
		this.teacherNumber = teacherNumber;
	}

	/**
	 * @return Integer
	 */
	public Integer getKeyPerson() {
		return keyPerson;
	}

	/**
	 * @return IPessoa
	 */
	public IPessoa getPerson() {
		return person;
	}

	/**
	 * Sets the keyPerson.
	 * @param keyPerson The keyPerson to set
	 */
	public void setKeyPerson(Integer keyPerson) {
		this.keyPerson = keyPerson;
	}

	/**
	 * Sets the person.
	 * @param person The person to set
	 */
	public void setPerson(IPessoa person) {
		this.person = person;
	}

}
