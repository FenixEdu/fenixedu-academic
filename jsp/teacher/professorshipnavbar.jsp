<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul>
	<li>
		<html:link page="/creditsManagement.do">
			<bean:message key="link.credits.management"/>
		</html:link>
	</li>
	<li>
		<html:link forward="logoff">
			<bean:message key="link.logout"/>
		</html:link>
	</li>
</ul>
	

