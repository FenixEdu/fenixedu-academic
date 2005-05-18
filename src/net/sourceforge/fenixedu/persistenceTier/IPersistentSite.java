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

import net.sourceforge.fenixedu.domain.ISite;

public interface IPersistentSite extends IPersistentObject {

    public ISite readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia;

}