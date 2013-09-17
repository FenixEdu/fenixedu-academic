<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
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
	

