package net.sourceforge.fenixedu.dataTransferObject.coordinator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeLog;
import net.sourceforge.fenixedu.domain.DegreeLog.DegreeLogTypes;
import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class SearchDegreeLogBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Degree degree;
    private Boolean viewPhoto;

    private Collection<DegreeLogTypes> degreeLogsTypes;
    private Collection<DegreeLog> degreeLogs;

    public String getEnumerationResourcesString(String name) {
	return ResourceBundle.getBundle("resources/EnumerationResources", Language.getLocale()).getString(name);
    }

    public String getApplicationResourcesString(String name) {
	return ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale()).getString(name);
    }

    public SearchDegreeLogBean(Degree degree) {
	setDegree(degree);
	setViewPhoto(true);
	setDegreeLogTypes(DegreeLogTypes.valuesAsList());
	degreeLogs = new ArrayList<DegreeLog>();
    }

    public Degree getDegree() {
	return this.degree;
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
    }

    public Boolean getViewPhoto() {
	return viewPhoto;
    }

    public void setViewPhoto(Boolean viewPhoto) {
	this.viewPhoto = viewPhoto;
    }

    public Collection<DegreeLogTypes> getDegreeLogTypes() {
	return degreeLogsTypes;
    }

    public Collection<DegreeLogTypes> getDegreeLogTypesAll() {
	return DegreeLogTypes.valuesAsList();
    }

    public void setDegreeLogTypes(Collection<DegreeLogTypes> degreeLogsTypes) {
	this.degreeLogsTypes = degreeLogsTypes;
    }

    public Collection<DegreeLog> getDegreeLogs() {
	Collection<DegreeLog> dlogs = new ArrayList<DegreeLog>();
	for (DegreeLog degreeLog : degreeLogs) {
	    dlogs.add(degreeLog);
	}
	return dlogs;
    }

    public void setDegreeLogs(Collection<DegreeLog> degreeLogs) {
	Collection<DegreeLog> dlogs = new ArrayList<DegreeLog>();
	for (DegreeLog dlog : degreeLogs) {
	    dlogs.add(dlog);
	}
	this.degreeLogs = dlogs;
    }

    public Predicate<DegreeLog> getFilters() {

	Collection<Predicate<DegreeLog>> filters = new ArrayList<Predicate<DegreeLog>>();

	if (getDegreeLogTypes().size() < DegreeLogTypes.values().length) {
	    filters.add(new InlinePredicate<DegreeLog, Collection<DegreeLogTypes>>(getDegreeLogTypes()) {

		@Override
		public boolean eval(DegreeLog degreeLog) {
		    return getValue().contains(degreeLog.getDegreeLogType());
		}

	    });
	}

	return new AndPredicate<DegreeLog>(filters);
    }

    public String getLabel() {

	String logTypeValues = "";

	for (DegreeLogTypes logType : DegreeLogTypes.values()) {
	    if (!logTypeValues.isEmpty()) {
		logTypeValues += ", ";
	    }
	    logTypeValues += getEnumerationResourcesString(logType.getQualifiedName());
	}

	return String.format("%s : %s", getApplicationResourcesString("log.label.selectLogType"), logTypeValues);

    }

    public String getSearchElementsAsParameters() {
	String parameters = "";

	parameters += "&amp;degree=" + getDegree().getIdInternal();
	if (viewPhoto) {
	    parameters += "&amp;viewPhoto=true";
	}
	if (getDegreeLogTypes() != null && !getDegreeLogTypes().isEmpty()) {
	    parameters += "&amp;degreeLogTypes=";
	    for (DegreeLogTypes logType : getDegreeLogTypes()) {
		parameters += logType.toString() + ":";
	    }
	}

	return parameters;
    }
}
