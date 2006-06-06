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
import net.sourceforge.fenixedu.domain.CurricularYear;
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

        List<CurricularCourse> curricularCourses = shift.getDisciplinaExecucao()
                .getAssociatedCurricularCourses();
        ExecutionCourse executionCourse = shift.getDisciplinaExecucao();

        List<SchoolClass> executionPeriodClasses = executionCourse.getExecutionPeriod()
                .getSchoolClasses();
        List<SchoolClass> shiftClasses = shift.getAssociatedClasses();

        infoClasses = new ArrayList();
        Iterator iter = executionPeriodClasses.iterator();
        while (iter.hasNext()) {
            SchoolClass schoolClass = (SchoolClass) iter.next();
            if (!shiftClasses.contains(schoolClass) && containsScope(curricularCourses, schoolClass)) {
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

                final Degree degree = degreeCurricularPlan.getDegree();
                final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                infoDegreeCurricularPlan.setInfoDegree(infoDegree);

                infoClasses.add(infoClass);
            }
        }

        return infoClasses;
    }

    private boolean containsScope(List<CurricularCourse> curricularCourses, SchoolClass schoolClass) {
        for (CurricularCourse curricularCourse : curricularCourses) {
            CurricularYear curricularYear = CurricularYear.readByYear(schoolClass.getAnoCurricular());            
            return curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(schoolClass
                    .getExecutionPeriod().getSemester(), curricularYear, schoolClass
                    .getExecutionDegree().getDegreeCurricularPlan(), schoolClass.getExecutionPeriod());
        }
        return false;
    }

}