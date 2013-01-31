package net.sourceforge.fenixedu.domain.protocols.util;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ProtocolAction implements Serializable {

	private final EnumSet<ProtocolActionType> protocolActionTypes;

	private final String otherTypes;

	public ProtocolAction(List<ProtocolActionType> protocolActionTypes, String otherTypes) {
		EnumSet<ProtocolActionType> actionTypes = EnumSet.noneOf(ProtocolActionType.class);
		for (ProtocolActionType actionType : protocolActionTypes) {
			actionTypes.add(actionType);
		}
		this.protocolActionTypes = actionTypes;
		this.otherTypes = otherTypes;
	}

	public ProtocolAction(EnumSet<ProtocolActionType> protocolActionTypes, String otherTypes) {
		this.protocolActionTypes = protocolActionTypes;
		this.otherTypes = otherTypes;
	}

	public String getOtherTypes() {
		return otherTypes;
	}

	public EnumSet<ProtocolActionType> getProtocolActionTypes() {
		return protocolActionTypes;
	}

	public boolean contains(List<ProtocolActionType> protocolActionTypes) {
		return getProtocolActionTypes().containsAll(protocolActionTypes);
	}

	public boolean contains(ProtocolActionType protocolActionType) {
		return getProtocolActionTypes().contains(protocolActionType);
	}

	public String getText() {
		StringBuilder stringBuilder = new StringBuilder();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
		if (getProtocolActionTypes() != null) {
			Iterator<ProtocolActionType> iterator = getProtocolActionTypes().iterator();
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

	private static final String protocolActionTypeSeparator = ",";
	private static final String protocolActionStringSeparator = ";";

	@Override
	public String toString() {
		StringBuilder dest = new StringBuilder();
		for (ProtocolActionType protocolActionType : getProtocolActionTypes()) {
			if (dest.length() != 0) {
				dest.append(protocolActionTypeSeparator);
			}
			dest.append(protocolActionType.name());
		}

		dest.append(protocolActionStringSeparator);
		if (!StringUtils.isEmpty(getOtherTypes())) {
			dest.append(getOtherTypes());
		}
		return dest.toString();
	}

	public static ProtocolAction fromString(String extRep) {
		String[] tokens = extRep.split(protocolActionStringSeparator, 2);
		EnumSet<ProtocolActionType> protocolsActionsTypes = EnumSet.noneOf(ProtocolActionType.class);
		for (String enumTokens : tokens[0].split(protocolActionTypeSeparator)) {
			if (!StringUtils.isEmpty(enumTokens)) {
				protocolsActionsTypes.add(ProtocolActionType.valueOf(enumTokens));
			}
		}
		return new ProtocolAction(protocolsActionsTypes, tokens[1].length() != 0 ? tokens[1] : null);
	}
}
