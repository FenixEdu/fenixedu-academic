/*
 * Created on Nov 22, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldIquiriesSummaryByDegreeIDAndExecutionPeriodID implements IServico{

    private static ReadOldIquiriesSummaryByDegreeIDAndExecutionPeriodID service = new ReadOldIquiriesSummaryByDegreeIDAndExecutionPeriodID();
    
    private ReadOldIquiriesSummaryByDegreeIDAndExecutionPeriodID() {
    }
    
    public String getNome() {
        return "ReadOldIquiriesSummaryByDegreeIDAndExecutionPeriodID";
    }
    
    public static ReadOldIquiriesSummaryByDegreeIDAndExecutionPeriodID getService() {
        return service;
    }
    
    public List run(Integer degreeID, Integer executionPeriodID) throws FenixServiceException {
        List oldInquiriesSummaryList = null;

        try {
            if (degreeID == null) {
                throw new FenixServiceException("nullDegreeId");
            }
            if (executionPeriodID == null) {
                throw new FenixServiceException("nullExecutionPeriodId");
            }
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentOldInquiriesSummary pois = sp.getIPersistentOldInquiriesSummary();
        
            oldInquiriesSummaryList = pois.readByDegreeIdAndExecutionPeriod(degreeID, executionPeriodID);
            
            CollectionUtils.transform(oldInquiriesSummaryList,new Transformer(){

                public Object transform(Object oldInquiriesSummary) {
                    InfoOldInquiriesSummary iois = new InfoOldInquiriesSummary();
                    try {
                        iois.copyFromDomain((IOldInquiriesSummary) oldInquiriesSummary);

                    } catch (Exception ex) {
                    }
                                        
                    return iois;
                }
             	});
                            
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return oldInquiriesSummaryList;
    }  
}
