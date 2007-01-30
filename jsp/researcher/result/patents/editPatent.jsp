<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="patentId" name="result" property="idInternal"/>
	<bean:define id="patent" name="result"/>
	<bean:define id="parameters" value="<%="resultId=" + patentId + "&resultType=" + patent.getClass().getSimpleName()%>"/>
	
	<em><bean:message key="link.patentsManagement" bundle="RESEARCHER_RESOURCES"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.edit.useCase.title"/></h2>
	
	
	<%-- Go back --%>
	<ul class="mvert2 list5">
		<li>
			<html:link page="/resultPatents/management.do">
				<bean:message bundle="RESEARCHER_RESOURCES" key="link.goBackToView"/>
			</html:link>
		</li>
		<logic:equal name="result" property="deletableByCurrentUser" value="true">
		<li>
			<html:link page="<%="/resultPatents/prepareDelete.do?resultId=" + patentId %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.delete" />
			</html:link>
		</li>
		</logic:equal>
	</ul>
	
	<logic:present name="confirm">
	<fr:form action="<%= "/resultPatents/delete.do?" + parameters %>">
	<p class="mbottom1 mtop2"><span class="warning0"><bean:message key="researcher.ResearchResultPatent.delete.useCase.title"/></span></p>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
			</html:submit>
		</p>
	</fr:form>		
	</logic:present>


	<%-- Last Modification Date --%>
	<p class="mtop15 mbottom2">
		<span class="greytxt1">
			<bean:message key="label.lastModificationDate"/>: 
			<fr:view name="result" property="lastModificationDate"/> (<fr:view name="result" property="modifiedBy"/>)
		</span>
	</p>
				
	<%-- Warnings--%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<logic:equal name="result" property="editableByCurrentUser" value="true">
	<bean:message key="link.edit" bundle="RESEARCHER_RESOURCES"/>: 
	<html:link page="<%="/resultPatents/prepareEditData.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="label.patentData" />
	</html:link>, 
	<html:link page="<%="/resultParticipations/prepareEdit.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.authors" />
	</html:link>, 
	<html:link page="<%="/resultAssociations/prepareEditUnitAssociations.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label" />
	</html:link>
	</logic:equal>
	
	<fr:view name="result" schema="patent.viewEditData">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
	</fr:layout>
	</fr:view>

	<%-- Documents --%>
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></strong>
	<logic:equal name="result" property="editableByCurrentUser" value="true">
	(<html:link page="<%="/resultDocumentFiles/prepareEdit.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.edit" />
		</html:link>
	)
	</logic:equal>
	</p>
	<jsp:include page="../commons/viewDocumentFiles.jsp"/>

	<%-- Event Associations --%>
	<%-- 
	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></b></p>
	<jsp:include page="../commons/viewEventAssociations.jsp"/>
	<ul class="mtop0 list5">
		<li>
		<html:link page="<%="/resultAssociations/prepareEditEventAssociations.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.eventAssociations.link" />
		</html:link>
		</li>
	</ul>
	--%>
	
</logic:present>
