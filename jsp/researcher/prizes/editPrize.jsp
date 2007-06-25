<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.editPrize"/></h2>


	<bean:define id="success" value=""/>
	<bean:define id="cancel" value=""/>
	<bean:define id="input" value=""/>

	<logic:notPresent name="result">
		<bean:define id="prizeID" name="prize" property="idInternal"/>
		<bean:define id="success" value="<%= "/prizes/prizeManagement.do?method=showPrize&oid=" + prizeID%>"/>
		<bean:define id="cancel" value="<%= "/prizes/prizeManagement.do?method=showPrize&oid=" + prizeID%>"/>
		<bean:define id="input" value="<%= "/prizes/prizeManagement.do?method=editPrize&oid=" + prizeID%>"/>	
	</logic:notPresent>
	<logic:present name="result">
		<bean:define id="resultId" name="result" property="idInternal"/>
		<bean:define id="resultType" name="result" property="class.simpleName"/>
		<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>"/>
		<bean:define id="prizeID" name="prize" property="idInternal"/>
		<logic:present name="unit">
			<bean:define id="unitID" name="unit" property="idInternal"/>
			<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
		</logic:present>	
		
		<bean:define id="success" value="<%= "/resultPublications/associatePrize.do?" + parameters  %>"/>
		<bean:define id="cancel" value="<%= "/resultPublications/associatePrize.do?" +  parameters%>"/>
		<bean:define id="input" value="<%= "/resultPublications/editPrize.do?oid=" + prizeID + "&amp;" + parameters %>"/>
	</logic:present>
		
<logic:present name="prize">

	<fr:edit name="prize" schema="prize.details">
		<fr:layout>
			<fr:property name="classes" value="tstyle5"/>
		</fr:layout>
	<fr:destination name="success" path="<%= success %>"/>
	<fr:destination name="cancel" path="<%= cancel %>"/>
	<fr:destination name="input" path="<%= input %>"/>
	</fr:edit>
</logic:present>

<logic:notPresent name="prize">
	<bean:message key="label.not.permitted.to.edit.prize" bundle="RESEARCHER_RESOURCES"/>
</logic:notPresent>