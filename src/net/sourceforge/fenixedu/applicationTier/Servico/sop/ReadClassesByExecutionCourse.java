package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author LuisCruz & Sara Ribeiro
 */
public class ReadClassesByExecutionCourse implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                .readByOID(ExecutionCourse.class, infoExecutionCourse.getIdInternal());

        final List classes = sp.getITurmaPersistente().readByExecutionCourse(
                executionCourse.getIdInternal());
        final List infoClasses = new ArrayList(classes.size());

        final Map infoExecutionDegrees = new HashMap();

        for (final Iterator iterator = classes.iterator(); iterator.hasNext();) {
            final ISchoolClass schoolClass = (ISchoolClass) iterator.next();
            final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);

            final IExecutionDegree executionDegree = schoolClass.getExecutionDegree();
            final InfoExecutionDegree infoExecutionDegree;
            final String executionDegreeKey = executionDegree.getIdInternal().toString();
            if (infoExecutionDegrees.containsKey(executionDegreeKey)) {
                infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegrees.get(executionDegreeKey);
            } else {
                // final InfoExecutionDegree infoExecutionDegree =
                // InfoExecutionDegree.newInfoFromDomain(executionDegree);
                infoExecutionDegree = new InfoExecutionDegree();
                infoExecutionDegrees.put(executionDegreeKey, infoExecutionDegree);

                infoExecutionDegree.setIdInternal(executionDegree.getIdInternal());

                final IDegreeCurricularPlan degreeCurricularPlan = executionDegree
                        .getDegreeCurricularPlan();
                // final InfoDegreeCurricularPlan infoDegreeCurricularPlan =
                // InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
                final InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
                infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

                final IDegree degree = degreeCurricularPlan.getDegree();
                final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                infoDegreeCurricularPlan.setInfoDegree(infoDegree);
            }
            infoClass.setInfoExecutionDegree(infoExecutionDegree);

            infoClasses.add(infoClass);
        }

        return infoClasses;
    }
}