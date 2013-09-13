<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/units" prefix="un" %>

<bean:define id="result" name="result"/>
<bean:define id="resultId" name="result" property="externalId"/>
<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
	<logic:present name="unit">
		<bean:define id="unitID" name="unit" property="externalId"/>
		<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
	</logic:present>
<bean:define id="path" value="<%= "/resultParticipations/createUnit.do?" + parameters %>"/>
<bean:define id="cancel" value="<%= "/resultParticipations/selectUnit.do?" + parameters %>"/>
	
<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.createUnit"/></h2>

<fr:edit id="externalUnitBean" name="externalUnitBean" schema="createExternalUnit"  action="<%= path %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= cancel %>"/>
</fr:edit>