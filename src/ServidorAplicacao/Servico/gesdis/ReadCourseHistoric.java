/*
 * Created on Feb 17, 2004
 *  
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseWithInfoDegreeCurricularPlan;
import DataBeans.gesdis.InfoCourseHistoric;
import DataBeans.gesdis.InfoCourseHistoricWithInfoCurricularCourse;
import DataBeans.gesdis.InfoSiteCourseHistoric;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IExecutionYear;
import Dominio.gesdis.ICourseHistoric;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseHistoric;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class ReadCourseHistoric implements IService {
    public ReadCourseHistoric() {
    }

    public List run(Integer executionCourseId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            Integer semester = executionCourse.getExecutionPeriod().getSemester();
            List curricularCourses = executionCourse.getAssociatedCurricularCourses();
            return getInfoSiteCoursesHistoric(executionCourse, curricularCourses, semester, sp);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    /**
     * @param curricularCourses
     * @param sp
     * @return
     */
    private List getInfoSiteCoursesHistoric(IExecutionCourse executionCourse, List curricularCourses, Integer semester,
            ISuportePersistente sp) throws ExcepcaoPersistencia {
        List infoSiteCoursesHistoric = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
            infoSiteCoursesHistoric.add(getInfoSiteCourseHistoric(executionCourse.getExecutionPeriod().getExecutionYear(),curricularCourse, semester, sp));
        }
        return infoSiteCoursesHistoric;
    }

    /**
     * @param curricularCourse
     * @param sp
     * @return
     */
    private InfoSiteCourseHistoric getInfoSiteCourseHistoric(final IExecutionYear executionYear, ICurricularCourse curricularCourse,
            Integer semester, ISuportePersistente sp) throws ExcepcaoPersistencia {
        InfoSiteCourseHistoric infoSiteCourseHistoric = new InfoSiteCourseHistoric();
        //CLONER
        //InfoCurricularCourse infoCurricularCourse = Cloner
        //.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
        InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegreeCurricularPlan
                .newInfoFromDomain(curricularCourse);
        infoSiteCourseHistoric.setInfoCurricularCourse(infoCurricularCourse);

        IPersistentCourseHistoric persistentCourseHistoric = sp.getIPersistentCourseHistoric();
        List coursesHistoric = persistentCourseHistoric.readByCurricularCourseAndSemester(
                curricularCourse, semester);
        List infoCoursesHistoric = (List) CollectionUtils.collect(coursesHistoric, new Transformer() {
            public Object transform(Object arg0) {
                ICourseHistoric courseHistoric = (ICourseHistoric) arg0;
                //CLONER
                //return Cloner
                //.copyICourseHistoric2InfoCourseHistoric(courseHistoric);
                return InfoCourseHistoricWithInfoCurricularCourse.newInfoFromDomain(courseHistoric);
            }

        });

        
        // the historic must only show info regarding the years previous to the year chosen by the user
        final IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        List infoCoursesHistoricToView = (List) CollectionUtils.select(infoCoursesHistoric, new Predicate() {
            public boolean evaluate(Object arg0) {
                InfoCourseHistoric infoCourseHistoric = (InfoCourseHistoric) arg0;
                
                boolean evaluation = false;
                try{
                    IExecutionYear courseHistoricExecutionYear = persistentExecutionYear.readExecutionYearByName(infoCourseHistoric.getCurricularYear());
                    evaluation = courseHistoricExecutionYear.getBeginDate().before(executionYear.getBeginDate());
                }catch (ExcepcaoPersistencia e) {
                }
                
                return evaluation;
            }

        });

        
        Collections.sort(infoCoursesHistoricToView, new Comparator() {

            public int compare(Object o1, Object o2) {
                InfoCourseHistoric infoCourseHistoric1 = (InfoCourseHistoric) o1;
                InfoCourseHistoric infoCourseHistoric2 = (InfoCourseHistoric) o2;
                return infoCourseHistoric2.getCurricularYear().compareTo(
                        infoCourseHistoric1.getCurricularYear());
            }
        });

        infoSiteCourseHistoric.setInfoCourseHistorics(infoCoursesHistoricToView);
        return infoSiteCourseHistoric;
    }
}