/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentCareer extends IPersistentObject {

    List readAllByTeacherAndCareerType(ITeacher teacher, CareerType careerType)
            throws ExcepcaoPersistencia;
}