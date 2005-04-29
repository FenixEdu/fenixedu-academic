/*
 * Created on Dec 21, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ExecutionDegreeCoordinatorAuthorizationFilter extends DomainObjectAuthorizationFilter {

    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    protected boolean verifyCondition(IUserView id, Integer objectId) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

        IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                ExecutionDegree.class, objectId);

        ITeacher coordinator = persistentTeacher.readTeacherByUsername(id.getUtilizador());
        List executionDegrees = persistentExecutionDegree.readByTeacher(coordinator);

        return executionDegrees.contains(executionDegree);
    }
}