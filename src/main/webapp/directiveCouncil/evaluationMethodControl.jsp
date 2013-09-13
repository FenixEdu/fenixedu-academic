<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<html:xhtml/>

<logic:present role="DIRECTIVE_COUNCIL">

	<em><bean:message key="DIRECTIVE_COUNCIL" /></em>
	<h2><bean:message key="label.evaluationMethodControl"/></h2>

	<jsp:include page="./evaluationMethodControlCore.jsp"/>
</logic:present>
