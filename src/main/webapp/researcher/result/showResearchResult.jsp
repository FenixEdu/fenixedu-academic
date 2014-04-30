<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<em><bean:message key="label.research" bundle="RESEARCHER_RESOURCES"/></em>

	<h2><bean:message key="label.publication" bundle="RESEARCHER_RESOURCES"/></h2>
	
	<bean:define id="resultPublicationType" name="resultPublicationType"/>				
	<fr:view name="result" schema="<%="result.publication.presentation."+resultPublicationType + ".mainInfo" %>">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	</fr:layout>
	</fr:view>

	<%-- Documents --%>
	
	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b>
	</p>
	<jsp:include page="commons/viewDocumentFiles.jsp"/>
	
