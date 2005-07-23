package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequentaWithInfoStudentAndPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSubmitMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.Ftp;
import net.sourceforge.fenixedu.util.middleware.CreateFile;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class SubmitMarks implements IService {

    public Object run(Integer executionCourseCode, Integer evaluationCode, Date evaluationDate,
            IUserView userView) throws FenixServiceException {
		MultiHashMap enrolmentEvaluationTableByDegree = new MultiHashMap();
		MultiHashMap improvmentEnrolmentEvaluationTableByDegree = new MultiHashMap();
		List notEnrolled = new ArrayList();
		List postGraduate = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
            IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();
			IPersistentEmployee persistentEmployee = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentEmployee();
            
            //execution course and execution course's site
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);

			ISite site = executionCourse.getSite();

            //evaluation
			IEvaluation evaluation = (IEvaluation) persistentEvaluation.readByOID(Evaluation.class,evaluationCode);

            //attend list
            List attendList = executionCourse.getAttends();

            List professors = executionCourse.responsibleFors();

            //employee logged
            IPerson pessoa = pessoaPersistente.lerPessoaPorUsername(userView.getUtilizador());

			IEmployee employee = persistentEmployee.readByPerson(pessoa.getIdInternal().intValue());
			
            ITeacher teacher = ((IProfessorship) professors.get(0)).getTeacher();

            //Separate improvments/normal/not enrolled/postGraduate attends
            Integer nSubmitted = separateAttends(executionCourse, attendList, evaluation, evaluationDate, employee, teacher,
					enrolmentEvaluationTableByDegree, improvmentEnrolmentEvaluationTableByDegree,
					notEnrolled, postGraduate);
            
            List fileList = submitMarksAndCreateFiles(enrolmentEvaluationTableByDegree);
            Ftp.enviarFicheiros("/DegreeGradesFtpServerConfig.properties", fileList, "notas/");
            
            List improvmentFileList = submitMarksAndCreateFiles(improvmentEnrolmentEvaluationTableByDegree);

            //Send the files via FTP
            Ftp.enviarFicheiros("/DegreeGradesFtpServerConfig.properties", improvmentFileList, "melhorias/");

            return createSiteView(site, evaluation, nSubmitted, notEnrolled, postGraduate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private Integer separateAttends(
            IExecutionCourse executionCourse, 
            List attendList, IEvaluation evaluation,
            Date evaluationDate, IEmployee employee, ITeacher teacher,
            MultiHashMap enrolmentEvaluationTableByDegree,
            MultiHashMap improvmentEnrolmentEvaluationTableByDegree,
            List notEnrolled,
            List postGraduate) throws FenixServiceException {

        try {
            int submitted = 0;
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentMark persistentMark = sp.getIPersistentMark();
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = sp
                    .getIPersistentEnrolmentEvaluation();
            Iterator iter = attendList.iterator();

            verifyAlreadySubmittedMarks(attendList, executionCourse.getExecutionPeriod(), enrolmentEvaluationDAO);

            List markList = persistentMark.readBy(evaluation);

            //Check if there is any mark. If not, we can not submit
            if (markList.isEmpty()) {
                throw new FenixServiceException("errors.submitMarks.noMarks");
            }

            while (iter.hasNext()) {
                IAttends attend = (IAttends) iter.next();
                IEnrolment enrolment = attend.getEnrolment();
                IEnrolmentEvaluation enrolmentEvaluation = null;

                //check student´s degree type
                if (attend.getAluno().getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
                    
                    postGraduate.add(InfoFrequentaWithInfoStudentAndPerson.newInfoFromDomain(attend));
                    continue;
                }

                //check if this student is enrolled
                if (enrolment == null) {
                    notEnrolled.add(InfoFrequentaWithInfoStudentAndPerson.newInfoFromDomain(attend));
                    continue;
                }
                
				String observation = "Submissão da Pauta";
                IMark mark = getMark(evaluation, markList, attend);
                if(enrolment.isImprovementForExecutionCourse(executionCourse)) {
					enrolmentEvaluation = enrolment.submitEnrolmentEvaluation(EnrolmentEvaluationType.IMPROVEMENT, mark, employee, teacher.getPerson(), evaluationDate,observation);
                    improvmentEnrolmentEvaluationTableByDegree.put(enrolment.getStudentCurricularPlan().getDegreeCurricularPlan().getDegree().getIdInternal(), enrolmentEvaluation);
                } else {
					enrolmentEvaluation = enrolment.submitEnrolmentEvaluation(EnrolmentEvaluationType.NORMAL, mark, employee, teacher.getPerson(), evaluationDate,observation);
                    enrolmentEvaluationTableByDegree.put(enrolment.getStudentCurricularPlan().getDegreeCurricularPlan().getDegree().getIdInternal(), enrolmentEvaluation);
                }
                submitted++;
            }
            
            return new Integer(submitted);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private void verifyAlreadySubmittedMarks(List attendList, final IExecutionPeriod executionPeriod,
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO) throws ExcepcaoPersistencia,
            FenixServiceException {
        List enrolmentListIds = (List) CollectionUtils.collect(attendList, new Transformer() {

            public Object transform(Object input) {
                IAttends attend = (IAttends) input;
                IEnrolment enrolment = attend.getEnrolment();
                if(enrolment != null) {
                    if(enrolment.getExecutionPeriod().equals(executionPeriod))
                        return enrolment.getIdInternal();
                }
                return null;
            }
        });

        enrolmentListIds = (List) CollectionUtils.select(enrolmentListIds, new Predicate() {
            public boolean evaluate(Object arg0) {
                return arg0 != null;
            }
        });
        List alreadySubmiteMarks = new ArrayList();
        if(!enrolmentListIds.isEmpty()) {
            alreadySubmiteMarks = enrolmentEvaluationDAO.readAlreadySubmitedMarks(enrolmentListIds);
        }

        if (!alreadySubmiteMarks.isEmpty()) {
            throw new FenixServiceException("errors.submitMarks.yetSubmited");
        }
    }

    private IMark getMark(final IEvaluation evaluation, List markList, final IAttends attend) {

		return (IMark)CollectionUtils.find(markList,new Predicate() {

			public boolean evaluate(Object o) {
				IMark mark = (IMark)o;
				return mark.getAttend().equals(attend) &&
						mark.getEvaluation().equals(evaluation);
			}
		});
    }

    private List submitMarksAndCreateFiles(MultiHashMap enrolmentEvaluationTableByDegree)
            throws FenixServiceException {
        Set degrees = enrolmentEvaluationTableByDegree.keySet();
        Iterator iter = degrees.iterator();
        List fileList = new ArrayList();
        try {
            //degrees
            while (iter.hasNext()) {
                List enrolmentEvaluationsByDegree = (List) enrolmentEvaluationTableByDegree.get(iter
                        .next());
                File file = CreateFile.buildFile(enrolmentEvaluationsByDegree);
                if (file != null) {
                    fileList.add(file);
                }
            }
            return fileList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private Object createSiteView(ISite site, IEvaluation evaluation, Integer submited,
            List notEnrolledList, List mestradoList) throws FenixServiceException {

        InfoSiteSubmitMarks infoSiteSubmitMarks = new InfoSiteSubmitMarks();

        infoSiteSubmitMarks.setInfoEvaluation(InfoEvaluation.newInfoFromDomain(evaluation));
        infoSiteSubmitMarks.setSubmited(submited);

        // order errorsNotEnrolmented list by student's number
        if (notEnrolledList != null) {
            Collections.sort(notEnrolledList, new BeanComparator("aluno.number"));
            infoSiteSubmitMarks.setNotEnrolmented(notEnrolledList);
        }

        // order errorsMarkNotPublished list by student's number
        if (mestradoList != null) {
            Collections.sort(mestradoList, new BeanComparator("aluno.number"));
            infoSiteSubmitMarks.setMestrado(mestradoList);
        }

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                infoSiteSubmitMarks);
        return siteView;
    }
}