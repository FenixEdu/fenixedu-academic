/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.student;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.Delegate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public interface IPersistentDelegate extends IPersistentObject {
    public List readByDegreeAndExecutionYear(Degree degree, ExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    public List readByDegreeAndExecutionYearAndYearType(Degree degree, ExecutionYear executionYear,
            DelegateYearType type) throws ExcepcaoPersistencia;

    /**
     *@deprecated This method doesn't read a unique student! TODO
     */
    public Delegate readByStudent(Student student) throws ExcepcaoPersistencia;

    public List readDegreeDelegateByDegreeAndExecutionYear(Degree degree, ExecutionYear executionYear)
            throws ExcepcaoPersistencia;
}