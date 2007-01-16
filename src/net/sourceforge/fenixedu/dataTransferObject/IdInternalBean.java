/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class IdInternalBean implements Serializable {

    private Integer idInternal;

    public IdInternalBean(Integer idInternal) {
	super();
	this.idInternal = idInternal;
    }

    public Integer getIdInternal() {
	return idInternal;
    }

}
