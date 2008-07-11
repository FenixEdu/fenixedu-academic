<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="label.debtManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>

<logic:present name="searchBean" property="residenceMonth">
<bean:define id="monthOID" name="searchBean" property="residenceMonth.OID"/>

<ul>
	<li>
		<html:link page="<%= "/residenceEventManagement.do?method=generatePaymentCodes&selectedMonth=" +  monthOID %>">Gerar códigos</html:link>
	</li>
</ul>
</logic:present>

<fr:form action="/residenceEventManagement.do">
	<fr:edit id="searchEventMonth" name="searchBean" schema="edit.import.residence.bean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thleft thlight"/>
			</fr:layout>
			<fr:destination name="postback" path="/residenceEventManagement.do?method=manageResidenceEvents"/>
	</fr:edit>
</fr:form>

<logic:present name="searchBean">
	<logic:present name="searchBean" property="residenceMonth">
		<fr:view name="searchBean" property="residenceMonth.events" schema="show.residenceEvent">	
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
			</fr:layout>
			<fr:destination name="personLink" path="/residenceEventManagement.do?method=viewPersonResidenceEvents&person=${person.OID}&month=${residenceMonth.OID}"/>
		</fr:view>
	</logic:present>
</logic:present>

