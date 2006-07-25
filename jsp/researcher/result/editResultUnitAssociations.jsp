<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal" />
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="unitAssociations" name="result" property="resultUnitAssociations"/>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	
	<%-- Title --%>		
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.useCaseTitle"/></h2>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<%-- List of existing unit associations --%>
	<logic:empty name="unitAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="unitAssociations">
		<fr:view name="unitAssociations" layout="tabular" schema="resultUnitAssociation.summary">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/result/resultAssociationsManagement.do?method=removeUnitAssociation" + 
				        									"&resultId=" + resultId + 
				        									"&resultType=" + resultType %>"/>
				<fr:property name="param(remove)" value="idInternal/associationId"/>
				<fr:property name="key(remove)" value="link.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<%-- Create new result unit association --%>
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.addNewUnitAssociation"/> </h3>
	<logic:present name="bean">
		<fr:edit 	id="bean" name="bean" schema="resultUnitAssociation.create"
					action="<%="/result/resultAssociationsManagement.do?method=createUnitAssociation&resultId=" + resultId + "&resultType=" + resultType %>">
			<fr:destination name="invalid" path="<%="/result/resultAssociationsManagement.do?method=prepareEditUnitAssociations&resultId=" + resultId + "&resultType=" + resultType %>"/>	
			<fr:destination name="cancel" path="<%="/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId + "&resultType=" + resultType %>"/>	
		</fr:edit>
	</logic:present>
	<br/>
	
	<%-- Go to previous page --%>
	<html:link page="<%="/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>