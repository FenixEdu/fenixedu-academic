<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<logic:present name="altSite">	
	<html:link href="<%= pageContext.findAttribute("altSite")%>" target="_blank">
		<%= pageContext.findAttribute("altSite")%>
	</html:link>	
</logic:present>
<br/>
<logic:present name="siteMail">	
	<html:link href="<%= "mailto:" + pageContext.findAttribute("siteMail")%>">
		<%= pageContext.findAttribute("siteMail")%>
	</html:link>
</logic:present>
<br/>
