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

import net.sourceforge.fenixedu.domain.ISchoolClass;

public interface ITurmaPersistente extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public List readByExecutionPeriodAndCurricularYearAndExecutionDegree(Integer executionPeriodOID,
            Integer curricularYear, Integer executionDegreeOID) throws ExcepcaoPersistencia;

    public List readByExecutionDegreeAndDegreeAndExecutionPeriod(Integer execucaoOID, Integer degreeOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia;

    public List readByExecutionPeriod(Integer executionPeriodOID) throws ExcepcaoPersistencia;

    public ISchoolClass readByNameAndExecutionDegreeAndExecutionPeriod(String className,
            Integer executionDegreeOID, Integer executionPeriodOID) throws ExcepcaoPersistencia;

    public List readByExecutionDegree(Integer executionDegreeOID) throws ExcepcaoPersistencia;

    public List readByExecutionDegreeAndExecutionPeriod(Integer execucaoOID, Integer executionPeriodOID)
            throws ExcepcaoPersistencia;

    public List readByExecutionCourse(Integer executionCourseOID) throws ExcepcaoPersistencia;

    public List readClassesThatContainsStudentAttendsOnExecutionPeriod(Integer studentOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia;
}