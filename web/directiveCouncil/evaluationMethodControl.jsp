<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

<logic:present role="DIRECTIVE_COUNCIL">

	<em><bean:message key="DIRECTIVE_COUNCIL" /></em>
	<h2><bean:message key="label.evaluationMethodControl"/></h2>

	<jsp:include page="./evaluationMethodControlCore.jsp"/>
</logic:present>
