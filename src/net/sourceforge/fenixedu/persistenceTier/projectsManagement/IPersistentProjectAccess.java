/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.projectsManagement;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProjectAccess extends IPersistentObject {

    public abstract ProjectAccess readByPersonIdAndProjectAndDate(Integer personId, Integer keyProject) throws ExcepcaoPersistencia;

    public abstract List<ProjectAccess> readByCoordinator(Integer coordinatorCode) throws ExcepcaoPersistencia;

    public abstract List<ProjectAccess> readByPersonUsernameAndCoordinatorAndDate(String username, Integer coordinatorCode)
            throws ExcepcaoPersistencia;

    public List<Integer> readProjectCodesByPersonUsernameAndDateAndCC(String username, String cc) throws ExcepcaoPersistencia;

    public List<Integer> readCCCodesByPersonUsernameAndDate(String username) throws ExcepcaoPersistencia;

    public abstract List<Integer> readProjectCodesByPersonUsernameAndCoordinator(String username, Integer coordinatorCode, boolean all)
            throws ExcepcaoPersistencia;

    public abstract List<Integer> readCoordinatorsCodesByPersonUsernameAndDatesAndCC(String username, String cc) throws ExcepcaoPersistencia;

    public abstract boolean hasPersonProjectAccess(String personUsername, Integer projectCode) throws ExcepcaoPersistencia;

    public abstract int countByPersonAndProject(Person person, Integer keyProject) throws ExcepcaoPersistencia;

    public abstract int countByPersonAndCC(Person person, Boolean cc) throws ExcepcaoPersistencia;

    public abstract void deleteByPersonAndCC(Person person, Boolean cc) throws ExcepcaoPersistencia;

    public abstract void deleteByPersonAndDate(Person person) throws ExcepcaoPersistencia;

    public abstract void delete(ProjectAccess projectAccess) throws ExcepcaoPersistencia;
}