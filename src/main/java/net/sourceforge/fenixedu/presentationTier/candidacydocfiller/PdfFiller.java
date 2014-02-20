package net.sourceforge.fenixedu.presentationTier.candidacydocfiller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.lang.StringUtils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;

public abstract class PdfFiller {
    protected AcroFields form;

    protected String getMail(Person person) {
        if (person.hasInstitutionalEmailAddress()) {
            return person.getInstitutionalEmailAddressValue();
        } else {
            String emailForSendingEmails = person.getEmailForSendingEmails();
            return emailForSendingEmails != null ? emailForSendingEmails : StringUtils.EMPTY;
        }
    }

    public abstract ByteArrayOutputStream getFilledPdf(Person person) throws IOException, DocumentException;

    protected void setField(String fieldName, String fieldContent) throws IOException, DocumentException {
        if (fieldContent != null) {
            form.setField(fieldName, fieldContent);
        }
    }
}
