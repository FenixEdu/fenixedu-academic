package net.sourceforge.fenixedu.injectionCode;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.injectionCode.injector.CodeGenerator;

public class FenixDomainObjectLogCallsGenerator implements CodeGenerator {

    public String getCode(Map<String, Object> annotationParameters) {

	StringBuilder buffer = new StringBuilder();
	String methodName = (String) annotationParameters.get("actionName");
	String[] parameters = (String[]) annotationParameters.get("parameters");

	buffer.append("{");
	
	buffer.append("java.util.Map ___map = new java.util.HashMap();");

	if (parameters != null) {
	    for (int i = 0; i < parameters.length; i++) {
		if (parameters[i] != null && !StringUtils.isEmpty(parameters[i].trim())) {
		    buffer.append("___map.put(\"" + parameters[i].trim() + "\"," + parameters[i].trim() + ");");
		}
	    }
	}

	if(methodName != null) {
            buffer.append("new net.sourceforge.fenixedu.domain.DomainObjectActionLog(");
            buffer.append("net.sourceforge.fenixedu.injectionCode.AccessControl.getUserView().getPerson(),this,");
            buffer.append("\"" + methodName.trim()).append("\",___map);");
	}
	
	buffer.append("}");

	return buffer.toString();
    }
}
