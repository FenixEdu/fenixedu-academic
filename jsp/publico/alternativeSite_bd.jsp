<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="<%=SessionConstants.ALTERNATIVE_SITE%>">	
	<html:link href="<%=(String)session.getAttribute(SessionConstants.ALTERNATIVE_SITE)%>" target="_blank">
		<%=(String)session.getAttribute(SessionConstants.ALTERNATIVE_SITE)%>
	</html:link>	
</logic:present>
<br/>

<logic:present name="<%=SessionConstants.MAIL%>">	
	<html:link href="<%= "mailto:" + (String)session.getAttribute(SessionConstants.MAIL)%>">
		<%=(String)session.getAttribute(SessionConstants.MAIL)%>
	</html:link>
</logic:present>
<br/>
