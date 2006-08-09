package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequentaWithInfoStudentAndPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoMark;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério
 *  
 */
public class InsertEvaluationMarks extends Service {

    public Object run(Integer executionCourseCode, Integer evaluationCode, HashMap hashMarks)
            throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

        Site site = null;
        Evaluation evaluation = null;
        List marksErrorsInvalidMark = null;
        List attendList = null;
        HashMap newHashMarks = new HashMap();

        //Site
    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
        site = executionCourse.getSite();

        //Evaluation
        evaluation = rootDomainObject.readEvaluationByOID(evaluationCode);

        //Attend List
        attendList = executionCourse.getAttends();

        marksErrorsInvalidMark = new ArrayList();
        ListIterator iterAttends = attendList.listIterator();

        while (iterAttends.hasNext()) {
            Attends attend = (Attends) iterAttends.next();

            String mark = (String) hashMarks.get(attend.getAluno().getNumber().toString());
            hashMarks.remove(attend.getAluno().getNumber().toString());

            if (mark != null && mark.length() > 0) {
                if (!isValidMark(evaluation, mark, attend.getAluno())) {
                    InfoMark infoMark = new InfoMark();
                    infoMark.setMark(mark);

                    infoMark.setInfoFrequenta(InfoFrequentaWithInfoStudentAndPerson
                            .newInfoFromDomain(attend));
                    marksErrorsInvalidMark.add(infoMark);
                } else {
                    newHashMarks.put(attend.getAluno().getNumber().toString(), mark);
                    Mark domainMark = evaluation.getMarkByAttend(attend);
                    //verify if the student has already a mark
                    if (domainMark == null) {
                        domainMark = new Mark();
                        domainMark.setAttend(attend);
                        domainMark.setEvaluation(evaluation);
                        domainMark.setMark(mark.toUpperCase());

                    } else {
                        if (!domainMark.getMark().equals(mark)) {
                            domainMark.setMark(mark.toUpperCase());
                        }
                    }

                }
            } else {
                Mark domainMark = evaluation.getMarkByAttend(attend);
                if (domainMark != null) {
                	domainMark.delete();
                }
            }
        }

        return createSiteView(site, evaluation, newHashMarks, marksErrorsInvalidMark, attendList,
                hashMarks);
    }

    private Object createSiteView(Site site, Evaluation evaluation, HashMap hashMarks,
            List marksErrorsInvalidMark, List attendList, HashMap nonExistingStudents)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoSiteMarks infoSiteMarks = new InfoSiteMarks();

        infoSiteMarks.setInfoEvaluation(InfoEvaluation.newInfoFromDomain(evaluation));
        infoSiteMarks.setHashMarks(hashMarks);
        infoSiteMarks.setMarksListErrors(marksErrorsInvalidMark);
        infoSiteMarks.setInfoAttends(attendList);

        List studentsListErrors = new ArrayList();
        Iterator iter = nonExistingStudents.keySet().iterator();
        while (iter.hasNext()) {
            studentsListErrors.add(iter.next());
        }
        infoSiteMarks.setStudentsListErrors(studentsListErrors);

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                infoSiteMarks);

        return siteView;
    }

    private boolean isValidMark(Evaluation evaluation, String mark, Student student) {
        StudentCurricularPlan studentCurricularPlan = null;
        if(student != null) {
        	studentCurricularPlan = student.getActiveStudentCurricularPlan();
        }

        DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

        // test marks by execution course: strategy
        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                .getInstance();
        IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = degreeCurricularPlanStrategyFactory
                .getDegreeCurricularPlanStrategy(degreeCurricularPlan);

        if (mark == null || mark.length() == 0) {
            return true;
        }

        return degreeCurricularPlanStrategy.checkMark(mark, InfoEvaluation.newInfoFromDomain(evaluation)
                .getEvaluationType());

    }
}