/*
 * Created on Feb 25, 2004
 *  
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.gesdis.InfoCourseHistoric;
import DataBeans.gesdis.InfoSiteCourseHistoric;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionPeriod;
import Dominio.gesdis.ICourseHistoric;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseHistoric;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class ReadCurricularCourseHistoric implements IService {
    /**
     *  
     */
    public ReadCurricularCourseHistoric() {
        super();
    }

    public InfoSiteCourseHistoric run(Integer curricularCourseId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseId);

            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            Integer semester = executionPeriod.getSemester();
            // TODO: corrigir o calculo do semestre
            semester = new Integer(semester.intValue() == 2 ? 1 : 2);
            return getInfoSiteCourseHistoric(curricularCourse, semester, sp);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
     * @param curricularCourse
     * @param sp
     * @return
     */
    private InfoSiteCourseHistoric getInfoSiteCourseHistoric(ICurricularCourse curricularCourse,
            Integer semester, ISuportePersistente sp) throws ExcepcaoPersistencia {
        InfoSiteCourseHistoric infoSiteCourseHistoric = new InfoSiteCourseHistoric();
        InfoCurricularCourse infoCurricularCourse = Cloner
                .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
        infoSiteCourseHistoric.setInfoCurricularCourse(infoCurricularCourse);

        IPersistentCourseHistoric persistentCourseHistoric = sp.getIPersistentCourseHistoric();
        List coursesHistoric = persistentCourseHistoric.readByCurricularCourseAndSemester(
                curricularCourse, semester);
        List infoCoursesHistoric = (List) CollectionUtils.collect(coursesHistoric, new Transformer() {
            public Object transform(Object arg0) {
                ICourseHistoric courseHistoric = (ICourseHistoric) arg0;
                return Cloner.copyICourseHistoric2InfoCourseHistoric(courseHistoric);
            }

        });

        Collections.sort(infoCoursesHistoric, new Comparator() {

            public int compare(Object o1, Object o2) {
                InfoCourseHistoric infoCourseHistoric1 = (InfoCourseHistoric) o1;
                InfoCourseHistoric infoCourseHistoric2 = (InfoCourseHistoric) o2;
                return infoCourseHistoric2.getCurricularYear().compareTo(
                        infoCourseHistoric1.getCurricularYear());
            }
        });

        infoSiteCourseHistoric.setInfoCourseHistorics(infoCoursesHistoric);
        return infoSiteCourseHistoric;
    }
}