package ServidorPersistente;

import java.util.ArrayList;

import Dominio.ICurso;
import Dominio.IPlanoCurricularCurso;

public interface IPlanoCurricularCursoPersistente extends IPersistentObject {
    
    public ArrayList lerTodosOsPlanosCurriculares() throws ExcepcaoPersistencia;
    public void escreverPlanoCurricular(IPlanoCurricularCurso planoCurricular) throws ExcepcaoPersistencia;
    public void apagarPlanoCurricular(IPlanoCurricularCurso planoCurricular) throws ExcepcaoPersistencia;
    public void apagarTodosOsPlanosCurriculares() throws ExcepcaoPersistencia;
    public IPlanoCurricularCurso readByNameAndDegree(String name, ICurso degree) throws ExcepcaoPersistencia;
     
}
