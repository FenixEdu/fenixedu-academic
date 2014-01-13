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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadOldIquiriesSummaryByDegreeID {

    private static final Logger logger = LoggerFactory.getLogger(ReadOldIquiriesSummaryByDegreeID.class);

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
                    logger.error(ex.getMessage(), ex);
                }

                return iois;
            }
        });

        return oldInquiriesSummaryList;
    }
}