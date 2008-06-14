<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<%-- Last Modification Date --%>
	<p class="mtop15 mbottom2">
		<span class="greytxt1">
			<bean:message key="label.lastModificationDate"/>: 
				<fr:view name="result" property="lastModificationDate"/> (<fr:view name="result" property="modifiedBy"/>)
		</span>
	</p>

	<%-- Participations --%>
	<p class="mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></strong></p>
	<jsp:include page="../commons/viewParticipations.jsp"/>
	
	<%-- Data --%>		
	<%--
	<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></b></p>
	--%>
	<p class="mbottom0"><b>Detalhes da patente</b></p> <!-- tobundle -->
	<fr:view name="result" schema="patent.viewEditData">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 thlight thright thtop width600px"/>
        	<fr:property name="columnClasses" value="width12em,,"/>
	    </fr:layout>
	</fr:view>
	
	<%-- Documents --%>
	<p class="mbottom0 mtop15"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b></p>
	<jsp:include page="../commons/viewDocumentFiles.jsp"/>
	
	<%-- Event Associations --%>
	<%-- 
	<p class="mbottom0 mtop2"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></b></p>
	<jsp:include page="../commons/viewEventAssociations.jsp"/>
	--%>
	
	<%-- Unit Associations --%>	
	<p class="mbottom0 mtop2"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></b></p>
	<jsp:include page="../commons/viewUnitAssociations.jsp"/>
</logic:present>