<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="result" name="result"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.management.title"/></em>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.edit.useCase.title"/></h3>
	
	<%-- Last Modification Date --%>
	<p class="mtop0 mbottom2">
		<span style="background-color: #eee; padding: 0.25em;">
			<bean:message key="label.lastModificationDate"/>:&nbsp;
			<b><fr:view name="result" property="lastModificationDate"/></b> (<fr:view name="result" property="modifyedBy"/>)
		</span>
	</p>
				
	<%-- Warnings--%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<%-- Participations --%>
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></b>:
	<html:link page="<%="/resultParticipations/prepareEdit.do?" + parameters %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.participations.link" />
	</html:link>
	<jsp:include page="../commons/viewParticipations.jsp"/>
	<br/>
	
	<%-- Data --%>		
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></b>:
	<html:link page="<%="/resultPatents/prepareEditData.do?" + parameters %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.edit.data" />
	</html:link>
	<fr:view name="result" schema="patent.viewEditData">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle4"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<br/>
	
	<%-- Documents --%>
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b>:
	<html:link page="<%="/resultDocumentFiles/prepareEdit.do?" + parameters %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.documents.link" />
	</html:link>
	<jsp:include page="../commons/viewDocumentFiles.jsp"/>
	<br/>
	
	<%-- Event Associations --%>
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></b>:
	<html:link page="<%="/resultAssociations/prepareEditEventAssociations.do?" + parameters %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.eventAssociations.link" />
	</html:link>
	<jsp:include page="../commons/viewEventAssociations.jsp"/>
	<br/>
	
	<%-- Unit Associations --%>
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></b>:
	<html:link page="<%="/resultAssociations/prepareEditUnitAssociations.do?" + parameters %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.unitAssociations.link" />
	</html:link>	
	<jsp:include page="../commons/viewUnitAssociations.jsp"/>
	<br/>
	<br/>
	
	<%-- Go back --%>
	<html:link page="/resultPatents/management.do">
		<bean:message bundle="RESEARCHER_RESOURCES" key="link.goBackToView"/>
	</html:link>
</logic:present>
