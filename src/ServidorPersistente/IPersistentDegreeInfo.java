package ServidorPersistente;

import java.util.List;

import Dominio.ICurso;
import Dominio.IExecutionYear;

/**
 * @author Tânia Pousão Created on 30/Out/2003
 */
public interface IPersistentDegreeInfo extends IPersistentObject {
    public List readDegreeInfoByDegree(ICurso degree) throws ExcepcaoPersistencia;

    public List readDegreeInfoByDegreeAndExecutionYear(ICurso degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;
}