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
public class Teacher extends DomainObject implements ITeacher {
	private Integer teacherNumber;
	private IPessoa person;
	private Integer keyPerson;

	/** Creates a new instance of Teacher */
	public Teacher() {
	}
	public Teacher(IPessoa person, Integer teacherNumber) {
		setPerson(person);
		setTeacherNumber(teacherNumber);
		
	}
	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof ITeacher) {
			resultado =
				getTeacherNumber().equals(((ITeacher) obj).getTeacherNumber());
		}
		return resultado;
	}
	
	/**
	 * @return Integer
	 */
	public Integer getTeacherNumber() {
		return teacherNumber;
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
	public String toString() {
		String result = "[Dominio.Teacher ";
		result += ", teacherNumber=" + getTeacherNumber();
		result += ", person=" + getPerson();
		result += "]";
		return result;
	}
}
