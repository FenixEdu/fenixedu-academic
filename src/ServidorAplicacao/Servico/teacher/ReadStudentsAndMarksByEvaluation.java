package ServidorAplicacao.Servico.teacher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoFrequenta;
import DataBeans.InfoFrequentaWithAll;
import DataBeans.InfoMark;
import DataBeans.InfoMarkWithInfoAttendAndInfoStudent;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.TeacherAdministrationSiteView;
import Dominio.Evaluation;
import Dominio.ExecutionCourse;
import Dominio.IEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadStudentsAndMarksByEvaluation implements IService {

    public ReadStudentsAndMarksByEvaluation() {

    }

    public Object run(Integer executionCourseCode, Integer evaluationCode)
            throws ExcepcaoInexistente, FenixServiceException {
        try {
            ISite site = new Site();
            IExecutionCourse executionCourse = new ExecutionCourse();
            IEvaluation evaluation = new Evaluation();
            InfoEvaluation infoEvaluation = new InfoEvaluation();
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //Execution Course

            IPersistentExecutionCourse disciplinaExecucaoDAO = sp
                    .getIPersistentExecutionCourse();
            executionCourse = (IExecutionCourse) disciplinaExecucaoDAO
                    .readByOID(ExecutionCourse.class, executionCourseCode);

            //Site
            IPersistentSite siteDAO = sp.getIPersistentSite();
            site = siteDAO.readByExecutionCourse(executionCourse);

            //Evaluation

            IPersistentEvaluation evaluationDAO = sp.getIPersistentEvaluation();
            evaluation = (IEvaluation) evaluationDAO.readByOID(
                    Evaluation.class, evaluationCode);
            //CLONER
            //infoEvaluation =
            // Cloner.copyIEvaluation2InfoEvaluation(evaluation);
            infoEvaluation = InfoEvaluation.newInfoFromDomain(evaluation);

            //Attends
            IFrequentaPersistente frequentaPersistente = sp
                    .getIFrequentaPersistente();
            List attendList = frequentaPersistente
                    .readByExecutionCourse(executionCourse);

            //Marks
            IPersistentMark persistentMark = sp.getIPersistentMark();
            List marksList = persistentMark.readBy(evaluation);

            List infoAttendList = (List) CollectionUtils.collect(attendList,
                    new Transformer() {
                        public Object transform(Object input) {
                            IFrequenta attend = (IFrequenta) input;
                            //CLONER
                            //InfoFrequenta infoAttend =
                            // Cloner.copyIFrequenta2InfoFrequenta(attend);
                            InfoFrequenta infoAttend = InfoFrequentaWithAll
                                    .newInfoFromDomain(attend);
                            return infoAttend;
                        }
                    });

            List infoMarkList = (List) CollectionUtils.collect(marksList,
                    new Transformer() {
                        public Object transform(Object input) {
                            IMark mark = (IMark) input;
                            //CLONER
                            //InfoMark infoMark =
                            // Cloner.copyIMark2InfoMark(mark);
                            InfoMark infoMark = InfoMarkWithInfoAttendAndInfoStudent
                                    .newInfoFromDomain(mark);
                            return infoMark;
                        }
                    });

            HashMap hashMarks = new HashMap();
            Iterator iter = infoMarkList.iterator();
            while (iter.hasNext()) {
                InfoMark infoMark = (InfoMark) iter.next();
                hashMarks.put(infoMark.getInfoFrequenta().getAluno()
                        .getNumber().toString(), infoMark.getMark());
            }
            InfoSiteMarks infoSiteMarks = new InfoSiteMarks();
            infoSiteMarks.setMarksList(infoMarkList);
            infoSiteMarks.setInfoEvaluation(infoEvaluation);
            infoSiteMarks.setInfoAttends(infoAttendList);
            infoSiteMarks.setHashMarks(hashMarks);

            TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
            ISiteComponent commonComponent = componentBuilder.getComponent(
                    new InfoSiteCommon(), site, null, null, null);

            TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(
                    commonComponent, infoSiteMarks);

            return siteView;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadMarksList");
        }

    }
}