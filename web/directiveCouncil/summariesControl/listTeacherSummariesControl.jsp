<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="DIRECTIVE_COUNCIL,DEPARTMENT_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="DIRECTIVE_COUNCIL" /></em>
	<h2><bean:message key="link.summaries.control"/></h2>
		
	<jsp:include page="listTeachersSummariesControlCore.jsp"/>
</logic:present>