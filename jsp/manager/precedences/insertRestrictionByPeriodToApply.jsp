<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	<%--<html:form action="/makeSimplePrecedence.do">
		
	</html:form>--%>
<logic:equal name="className" value="RestrictionPeriodToApply">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionPeriodToApply"/></h3>
</logic:equal>
