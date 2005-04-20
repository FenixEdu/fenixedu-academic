/*
 * Created on 27/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * Dominio
 * 
 * @author João Mota 27/Out/2003
 * 
 */
public class Coordinator extends Coordinator_Base {

	private ITeacher teacher;

	/**
	 * @param integer
	 */
	public Coordinator(Integer integer) {
		setIdInternal(integer);
	}

	public Coordinator() {
	}

	/**
	 * @return
	 */
	public ITeacher getTeacher() {
		return teacher;
	}

	/**
	 * @param teacher
	 */
	public void setTeacher(ITeacher teacher) {
		this.teacher = teacher;
	}

}