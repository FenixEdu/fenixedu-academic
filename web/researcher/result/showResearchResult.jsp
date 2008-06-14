<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<em><bean:message key="label.research" bundle="RESEARCHER_RESOURCES"/></em>

	<logic:notEqual name="resultType" value="ResearchResultPatent">
	<h2><bean:message key="label.publication" bundle="RESEARCHER_RESOURCES"/></h2>
	</logic:notEqual>
	<logic:equal name="resultType" value="ResearchResultPatent">
	<h2><bean:message key="label.patent" bundle="RESEARCHER_RESOURCES"/></h2>
	</logic:equal>
	
	<logic:notEqual name="resultType" value="ResearchResultPatent">
	<bean:define id="resultPublicationType" name="resultPublicationType"/>				
	<fr:view name="result" schema="<%="result.publication.presentation."+resultPublicationType + ".mainInfo" %>">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	</fr:layout>
	</fr:view>
	</logic:notEqual>

	<logic:equal name="resultType" value="ResearchResultPatent">
	<fr:view name="result" schema="patent.viewEditData">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	</fr:layout>
	</fr:view>
	</logic:equal>
	<%-- Documents --%>
	
	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b>
	</p>
	<jsp:include page="commons/viewDocumentFiles.jsp"/>
	
