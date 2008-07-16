<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="label.priceTable" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>

<bean:define id="monthOID" name="residenceMonth" property="OID"/>

<fr:form action="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID=" + monthOID%>">
	
	<fr:edit name="residenceMonth" property="year" schema="residenceYear.configuration.roomValues">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thleft"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
</fr:form>