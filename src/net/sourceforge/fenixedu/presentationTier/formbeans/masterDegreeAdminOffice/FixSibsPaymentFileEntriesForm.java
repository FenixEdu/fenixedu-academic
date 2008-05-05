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

    private Integer sibsPaymentFileEntryId;

    public Integer getPage() {
	return page;
    }

    public void setPage(Integer page) {
	this.page = page;
    }

    public Integer getSibsPaymentFileEntryId() {
	return sibsPaymentFileEntryId;
    }

    public void setSibsPaymentFileEntryId(Integer sibsPaymentFileEntryId) {
	this.sibsPaymentFileEntryId = sibsPaymentFileEntryId;
    }
}
