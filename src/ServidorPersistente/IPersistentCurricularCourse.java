/*
 * IPersistentCurricularCourse.java
 *
 * Created on 28 of December 2002, 11:56
 */

/**
 *
 * @author Nuno Nunes & Joana Mota
 */
 

package ServidorPersistente;

import java.util.ArrayList;

import Dominio.ICurricularCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

public interface IPersistentCurricularCourse extends IPersistentObject {
    
// TO DO    
//    public boolean alterarDisciplinaCurricular(ICurricularCourse disciplinaCurricular);
//    public ArrayList lerDisciplinasCurricularesDeDisciplinaExecucao(DisciplinaExecucao discExec);
//    public boolean associarDisciplinaCurricularADisciplinaExecucao(int chaveDisciplinaCurricular, int chaveDisciplinaExecucao);
//    public boolean apagarAssociacoesDeDisciplinaCurricularADisciplinaExecucao();

    public ICurricularCourse readCurricularCourseByNameCode(String name, String code);
    public ArrayList readAllCurricularCourses();
    public void writeCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia, ExistingPersistentException;

    public boolean deleteCurricularCourse(ICurricularCourse curricularCourse);
    public boolean deleteAllCurricularCourse();

// TO REMEMBER
// JÁ NÃO EXISTEM ALGUNS DESTES ATRIBUTOS:
//    public CurricularCourse lerDisciplinaCurricular(String codigo, int anoCurricular, int semestre, int chaveLicenciatura, int chaveRamo);
//    public ArrayList lerDisciplinasCurricularesPorCodigo(String codigo);
}
