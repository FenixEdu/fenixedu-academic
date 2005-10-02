/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.teacher;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentOldPublication extends IPersistentObject {

	public List readAllByTeacherIdAndOldPublicationType(final Integer teacherId,
            final OldPublicationType oldPublicationType) throws ExcepcaoPersistencia;
}