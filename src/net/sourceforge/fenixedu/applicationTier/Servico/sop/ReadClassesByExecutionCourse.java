package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author LuisCruz & Sara Ribeiro
 */
public class ReadClassesByExecutionCourse extends Service {

    public List run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());

        final Set<SchoolClass> classes = executionCourse.findSchoolClasses();
        final List infoClasses = new ArrayList(classes.size());

        final Map infoExecutionDegrees = new HashMap();

        for (final Iterator iterator = classes.iterator(); iterator.hasNext();) {
            final SchoolClass schoolClass = (SchoolClass) iterator.next();
            final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);

            final ExecutionDegree executionDegree = schoolClass.getExecutionDegree();
            final InfoExecutionDegree infoExecutionDegree;
            final String executionDegreeKey = executionDegree.getIdInternal().toString();
            if (infoExecutionDegrees.containsKey(executionDegreeKey)) {
                infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegrees.get(executionDegreeKey);
            } else {
                infoExecutionDegree = new InfoExecutionDegree();
                infoExecutionDegrees.put(executionDegreeKey, infoExecutionDegree);

                infoExecutionDegree.setIdInternal(executionDegree.getIdInternal());

                final DegreeCurricularPlan degreeCurricularPlan = executionDegree
                        .getDegreeCurricularPlan();
                final InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan(degreeCurricularPlan);
                infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
            }
            infoClass.setInfoExecutionDegree(infoExecutionDegree);

            infoClasses.add(infoClass);
        }

        return infoClasses;
    }
}