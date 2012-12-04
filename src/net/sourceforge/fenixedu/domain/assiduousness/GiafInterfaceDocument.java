package net.sourceforge.fenixedu.domain.assiduousness;

import java.sql.SQLException;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.GiafInterfaceBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class GiafInterfaceDocument extends GiafInterfaceDocument_Base {

    public GiafInterfaceDocument(GiafInterfaceBean giafInterfaceBean) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreatedWhen(new DateTime());
	Employee employee = AccessControl.getPerson().getEmployee();
	setModifiedBy(employee);
	new GiafInterfaceFile(giafInterfaceBean, this);
    }

    @Service
    public static GiafInterfaceDocument createGiafInterfaceDocument(GiafInterfaceBean giafInterfaceBean) throws SQLException,
	    ExcepcaoPersistencia {
	GiafInterfaceDocument giafInterfaceDocument = new GiafInterfaceDocument(giafInterfaceBean);
	return giafInterfaceDocument;
    }

    public void delete() {
	removeModifiedBy();
	removeRootDomainObject();
	getGiafInterfaceFile().delete();
	removeGiafInterfaceFile();
	deleteDomainObject();
    }
}
