/*
 * TurmaOJB.java
 * 
 * Created on 17 de Outubro de 2002, 18:44
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;

import org.apache.ojb.broker.query.Criteria;

public class TurmaOJB extends PersistentObjectOJB implements ITurmaPersistente {

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(SchoolClass.class, new Criteria());
    }

    public List readByExecutionPeriodAndCurricularYearAndExecutionDegree(Integer executionPeriodOID,
            Integer curricularYear, Integer executionDegreeOID) throws ExcepcaoPersistencia {

        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);
        Criteria crit = new Criteria();
        crit.addEqualTo("executionPeriod.executionYear.year", executionPeriod.getExecutionYear()
                .getYear());
        crit.addEqualTo("executionPeriod.name", executionPeriod.getName());
        crit.addEqualTo("anoCurricular", curricularYear);
        crit.addEqualTo("executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        crit.addEqualTo("executionDegree.degreeCurricularPlan.name", executionDegree
                .getDegreeCurricularPlan().getName());
        crit.addEqualTo("executionDegree.degreeCurricularPlan.degree.sigla", executionDegree
                .getDegreeCurricularPlan().getDegree().getSigla());
        return queryList(SchoolClass.class, crit);

    }

    public ISchoolClass readByNameAndExecutionDegreeAndExecutionPeriod(String className,
            Integer executionDegreeOID, Integer executionPeriodOID) throws ExcepcaoPersistencia {

        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("nome", className);
        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("executionPeriod.executionYear.year", executionPeriod.getExecutionYear()
                .getYear());
        criteria.addEqualTo("executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        criteria.addEqualTo("executionDegree.degreeCurricularPlan.name", executionDegree
                .getDegreeCurricularPlan().getName());
        criteria.addEqualTo("executionDegree.degreeCurricularPlan.degree.sigla", executionDegree
                .getDegreeCurricularPlan().getDegree().getSigla());

        return (ISchoolClass) queryObject(SchoolClass.class, criteria);

    }

    public List readByExecutionPeriod(Integer executionPeriodOID) throws ExcepcaoPersistencia {

        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);

        Criteria crit = new Criteria();
        crit.addEqualTo("executionPeriod.executionYear.year", executionPeriod.getExecutionYear()
                .getYear());
        crit.addEqualTo("executionPeriod.name", executionPeriod.getName());
        return queryList(SchoolClass.class, crit);

    }

    public List readByExecutionDegree(Integer executionDegreeOID) throws ExcepcaoPersistencia {

        IExecutionDegree executionDegree = (IExecutionDegree) readByOID(ExecutionDegree.class,
                executionDegreeOID);

        Criteria crit = new Criteria();
        crit.addEqualTo("executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        crit.addEqualTo("executionDegree.degreeCurricularPlan.name", executionDegree
                .getDegreeCurricularPlan().getName());
        crit.addEqualTo("executionDegree.degreeCurricularPlan.degree.sigla", executionDegree
                .getDegreeCurricularPlan().getDegree().getSigla());
        return queryList(SchoolClass.class, crit);

    }

    public List readByExecutionDegreeAndExecutionPeriod(Integer execucaoOID, Integer executionPeriodOID)
            throws ExcepcaoPersistencia {
        IExecutionDegree execucao = (IExecutionDegree) readByOID(ExecutionDegree.class, execucaoOID);
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.executionYear.year", executionPeriod.getExecutionYear()
                .getYear());
        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("executionDegree.executionYear.year", execucao.getExecutionYear().getYear());
        criteria.addEqualTo("executionDegree.degreeCurricularPlan.name", execucao
                .getDegreeCurricularPlan().getName());
        criteria.addEqualTo("executionDegree.degreeCurricularPlan.degree.sigla", execucao
                .getDegreeCurricularPlan().getDegree().getSigla());
        // Query queryPB = new QueryByCriteria(SchoolClass.class, criteria);
        return queryList(SchoolClass.class, criteria);
    }

    public List readByExecutionDegreeAndDegreeAndExecutionPeriod(Integer execucaoOID, Integer degreeOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia {
        IExecutionDegree execucao = (IExecutionDegree) readByOID(ExecutionDegree.class, execucaoOID);
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodOID);
        IDegree degree = (IDegree) readByOID(Degree.class, degreeOID);
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionDegree.idInternal", execucao.getIdInternal());
        criteria.addEqualTo("executionDegree.degreeCurricularPlan.degree.idInternal", degree
                .getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(SchoolClass.class, criteria);
    }

    public List readByExecutionCourse(Integer executionCourseOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("associatedShifts.disciplinaExecucao.idInternal", executionCourseOID);
        return queryList(SchoolClass.class, criteria, true);
    }

    public List readClassesThatContainsStudentAttendsOnExecutionPeriod(Integer studentOID,
            Integer executionPeriodOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("associatedShifts.disciplinaExecucao.attendingStudents.idInternal",
                studentOID);
        criteria.addEqualTo("associatedShifts.disciplinaExecucao.executionPeriod.idInternal",
                executionPeriodOID);
        return queryList(SchoolClass.class, criteria, true);
    }
}