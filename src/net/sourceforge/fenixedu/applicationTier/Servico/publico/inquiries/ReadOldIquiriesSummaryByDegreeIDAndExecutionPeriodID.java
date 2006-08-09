/*
 * Created on Nov 22, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldIquiriesSummaryByDegreeIDAndExecutionPeriodID extends Service {

	public List run(Integer degreeID, Integer executionPeriodID) throws FenixServiceException,
			ExcepcaoPersistencia {
		Degree degree = rootDomainObject.readDegreeByOID(degreeID);
		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);

		if (degree == null) {
			throw new FenixServiceException("nullDegreeId");
		}
		if (executionPeriod == null) {
			throw new FenixServiceException("nullExecutionPeriodId");
		}
		
		List<OldInquiriesSummary> oldInquiriesSummaryList = degree.getOldInquiriesSummariesByExecutionPeriod(executionPeriod); 

		CollectionUtils.transform(oldInquiriesSummaryList, new Transformer() {

			public Object transform(Object oldInquiriesSummary) {
				InfoOldInquiriesSummary iois = new InfoOldInquiriesSummary();
				try {
					iois.copyFromDomain((OldInquiriesSummary) oldInquiriesSummary);

				} catch (Exception ex) {
				}

				return iois;
			}
		});

		return oldInquiriesSummaryList;
	}
}
