/*
 * ISitioPersistente.java
 *
 * Created on 25 de Agosto de 2002, 0:53
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author ars
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;

public interface IPersistentSite extends IPersistentObject {

    //	List readAnnouncementsByExecutionCourse(IDisciplinaExecucao
    // executionCourse) throws ExcepcaoPersistencia;
    ISite readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

    List readAll() throws ExcepcaoPersistencia;

    void delete(ISite site) throws ExcepcaoPersistencia;

}