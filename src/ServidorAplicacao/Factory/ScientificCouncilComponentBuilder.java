/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoSiteBasicCurricularCourses;
import DataBeans.InfoSiteCurricularCourses;
import DataBeans.InfoSiteDegreeCurricularPlans;
import DataBeans.InfoSiteSCDegrees;
import DataBeans.util.Cloner;
import Dominio.Degree;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegree;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Factory
 *  
 */
public class ScientificCouncilComponentBuilder {

    private static ScientificCouncilComponentBuilder instance = null;

    public ScientificCouncilComponentBuilder() {
    }

    public static ScientificCouncilComponentBuilder getInstance() {
        if (instance == null) {
            instance = new ScientificCouncilComponentBuilder();
        }
        return instance;
    }

    public ISiteComponent getComponent(ISiteComponent component, Integer degreeId,
            Integer curricularYear, Integer degreeCurricularPlanId) throws FenixServiceException {
        if (component instanceof InfoSiteSCDegrees) {
            return getInfoSiteSCDegrees((InfoSiteSCDegrees) component);
        } else if (component instanceof InfoSiteDegreeCurricularPlans) {
            return getInfoSiteDegreeCurricularPlans((InfoSiteDegreeCurricularPlans) component, degreeId);
        } else if (component instanceof InfoSiteCurricularCourses) {
            return getInfoSiteCurricularCourses((InfoSiteCurricularCourses) component,
                    degreeCurricularPlanId);
        } else if (component instanceof InfoSiteBasicCurricularCourses) {
            return getInfoSiteBasicCurricularCourses((InfoSiteBasicCurricularCourses) component,
                    degreeCurricularPlanId);
        } else {
            return null;
        }

    }

    /**
     * @param courses
     * @param degreeCurricularPlanId
     * @return
     */
    private ISiteComponent getInfoSiteBasicCurricularCourses(InfoSiteBasicCurricularCourses component,
            Integer degreeCurricularPlanId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentObject
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

            if (degreeCurricularPlan == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            List nonBasicCurricularCourses = persistentCurricularCourse
                    .readCurricularCoursesByDegreeCurricularPlanAndBasicAttribute(degreeCurricularPlan,
                            new Boolean(false));

            List basicCurricularCourses = persistentCurricularCourse
                    .readCurricularCoursesByDegreeCurricularPlanAndBasicAttribute(degreeCurricularPlan,
                            new Boolean(true));
            Iterator iter = nonBasicCurricularCourses.iterator();
            Iterator iter1 = basicCurricularCourses.iterator();
            List infoNonBasicCurricularCourses = new ArrayList();
            List infoBasicCurricularCourses = new ArrayList();
            while (iter.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                InfoCurricularCourse infoCurricularCourse = Cloner
                        .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoNonBasicCurricularCourses.add(infoCurricularCourse);
            }
            while (iter1.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter1.next();
                InfoCurricularCourse infoCurricularCourse = Cloner
                        .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoBasicCurricularCourses.add(infoCurricularCourse);
            }

            component.setBasicCurricularCourses(infoBasicCurricularCourses);
            component.setNonBasicCurricularCourses(infoNonBasicCurricularCourses);
            component.setInfoDegreeCurricularPlan(Cloner
                    .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan));

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return component;
    }

    /**
     * @param courses
     * @param degreeCurricularPlanId
     * @return
     */
    private ISiteComponent getInfoSiteCurricularCourses(InfoSiteCurricularCourses component,
            Integer degreeCurricularPlanId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentObject
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

            if (degreeCurricularPlan == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            List curricularCourses = persistentCurricularCourse
                    .readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);
            Iterator iter = curricularCourses.iterator();
            List infoCurricularCourses = new ArrayList();
            while (iter.hasNext()) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                InfoCurricularCourse infoCurricularCourse = Cloner
                        .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoCurricularCourses.add(infoCurricularCourse);
            }
            component.setCurricularCourses(infoCurricularCourses);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return component;
    }

    /**
     * @param courses
     * @param degreeId
     * @param curricularYear
     * @return
     */
    private ISiteComponent getInfoSiteDegreeCurricularPlans(InfoSiteDegreeCurricularPlans component,
            Integer degreeId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            IDegree degree = (IDegree) persistentObject.readByOID(Degree.class, degreeId);
            if (degree == null) {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();
            List degreeCurricularPlans = persistentDegreeCurricularPlan.readByDegreeAndState(degree,
                    new DegreeCurricularPlanState(1));
            Iterator iter = degreeCurricularPlans.iterator();
            List infoDegreeCurricularPlans = new ArrayList();
            while (iter.hasNext()) {
                IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iter.next();
                InfoDegreeCurricularPlan infoDegreeCurricularPlan = Cloner
                        .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan);
                infoDegreeCurricularPlans.add(infoDegreeCurricularPlan);
            }
            component.setDegreeCurricularPlans(infoDegreeCurricularPlans);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return component;
    }

    /**
     * @param component
     * @return
     */
    private ISiteComponent getInfoSiteSCDegrees(InfoSiteSCDegrees component)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoPersistente persistentDegree = sp.getICursoPersistente();
            List degrees = persistentDegree.readAll();
            Iterator degreeIterator = degrees.iterator();
            List infoDegrees = new ArrayList();
            while (degreeIterator.hasNext()) {
                IDegree degree = (IDegree) degreeIterator.next();
                InfoDegree infoDegree = Cloner.copyIDegree2InfoDegree(degree);
                infoDegrees.add(infoDegree);
            }

            component.setDegrees(infoDegrees);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return component;
    }

}