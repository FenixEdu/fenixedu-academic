/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBasicCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteDegreeCurricularPlans;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSCDegrees;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.DegreeCurricularPlanState;

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