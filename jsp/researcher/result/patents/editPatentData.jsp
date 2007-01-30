<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="parameter" value="<%= "resultId=" + resultId %>"/>
	
	<%-- Title messages --%>
	<em><bean:message key="link.patentsManagement" bundle="RESEARCHER_RESOURCES"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.edit.data"/></h2>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Form edit Patent Data --%>
	<p class="mtop2 mbottom0"><strong><bean:message key="researcher.ResearchResultPatent.details.useCase.title" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
	<fr:edit id="editPatent" name="result" schema="patent.edit" 
				action="<%= "/resultPatents/updateMetaInformation.do?" + parameter %>">
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle1 thlight thright thtop"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	    <fr:destination name="exception" path="<%= "/resultPatents/prepareEditData.do?" + parameter %>"/>
	    <fr:destination name="invalid" path="<%= "/resultPatents/prepareEditData.do?" + parameter %>"/>
   	    <fr:destination name="cancel" path="<%= "/resultPatents/prepareEdit.do?" + parameter %>"/>
	</fr:edit>
</logic:present>
