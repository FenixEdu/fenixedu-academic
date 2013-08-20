/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.formbeans.masterDegreeAdminOffice;

import org.apache.struts.action.ActionForm;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class FixSibsPaymentFileEntriesForm extends ActionForm {

    private static final long serialVersionUID = 1L;

    private Integer page = 0;

    private String sibsPaymentFileEntryId;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSibsPaymentFileEntryId() {
        return sibsPaymentFileEntryId;
    }

    public void setSibsPaymentFileEntryId(String sibsPaymentFileEntryId) {
        this.sibsPaymentFileEntryId = sibsPaymentFileEntryId;
    }
}
