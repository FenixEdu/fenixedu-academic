/*
 * IPersistentCurricularCourse.java
 *
 * Created on 28 of December 2002, 11:56
 */

/**
 *
 * @author Nuno Nunes & Joana Mota
 */
 

package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IBranch;
import ServidorPersistente.exceptions.ExistingPersistentException;

public interface IPersistentBranch extends IPersistentObject {

    public IBranch readBranchByNameAndCode(String name, String code) throws ExcepcaoPersistencia;
    public ArrayList readAllBranches() throws ExcepcaoPersistencia;
    public void writeBranch(IBranch branch) throws ExcepcaoPersistencia, ExistingPersistentException;
    public void deleteBranch(IBranch branch) throws ExcepcaoPersistencia;
    public void deleteAllBranches() throws ExcepcaoPersistencia;
}
