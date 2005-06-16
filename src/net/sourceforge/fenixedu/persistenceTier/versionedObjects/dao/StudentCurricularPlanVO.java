package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class StudentCurricularPlanVO extends VersionedObjectsBase implements
        IPersistentStudentCurricularPlan {

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(StudentCurricularPlan.class);
    }

    public IStudentCurricularPlan readActiveByStudentNumberAndDegreeType(Integer number,
            DegreeType degreeType) throws ExcepcaoPersistencia {

        List<IStudentCurricularPlan> studentCurricularPlans = readAll();
        for (IStudentCurricularPlan scp : studentCurricularPlans) {
            if (scp.getStudent().getNumber().equals(number)
                    && scp.getStudent().getDegreeType().equals(degreeType)
                    && (scp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE) || scp
                            .getCurrentState().equals(StudentCurricularPlanState.SCHOOLPARTCONCLUDED))) {
                return scp;
            }
        }
        return null;
    }

    public IStudentCurricularPlan readActiveStudentCurricularPlan(String username, DegreeType degreeType)
            throws ExcepcaoPersistencia {

        List<IStudentCurricularPlan> studentCurricularPlans = readAll();
        for (IStudentCurricularPlan scp : studentCurricularPlans) {
            if (scp.getStudent().getPerson().getUsername().equals(username)
                    && scp.getStudent().getDegreeType().equals(degreeType)
                    && scp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE))
                return scp;
        }
        return null;
    }

    public IStudentCurricularPlan readActiveStudentCurricularPlan(Integer studentNumber,
            DegreeType degreeType) throws ExcepcaoPersistencia {

        List<IStudentCurricularPlan> studentCurricularPlans = readAll();
        for (IStudentCurricularPlan scp : studentCurricularPlans) {
            if (scp.getStudent().getNumber().equals(studentNumber)
                    && scp.getStudent().getDegreeType().equals(degreeType)
                    && scp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE))
                return scp;
        }
        return null;
    }

    public void delete(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {
        studentCurricularPlan.setBranch(null);
        studentCurricularPlan.setDegreeCurricularPlan(null);
        studentCurricularPlan.setEmployee(null);
        for (Iterator iter = studentCurricularPlan.getEnrolments().iterator(); iter.hasNext();) {
            IEnrolment enrolment = (IEnrolment) iter.next();
            enrolment.setStudentCurricularPlan(null);
        }
        for (Iterator iter = studentCurricularPlan.getGratuitySituations().iterator(); iter.hasNext();) {
            IGratuitySituation gratuitySituation = (IGratuitySituation) iter.next();
            deleteByOID(GratuitySituation.class,gratuitySituation.getIdInternal());
        }
        studentCurricularPlan.setMasterDegreeThesis(null);

        for (Iterator iter = studentCurricularPlan.getNotNeedToEnrollCurricularCourses().iterator(); iter.hasNext();) {
            NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (NotNeedToEnrollInCurricularCourse) iter.next();
            deleteByOID(NotNeedToEnrollInCurricularCourse.class,notNeedToEnrollInCurricularCourse.getIdInternal());
        }
        
        studentCurricularPlan.setSecundaryBranch(null);
        studentCurricularPlan.setStudent(null);
    }

    public List readAllFromStudent(final int studentNumber) throws ExcepcaoPersistencia {
        List<IStudentCurricularPlan> studentCurricularPlans = readAll();
        List result = (List) CollectionUtils.select(studentCurricularPlans, new Predicate() {

            public boolean evaluate(Object arg0) {
                IStudentCurricularPlan scp = (IStudentCurricularPlan) arg0;
                return scp.getStudent().getNumber().intValue() == studentNumber;
            }
        });

        return result;
    }

    public List readByUsername(final String username) throws ExcepcaoPersistencia {
        List<IStudentCurricularPlan> studentCurricularPlans = readAll();
        List result = (List) CollectionUtils.select(studentCurricularPlans, new Predicate() {

            public boolean evaluate(Object arg0) {
                IStudentCurricularPlan scp = (IStudentCurricularPlan) arg0;
                return scp.getStudent().getPerson().getUsername().equals(username);
            }
        });

        return result;
    }

    public List readByStudentNumberAndDegreeType(final Integer number, final DegreeType degreeType)
            throws ExcepcaoPersistencia {
        List<IStudentCurricularPlan> studentCurricularPlans = readAll();
        List result = (List) CollectionUtils.select(studentCurricularPlans, new Predicate() {

            public boolean evaluate(Object arg0) {
                IStudentCurricularPlan scp = (IStudentCurricularPlan) arg0;
                return scp.getStudent().getNumber().equals(number)
                        && scp.getStudent().getDegreeType().equals(degreeType);
            }
        });

        return result;
    }

    public List readAllByStudentNumberAndSpecialization(final Integer studentNumber,
            final DegreeType degreeType, final Specialization specialization)
            throws ExcepcaoPersistencia {
        List<IStudentCurricularPlan> studentCurricularPlans = readAll();
        List finalList = (List) CollectionUtils.select(studentCurricularPlans, new Predicate() {

            public boolean evaluate(Object arg0) {
                IStudentCurricularPlan scp = (IStudentCurricularPlan) arg0;
                boolean result = scp.getStudent().getNumber().equals(studentNumber)
                        && scp.getStudent().getDegreeType().equals(degreeType)
                        && scp.getSpecialization().equals(specialization);
                return result;
            }
        });

        return finalList;
    }

    public List readAllByStudentNumberAndSpecializationAndState(final Integer studentNumber,
            final DegreeType degreeType, final Specialization specialization,
            final StudentCurricularPlanState state) throws ExcepcaoPersistencia {
        List<IStudentCurricularPlan> studentCurricularPlans = readAll();
        List finalList = (List) CollectionUtils.select(studentCurricularPlans, new Predicate() {

            public boolean evaluate(Object arg0) {
                IStudentCurricularPlan scp = (IStudentCurricularPlan) arg0;
                boolean result = scp.getStudent().getNumber().equals(studentNumber)
                        && scp.getStudent().getDegreeType().equals(degreeType)
                        && scp.getSpecialization().equals(specialization)
                        && scp.getCurrentState().equals(state);
                return result;
            }
        });
        return finalList;
    }

}
