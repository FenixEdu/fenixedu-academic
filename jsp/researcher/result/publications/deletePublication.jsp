<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="resultPublicationType" name="resultPublicationType"/>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.delete"/></h2>
	
	<p class="mvert2"><span class="warning0"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.delete.useCase.title"/></span></p>

	<%-- Last Modification Date --%>
	<p class="mtop15 mbottom2">
		<span class="greytxt1">
			<bean:message key="label.lastModificationDate"/>:&nbsp;
				<fr:view name="result" property="lastModificationDate"/> (<fr:view name="result" property="modifiedBy"/>)
		</span>
	</p>
		

	<%-- Participations --%>
	<p class="mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></strong></p>
	<jsp:include page="../commons/viewParticipations.jsp"/>
	
	<%-- Data --%>
	<p class="mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/> (<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.ResultPublication.type."+resultPublicationType%>"/>)</b></p>
 	<fr:view name="result" layout="tabular" schema="<%="result.publication.details."+resultPublicationType %>">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 thlight thright thtop width600px"/>
        	<fr:property name="columnClasses" value="width12em,,"/>
	    </fr:layout>
   	</fr:view>
	
	<%-- Document Files --%>
	<p class="mbottom0 mtop15"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b></p>
	<jsp:include page="../commons/viewDocumentFiles.jsp"/>
	
 	<%-- Event Associations --%>
	<%-- 
	<p class="mbottom1 mtop2"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></b></p>
	<jsp:include page="../commons/viewEventAssociations.jsp"/>
	--%>
	
	<%-- Unit Associations --%>
	<p class="mbottom1 mtop2"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></b></p>
	<jsp:include page="../commons/viewUnitAssociations.jsp"/>
	
	<%-- Confirmation buttons --%>
	<p class="mbottom1 mtop2"><span class="warning0"><bean:message key="researcher.ResultPublication.delete.useCase.title"/></span></p>
	<fr:form action="<%= "/resultPublications/delete.do?resultId=" + resultId %>">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:submit>
	</fr:form>
</logic:present>
