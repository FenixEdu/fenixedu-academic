package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.DocumentType;
import net.sourceforge.fenixedu.util.GraduationType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public interface IPrice extends IDomainObject {

    public GraduationType getGraduationType();

    public Double getPrice();

    public String getDescription();

    public DocumentType getDocumentType();

    public void setGraduationType(GraduationType graduationType);

    public void setPrice(Double price);

    public void setDescription(String description);

    public void setDocumentType(DocumentType documentType);
}