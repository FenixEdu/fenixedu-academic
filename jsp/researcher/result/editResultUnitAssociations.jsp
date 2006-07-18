<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal" />
	<bean:define id="unitAssociations" name="result" property="resultUnitAssociations"/>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	
	<%-- Title --%>		
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.useCaseTitle"/></h2>

	<%-- List of existing unit associations --%>	
	<logic:notEmpty name="unitAssociations">
		<fr:edit 	id="unitAssociations" name="unitAssociations" layout="tabular-editable" schema="resultUnitAssociations.edit-role"
					action="<%= "/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId %>">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/result/resultAssociationsManagement.do?method=removeUnitAssociation&resultId=" + resultId %>"/>
				<fr:property name="param(remove)" value="idInternal/associationId"/>
				<fr:property name="key(remove)" value="researcher.result.editResult.unit.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/result/resultAssociationsManagement.do?method=prepareEditUnitAssociations&resultId=" + resultId %>"/>	
		</fr:edit>
	</logic:notEmpty>
	<br/>
	
	<%-- Create new result unit association --%>
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.addNewUnitAssociation"/> </h3>
	<logic:present name="bean">
		<fr:edit id="bean" name="bean" action="<%="/result/resultAssociationsManagement.do?method=createUnitAssociation&resultId=" + resultId %>" schema="resultUnitAssociation.create">
			<fr:destination name="invalid" path="<%="/result/resultAssociationsManagement.do?method=prepareEditUnitAssociations&resultId=" + resultId %>"/>	
			<fr:destination name="cancel" path="<%="/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId %>"/>	
		</fr:edit>
	</logic:present>

	<br/>
	<html:link page="<%="/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>