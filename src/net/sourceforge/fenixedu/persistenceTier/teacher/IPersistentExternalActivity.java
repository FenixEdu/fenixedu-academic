/*
 * Created on 15/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentExternalActivity extends IPersistentObject {
    public List readAllByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}