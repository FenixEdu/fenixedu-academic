/**
 * DisciplinaExecucaoOJB.java
 * 
 * Created on 25 de Agosto de 2002, 1:02
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryBySQL;
import org.apache.ojb.odmg.HasBroker;

public class ExecutionCourseOJB extends PersistentObjectOJB implements IPersistentExecutionCourse {

    public ExecutionCourseOJB() {
    }

    
    public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(Integer curricularYear,
            Integer executionPeriodSemestre, String degreeCurricularPlanName, String degreeSigla,
            Integer executionPeriodID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.curricularYear.year",
                curricularYear);
        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.semester",
                executionPeriodSemestre);
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.name",
                degreeCurricularPlanName);
        criteria
                .addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.sigla", degreeSigla);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodID);

        List executionCourseList = queryList(ExecutionCourse.class, criteria, true);
        return executionCourseList;
    }

    
    public List readByCurricularYearAndAllExecutionPeriodAndExecutionDegree(Integer curricularYear,
            Integer executionPeriodID, String degreeCurricularPlanName,
            String degreeSigla) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.curricularYear.year",
                curricularYear);
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.name",
                degreeCurricularPlanName);
        criteria
                .addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.sigla", degreeSigla);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodID);

        List executionCourseList = queryList(ExecutionCourse.class, criteria, true);
        return executionCourseList;
    }

    
    public List readByExecutionPeriodAndExecutionDegree(Integer executionPeriodID,
            String curricularPlanName,
            String degreeSigla) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.name", curricularPlanName);
        criteria
                .addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.sigla", degreeSigla);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodID);

        List executionCourseList = queryList(ExecutionCourse.class, criteria, true);

        return executionCourseList;
    }

    
    public IExecutionCourse readByExecutionCourseInitialsAndExecutionPeriodId(String courseInitials,
            Integer executionPeriodId) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodId);
        criteria.addEqualTo("sigla", courseInitials);
        return (IExecutionCourse) queryObject(ExecutionCourse.class, criteria);
    }

    
    public List readByExecutionPeriod(Integer executionPeriodID, DegreeType curso)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodID);
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.tipoCurso", curso);
        return queryList(ExecutionCourse.class, criteria);
    }


    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
            Integer executionPeriodID, Integer degreeCurricularPlanID,
            Integer curricularYearID, String executionCourseName) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        
        if (executionCourseName != null && !executionCourseName.equals("")) {
            criteria.addLike("nome", executionCourseName);
        }
        
        if (curricularYearID != null) {
            criteria.addEqualTo(
                    "associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
                    curricularYearID);
        }

        if (degreeCurricularPlanID != null) {
            criteria
                    .addEqualTo(
                            "associatedCurricularCourses.scopes.curricularCourse.degreeCurricularPlan.idInternal",
                            degreeCurricularPlanID);
        }
        
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodID);

        List temp = queryList(ExecutionCourse.class, criteria, true);

        return temp;
    }

    
    public List readbyCurricularCourseAndExecutionPeriod(Integer curricularCourseID,
            Integer executionPeriodID) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodID);
        criteria.addEqualTo("associatedCurricularCourses.idInternal", curricularCourseID);

        return queryList(ExecutionCourse.class, criteria);

    }
    
    
    public List readByDegreeCurricularPlanAndExecutionYear(Integer degreeCurricularPlanID,
            Integer executionYearID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.idInternal", degreeCurricularPlanID);
        criteria.addEqualTo("executionPeriod.executionYear.idInternal", executionYearID);
        return queryList(ExecutionCourse.class, criteria);
    }

    
    public List readByExecutionDegreeAndExecutionPeriod(Integer degreeCurricularPlanID,
            Integer executionPeriodID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.idInternal",
                degreeCurricularPlanID);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodID);
        return queryList(ExecutionCourse.class, criteria, true);
    }

    
    public List readByExecutionCourseIds(List<Integer> executionCourseIds) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addIn("idInternal", executionCourseIds);
        return queryList(ExecutionCourse.class, criteria);
    }

    
    public List readByExecutionPeriodWithNoCurricularCourses(Integer executionPeriodID)
            throws ExcepcaoPersistencia {

        DescriptorRepository descriptorRepository = MetadataManager.getInstance().getRepository();
        ClassDescriptor classDescriptor = descriptorRepository.getDescriptorFor(ExecutionCourse.class
                .getName());
        CollectionDescriptor collectionDescriptor = classDescriptor
                .getCollectionDescriptorByName("associatedCurricularCourses");

        StringBuffer sqlStatement = new StringBuffer();
        sqlStatement.append("select ec.* from ").append(classDescriptor.getFullTableName()).append(
                " as ec ").append(" left join ").append(collectionDescriptor.getIndirectionTable())
                .append(" as ccec").append(" on ec.").append(
                        classDescriptor.getFieldDescriptorByName("idInternal").getColumnName()).append(
                        " = ccec.").append(collectionDescriptor.getFksToThisClass()[0]).append(
                        " where ccec.").append(collectionDescriptor.getFksToThisClass()[0]).append(
                        " is null and ec.").append(
                        classDescriptor.getFieldDescriptorByName("keyExecutionPeriod").getColumnName())
                .append(" = ").append(executionPeriodID);

        Query query = new QueryBySQL(ExecutionCourse.class, sqlStatement.toString());
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        List collection = (List) pb.getCollectionByQuery(query);

        lockRead(collection);
        return collection;
    }

    
    public List readByCurricularYearAndExecutionPeriodAndExecutionDegreeList(Integer curricularYear,
            Integer executionPeriodID, Integer executionPeriodSemestre,
            List<IExecutionDegree> executionDegreeList) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        Criteria criteriaExcutionDegreeName = new Criteria();
        Criteria criteriaExcutionDegreeSigla = new Criteria();

        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.curricularYear.year",
                curricularYear);
        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.semester",
                executionPeriodSemestre);
        if ((executionDegreeList != null) && (executionDegreeList.size() != 0)) {
            List executionArrayName = new ArrayList();
            List executionArraySigla = new ArrayList();
            Iterator iterator = executionDegreeList.iterator();
            while (iterator.hasNext()) {

                IExecutionDegree cursoExecucao = (IExecutionDegree) iterator.next();

                executionArrayName.add(cursoExecucao.getDegreeCurricularPlan().getName());
                executionArraySigla.add(cursoExecucao.getDegreeCurricularPlan().getDegree().getSigla());

            }
            criteriaExcutionDegreeName.addIn("associatedCurricularCourses.degreeCurricularPlan.name",
                    executionArrayName);
            criteria.addAndCriteria(criteriaExcutionDegreeName);
            criteriaExcutionDegreeSigla
                    .addIn("associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
                            executionArraySigla);
            criteria.addAndCriteria(criteriaExcutionDegreeName);

        }
        
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodID);

        List executionCourseList = queryList(ExecutionCourse.class, criteria, true);
        return executionCourseList;
    }
}