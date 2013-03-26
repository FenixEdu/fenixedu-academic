<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="PEDAGOGICAL_COUNCIL">

	<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL"/></em>
	<h2><bean:message key="label.evaluationMethodControl" bundle="APPLICATION_RESOURCES"/></h2>
		
	<jsp:include page="../../directiveCouncil/evaluationMethodControlCore.jsp"/>
</logic:present>