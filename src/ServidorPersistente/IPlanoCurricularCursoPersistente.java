package ServidorPersistente;

import java.util.ArrayList;

import Dominio.ICurso;
import Dominio.IPlanoCurricularCurso;

public interface IPlanoCurricularCursoPersistente extends IPersistentObject {
    
    /**
     * @deprecated
     * @param nome
     * @param sigla
     * @return IPlanoCurricularCurso
     * @throws ExcepcaoPersistencia
     */
    public IPlanoCurricularCurso lerPlanoCurricularPorNomeESigla(String nome, String sigla) throws ExcepcaoPersistencia;
    public IPlanoCurricularCurso lerPlanoCurricularPorNomeSiglaCurso(String nome, String sigla, ICurso curso) throws ExcepcaoPersistencia;
    public ArrayList lerTodosOsPlanosCurriculares() throws ExcepcaoPersistencia;
    public void escreverPlanoCurricular(IPlanoCurricularCurso planoCurricular) throws ExcepcaoPersistencia;
    public void apagarPlanoCurricularPorNomeESigla(String nome, String sigla) throws ExcepcaoPersistencia;
    public void apagarPlanoCurricular(IPlanoCurricularCurso planoCurricular) throws ExcepcaoPersistencia;
    public void apagarTodosOsPlanosCurriculares() throws ExcepcaoPersistencia;
    public IPlanoCurricularCurso readByName(String name) throws ExcepcaoPersistencia; 
}
