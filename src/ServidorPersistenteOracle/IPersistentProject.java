/*
 * Created on Jan 11, 2005
 *
 */
package ServidorPersistenteOracle;

import java.util.List;

import Dominio.projectsManagement.IProject;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProject {
    public abstract List getAllProjectsByUserLogin(String userLogin) throws ExcepcaoPersistencia;

    public abstract IProject readProject(Integer projectCode) throws ExcepcaoPersistencia;

    public abstract boolean isUserProject(Integer userCode, Integer projectCode) throws ExcepcaoPersistencia;
}