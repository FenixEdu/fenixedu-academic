/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeObjectives;

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