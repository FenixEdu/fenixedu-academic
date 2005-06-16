/*
 * StudentCurricularPlanOJB.java Created on 21 of December of 2002, 17:01
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author Nuno Nunes & Joana Mota
 */

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

import org.apache.ojb.broker.query.Criteria;

public class StudentCurricularPlanOJB extends PersistentObjectOJB implements
        IPersistentStudentCurricularPlan {

    /** Creates a new instance of StudentCurricularPlanOJB */
    public StudentCurricularPlanOJB() {
    }

    public IStudentCurricularPlan readActiveStudentCurricularPlan(String username, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.person.username", username);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE);
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, crit);
    }

    // TODO Remove DegreeType from method interface...
    public IStudentCurricularPlan readActiveStudentCurricularPlan(Integer studentNumber,
            DegreeType degreeType) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("currentState", StudentCurricularPlanState.ACTIVE);
        return (IStudentCurricularPlan) queryObject(StudentCurricularPlan.class, crit);

    }

    public void delete(IStudentCurricularPlan curricularPlan) throws ExcepcaoPersistencia {
        super.delete(curricularPlan);
    }

    public List readAllFromStudent(int studentNumber) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.number", new Integer(studentNumber));
        return queryList(StudentCurricularPlan.class, criteria);
    }

    public List readAllByStudentNumberAndSpecialization(Integer studentNumber, DegreeType degreeType,
            Specialization specialization) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("specialization", specialization);
        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readAllByStudentNumberAndSpecializationAndState(Integer studentNumber,
            DegreeType degreeType, Specialization specialization, StudentCurricularPlanState state)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", studentNumber);
        crit.addEqualTo("student.degreeType", degreeType);
        crit.addEqualTo("currentState", state);
        crit.addEqualTo("specialization", specialization);

        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readByUsername(String username) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.person.username", username);
        return queryList(StudentCurricularPlan.class, crit);

    }

    public List readByStudentNumberAndDegreeType(Integer number, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("student.number", number);
        crit.addEqualTo("student.degreeType", degreeType);
        return queryList(StudentCurricularPlan.class, crit);

    }

    //modified by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 24/Set/2003
    /**
     * Service rewritten by Naat & Sana to correct bug that cause the returning
     * of an empty StudentCurricular when a student does not have an active
     * version. The new version now uses Persistence Broker criteria API
     */
    public IStudentCurricularPlan readActiveByStudentNumberAndDegreeType(Integer number,
            DegreeType degreeType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        List studentPlanState = new ArrayList();
        studentPlanState.add(StudentCurricularPlanState.ACTIVE);
        studentPlanState.add(StudentCurricularPlanState.SCHOOLPARTCONCLUDED);

        criteria.addIn("currentState", studentPlanState);
        //   criteria.addEqualTo("currentState",
        // StudentCurricularPlanState.ACTIVE_OBJ);
        criteria.addEqualTo("student.number", number);
        criteria.addEqualTo("student.degreeType", degreeType);

        IStudentCurricularPlan storedStudentCurricularPlan = (IStudentCurricularPlan) queryObject(
                StudentCurricularPlan.class, criteria);
        return storedStudentCurricularPlan;

    }

}