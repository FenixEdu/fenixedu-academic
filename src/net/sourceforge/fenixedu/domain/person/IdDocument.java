package net.sourceforge.fenixedu.domain.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.pstm.Transaction;

public class IdDocument extends IdDocument_Base {

    public IdDocument(final Person person, final String value, final IdDocumentTypeObject idDocumentType) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setPerson(person);
	setIdDocumentType(idDocumentType);
	setValue(value);
    }

    public IdDocument(final Person person, final String value, final IDDocumentType documentType) {
	this(person, value, IdDocumentTypeObject.readByIDDocumentType(documentType));
    }

    public static Collection<IdDocument> find(final String idDocumentValue) {
	// final Collection<IdDocument> idDocuments = new
	// ArrayList<IdDocument>();
	// for (final IdDocument idDocument :
	// RootDomainObject.getInstance().getIdDocumentsSet()) {
	// if (idDocument.getValue().equalsIgnoreCase(idDocumentValue)) {
	// idDocuments.add(idDocument);
	// }
	// }

	return findWithDatabase(idDocumentValue);
    }

    private static Collection<IdDocument> findWithDatabase(final String idDocumentValue) {
	// For performance reasons...
	final Collection<IdDocument> idDocuments = new ArrayList<IdDocument>();

	PreparedStatement stmt = null;
	try {
	    final Connection connection = Transaction.getCurrentJdbcConnection();
	    stmt = connection.prepareStatement("SELECT ID_INTERNAL FROM ID_DOCUMENT WHERE UPPER(VALUE) = '"
		    + idDocumentValue.toUpperCase() + "'");
	    final ResultSet resultSet = stmt.executeQuery();
	    if (resultSet.next()) {
		idDocuments.add(RootDomainObject.getInstance().readIdDocumentByOID(resultSet.getInt(1)));
	    }
	} catch (SQLException e) {
	    throw new Error(e);
	} finally {
	    if (stmt != null) {
		try {
		    stmt.close();
		} catch (SQLException e) {
		    throw new Error(e);
		}
	    }
	}

	return idDocuments;

    }

    public void setIdDocumentType(IDDocumentType documentType) {
	super.setIdDocumentType(IdDocumentTypeObject.readByIDDocumentType(documentType));
    }

    public void delete() {
	removePerson();
	removeIdDocumentType();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
