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
	
	<!-- Action paths definitions -->

	<bean:define id="create" value="<%="/resultAssociations/createUnitAssociation.do?forwardTo=editUnitAssociations&" + parameters%>" />
	<bean:define id="prepareEdit" value="<%="/resultAssociations/prepareEditUnitAssociations.do?forwardTo=editUnitAssociations&" + parameters%>" />
	<bean:define id="prepareAlter" value="<%="/resultAssociations/prepareEditUnitRole.do?forwardTo=editUnitAssociations&" + parameters%>"/>	
	<bean:define id="remove" value="<%="/resultAssociations/removeUnitAssociation.do?forwardTo=editUnitAssociations&" + parameters%>"/>
	<bean:define id="backLink" value="<%="/resultAssociations/backToResult.do?forwardTo=editUnitAssociations&" + parameters%>" />

<%-- Title --%>		
	<logic:equal name="resultType" value="ResultPatent">
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.management.title"/></em>
	</logic:equal>
	<logic:notEqual name="resultType" value="ResultPatent">
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
	</logic:notEqual>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.useCase.title"/>: <fr:view name="result" property="title"/></h2>

	<%-- Go to previous page --%>
		<ul class="mvert2 list5">
		<li>
			<html:link page="<%= backLink %>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.goBackToView"/></html:link>
		</li>
	</ul>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<%-- List of existing unit associations --%>
	<p class="mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></b></p>
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
	
	
	
	<%-- Create new result unit association --%>
	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.add"/></b></p>
	<logic:present name="unitBean">
		
		<logic:equal name="unitBean" property="externalUnit" value="true">
				<bean:define id="schema" value="resultUnitAssociation.create.external" toScope="request"/>
		</logic:equal>
		<logic:equal name="unitBean" property="externalUnit" value="false">
			<bean:define id="schema" value="resultUnitAssociation.create.internal" toScope="request"/>
		</logic:equal>
		
		<bean:define id="schema" name="schema" type="java.lang.String"/>
		

		<fr:edit id="unitBean" name="unitBean" schema="<%= schema %>" action="<%= create %>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="exception" path="<%= prepareEdit %>"/>
			<fr:destination name="invalid" path="<%= prepareEdit %>"/>	
			<fr:destination name="cancel" path="<%= backLink %>"/>	
			<fr:destination name="postBack" path="<%= "/resultAssociations/changeTypeOfUnit.do?forwardTo=editUnitAssociations&" + parameters %>"/>
		</fr:edit>

	</logic:present>

</logic:present>