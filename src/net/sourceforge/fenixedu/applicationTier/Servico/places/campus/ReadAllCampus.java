/*
 * Created on Dec 5, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.places.campus;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author jpvl
 */
public class ReadAllCampus implements IService {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {
		List infoCampusList;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCampus campus = sp.getIPersistentCampus();
		List campusList = campus.readAll();
		infoCampusList = (List) CollectionUtils.collect(campusList, new Transformer() {

			public Object transform(Object input) {
				Campus campus = (Campus) input;
				InfoCampus infoCampus = InfoCampus.newInfoFromDomain(campus);
				return infoCampus;
			}
		});

		return infoCampusList;

	}
}