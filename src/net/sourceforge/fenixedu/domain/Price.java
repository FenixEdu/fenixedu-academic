package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

public class Price extends Price_Base {

    public Price() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Price(GraduationType graduationType, DocumentType documentType, String description, Double price) {
	this();
	this.setDescription(description);
	this.setDocumentType(documentType);
	this.setGraduationType(graduationType);
	this.setPrice(price);
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static Price readByGraduationTypeAndDocumentTypeAndDescription(GraduationType graduationType,
	    DocumentType documentType, String description) {
	for (final Price price : RootDomainObject.getInstance().getPricesSet()) {
	    if (price.getGraduationType() == graduationType && price.getDocumentType() == documentType
		    && price.getDescription().equals(description)) {
		return price;
	    }
	}
	return null;
    }

    public static List<Price> readByGraduationTypeAndDocumentTypes(GraduationType graduationType, List<DocumentType> documentTypes) {
	final List<Price> result = new ArrayList<Price>();
	for (final Price price : RootDomainObject.getInstance().getPricesSet()) {
	    if (price.getGraduationType() == graduationType && documentTypes.contains(price.getDocumentType())) {
		result.add(price);
	    }
	}
	return result;
    }
}
