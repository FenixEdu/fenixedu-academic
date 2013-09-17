package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequentaWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoMark;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadStudentsAndMarksByEvaluation {

    protected Object run(String executionCourseCode, String evaluationCode) throws FenixServiceException {

        InfoEvaluation infoEvaluation = new InfoEvaluation();

        // Execution Course
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);

        // Site
        final ExecutionCourseSite site = executionCourse.getSite();

        // Evaluation
        Evaluation evaluation = FenixFramework.getDomainObject(evaluationCode);

        infoEvaluation = InfoEvaluation.newInfoFromDomain(evaluation);

        // Attends
        Collection attendList = executionCourse.getAttends();

        // Marks
        Collection<Mark> marksList = evaluation.getMarks();

        List infoAttendList = (List) CollectionUtils.collect(attendList, new Transformer() {
            @Override
            public Object transform(Object input) {
                Attends attend = (Attends) input;
                InfoFrequenta infoAttend = InfoFrequentaWithAll.newInfoFromDomain(attend);
                // Melhoria Alterar isto depois: isto está feio assim
                if (attend.getEnrolment() != null) {
                    if (!attend.getEnrolment().getExecutionPeriod().equals(executionCourse.getExecutionPeriod())) {
                        infoAttend.setEnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT);
                    }
                }
                return infoAttend;
            }
        });

        List infoMarkList = (List) CollectionUtils.collect(marksList, new Transformer() {
            @Override
            public Object transform(Object input) {
                Mark mark = (Mark) input;

                InfoMark infoMark = InfoMark.newInfoFromDomain(mark);
                return infoMark;
            }
        });

        HashMap hashMarks = new HashMap();
        Iterator iter = infoMarkList.iterator();
        while (iter.hasNext()) {
            InfoMark infoMark = (InfoMark) iter.next();
            hashMarks.put(infoMark.getInfoFrequenta().getAluno().getNumber().toString(), infoMark.getMark());
        }
        InfoSiteMarks infoSiteMarks = new InfoSiteMarks();
        infoSiteMarks.setMarksList(infoMarkList);
        infoSiteMarks.setInfoEvaluation(infoEvaluation);
        infoSiteMarks.setInfoAttends(infoAttendList);
        infoSiteMarks.setHashMarks(hashMarks);

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, infoSiteMarks);

        return siteView;

    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsAndMarksByEvaluation serviceInstance = new ReadStudentsAndMarksByEvaluation();

    @Atomic
    public static Object runReadStudentsAndMarksByEvaluation(String executionCourseCode, String evaluationCode)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, evaluationCode);
    }

}