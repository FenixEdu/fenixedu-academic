<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="associations" name="result" property="resultUnitAssociations"/>
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="result" name="result"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>"/>		
		<logic:present name="unit">
		<bean:define id="unitID" name="unit" property="idInternal"/>
		<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
	</logic:present>
	<!-- Action paths definitions -->

	<bean:define id="create" value="<%="/resultAssociations/createUnitAssociation.do?" + parameters%>" />
	<bean:define id="prepareEdit" value="<%="/resultAssociations/prepareEditUnitAssociations.do?" + parameters%>" />
	<bean:define id="prepareAlter" value="<%="/resultAssociations/prepareEditUnitRole.do?" + parameters%>"/>	
	<bean:define id="remove" value="<%="/resultAssociations/removeUnitAssociation.do?" + parameters%>"/>
	<bean:define id="backLink" value="<%="/resultAssociations/backToResult.do?" + parameters%>" />
	<bean:define id="addSugestion" value="<%="/resultAssociations/addSugestion.do?" + parameters%>"/>

<%-- Title --%>		
	<logic:equal name="resultType" value="ResultPatent">
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.management.title"/></em>
	</logic:equal>
	
	<logic:notEqual name="resultType" value="ResultPatent">
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
	</logic:notEqual>

	<h2><bean:message key="label.prizeUnits" bundle="RESEARCHER_RESOURCES"/></h2>
	
	<h3><fr:view name="result" property="title"/></h3>

	<bean:define id="linkKey" value="link.goBackToView" toScope="request"/>
	
	<logic:present name="publicationCreated">
			<bean:define id="linkKey" value="link.viewPublicationPage" toScope="request"/>
			<p><span class="success0"><bean:message key="label.publication.created.with.success" bundle="RESEARCHER_RESOURCES"/>.</span></p>
			<p class="mvert05"><bean:message key="label.publication.created.note" bundle="RESEARCHER_RESOURCES"/></p>
	</logic:present>
	
	
	<%-- Go to previous page --%>
	<p class="mtop05"><html:link page="<%= backLink %>"><bean:message bundle="RESEARCHER_RESOURCES" key="<%= linkKey%>"/></html:link></p> 
	
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<%-- List of existing unit associations --%>
	<p class="mtop25 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></b></p>
	<logic:empty name="associations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="associations">
		<logic:present name="editExisting">
			<fr:edit id="editRole" name="associations"schema="resultUnitAssociation.details" action="<%= prepareEdit %>">
				<fr:layout name="tabular-row">
					<fr:property name="classes" value="tstyle2" />
					<fr:property name="columnClasses" value=",acenter,acenter"/>
				</fr:layout>
				<fr:destination name="exception" path="<%= prepareEdit + "&editExisting=true" %>" />
				<fr:destination name="invalid" path="<%= prepareEdit + "&editExisting=true"%>"/>	
			</fr:edit>
		</logic:present>
		<logic:notPresent name="editExisting">	
			<fr:view name="associations"schema="resultUnitAssociation.details">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2" />
					<fr:property name="columnClasses" value=",acenter,acenter"/>
					
					<fr:property name="link(remove)" value="<%= remove %>"/>
					<fr:property name="param(remove)" value="idInternal/associationId"/>
					<fr:property name="key(remove)" value="link.remove"/>
					<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
				</fr:layout>
			</fr:view>
			<%-- 
			<html:link page="<%=prepareAlter%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.edit"/></html:link>
			--%>
		</logic:notPresent>
	</logic:notEmpty>
	
	
	<%-- Unit suggestions --%>
	<logic:present name="unitBean">
	
	<div class="infoop2">
		<bean:message key="label.unit.suggestion.explanation" bundle="RESEARCHER_RESOURCES"/>
	</div>
	
	<p class="mtop2 mbottom0"><b>1) <bean:message key="label.unit.suggestion" bundle="RESEARCHER_RESOURCES"/>:</b></p>
	<fr:form action="<%= addSugestion %>">
	<fr:edit id="suggestion" name="unitBean" schema="resultUnitAssociation.create.with.suggestion" >
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="label.addUnit" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</fr:form>
	
	<%-- Create new result unit association --%>
	<p class="mtop2 mbottom0"><b>2) <bean:message bundle="RESEARCHER_RESOURCES" key="label.otherUnits"/></b></p>
		<bean:define id="beanForUnit" name="unitBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean"/>
		<fr:form action="<%= create %>">
		<fr:edit id="unitBean" name="unitBean" schema="<%= "resultUnitAssociation.create." + beanForUnit.getUnitType()%>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="exception" path="<%= prepareEdit %>"/>
			<fr:destination name="invalid" path="<%= prepareEdit %>"/>	
			<fr:destination name="cancel" path="<%= backLink %>"/>	
			<fr:destination name="postBack" path="<%= "/resultAssociations/changeTypeOfUnit.do?" + parameters %>"/>
			<fr:destination name="change.unit.searchType" path="<%= "/resultAssociations/changeTypeOfUnit.do?" + parameters %>"/>
		</fr:edit>
		<html:submit><bean:message key="label.addUnit" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
	</logic:present>

</logic:present>