/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.student;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.student.IDelegate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public interface IPersistentDelegate extends IPersistentObject {
    public List readByDegreeAndExecutionYear(IDegree degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    public List readByDegreeAndExecutionYearAndYearType(IDegree degree, IExecutionYear executionYear,
            DelegateYearType type) throws ExcepcaoPersistencia;

    /**
     *@deprecated This method doesn't read a unique student! TODO
     */
    public IDelegate readByStudent(IStudent student) throws ExcepcaoPersistencia;

    public List readDegreeDelegateByDegreeAndExecutionYear(IDegree degree, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;
}