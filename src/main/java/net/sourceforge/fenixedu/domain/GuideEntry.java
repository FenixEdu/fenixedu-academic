package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;

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
