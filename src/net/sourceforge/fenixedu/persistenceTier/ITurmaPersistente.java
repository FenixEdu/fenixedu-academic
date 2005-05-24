/*
 * ITurmaPersistente.java
 *
 * Created on 17 de Outubro de 2002, 18:38
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IStudent;

public interface ITurmaPersistente extends IPersistentObject {

    public void delete(ISchoolClass turma) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readByExecutionPeriodAndCurricularYearAndExecutionDegree(
            IExecutionPeriod executionPeriod, Integer curricularYear, IExecutionDegree executionDegree)
            throws ExcepcaoPersistencia;

    public List readByExecutionDegreeAndDegreeAndExecutionPeriod(IExecutionDegree execucao, IDegree degree,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    /**
     * 
     * @param executionPeriod
     * @return ArrayList
     * @throws ExcepcaoPersistencia
     */
    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    /**
     * Method readByNameAndExecutionDegreeAndExecutionPeriod.
     * 
     * @param className
     * @param executionDegree
     * @param executionPeriod
     * @return ISchoolClass
     */
    public ISchoolClass readByNameAndExecutionDegreeAndExecutionPeriod(String className,
            IExecutionDegree executionDegree, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public List readByExecutionDegree(IExecutionDegree executionDegree) throws ExcepcaoPersistencia;

    public List readByExecutionDegreeAndExecutionPeriod(IExecutionDegree execucao,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    /**
     * @param executionCourse
     * @return
     */
    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    /**
     * @param student
     * @param executionPeriod
     * @return
     */
    public List readClassesThatContainsStudentAttendsOnExecutionPeriod(IStudent student,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;
}