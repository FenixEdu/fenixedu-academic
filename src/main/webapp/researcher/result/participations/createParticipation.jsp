<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="participations" name="result" property="orderedResultParticipations"/>
<bean:define id="result" name="result"/>
<bean:define id="resultId" name="result" property="externalId"/>
<bean:define id="parameters" value="<%="resultId=" + resultId + "&amp;resultType=" + result.getClass().getSimpleName()%>"/>
<logic:present name="unit">
	<bean:define id="unitID" name="unit" property="externalId"/>
	<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
</logic:present>
<!-- Action paths definitions -->

<bean:define id="prepareEdit" value="<%="/resultParticipations/prepareEdit.do?" + parameters%>"/>
<bean:define id="create" value="<%="/resultParticipations/create.do?" + parameters%>"/>
<bean:define id="cancel" value="<%="/resultParticipations/backToResult.do?" + parameters%>"/>
<bean:define id="createExternalPerson" value="<%= "/resultParticipations/prepareCreateParticipator.do?" + parameters%>"/>

<logic:notEqual name="bean" property="beanExternal" value="true">
	<p class="mtop2 mbottom05"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultParticipation.add"/></b></p>
</logic:notEqual>
<logic:equal name="bean" property="beanExternal" value="true">
 	<p class="mtop2 mbottom05"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultParticipation.addExternal"/></b></p>
</logic:equal>

<logic:messagesPresent name="messages" message="true">
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="needToCreatePerson"> 	
	<logic:messagesNotPresent name="messages" message="true">
		<div class="warning0">
			<strong><bean:message key="label.attention" bundle="RESEARCHER_RESOURCES"/>:</strong><br/>
			<bean:message key="label.informationForCreateUser" bundle="RESEARCHER_RESOURCES"/>
		</div>
		<br/>
	</logic:messagesNotPresent>
</logic:present>

<logic:notPresent name="duringCreation">
<!-- Schema definitions -->
<bean:define id="createSchema" name="createSchema" type="java.lang.String"/>

<%-- From to Create Participation --%>
<div class="dinline forminline">
	<fr:form action="<%="/resultParticipations/createWrapper.do?" + parameters%>">
		<fr:edit id="bean" name="bean" schema="<%= createSchema %>" >
			<fr:layout name="tabular">
		        <fr:property name="classes" value="tstyle5 thlight mtop05 dinline"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="exception" path="<%= prepareEdit %>"/>	
			<fr:destination name="invalid" path="<%= prepareEdit %>"/>	
			<fr:destination name="cancel" path="<%= cancel %>"/>
			<fr:destination name="postBack" path="/resultParticipations/changeParticipationType.do"/>	
			<fr:destination name="change.unitType" path="/resultParticipations/changeUnitType.do"/>
		</fr:edit>
		<br/>
		<div class="mvert15"></div>
		<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
		<logic:present name="needToCreatePerson"> 	
			<html:submit property="createNew"><bean:message key="label.createPerson" bundle="RESEARCHER_RESOURCES"/></html:submit>
	 	</logic:present>
	</fr:form>
	<fr:form action="<%= cancel %>">
		<html:submit><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
</div>
</logic:notPresent>

<logic:present name="duringCreation">
<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean"/>
<bean:define id="schema" value="<%= "resultParticipation.fullCreation" + (bean.getResult().getIsPossibleSelectPersonRole() ? "WithRole" : "") + ".external.readOnly"%>" type="java.lang.String"/>
<bean:define id="path" value="<%= "/resultParticipations/createParticipator.do?" + parameters %>"/>

	<bean:define id="schema" value="<%= "resultParticipation.fullCreation" + (bean.getResult().getIsPossibleSelectPersonRole() ? "WithRole" : "") + ".external.readOnly"%>" type="java.lang.String"/>

	<div class="dinline forminline">
		<fr:form action="<%= "/resultParticipations/unitWrapper.do?" + parameters %>">
			<fr:edit id="beanForExternalPerson" name="bean" schema="<%= schema %>">
				<fr:layout name="tabular">
			        <fr:property name="classes" value="tstyle5 thlight thright mtop05 dinline"/>
			        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
			</fr:edit>
			<br/>
			<div class="mvert1"></div>
			<html:submit property="createNewUnit"><bean:message key="button.submit" bundle="RESEARCHER_RESOURCES"/></html:submit>
		</fr:form>
		<fr:form action="<%= "/resultParticipations/prepareEdit.do?" + parameters %>">
			<html:submit><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:submit>
		</fr:form>		
	</div>
	
</logic:present>
