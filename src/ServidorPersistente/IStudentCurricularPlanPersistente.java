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

import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

import Dominio.IStudentCurricularPlan;

public interface IStudentCurricularPlanPersistente extends IPersistentObject {
    IStudentCurricularPlan readActiveStudentCurricularPlan(Integer studentNumber, TipoCurso degreeType ) throws ExcepcaoPersistencia;
    void lockWrite(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia, ExistingPersistentException;
    void delete(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
    void deleteAll() throws ExcepcaoPersistencia;
    public List readAllFromStudent(int studentNumber /*, StudentType studentType */) throws ExcepcaoPersistencia;
}
