/*
 * Created on Feb 18, 2005
 *
 */
package ServidorPersistente.projectsManagement;

import java.util.List;

import Dominio.IPessoa;
import Dominio.projectsManagement.IProjectAccess;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProjectAccess extends IPersistentObject {

    public abstract IProjectAccess readByPersonIdAndProjectAndDate(Integer personId, Integer keyProject) throws ExcepcaoPersistencia;

    public abstract List readByCoordinator(Integer coordinatorCode) throws ExcepcaoPersistencia;

    public abstract List readByPersonUsernameAndCoordinatorAndDate(String username, Integer coordinatorCode) throws ExcepcaoPersistencia;

    public abstract List readProjectCodesByPersonUsernameAndDate(String username) throws ExcepcaoPersistencia;

    public abstract List readProjectCodesByPersonUsernameAndCoordinator(String username, Integer coordinatorCode, boolean all)
            throws ExcepcaoPersistencia;

    public abstract List readCoordinatorsCodesByPersonUsernameAndDates(String username) throws ExcepcaoPersistencia;

    public abstract boolean hasPersonProjectAccess(String personUsername, Integer projectCode) throws ExcepcaoPersistencia;

    public abstract int countByPersonAndProject(IPessoa person, Integer keyProject) throws ExcepcaoPersistencia;

    public abstract int countByPerson(IPessoa person) throws ExcepcaoPersistencia;

    public abstract void deleteByPerson(IPessoa person) throws ExcepcaoPersistencia;

    public abstract void deleteByPersonAndDate(IPessoa person) throws ExcepcaoPersistencia;

    public abstract void delete(IProjectAccess projectAccess) throws ExcepcaoPersistencia;
}