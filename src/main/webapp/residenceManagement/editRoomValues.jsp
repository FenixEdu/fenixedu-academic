<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="title.modify.roomValues" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>

<bean:define id="monthOID" name="residenceMonth" property="OID"/>

<div class="forminline">
	<fr:form action="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID=" + monthOID%>">
		
		<fr:edit name="priceTable" schema="configuration.roomValues">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thleft mbottom1"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>

		<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
	
	<fr:form action="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID=" + monthOID%>">
		<html:submit><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
</div>