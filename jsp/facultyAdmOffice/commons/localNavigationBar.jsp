<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present role="grantOwnerManager">
	<strong>&raquo; <bean:message key="link.group.grantsManagement.title"/></strong>
	<ul>
		<li><bean:message key="link.create.grant.owner"/></li>
	</ul>
	<br/>
</logic:present>
<logic:present role="creditsManager">
	<strong>&raquo; <bean:message key="link.group.creditsManagement.title"/></strong>
	<ul>
			<li><bean:message key="link.teacher.credits.sheet.management"/></li>
	</ul>
</logic:present>

