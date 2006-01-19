/*
 * Created on Nov 22, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldIquiriesSummaryByDegreeID extends Service {

	public List run(Integer degreeID) throws FenixServiceException, ExcepcaoPersistencia {
		List oldInquiriesSummaryList = null;

		if (degreeID == null) {
			throw new FenixServiceException("nullDegreeId");
		}
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentOldInquiriesSummary pois = sp.getIPersistentOldInquiriesSummary();

		oldInquiriesSummaryList = pois.readByDegreeId(degreeID);

		CollectionUtils.transform(oldInquiriesSummaryList, new Transformer() {

			public Object transform(Object oldInquiriesSummary) {
				InfoOldInquiriesSummary iois = new InfoOldInquiriesSummary();
				try {
					iois.copyFromDomain((OldInquiriesSummary) oldInquiriesSummary);

				} catch (Exception ex) {
					ex.printStackTrace();
				}

				return iois;
			}
		});

		return oldInquiriesSummaryList;
	}
}
