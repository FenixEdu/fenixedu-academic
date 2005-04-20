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

    private IExecutionDegree executionDegree;

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
    public IExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    /**
     * @param executionDegree
     */
    public void setExecutionDegree(IExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
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