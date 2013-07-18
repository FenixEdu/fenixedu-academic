package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequentaWithInfoStudentAndPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoMarkEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Fernanda Quitério
 * 
 */
public class InsertEvaluationMarks {

    protected TeacherAdministrationSiteView run(Integer executionCourseCode, Integer evaluationCode, HashMap hashMarks)
            throws ExcepcaoInexistente, FenixServiceException {

        ExecutionCourseSite site = null;
        Evaluation evaluation = null;
        List<InfoMarkEditor> marksErrorsInvalidMark = null;
        List attendList = null;
        HashMap<String, String> newHashMarks = new HashMap<String, String>();

        // Site
        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseCode);
        site = executionCourse.getSite();

        // Evaluation
        evaluation = RootDomainObject.getInstance().readEvaluationByOID(evaluationCode);

        // Attend List
        attendList = executionCourse.getAttends();

        marksErrorsInvalidMark = new ArrayList<InfoMarkEditor>();
        ListIterator iterAttends = attendList.listIterator();

        while (iterAttends.hasNext()) {
            Attends attend = (Attends) iterAttends.next();

            String mark = (String) hashMarks.get(attend.getRegistration().getNumber().toString());
            mark = mark.replace(",", ".");
            hashMarks.remove(attend.getRegistration().getNumber().toString());

            if (mark != null && mark.length() > 0) {
                if (!isValidMark(evaluation, mark, attend.getRegistration())) {
                    InfoMarkEditor infoMark = new InfoMarkEditor();
                    infoMark.setMark(mark);

                    infoMark.setInfoFrequenta(InfoFrequentaWithInfoStudentAndPerson.newInfoFromDomain(attend));
                    marksErrorsInvalidMark.add(infoMark);
                } else {
                    newHashMarks.put(attend.getRegistration().getNumber().toString(), mark);
                    Mark domainMark = evaluation.getMarkByAttend(attend);
                    // verify if the student has already a mark
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

        return createSiteView(site, evaluation, newHashMarks, marksErrorsInvalidMark, attendList, hashMarks);
    }

    private TeacherAdministrationSiteView createSiteView(ExecutionCourseSite site, Evaluation evaluation, HashMap hashMarks,
            List marksErrorsInvalidMark, List attendList, HashMap nonExistingStudents) throws FenixServiceException {
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
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, infoSiteMarks);

        return siteView;
    }

    private boolean isValidMark(Evaluation evaluation, String mark, Registration registration) {
        StudentCurricularPlan studentCurricularPlan = null;
        if (registration != null) {
            studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        }

        DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

        // test marks by execution course: strategy
        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory =
                DegreeCurricularPlanStrategyFactory.getInstance();
        IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy =
                degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);

        if (mark == null || mark.length() == 0) {
            return true;
        }

        return degreeCurricularPlanStrategy.checkMark(mark, InfoEvaluation.newInfoFromDomain(evaluation).getEvaluationType());

    }

    // Service Invokers migrated from Berserk

    private static final InsertEvaluationMarks serviceInstance = new InsertEvaluationMarks();

    @Service
    public static TeacherAdministrationSiteView runInsertEvaluationMarks(Integer executionCourseCode, Integer evaluationCode,
            HashMap hashMarks) throws ExcepcaoInexistente, FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, evaluationCode, hashMarks);
    }

}