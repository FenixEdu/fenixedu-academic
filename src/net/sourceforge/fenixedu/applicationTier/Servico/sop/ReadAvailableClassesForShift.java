/*
 * Created on 30/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Mota
 * 
 * 30/Jun/2003 fenix-branch ServidorAplicacao.Servico.sop
 * 
 */
public class ReadAvailableClassesForShift extends Service {

    public List run(Integer shiftOID) throws ExcepcaoPersistencia {

        final Shift shift = rootDomainObject.readShiftByOID(shiftOID);
        final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
        final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
        final ExecutionYear executionYear = executionPeriod.getExecutionYear();

        final Set<SchoolClass> availableSchoolClasses = new HashSet<SchoolClass>();
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
        	final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
        	for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
        		if (executionDegree.getExecutionYear() == executionYear) {
        			for (final SchoolClass schoolClass : executionDegree.getSchoolClassesSet()) {
        				if (schoolClass.getExecutionPeriod() == executionPeriod) {
        					if (!shift.getAssociatedClassesSet().contains(schoolClass)) {
        						availableSchoolClasses.add(schoolClass);
        					}
        				}
        			}
        		}
        	}
        }

        final List<InfoClass> infoClasses = new ArrayList<InfoClass>(availableSchoolClasses.size());
        for (final SchoolClass schoolClass : availableSchoolClasses) {
            final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);

            final ExecutionDegree executionDegree = schoolClass.getExecutionDegree();
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                    .newInfoFromDomain(executionDegree);
            infoClass.setInfoExecutionDegree(infoExecutionDegree);

            final DegreeCurricularPlan degreeCurricularPlan = executionDegree
                    .getDegreeCurricularPlan();
            final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                    .newInfoFromDomain(degreeCurricularPlan);
            infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

            infoClasses.add(infoClass);
        }

        return infoClasses;
    }

}