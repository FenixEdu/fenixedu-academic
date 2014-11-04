/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.math.BigDecimal;
import java.util.Collection;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.reimbursementGuide.ReimbursementGuideEntry;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideEntry extends GuideEntry_Base {

    public GuideEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public GuideEntry(GraduationType graduationType, DocumentType documentType, String description, Integer quantity,
            BigDecimal price, Guide guide) {
        this();
        this.setDescription(description);
        this.setGuide(guide);
        this.setDocumentType(documentType);
        this.setGraduationType(graduationType);
        this.setPriceBigDecimal(price);
        this.setQuantity(quantity);

    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        if (getPaymentTransaction() != null) {
            getPaymentTransaction().delete();
        }

        Guide guide = getGuide();
        setGuide(null);
        guide.updateTotalValue();

        setRootDomainObject(null);

        deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getReimbursementGuideEntriesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "guide.entry.cannot.be.deleted"));
        }
    }

    @Deprecated
    public Double getPrice() {
        return getPriceBigDecimal().doubleValue();
    }

    @Deprecated
    public void setPrice(Double price) {
        setPriceBigDecimal(BigDecimal.valueOf(price));
    }

    public BigDecimal getValueWithAdjustment() {

        BigDecimal reimbursedValue = BigDecimal.ZERO;
        for (final ReimbursementGuideEntry reimbursementGuideEntry : getReimbursementGuideEntriesSet()) {
            if (reimbursementGuideEntry.getReimbursementGuide().isPayed()) {
                reimbursedValue = reimbursedValue.add(reimbursementGuideEntry.getValueBigDecimal());
            }
        }

        return getPriceBigDecimal().subtract(reimbursedValue);

    }

}
