/*
 * IStudentCurricularPlan.java
 *
 * Created on 21 of December de 2002, 16:57
 */

package ServidorPersistente;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

import java.util.List;

import Dominio.IStudentCurricularPlan;

public interface IStudentCurricularPlanPersistente extends IPersistentObject {
    IStudentCurricularPlan readActiveStudentCurricularPlan(int studentNumber /*, StudentType studentType */) throws ExcepcaoPersistencia;
    void lockWrite(IStudentCurricularPlan curricularPlan) throws ExcepcaoPersistencia;
    void delete(IStudentCurricularPlan curricularPlan) throws ExcepcaoPersistencia;
    void deleteAll() throws ExcepcaoPersistencia;
    public List readAllFromStudent(int studentNumber /*, StudentType studentType */) throws ExcepcaoPersistencia;
}
