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
package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;

import org.fenixedu.bennu.core.domain.Bennu;

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
        if (canBeDeleted()) {
            if (hasPaymentTransaction()) {
                getPaymentTransaction().delete();
            }

            Guide guide = getGuide();
            setGuide(null);
            guide.updateTotalValue();

            setRootDomainObject(null);

            deleteDomainObject();
        } else {
            throw new DomainException("guide.entry.cannot.be.deleted");
        }
    }

    public boolean canBeDeleted() {
        return !hasAnyReimbursementGuideEntries();
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
        for (final ReimbursementGuideEntry reimbursementGuideEntry : getReimbursementGuideEntries()) {
            if (reimbursementGuideEntry.getReimbursementGuide().isPayed()) {
                reimbursedValue = reimbursedValue.add(reimbursementGuideEntry.getValueBigDecimal());
            }
        }

        return getPriceBigDecimal().subtract(reimbursedValue);

    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry> getReimbursementGuideEntries() {
        return getReimbursementGuideEntriesSet();
    }

    @Deprecated
    public boolean hasAnyReimbursementGuideEntries() {
        return !getReimbursementGuideEntriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasPriceBigDecimal() {
        return getPriceBigDecimal() != null;
    }

    @Deprecated
    public boolean hasPaymentTransaction() {
        return getPaymentTransaction() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasQuantity() {
        return getQuantity() != null;
    }

    @Deprecated
    public boolean hasGuide() {
        return getGuide() != null;
    }

    @Deprecated
    public boolean hasDocumentType() {
        return getDocumentType() != null;
    }

    @Deprecated
    public boolean hasGraduationType() {
        return getGraduationType() != null;
    }

}
