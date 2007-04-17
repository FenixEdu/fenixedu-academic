package net.sourceforge.fenixedu.domain.protocols.util;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.lang.StringUtils;

public class ProtocolAction implements Serializable {

    private EnumSet<ProtocolActionType> procotolActionTypes;

    private String otherTypes;

    public ProtocolAction(List<ProtocolActionType> procotolActionTypes, String otherTypes) {
	EnumSet<ProtocolActionType> actionTypes = EnumSet.noneOf(ProtocolActionType.class);
	for (ProtocolActionType actionType : procotolActionTypes) {
	    actionTypes.add(actionType);
	}
	setProcotolActionTypes(actionTypes);
	setOtherTypes(otherTypes);
    }

    public ProtocolAction(EnumSet<ProtocolActionType> protocolsActionsTypes, String otherTypes) {
	setProcotolActionTypes(protocolsActionsTypes);
	setOtherTypes(otherTypes);
    }

    public String getOtherTypes() {
	return otherTypes;
    }

    public void setOtherTypes(String otherTypes) {
	this.otherTypes = otherTypes;
    }

    public EnumSet<ProtocolActionType> getProcotolActionTypes() {
	return procotolActionTypes;
    }

    public void setProcotolActionTypes(EnumSet<ProtocolActionType> procotolActionTypes) {
	this.procotolActionTypes = procotolActionTypes;
    }

    public boolean contains(List<ProtocolActionType> protocolActionTypes) {
	for (ProtocolActionType protocolActionType : protocolActionTypes) {
	    if (getProcotolActionTypes() == null
		    || !getProcotolActionTypes().contains(protocolActionType)) {
		return false;
	    }
	}
	return true;
    }

    public String getText() {
	StringBuilder stringBuilder = new StringBuilder();
	ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources",
		LanguageUtils.getLocale());
	if (getProcotolActionTypes() != null) {
	    Iterator<ProtocolActionType> iterator = getProcotolActionTypes().iterator();
	    while (iterator.hasNext()) {
		ProtocolActionType protocolActionType = iterator.next();
		stringBuilder.append(resourceBundle.getString(protocolActionType.name()));
		if (iterator.hasNext()) {
		    stringBuilder.append(", ");
		}
	    }
	}
	if (!StringUtils.isEmpty(getOtherTypes())) {
	    if (!StringUtils.isEmpty(stringBuilder.toString())) {
		stringBuilder.append(", ");
	    }
	    stringBuilder.append(getOtherTypes());
	}
	return stringBuilder.toString();
    }
}
