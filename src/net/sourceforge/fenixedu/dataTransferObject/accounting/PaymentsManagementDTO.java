/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaymentsManagementDTO implements Serializable {

    public List<EntryDTO> entryDTOs;
    
    public PaymentsManagementDTO() {
        entryDTOs = new ArrayList<EntryDTO>();
    }

    public List<EntryDTO> getEntryDTOs() {
        return entryDTOs;
    }

}
