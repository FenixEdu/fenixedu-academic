/*
 * Created on 21/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IOrientation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.OrientationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentOrientation extends IPersistentObject {
    public IOrientation readByTeacherAndOrientationType(ITeacher teacher, OrientationType orientationType)
            throws ExcepcaoPersistencia;

    public List readAllByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}