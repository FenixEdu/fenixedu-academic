<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>	

<logic:present name="<%= SessionConstants.INFO_SITES_LIST %>" scope="session">
	<bean:message key="label.professorShips" />	
	<logic:iterate name="<%= SessionConstants.INFO_SITES_LIST %>" id="site" indexID="index">
		<bean:define id="executionCourse" name="site" property="infoExecutionCourse"/>
		<html:link forward="/viewSite.do" >			
			<bean:write name="executionCourse" property="code"/>
		</html:link>	
	</logic:iterate>
	
	
</logic:present>
