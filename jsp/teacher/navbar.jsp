<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:link page="/firstPage.do">
	<bean:message key="link.home"/>
</html:link>
<html:link page="/accessAnnouncementManagementAction.do">
	<bean:message key="link.announcements"/>
</html:link>
<html:link page="/objectivesManagerDA.do?method=acessObjectives">
	<bean:message key="link.objectives"/>
</html:link>
<html:link page="/programManagerDA.do?method=acessProgram">
	<bean:message key="link.program"/>
</html:link>
<html:link page="/firstPage.do">
	<bean:message key="link.bibliography"/>
</html:link>
<html:link page="/logoff.do">
	<bean:message key="link.logout"/>
</html:link>
