/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICurso;
import Dominio.IDegreeObjectives;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorPersistente
 *  
 */
public interface IPersistentDegreeObjectives extends IPersistentObject {

    public IDegreeObjectives readCurrentByDegree(ICurso degree) throws ExcepcaoPersistencia;

    public List readByDegree(ICurso degree) throws ExcepcaoPersistencia;

}