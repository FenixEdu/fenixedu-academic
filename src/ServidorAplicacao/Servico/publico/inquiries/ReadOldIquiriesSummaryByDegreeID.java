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
public class ReadOldIquiriesSummaryByDegreeID implements IServico{

    private static ReadOldIquiriesSummaryByDegreeID service = new ReadOldIquiriesSummaryByDegreeID();
    
    private ReadOldIquiriesSummaryByDegreeID() {
    }
    
    public String getNome() {
        return "ReadOldIquiriesSummaryByDegreeID";
    }
    
    public static ReadOldIquiriesSummaryByDegreeID getService() {
        return service;
    }
    
    public List run(Integer degreeID) throws FenixServiceException {
        List oldInquiriesSummaryList = null;

        try {
            if (degreeID == null) {
                throw new FenixServiceException("nullDegreeId");
            }
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentOldInquiriesSummary pois = sp.getIPersistentOldInquiriesSummary();
        
            oldInquiriesSummaryList = pois.readByDegreeId(degreeID);
            
            CollectionUtils.transform(oldInquiriesSummaryList,new Transformer(){

                public Object transform(Object oldInquiriesSummary) {
                    InfoOldInquiriesSummary iois = new InfoOldInquiriesSummary();
                    try {
                        iois.copyFromDomain((IOldInquiriesSummary) oldInquiriesSummary);

                    } catch (Exception ex) {
                    	ex.printStackTrace();
                    }
                    
                    //TODO: DEBUG
//                    System.out.println("Transform:");
//                    System.out.println("getAverage2_3 original: " + ((IOldInquiriesSummary)oldInquiriesSummary).getAverage2_3());
//                    System.out.println("getAverage2_3 Transformado:" + iois.getAverage2_3());
//                    System.out.println("getAverage2_7 original: \"" + ((IOldInquiriesSummary)oldInquiriesSummary).getAverage2_7() + "\"");
//                    System.out.println("getAverage2_7 Transformado: \"" + iois.getAverage2_7() + "\"\n\n");
                    
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
