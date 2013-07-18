/*
 * Created on Nov 22, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesSummary;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldIquiriesSummaryByDegreeID {

    @Service
    public static List run(Integer degreeID) throws FenixServiceException {
        Degree degree = RootDomainObject.getInstance().readDegreeByOID(degreeID);

        if (degree == null) {
            throw new FenixServiceException("nullDegreeId");
        }

        List<OldInquiriesSummary> oldInquiriesSummaryList = degree.getAssociatedOldInquiriesSummaries();

        CollectionUtils.transform(oldInquiriesSummaryList, new Transformer() {

            @Override
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