<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>

<logic:present name="message">
	<span class="error"><!-- Error messages go here --><bean:write name="message"/></span>
</logic:present>

<logic:notEmpty name="objectToEdit">
	<bean:define id="schemaName">Edit<bean:write name="objectToEdit" property="class.simpleName"/>DomainObject</bean:define>		
	<fr:edit name="objectToEdit" schema="<%= schemaName %>" action="/domainObjectManager.do?method=prepare"/>
</logic:notEmpty>
<logic:empty name="objectToEdit">
	<bean:message key="label.domainObjectManager.instance.not.found" bundle="MANAGER_RESOURCES"/>
</logic:empty>
