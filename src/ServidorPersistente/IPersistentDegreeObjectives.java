/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDegree;
import Dominio.IDegreeObjectives;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorPersistente
 *  
 */
public interface IPersistentDegreeObjectives extends IPersistentObject {

    public IDegreeObjectives readCurrentByDegree(IDegree degree) throws ExcepcaoPersistencia;

    public List readByDegree(IDegree degree) throws ExcepcaoPersistencia;

}