/*
 * Created on Nov 22, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesSummary;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldIquiriesSummaryByDegreeID {

    @Atomic
    public static Collection run(String degreeID) throws FenixServiceException {
        Degree degree = FenixFramework.getDomainObject(degreeID);

        if (degree == null) {
            throw new FenixServiceException("nullDegreeId");
        }

        Collection<OldInquiriesSummary> oldInquiriesSummaryList = degree.getAssociatedOldInquiriesSummaries();

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