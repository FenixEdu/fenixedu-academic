/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.student.ExternalRegistrationDataBean;
import net.sourceforge.fenixedu.domain.student.ExternalRegistrationData;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalRegistrationDataFactoryExecutor {

    @SuppressWarnings("serial")
    public static class ExternalRegistrationDataEditor extends ExternalRegistrationDataBean implements
	    FactoryExecutor, Serializable {

	public ExternalRegistrationDataEditor(ExternalRegistrationData externalRegistrationData) {
	    super(externalRegistrationData);
	}

	public Object execute() {
	    getExternalRegistrationData().edit(this);
	    return null;
	}

    }

}
