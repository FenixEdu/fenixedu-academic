<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present role="role(PEDAGOGICAL_COUNCIL)">

	<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL"/></em>
	<h2><bean:message key="link.summaries.control"/></h2>
			
	<jsp:include page="../../directiveCouncil/summariesControl/listTeachersSummariesControlCore.jsp"/>
</logic:present>