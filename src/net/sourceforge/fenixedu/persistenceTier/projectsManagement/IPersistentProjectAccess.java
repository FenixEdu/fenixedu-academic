/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.projectsManagement;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProjectAccess extends IPersistentObject {

    public abstract IProjectAccess readByPersonIdAndProjectAndDate(Integer personId, Integer keyProject) throws ExcepcaoPersistencia;

    public abstract List<IProjectAccess> readByCoordinator(Integer coordinatorCode) throws ExcepcaoPersistencia;

    public abstract List<IProjectAccess> readByPersonUsernameAndCoordinatorAndDate(String username, Integer coordinatorCode)
            throws ExcepcaoPersistencia;

    public List<Integer> readProjectCodesByPersonUsernameAndDateAndCC(String username, String cc) throws ExcepcaoPersistencia;

    public List<Integer> readCCCodesByPersonUsernameAndDate(String username) throws ExcepcaoPersistencia;

    public abstract List<Integer> readProjectCodesByPersonUsernameAndCoordinator(String username, Integer coordinatorCode, boolean all)
            throws ExcepcaoPersistencia;

    public abstract List<Integer> readCoordinatorsCodesByPersonUsernameAndDatesAndCC(String username, String cc) throws ExcepcaoPersistencia;

    public abstract boolean hasPersonProjectAccess(String personUsername, Integer projectCode) throws ExcepcaoPersistencia;

    public abstract int countByPersonAndProject(IPerson person, Integer keyProject) throws ExcepcaoPersistencia;

    public abstract int countByPersonAndCC(IPerson person, Boolean cc) throws ExcepcaoPersistencia;

    public abstract void deleteByPersonAndCC(IPerson person, Boolean cc) throws ExcepcaoPersistencia;

    public abstract void deleteByPersonAndDate(IPerson person) throws ExcepcaoPersistencia;

    public abstract void delete(IProjectAccess projectAccess) throws ExcepcaoPersistencia;
}