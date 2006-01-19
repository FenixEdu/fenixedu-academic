/*
 * Created on 3/Set/2003, 15:10:43
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoModality;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryModality;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 3/Set/2003, 15:10:43
 * 
 */
public class GetAllModalities implements IService {

	public List run() throws BDException, ExcepcaoPersistencia {
		List infoCases = new LinkedList();

		ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IPersistentSeminaryModality persistentModality = persistenceSupport
				.getIPersistentSeminaryModality();
		List cases = persistentModality.readAll();

		for (Iterator iterator = cases.iterator(); iterator.hasNext();) {
			Modality modality = (Modality) iterator.next();

			infoCases.add(InfoModality.newInfoFromDomain(modality));
		}

		return infoCases;
	}

}