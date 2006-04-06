/*
 * Created on 30/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
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

        List infoClasses = null;

        Shift shift = rootDomainObject.readShiftByOID(shiftOID);

        List curricularCourses = shift.getDisciplinaExecucao().getAssociatedCurricularCourses();
        List scopes = new ArrayList();
        for (int i = 0; i < curricularCourses.size(); i++) {
            CurricularCourse curricularCourse = (CurricularCourse) curricularCourses.get(i);
            scopes.addAll(curricularCourse.getScopes());
        }

        ExecutionCourse executionCourse = shift.getDisciplinaExecucao();

        List classes = executionCourse.getExecutionPeriod().getSchoolClasses();

        infoClasses = new ArrayList();
        Iterator iter = classes.iterator();
        while (iter.hasNext()) {
            SchoolClass classImpl = (SchoolClass) iter.next();
            if (!shift.getAssociatedClasses().contains(classImpl) && containsScope(scopes, classImpl)) {
                final InfoClass infoClass = InfoClass.newInfoFromDomain(classImpl);

                final ExecutionDegree executionDegree = classImpl.getExecutionDegree();
                final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                        .newInfoFromDomain(executionDegree);
                infoClass.setInfoExecutionDegree(infoExecutionDegree);

                final DegreeCurricularPlan degreeCurricularPlan = executionDegree
                        .getDegreeCurricularPlan();
                final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                        .newInfoFromDomain(degreeCurricularPlan);
                infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

                final Degree degree = degreeCurricularPlan.getDegree();
                final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                infoDegreeCurricularPlan.setInfoDegree(infoDegree);

                infoClasses.add(infoClass);
            }
        }

        return infoClasses;
    }

    /**
     * @param scopes
     * @param classImpl
     * @return
     */
    private boolean containsScope(List scopes, SchoolClass classImpl) {
        for (int i = 0; i < scopes.size(); i++) {
            CurricularCourseScope scope = (CurricularCourseScope) scopes.get(i);

            if (scope.getCurricularCourse().getDegreeCurricularPlan().equals(
                    classImpl.getExecutionDegree().getDegreeCurricularPlan())
                    && scope.getCurricularSemester().getCurricularYear().getYear().equals(
                            classImpl.getAnoCurricular()))
                return true;
        }

        return false;
    }

}