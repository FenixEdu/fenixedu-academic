<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<bean:define id="result" name="result"/>
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
	<bean:define id="path" value="<%= "/resultParticipations/createParticipator.do?" + parameters %>"/>
	<bean:define id="cancel" value="<%= "/resultParticipations/selectUnit.do?" + parameters %>"/>
	
<logic:present role="RESEARCHER">
<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.createPerson"/></h2>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean"/>
<bean:define id="schema" value="<%= "resultParticipation.fullCreation" + (bean.getResult().getIsPossibleSelectPersonRole() ? "WithRole" : "") + ".external.readOnly"%>" type="java.lang.String"/>

<logic:notPresent name="createUnit">
<fr:edit id="beanForExternalPerson" name="bean" schema="<%= schema %>" action="<%= path %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= cancel %>"/>
</fr:edit>	
</logic:notPresent>
 
<logic:present name="createUnit">
	<span class="infoop2">
	<bean:message key="label.informationForCreateUnit" bundle="RESEARCHER_RESOURCES"/>	
	</span>
	<table class="tstyle5">
	<tr><th><bean:message key="label.person" bundle="RESEARCHER_RESOURCES"/></th><td><fr:view name="bean" property="personParticipationType"/></td></tr>
	<tr><th><bean:message key="label.personName" bundle="RESEARCHER_RESOURCES"/></th><td><fr:view name="bean" property="participatorName"/></td></tr>
	<tr><th><bean:message key="label.role" bundle="RESEARCHER_RESOURCES"/></th><td><fr:view name="bean" property="unitParticipationType"/></td></tr>
	<tr><th><bean:message key="label.unit" bundle="RESEARCHER_RESOURCES"/></th><td><fr:view name="bean" property="organizationName"/></td></tr>
	</table>

	<bean:define id="name" name="bean" property="participatorName"/>
	<bean:define id="role" name="bean" property="role"/>

	<div class="dinline forminline">
	<fr:form action="createUnit" action="<%= "/resultParticipations/createUnit.do?" + parameters %>">
		<fr:edit id="externalUnitBean" name="bean" visible="false"/>
		<html:submit><bean:message key="label.createUnit" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</fr:form>
	<fr:form action="createUnit" action="<%= "/resultParticipations/prepareCreateParticipator.do?" + parameters + "&amp;name=" + name + "&amp;role=" + role%>">
		<html:submit><bean:message key="label.changeUnit" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</fr:form>	
	</div>
	
</logic:present>

</logic:present>