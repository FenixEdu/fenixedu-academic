package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequentaWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoMark;
import net.sourceforge.fenixedu.dataTransferObject.InfoMarkWithInfoAttendAndInfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadStudentsAndMarksByEvaluation implements IService {

    public ReadStudentsAndMarksByEvaluation() {

    }

    public Object run(Integer executionCourseCode, Integer evaluationCode) throws ExcepcaoInexistente,
            FenixServiceException {
        try {
            ISite site = new Site();

            IEvaluation evaluation = new Evaluation();
            InfoEvaluation infoEvaluation = new InfoEvaluation();
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            //Execution Course

            IPersistentExecutionCourse disciplinaExecucaoDAO = sp.getIPersistentExecutionCourse();
            final IExecutionCourse executionCourse = (IExecutionCourse) disciplinaExecucaoDAO.readByOID(ExecutionCourse.class,
                    executionCourseCode);

            //Site
            IPersistentSite siteDAO = sp.getIPersistentSite();
            site = siteDAO.readByExecutionCourse(executionCourse);

            //Evaluation

            IPersistentEvaluation evaluationDAO = sp.getIPersistentEvaluation();
            evaluation = (IEvaluation) evaluationDAO.readByOID(Evaluation.class, evaluationCode);
            //CLONER
            //infoEvaluation =
            // Cloner.copyIEvaluation2InfoEvaluation(evaluation);
            infoEvaluation = InfoEvaluation.newInfoFromDomain(evaluation);

            //Attends
            IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
            List attendList = frequentaPersistente.readByExecutionCourse(executionCourse);

            //Marks
            IPersistentMark persistentMark = sp.getIPersistentMark();
            List marksList = persistentMark.readBy(evaluation);

            List infoAttendList = (List) CollectionUtils.collect(attendList, new Transformer() {
                public Object transform(Object input) {
                    IAttends attend = (IAttends) input;
                    //CLONER
                    //InfoFrequenta infoAttend =
                    // Cloner.copyIFrequenta2InfoFrequenta(attend);
                    InfoFrequenta infoAttend = InfoFrequentaWithAll.newInfoFromDomain(attend);
                    //Melhoria Alterar isto depois: isto está feio assim  
                    if(attend.getEnrolment() != null) {
                        if(!attend.getEnrolment().getExecutionPeriod().equals(executionCourse.getExecutionPeriod())) {
                            infoAttend.getInfoEnrolment().setEnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT_OBJ);
                        }
                    }
                        
                    return infoAttend;
                }
            });

            List infoMarkList = (List) CollectionUtils.collect(marksList, new Transformer() {
                public Object transform(Object input) {
                    IMark mark = (IMark) input;
                    //CLONER
                    //InfoMark infoMark =
                    // Cloner.copyIMark2InfoMark(mark);
                    InfoMark infoMark = InfoMarkWithInfoAttendAndInfoStudent.newInfoFromDomain(mark);
                    return infoMark;
                }
            });

            HashMap hashMarks = new HashMap();
            Iterator iter = infoMarkList.iterator();
            while (iter.hasNext()) {
                InfoMark infoMark = (InfoMark) iter.next();
                hashMarks.put(infoMark.getInfoFrequenta().getAluno().getNumber().toString(), infoMark
                        .getMark());
            }
            InfoSiteMarks infoSiteMarks = new InfoSiteMarks();
            infoSiteMarks.setMarksList(infoMarkList);
            infoSiteMarks.setInfoEvaluation(infoEvaluation);
            infoSiteMarks.setInfoAttends(infoAttendList);
            infoSiteMarks.setHashMarks(hashMarks);

            TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
            ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site,
                    null, null, null);

            TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                    infoSiteMarks);

            return siteView;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadMarksList");
        }

    }
}