package ServidorPersistente;

import java.util.List;

import Dominio.IDegree;
import Dominio.IExecutionYear;

/**
 * @author Tânia Pousão Created on 30/Out/2003
 */
public interface IPersistentDegreeInfo extends IPersistentObject {
    public List readDegreeInfoByDegree(IDegree degree) throws ExcepcaoPersistencia;

    public List readDegreeInfoByDegreeAndExecutionYear(IDegree degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;
}