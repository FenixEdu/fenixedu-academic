/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlans implements IService {

    // delete a set of degreeCurricularPlans
    public List run(List degreeCurricularPlansIds) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentBranch persistentBranch = sp.getIPersistentBranch();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            Iterator iter = degreeCurricularPlansIds.iterator();

            List undeletedDegreeCurricularPlansNames = new ArrayList();
            List executionDegrees, curricularCourses, branches, studentCurricularPlans;
            Integer degreeCurricularPlanId;
            IDegreeCurricularPlan degreeCurricularPlan;

            while (iter.hasNext()) {

                degreeCurricularPlanId = (Integer) iter.next();
                degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOID(
                        DegreeCurricularPlan.class, degreeCurricularPlanId);
                if (degreeCurricularPlan != null) {
                    executionDegrees = persistentExecutionDegree
                            .readByDegreeCurricularPlan(degreeCurricularPlan);
                    if (!executionDegrees.isEmpty())
                        undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
                    else {
                        curricularCourses = persistentCurricularCourse
                                .readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);
                        if (!curricularCourses.isEmpty())
                            undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
                        else {
                            branches = persistentBranch.readByDegreeCurricularPlan(degreeCurricularPlan);
                            if (!branches.isEmpty())
                                undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
                            else {
                                studentCurricularPlans = persistentStudentCurricularPlan
                                        .readByDegreeCurricularPlan(degreeCurricularPlan);
                                if (!studentCurricularPlans.isEmpty())
                                    undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan
                                            .getName());
                                else
                                    persistentDegreeCurricularPlan
                                            .deleteDegreeCurricularPlan(degreeCurricularPlan);
                            }
                        }
                    }
                }
            }

            return undeletedDegreeCurricularPlansNames;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}