/*
 * Created on Jan 11, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle;

import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.Project;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProject {
    public abstract List<Project> readByUserLogin(String userLogin) throws ExcepcaoPersistencia;

    public abstract List<Project> readByProjectsCodes(List<Integer> projectCodes) throws ExcepcaoPersistencia;

    public abstract List<Project> readByCoordinatorAndNotProjectsCodes(Integer coordinatorId, List projectCodes)
	    throws ExcepcaoPersistencia;

    public abstract Project readProject(Integer projectCode) throws ExcepcaoPersistencia;

    public abstract boolean isUserProject(Integer userCode, Integer projectCode) throws ExcepcaoPersistencia;

    public abstract int countUserProject(Integer userCode) throws ExcepcaoPersistencia;
}