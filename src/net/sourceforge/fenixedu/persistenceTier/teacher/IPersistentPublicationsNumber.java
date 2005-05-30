/*
 * Created on 21/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.teacher;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.PublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentPublicationsNumber extends IPersistentObject {

    public IPublicationsNumber readByTeacherAndPublicationType(ITeacher teacher,
            PublicationType publicationType) throws ExcepcaoPersistencia;

}