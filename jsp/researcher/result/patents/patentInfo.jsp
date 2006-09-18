<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
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
	<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></b></p>
	<fr:view name="result" schema="patent.viewEditData">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	
	<%-- Documents --%>
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b>
	<jsp:include page="../commons/viewDocumentFiles.jsp"/>
	
	<%-- Event Associations --%>
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></b>	
	<jsp:include page="../commons/viewEventAssociations.jsp"/>
	
	<%-- Unit Associations --%>	
	<b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></b>	
	<jsp:include page="../commons/viewUnitAssociations.jsp"/>
</logic:present>