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
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.util.TipoCurso;
import net.sourceforge.fenixedu.util.classProperties.ExecutionCoursePropertyName;

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

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(ExecutionCourse.class, new Criteria());
    }

    public IExecutionCourse readBySiglaAndAnoLectivoAndSiglaLicenciatura(String sigla,
            String anoLectivo, String siglaLicenciatura) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("sigla", sigla);
        crit.addEqualTo("executionPeriod.executionYear.year", anoLectivo);
        crit.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
                siglaLicenciatura);
        return (IExecutionCourse) queryObject(ExecutionCourse.class, crit);

    }

    /**
     * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByCurricularYearAndExecutionPeriodAndExecutionDegree(java.lang.Integer,
     *      Dominio.IExecutionPeriod, java.lang.String)
     */
    public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(Integer curricularYear,
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.curricularYear.year",
                curricularYear);
        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.semester",
                executionPeriod.getSemester());
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.name", executionDegree
                .getDegreeCurricularPlan().getName());
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
                executionDegree.getDegreeCurricularPlan().getDegree().getSigla());
        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());

        List executionCourseList = queryList(ExecutionCourse.class, criteria, true);
        return executionCourseList;
    }

    /**/
    public List readByCurricularYearAndAllExecutionPeriodAndExecutionDegree(Integer curricularYear,
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.curricularYear.year",
                curricularYear);
        /*
         * criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.semester",
         * executionPeriod.getSemester());
         */
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.name", executionDegree
                .getDegreeCurricularPlan().getName());
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
                executionDegree.getDegreeCurricularPlan().getDegree().getSigla());
        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());

        List executionCourseList = queryList(ExecutionCourse.class, criteria, true);
        return executionCourseList;
    }

    /**
     * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionPeriodAndExecutionDegree(java.lang.Integer,
     *      Dominio.IExecutionPeriod, java.lang.String)
     */
    public List readByExecutionPeriodAndExecutionDegree(IExecutionPeriod executionPeriod,
            IExecutionDegree executionDegree) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.semester",
                executionPeriod.getSemester());
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.name", executionDegree
                .getDegreeCurricularPlan().getName());
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
                executionDegree.getDegreeCurricularPlan().getDegree().getSigla());
        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());

        List executionCourseList = queryList(ExecutionCourse.class, criteria, true);

        return executionCourseList;
    }

    /**
     * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionCourseInitials(java.lang.String)
     */
    public IExecutionCourse readByExecutionCourseInitialsAndExecutionPeriod(String courseInitials,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
        criteria.addEqualTo("executionPeriod.executionYear.year", executionPeriod.getExecutionYear()
                .getYear());
        criteria.addEqualTo("sigla", courseInitials);
        return (IExecutionCourse) queryObject(ExecutionCourse.class, criteria);
    }

    public IExecutionCourse readByExecutionCourseInitialsAndExecutionPeriodId(String courseInitials,
            Integer executionPeriodId) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyExecutionPeriod", executionPeriodId);
        criteria.addEqualTo("sigla", courseInitials);
        return (IExecutionCourse) queryObject(ExecutionCourse.class, criteria);
    }

    public void deleteExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {

        Criteria crit1 = new Criteria();
        crit1.addEqualTo("executionPeriod.name", executionCourse.getExecutionPeriod().getName());
        crit1.addEqualTo("executionPeriod.executionYear.year", executionCourse.getExecutionPeriod()
                .getExecutionYear().getYear());
        crit1.addEqualTo("sigla", executionCourse.getSigla());
        List result = queryList(ExecutionCourse.class, crit1);

        if (result != null && !result.isEmpty()) {
            IExecutionCourse executionCourseTemp = (IExecutionCourse) result.get(0);
            super.delete(executionCourseTemp);
        }

    }

    public List readByExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(ExecutionCourse.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionPeriod(Dominio.IExecutionPeriod,
     *      Util.TipoCurso)
     */
    public List readByExecutionPeriod(IExecutionPeriod executionPeriod, TipoCurso curso)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.tipoCurso", curso);
        return queryList(ExecutionCourse.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IDisciplinaExecucaoPersistente#readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(Dominio.IExecutionPeriod,
     *      Dominio.IExecutionDegree, Dominio.ICurricularYear, java.lang.String)
     */
    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree,
            ICurricularYear curricularYear, String executionCourseName) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        if (executionCourseName != null && !executionCourseName.equals("")) {
            criteria.addLike("nome", executionCourseName);
        }
        if (curricularYear != null) {
            criteria.addEqualTo(
                    "associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
                    curricularYear.getIdInternal());
        }

        if (executionDegree != null) {
            criteria
                    .addEqualTo(
                            "associatedCurricularCourses.scopes.curricularCourse.degreeCurricularPlan.idInternal",
                            executionDegree.getDegreeCurricularPlan().getIdInternal());
        }
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
        criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.semester",
                executionPeriod.getSemester());

        List temp = queryList(ExecutionCourse.class, criteria, true);

        return temp;
        //		return queryList(DisciplinaExecucao.class, criteria, true);
    }

    //	returns a list of teachers in charge ids
    public List readExecutionCourseTeachers(Integer executionCourseId) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyExecutionCourse", executionCourseId);

        return queryList(Professorship.class, criteria);
    }

    public Boolean readSite(Integer executionCourseId) throws ExcepcaoPersistencia {
        Boolean result = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourseId);

        ISite site = (ISite) queryObject(Site.class, criteria);
        if (site == null)
            result = new Boolean(false);
        else
            result = new Boolean(true);
        return result;
    }

    public List readbyCurricularCourseAndExecutionPeriod(ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
        criteria.addEqualTo("associatedCurricularCourses.idInternal", curricularCourse.getIdInternal());

        return queryList(ExecutionCourse.class, criteria);

    }

    public List readListbyCurricularCourseAndExecutionPeriod(ICurricularCourse curricularCourse,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
        criteria.addEqualTo("associatedCurricularCourses.idInternal", curricularCourse.getIdInternal());

        return queryList(ExecutionCourse.class, criteria);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionCourse#readByExecutionDegree(Dominio.ExecutionDegree)
     */
    public List readByDegreeCurricularPlanAndExecutionYear(IDegreeCurricularPlan degreeCurricularPlan,
            IExecutionYear executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlanKey", degreeCurricularPlan
                .getIdInternal());
        criteria.addEqualTo("executionPeriod.keyExecutionYear", executionYear.getIdInternal());
        return queryList(ExecutionCourse.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionCourse#readByExecutionDegreeAndExecutionPeriod(Dominio.IExecutionDegree,
     *      Dominio.IExecutionPeriod)
     */
    public List readByExecutionDegreeAndExecutionPeriod(IExecutionDegree executionDegree,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.idInternal",
                executionDegree.getDegreeCurricularPlan().getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return queryList(ExecutionCourse.class, criteria, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionCourse#readByExecutionCourseIds(java.util.List)
     */
    public List readByExecutionCourseIds(List executionCourseIds) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addIn("idInternal", executionCourseIds);
        return queryList(ExecutionCourse.class, criteria);
    }

    /*
     * @author Fernanda Quiterio @author jpvl
     *  
     */
    public List readByExecutionPeriodWithNoCurricularCourses(IExecutionPeriod executionPeriod)
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
                .append(" = ").append(executionPeriod.getIdInternal());

        Query query = new QueryBySQL(ExecutionCourse.class, sqlStatement.toString());
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        List collection = (List) pb.getCollectionByQuery(query);

        lockRead(collection);
        return collection;
    }

    public IExecutionCourse readbyCurricularCourseAndExecutionPeriodAndExecutionCoursePropertyName(
            ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod,
            ExecutionCoursePropertyName executionCoursePropertyName) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionPeriod", executionPeriod.getIdInternal());
        criteria.addEqualTo("associatedCurricularCourses.idInternal", curricularCourse.getIdInternal());
        criteria.addEqualTo("executionCourseProperties.name", executionCoursePropertyName);
        return (IExecutionCourse) queryObject(ExecutionCourse.class, criteria);
    }
    
	public List readByCurricularYearAndExecutionPeriodAndExecutionDegreeList(Integer curricularYear,
				IExecutionPeriod executionPeriod, List executionDegreeList)
				throws ExcepcaoPersistencia {
			Criteria criteria = new Criteria();
			Criteria criteriaExcutionDegreeName = new Criteria();
			Criteria criteriaExcutionDegreeSigla = new Criteria();
		
			criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.curricularYear.year",
					curricularYear);
			criteria.addEqualTo("associatedCurricularCourses.scopes.curricularSemester.semester",
					executionPeriod.getSemester());
			if ((executionDegreeList != null) && (executionDegreeList.size() != 0)) {
			   List executionArrayName = new ArrayList();
			   List executionArraySigla = new ArrayList();
			   Iterator iterator = executionDegreeList.iterator();
			   while (iterator.hasNext()) {

				IExecutionDegree cursoExecucao = (IExecutionDegree)iterator.next();

				executionArrayName.add(cursoExecucao.getDegreeCurricularPlan().getName());
				executionArraySigla.add(cursoExecucao.getDegreeCurricularPlan().getDegree().getSigla() );
			
			   }
			   criteriaExcutionDegreeName.addIn("associatedCurricularCourses.degreeCurricularPlan.name", executionArrayName);
			   criteria.addAndCriteria(criteriaExcutionDegreeName);
			   criteriaExcutionDegreeSigla.addIn("associatedCurricularCourses.degreeCurricularPlan.degree.sigla", executionArraySigla);
			   criteria.addAndCriteria(criteriaExcutionDegreeName);
			   
		   }
					
//					
//			criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.name", executionDegree
//					.getDegreeCurricularPlan().getName());
//			criteria.addEqualTo("associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
//					executionDegree.getDegreeCurricularPlan().getDegree().getSigla());
			criteria.addEqualTo("executionPeriod.name", executionPeriod.getName());
			criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());

			List executionCourseList = queryList(ExecutionCourse.class, criteria, true);
			return executionCourseList;
		}
/*	if ((situations != null) && (situations.size() != 0)) {
			   List situationsInteger = new ArrayList();
			   Iterator iterator = situations.iterator();
			   while (iterator.hasNext()) {
				   situationsInteger.add(((SituationName) iterator.next()).getSituationName());

			   }
			   criteriaSituations.addIn("situation", situationsInteger);
			   criteria.addAndCriteria(criteriaSituations);
		   }*/


}