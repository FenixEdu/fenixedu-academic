/*
 * Created on Feb 25, 2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseHistoric;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseHistoric;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.gesdis.ICourseHistoric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseHistoric;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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