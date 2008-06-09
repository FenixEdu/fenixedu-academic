package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.util.EnumSet;

import net.sourceforge.fenixedu.domain.protocols.util.ProtocolAction;
import net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ProtocolAction2SqlVarcharConverter implements FieldConversion {

    private static final String protocolActionTypeSeparator = ",";

    private static final String protocolActionStringSeparator = ";";

    public Object javaToSql(Object source) {
        if (source instanceof ProtocolAction) {
            ProtocolAction protocolAction = (ProtocolAction) source;
            StringBuilder dest = new StringBuilder();
            for (ProtocolActionType protocolActionType : protocolAction.getProcotolActionTypes()) {
                if (dest.length() != 0) {
                    dest.append(protocolActionTypeSeparator);
                }
                dest.append(protocolActionType.name());
            }
            dest.append(protocolActionStringSeparator);
            if (!StringUtils.isEmpty(protocolAction.getOtherTypes())) {
                dest.append(protocolAction.getOtherTypes());
            }
            return dest.toString();
        }
        return source;
    }

    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            String[] tokens = ((String) source).split(protocolActionStringSeparator, 2);
            EnumSet<ProtocolActionType> protocolsActionsTypes = null;
            for (String enumTokens : tokens[0].split(protocolActionTypeSeparator)) {
                if (!StringUtils.isEmpty(enumTokens)) {
                    if (protocolsActionsTypes == null) {
                        protocolsActionsTypes = EnumSet.noneOf(ProtocolActionType.class);
                    }
                    protocolsActionsTypes.add(ProtocolActionType.valueOf(enumTokens));
                }
            }
            return new ProtocolAction(protocolsActionsTypes, tokens[1].length() != 0 ? tokens[1] : null);
        }
        return source;
    }
}
