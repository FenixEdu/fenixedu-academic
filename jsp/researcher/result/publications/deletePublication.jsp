<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="resultPublicationType" name="resultPublicationType"/>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></em>	
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.delete"/></h3>
	
	<%-- Last Modification Date --%>
	<p class="mtop0 mbottom2">
		<span style="background-color: #eee; padding: 0.25em;">
			<bean:message key="label.lastModificationDate"/>:&nbsp;
				<b><fr:view name="result" property="lastModificationDate"/></b> (<fr:view name="result" property="modifyedBy"/>)
		</span>
	</p>
	
	<%-- Participations --%>
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></b>
	<jsp:include page="../commons/viewParticipations.jsp"/>
	
	<%-- Data --%>
	<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/>
	&nbsp;(<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.ResultPublication.type."+resultPublicationType%>"/>)</b></p>
 	<fr:view name="result" layout="tabular" schema="<%="result.publication.details."+resultPublicationType %>">
 		<fr:layout name="tabular">
        	<fr:property name="classes" value="style1"/>
	       	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
   	</fr:view>
	<br/>
	
	<%-- Document Files --%>
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b>
	<jsp:include page="../commons/viewDocumentFiles.jsp"/>
	<br/>
	
	<%-- Confirmation buttons --%>
	<p><bean:message key="researcher.ResultPublication.delete.useCase.title"/></p>	
	<fr:form action="<%= "/resultPublications/delete.do?resultId=" + resultId %>">
		<html:submit property="confirm">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
		</html:submit>
		<html:submit property="cancel">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:submit>
	</fr:form>
</logic:present>
