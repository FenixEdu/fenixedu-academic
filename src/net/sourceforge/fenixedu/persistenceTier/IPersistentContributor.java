/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersistentContributor extends IPersistentObject {
    /**
     * 
     * @param contributorNumber
     * @return IContributor
     * @throws ExcepcaoPersistencia
     */
    public IContributor readByContributorNumber(Integer contributorNumber) throws ExcepcaoPersistencia;

    /**
     * 
     * @return List
     * @throws ExcepcaoPersistencia
     * @throws ExistingPersistentException
     */
    public List readAll() throws ExcepcaoPersistencia, ExistingPersistentException;

    /**
     * 
     * @param contributorNumber
     * @return List
     * @throws ExcepcaoPersistencia
     */
    public List readContributorListByNumber(Integer contributorNumber) throws ExcepcaoPersistencia;

}