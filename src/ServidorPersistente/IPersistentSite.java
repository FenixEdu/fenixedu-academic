/*
 * ISitioPersistente.java
 *
 * Created on 25 de Agosto de 2002, 0:53
 */

package ServidorPersistente;

/**
 *
 * @author  ars
 */

import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.ISite;

public interface IPersistentSite extends IPersistentObject{
    
//	List readAnnouncementsByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
	ISite readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;
    List readAll() throws ExcepcaoPersistencia;
    void lockWrite(ISite site) throws ExcepcaoPersistencia;
    void delete(ISite site) throws ExcepcaoPersistencia;
   
}
