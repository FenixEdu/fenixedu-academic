package net.sourceforge.fenixedu.injectionCode;

import java.util.Map;

import net.sourceforge.fenixedu.injectionCode.injector.CodeGenerator;

public class FenixDomainObjectLogCallsGenerator implements CodeGenerator {

    public String getCode(Map<String, Object> annotationParameters) {

	StringBuilder buffer = new StringBuilder();
	String methodName = (String) annotationParameters.get("actionName");
	String[] parameters = (String[]) annotationParameters.get("parameters");

	buffer.append("{");
	buffer.append("java.util.Map __map = new java.util.HashMap();");

	for (int i = 0; i < parameters.length; i++) {
	    buffer.append("__map.put(\"" + parameters[i].trim() + "\"," + parameters[i].trim() + ");");
	}

	buffer.append("new net.sourceforge.fenixedu.domain.DomainObjectActionLog(");
	buffer.append("net.sourceforge.fenixedu.injectionCode.AccessControl.getUserView().getPerson(),this,");
	buffer.append("\"" + methodName.trim()).append("\",__map);");
		
	buffer.append("}");
	
	return buffer.toString();
    }
}
