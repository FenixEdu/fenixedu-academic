/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.degree.execution.ReadExecutionDegreesByExecutionYearAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 * 
 * This class read and prepare all information usefull for shift enrollment use
 * case
 *  
 */
public class PrepareDegreesListByStudentNumber implements IService {
    public PrepareDegreesListByStudentNumber() {

    }

    public Object run(InfoStudent infoStudent, TipoCurso degreeType, Integer executionDegreeId,
            Integer executionPeriodID)
    //Integer studentNumber, Integer executionDegreeIdChosen)
            throws FenixServiceException {
        List result = null;
        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(ExecutionPeriod.class, executionPeriodID);
            InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod);
            
            //read execution degrees by execution year and degree type
            ReadExecutionDegreesByExecutionYearAndDegreeType service = new ReadExecutionDegreesByExecutionYearAndDegreeType();
            List infoExecutionsDegreesList = service.run(infoExecutionPeriod.getInfoExecutionYear(), degreeType);
            if (infoExecutionsDegreesList == null || infoExecutionsDegreesList.size() <= 0) {
                throw new FenixServiceException("errors.impossible.operation");
            }

            //read student to enroll
            if (infoStudent == null || infoStudent.getNumber() == null) {
                throw new FenixServiceException("errors.impossible.operation");
            }

            
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = persistentStudent.readStudentByNumberAndDegreeType(infoStudent
                    .getNumber(), degreeType);
            if (student == null) {
                throw new FenixServiceException("errors.impossible.operation");
            }

            //select the first execution degree or the execution degree of the
            // student logged
            InfoExecutionDegree infoExecutionDegree = selectExecutionDegree(sp,
                    infoExecutionsDegreesList, executionDegreeId, student, degreeType);

            //it is return a list where the first element is the degree
            // pre-select and the tail is all degrees
            result = new ArrayList();
            result.add(infoExecutionDegree);
            result.addAll(infoExecutionsDegreesList);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            excepcaoPersistencia.printStackTrace();
            throw new FenixServiceException();
        } catch (FenixServiceException serviceException) {
            serviceException.printStackTrace();
            throw new FenixServiceException(serviceException.getMessage());
        }

        return result;
    }

    private InfoExecutionDegree selectExecutionDegree(ISuportePersistente sp,
            List infoExecutionDegreeList, Integer executionDegreeIdChosen, IStudent student,
            TipoCurso degreeType) throws ExcepcaoPersistencia {
        InfoExecutionDegree infoExecutionDegree = null;

        //read the execution degree chosen
        if (executionDegreeIdChosen != null) {
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();

            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, executionDegreeIdChosen);
            if (executionDegree != null) {
                return (InfoExecutionDegree) Cloner.get(executionDegree);
            }
        }

        //read the execution degree belongs to student
        IPersistentStudentCurricularPlan persistentCurricularPlan = sp
                .getIStudentCurricularPlanPersistente();
        IStudentCurricularPlan studentCurricularPlan = persistentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(student.getNumber(), degreeType);
        //execution degree isn't find, then it is chosen the list's first
        if (studentCurricularPlan == null || studentCurricularPlan.getDegreeCurricularPlan() == null
                || studentCurricularPlan.getDegreeCurricularPlan().getDegree() == null
                || studentCurricularPlan.getDegreeCurricularPlan().getDegree().getNome() == null) {
            return (InfoExecutionDegree) infoExecutionDegreeList.get(0);
        }
        
        final Integer degreeCurricularPlanCode = studentCurricularPlan.getDegreeCurricularPlan().getIdInternal();
        
        List infoExecutionDegreeListWithDegreeCode = (List) CollectionUtils.select(
                infoExecutionDegreeList, new Predicate() {
                    public boolean evaluate(Object input) {
                        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
                        return infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanCode);
                    }
                });
        if (!infoExecutionDegreeListWithDegreeCode.isEmpty()) {
            infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeListWithDegreeCode.get(0);
        } else {
            return (InfoExecutionDegree) infoExecutionDegreeList.get(0);
        }
        return infoExecutionDegree;
    }
}