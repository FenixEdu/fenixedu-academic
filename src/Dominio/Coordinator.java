/*
 * Created on 27/Out/2003
 *
 */
package Dominio;

/**
 * Dominio
 * 
 * @author João Mota 27/Out/2003
 *  
 */
public class Coordinator extends DomainObject implements ICoordinator {

    private ITeacher teacher;

    private ICursoExecucao executionDegree;

    private Boolean responsible;

    private Integer keyTeacher;

    private Integer keyExecutionDegree;

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
    public ICursoExecucao getExecutionDegree() {
        return executionDegree;
    }

    /**
     * @param executionDegree
     */
    public void setExecutionDegree(ICursoExecucao executionDegree) {
        this.executionDegree = executionDegree;
    }

    /**
     * @return
     */
    public Integer getKeyExecutionDegree() {
        return keyExecutionDegree;
    }

    /**
     * @param keyExecutionCourse
     */
    public void setKeyExecutionDegree(Integer keyExecutionDegree) {
        this.keyExecutionDegree = keyExecutionDegree;
    }

    /**
     * @return
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }

    /**
     * @param keyTeacher
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @return
     */
    public Boolean getResponsible() {
        return responsible;
    }

    /**
     * @param responsible
     */
    public void setResponsible(Boolean responsible) {
        this.responsible = responsible;
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