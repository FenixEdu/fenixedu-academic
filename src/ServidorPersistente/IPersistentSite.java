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

import Dominio.IDisciplinaExecucao;
import Dominio.ISite;

public interface IPersistentSite {
    
	List readAnnouncementsByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
//    ICurriculum readCurriculumByNomeSitio (String Nome) throws ExcepcaoPersistencia;
	ISite readByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
    List readAll() throws ExcepcaoPersistencia;
//    List readSiteOwners(String name) throws ExcepcaoPersistencia;
//    List readSiteTeachers(String name) throws ExcepcaoPersistencia;
    void lockWrite(ISite site) throws ExcepcaoPersistencia;
    void delete(ISite site) throws ExcepcaoPersistencia;
    void deleteAll() throws ExcepcaoPersistencia;
}
