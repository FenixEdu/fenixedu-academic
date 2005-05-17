/*
 * IStudentCurricularPlan.java
 * 
 * Created on 21 of December de 2002, 16:57
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author Nuno Nunes & Joana Mota
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.Specialization;

public interface IPersistentStudentCurricularPlan extends IPersistentObject {

    public IStudentCurricularPlan readActiveByStudentNumberAndDegreeType(Integer number,
            DegreeType degreeType) throws ExcepcaoPersistencia;

    public IStudentCurricularPlan readActiveStudentCurricularPlan(String username, DegreeType degreeType)
            throws ExcepcaoPersistencia;

    /**
     * @param studentNumber
     * @param degreeType
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public IStudentCurricularPlan readActiveStudentCurricularPlan(Integer studentNumber,
            DegreeType degreeType) throws ExcepcaoPersistencia;

    /**
     * @param studentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    public void delete(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;

    /**
     * @param studentNumber
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readAllFromStudent(int studentNumber /*
                                                      * , StudentType
                                                      * studentType
                                                      */
    ) throws ExcepcaoPersistencia;

    /**
     * @param studentNumber
     * @param degreeType
     * @param specialization
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public IStudentCurricularPlan readActiveStudentAndSpecializationCurricularPlan(
            Integer studentNumber, DegreeType degreeType, Specialization specialization)
            throws ExcepcaoPersistencia;

    /**
     * @param degreeCurricularPlan
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia;

    /**
     * @param username
     * @return List with the Student's Curricular Plans
     * @throws ExcepcaoPersistencia
     */
    public List readByUsername(String username) throws ExcepcaoPersistencia;

    /**
     * @param number
     * @param degreeType
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByStudentNumberAndDegreeType(Integer number, DegreeType degreeType)
            throws ExcepcaoPersistencia;

    public List readAllByDegreeCurricularPlanAndState(IDegreeCurricularPlan degreeCurricularPlan,
            StudentCurricularPlanState state) throws ExcepcaoPersistencia;

    /**
     * @param branch
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByBranch(IBranch branch) throws ExcepcaoPersistencia;

    /**
     * @param branch
     * @return @throws
     *         ExcepcaoPersistencia
     */
    //	public List readByCurricularCourseScope(ICurricularCourseScope
    // curricularCourseScope) throws
    // ExcepcaoPersistencia;
    public List readAllByStudentAntState(IStudent student, StudentCurricularPlanState state)
            throws ExcepcaoPersistencia;

    public IStudentCurricularPlan readByStudentDegreeCurricularPlanAndState(IStudent student,
            IDegreeCurricularPlan degreeCurricularPlan, StudentCurricularPlanState state)
            throws ExcepcaoPersistencia;

    public List readAllActiveStudentCurricularPlan(Integer studentNumber) throws ExcepcaoPersistencia;

    public List readAllByStudentAndDegreeCurricularPlan(IStudent student,
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia;

    public List readAllByDegreeCurricularPlanAndSpecialization(
            IDegreeCurricularPlan degreeCurricularPlan, Specialization specialization)
            throws ExcepcaoPersistencia;

    public List readAllActiveStudentCurricularPlansWithEnrollmentsInExecutionPeriod(
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readAllActiveStudentCurricularPlansByDegreeWithEnrollmentsInExecutionPeriod(
            IExecutionPeriod executionPeriod, IDegree degree) throws ExcepcaoPersistencia;

    public List readAllByStudentNumberAndSpecialization(Integer studentNumber, DegreeType degreeType,
            Specialization specialization) throws ExcepcaoPersistencia;

    public List readAllByStudentNumberAndSpecializationAndState(Integer studentNumber,
            DegreeType degreeType, Specialization specialization, StudentCurricularPlanState state)
            throws ExcepcaoPersistencia;

}