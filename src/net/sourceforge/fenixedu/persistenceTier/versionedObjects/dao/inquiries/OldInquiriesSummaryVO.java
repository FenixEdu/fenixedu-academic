/*
 * Created on 27/Jun/2005 - 11:56:48
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.inquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class OldInquiriesSummaryVO extends VersionedObjectsBase implements IPersistentOldInquiriesSummary {

    public List readAll() throws ExcepcaoPersistencia {
		List<OldInquiriesSummary> res = (List<OldInquiriesSummary>) readAll(OldInquiriesSummary.class);
		return res;
    }
    
    public List readByDegreeIdAndExecutionPeriod(Integer degreeID, Integer executionPeriodID) throws ExcepcaoPersistencia {		
		Degree degree = (Degree) readByOID(Degree.class, degreeID);
		List<OldInquiriesSummary> inquiriesSummaries = degree.getAssociatedOldInquiriesSummaries();
		
		List<OldInquiriesSummary> res = new ArrayList<OldInquiriesSummary>();
		for(OldInquiriesSummary iois : inquiriesSummaries) {
			if(iois.getExecutionPeriod().getIdInternal().equals(executionPeriodID))
				res.add(iois);
		}
		
		return res;
    }

    public List readByDegreeId(Integer degreeID) throws ExcepcaoPersistencia {
		Degree degree = (Degree) readByOID(Degree.class, degreeID);
		List<OldInquiriesSummary> res = degree.getAssociatedOldInquiriesSummaries();
		return res;
    }

}
