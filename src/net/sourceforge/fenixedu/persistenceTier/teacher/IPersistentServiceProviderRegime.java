/*
 * Created on 16/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.teacher;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentServiceProviderRegime extends IPersistentObject {
    public IServiceProviderRegime readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}