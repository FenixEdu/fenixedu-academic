package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IBranch;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public interface IPersistentBranch extends IPersistentObject {

//    public IBranch readBranchByNameAndCode(String name, String code) throws ExcepcaoPersistencia;
    public ArrayList readAll() throws ExcepcaoPersistencia;
    public void lockWrite(IBranch branch) throws ExcepcaoPersistencia, ExistingPersistentException;
    public void delete(IBranch branch) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}
