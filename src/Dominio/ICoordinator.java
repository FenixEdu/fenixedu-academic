/*
 * Created on 27/Out/2003
 *
 */
package Dominio;

/**
 * fenix-head Dominio
 * 
 * @author João Mota 27/Out/2003
 *  
 */
public interface ICoordinator extends IDomainObject {
    /**
     * @return
     */
    public ICursoExecucao getExecutionDegree();

    /**
     * @param executionCourse
     */
    public void setExecutionDegree(ICursoExecucao executionDegree);

    /**
     * @return
     */
    public Boolean getResponsible();

    /**
     * @param responsible
     */
    public void setResponsible(Boolean responsible);

    /**
     * @return
     */
    public ITeacher getTeacher();

    /**
     * @param teacher
     */
    public void setTeacher(ITeacher teacher);
}