<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.editPrize"/></h2>


<bean:define id="success" value=""/>
<bean:define id="cancel" value=""/>
<bean:define id="input" value=""/>

<logic:notPresent name="result">
	<bean:define id="prizeID" name="prize" property="externalId"/>
	<bean:define id="success" value="<%= "/prizes/prizeManagement.do?method=showPrize&oid=" + prizeID%>"/>
	<bean:define id="cancel" value="<%= "/prizes/prizeManagement.do?method=showPrize&oid=" + prizeID%>"/>
	<bean:define id="input" value="<%= "/prizes/prizeManagement.do?method=editPrize&oid=" + prizeID%>"/>	
</logic:notPresent>

<logic:present name="result">
	<bean:define id="resultId" name="result" property="externalId"/>
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>"/>
	<bean:define id="prizeID" name="prize" property="externalId"/>
	<logic:present name="unit">
		<bean:define id="unitID" name="unit" property="externalId"/>
		<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
	</logic:present>	
	
	<bean:define id="success" value="<%= "/resultPublications/associatePrize.do?" + parameters  %>"/>
	<bean:define id="cancel" value="<%= "/resultPublications/associatePrize.do?" +  parameters%>"/>
	<bean:define id="input" value="<%= "/resultPublications/editPrize.do?oid=" + prizeID + "&amp;" + parameters %>"/>
</logic:present>
		
<logic:present name="prize">

	<fr:edit name="prize" schema="create.prize">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	<fr:destination name="success" path="<%= success %>"/>
	<fr:destination name="cancel" path="<%= cancel %>"/>
	<fr:destination name="input" path="<%= input %>"/>
	</fr:edit>
</logic:present>

<logic:notPresent name="prize">
	<bean:message key="label.not.permitted.to.edit.prize" bundle="RESEARCHER_RESOURCES"/>
</logic:notPresent>