<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<table width="50%" border="0" align="center" cellpadding="0" cellspacing="2">
    
	<tr>
	    <td width="50%" nowrap="nowrap" class="navopgeral">
    		<html:link forward="home">Home</html:link>
	    </td>
		<td width="50%" nowrap="nowrap" class="centerContent"><html:link forward="logoff"><img alt="logOff" border="0" src="<%= request.getContextPath() %>/images/logout.gif"></html:link></td>
	</tr>
</table>	