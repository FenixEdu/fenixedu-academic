/*
 * Created on 21/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.teacher;

import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.OrientationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentOrientation extends IPersistentObject {
    public Orientation readByTeacherIdAndOrientationType(Integer teacherId, OrientationType orientationType)
            throws ExcepcaoPersistencia;

}