/*
 * Created on Nov 22, 2004
 * 
 */
package ServidorAplicacao.Servico.publico.inquiries;

import java.util.List;

import org.apache.commons.collections.Transformer;

import DataBeans.inquiries.InfoOldInquiriesSummary;
import Dominio.inquiries.IOldInquiriesSummary;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.inquiries.IPersistentOldInquiriesSummary;

import commons.CollectionUtils;

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
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
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
