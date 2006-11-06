<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

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
