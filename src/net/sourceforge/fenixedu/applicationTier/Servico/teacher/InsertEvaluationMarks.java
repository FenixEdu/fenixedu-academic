package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IServico;
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
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Fernanda Quitério
 *  
 */
public class InsertEvaluationMarks implements IServico {
    private static InsertEvaluationMarks _servico = new InsertEvaluationMarks();

    /**
     * The actor of this class.
     */
    private InsertEvaluationMarks() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "InsertEvaluationMarks";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static InsertEvaluationMarks getService() {
        return _servico;
    }

    public Object run(Integer executionCourseCode, Integer evaluationCode, HashMap hashMarks)
            throws ExcepcaoInexistente, FenixServiceException {

        ISite site = null;
        IExecutionCourse executionCourse = null;
        IEvaluation evaluation = null;
        List marksErrorsInvalidMark = null;
        List attendList = null;
        HashMap newHashMarks = new HashMap();
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentMark persistentMark = sp.getIPersistentMark();

            //Execution Course

            executionCourse = (IExecutionCourse) executionCourseDAO.readByOID(ExecutionCourse.class,
                    executionCourseCode);

            //Site
            site = persistentSite.readByExecutionCourse(executionCourse);

            //Evaluation

            evaluation = (IEvaluation) persistentEvaluation.readByOID(Evaluation.class, evaluationCode);

            //Attend List
            attendList = persistentAttend.readByExecutionCourse(executionCourse);

            marksErrorsInvalidMark = new ArrayList();
            ListIterator iterAttends = attendList.listIterator();

            while (iterAttends.hasNext()) {
                IAttends attend = (IAttends) iterAttends.next();

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
                        IMark domainMark = persistentMark.readBy(evaluation, attend);
                        //verify if the student has already a mark
                        if (domainMark == null) {
                            domainMark = new Mark();

                            persistentMark.simpleLockWrite(domainMark);

                            domainMark.setAttend(attend);
                            domainMark.setEvaluation(evaluation);
                            domainMark.setMark(mark.toUpperCase());

                        } else {
                            if (!domainMark.getMark().equals(mark)) {
                                persistentMark.simpleLockWrite(domainMark);
                                domainMark.setMark(mark.toUpperCase());
                            }
                        }

                    }
                } else {
                    IMark domainMark = persistentMark.readBy(evaluation, attend);
                    if (domainMark != null) {
                        persistentMark.delete(domainMark);
                    }
                }
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return createSiteView(site, evaluation, newHashMarks, marksErrorsInvalidMark, attendList,
                hashMarks);
    }

    /**
     * @param infoMark
     * @return
     */

    private Object createSiteView(ISite site, IEvaluation evaluation, HashMap hashMarks,
            List marksErrorsInvalidMark, List attendList, HashMap nonExistingStudents)
            throws FenixServiceException {
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

    private boolean isValidMark(IEvaluation evaluation, String mark, IStudent student) {
        IStudentCurricularPlan studentCurricularPlan = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudentCurricularPlan curricularPlanPersistente = sp
                    .getIStudentCurricularPlanPersistente();

            studentCurricularPlan = curricularPlanPersistente.readActiveStudentCurricularPlan(student
                    .getNumber(), student.getDegreeType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

        // test marks by execution course: strategy
        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                .getInstance();
        IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = degreeCurricularPlanStrategyFactory
                .getDegreeCurricularPlanStrategy(degreeCurricularPlan);

        if (mark == null || mark.length() == 0) {
            return true;
        }
        //return degreeCurricularPlanStrategy.checkMark(mark, Cloner
        //.copyIEvaluation2InfoEvaluation(evaluation)
        //.getEvaluationType());
        return degreeCurricularPlanStrategy.checkMark(mark, InfoEvaluation.newInfoFromDomain(evaluation)
                .getEvaluationType());

    }
}