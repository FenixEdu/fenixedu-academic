package ServidorAplicacao.Servico.publico;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoFrequenta;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.util.Cloner;
import Dominio.Evaluation;
import Dominio.IEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.ExecutionCourseSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 *  
 */
public class ReadPublishedMarksByExam implements IServico {
    private static ReadPublishedMarksByExam _servico = new ReadPublishedMarksByExam();

    /**
     * The actor of this class.
     */
    private ReadPublishedMarksByExam() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadPublishedMarksByExam";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadPublishedMarksByExam
     */
    public static ReadPublishedMarksByExam getService() {
        return _servico;
    }

    public Object run(Integer siteCode, Integer evaluationCode)
            throws ExcepcaoInexistente, FenixServiceException {
        List marksList = null;
        List infoMarksList = null;

        ISite site = null;
        IEvaluation evaluation = null;
        InfoEvaluation infoEvaluation = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //Site

            IPersistentSite siteDAO = sp.getIPersistentSite();
            site = (ISite) siteDAO.readByOID(Site.class, siteCode);

            //Execution Course
            IExecutionCourse executionCourse = site.getExecutionCourse();

            // Evaluation

            IPersistentEvaluation persistentEvaluation = sp
                    .getIPersistentEvaluation();
            evaluation = (IEvaluation) persistentEvaluation.readByOID(
                    Evaluation.class, evaluationCode);
            infoEvaluation = Cloner.copyIEvaluation2InfoEvaluation(evaluation);

            //Attends
            IFrequentaPersistente attendDAO = sp.getIFrequentaPersistente();
            List attendList = attendDAO.readByExecutionCourse(executionCourse);

            //Marks
            IPersistentMark markDAO = sp.getIPersistentMark();
            marksList = markDAO.readBy(evaluation);

            List infoAttendList = (List) CollectionUtils.collect(attendList,
                    new Transformer() {
                        public Object transform(Object input) {
                            IFrequenta attend = (IFrequenta) input;
                            InfoFrequenta infoAttend = Cloner
                                    .copyIFrequenta2InfoFrequenta(attend);
                            return infoAttend;
                        }
                    });

            List infoMarkList = (List) CollectionUtils.collect(marksList,
                    new Transformer() {
                        public Object transform(Object input) {
                            IMark mark = (IMark) input;
                            InfoMark infoMark = Cloner.copyIMark2InfoMark(mark);
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
            infoSiteMarks.setMarksList(infoMarksList);
            infoSiteMarks.setInfoEvaluation(infoEvaluation);
            infoSiteMarks.setHashMarks(hashMarks);
            infoSiteMarks.setInfoAttends(infoAttendList);

            ExecutionCourseSiteComponentBuilder componentBuilder = new ExecutionCourseSiteComponentBuilder();
            ISiteComponent commonComponent = componentBuilder.getComponent(
                    new InfoSiteCommon(), site, null, null, null);

            ExecutionCourseSiteView siteView = new ExecutionCourseSiteView(
                    commonComponent, infoSiteMarks);

            return siteView;

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadMarksList");
        }
    }
}