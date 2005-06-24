/*
 * Created on Jan 11, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle;

import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.IProject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProject {
    public abstract List<IProject> readByUserLogin(String userLogin) throws ExcepcaoPersistencia;

    public abstract List<IProject> readByProjectsCodes(List<Integer> projectCodes) throws ExcepcaoPersistencia;

    public abstract List<IProject> readByCoordinatorAndNotProjectsCodes(Integer coordinatorId, List projectCodes) throws ExcepcaoPersistencia;

    public abstract IProject readProject(Integer projectCode) throws ExcepcaoPersistencia;

    public abstract boolean isUserProject(Integer userCode, Integer projectCode) throws ExcepcaoPersistencia;

    public abstract int countUserProject(Integer userCode) throws ExcepcaoPersistencia;
}