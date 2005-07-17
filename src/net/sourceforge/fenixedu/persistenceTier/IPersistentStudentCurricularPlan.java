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

import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

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
     * @param studentNumber
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readAllFromStudent(int studentNumber) throws ExcepcaoPersistencia;

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

    public List readAllByStudentNumberAndSpecialization(Integer studentNumber, DegreeType degreeType,
            Specialization specialization) throws ExcepcaoPersistencia;

    public List readAllByStudentNumberAndSpecializationAndState(Integer studentNumber,
            DegreeType degreeType, Specialization specialization, StudentCurricularPlanState state)
            throws ExcepcaoPersistencia;

}