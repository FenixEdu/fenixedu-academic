/*
 * Created on 21/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentContributor;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ContributorVO extends VersionedObjectsBase implements
		IPersistentContributor {

	public IContributor readByContributorNumber(Integer contributorNumber)
			throws ExcepcaoPersistencia {

		List<IContributor> contributors = readAll();
		for (IContributor contributor : contributors) {
			if (contributor.getContributorNumber().equals(contributorNumber)) {
				return contributor;
			}

		}
		return null;
	}

	public List readAll() throws ExcepcaoPersistencia {
		return (List) readAll(Contributor.class);
	}
}