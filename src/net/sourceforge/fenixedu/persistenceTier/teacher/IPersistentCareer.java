/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentCareer extends IPersistentObject {

    List readAllByTeacherIdAndCareerType(Integer teacherId, CareerType careerType)
            throws ExcepcaoPersistencia;
}