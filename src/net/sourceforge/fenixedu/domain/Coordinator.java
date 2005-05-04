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
    private IExecutionDegree executionDegree = null;
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
	
    
    public net.sourceforge.fenixedu.domain.IExecutionDegree getExecutionDegree() {
        return executionDegree;
    }
    
    public void setExecutionDegree(net.sourceforge.fenixedu.domain.IExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

}