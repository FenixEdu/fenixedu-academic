/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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