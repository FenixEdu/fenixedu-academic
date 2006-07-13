<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<table width="50%" align="center" cellpadding="0">
	<tr>
	    <td width="50%" nowrap="nowrap" class="navopgeral-td">
    		<html:link forward="home">Home</html:link>
	    </td>
		<td width="50%" nowrap="nowrap" class="centerContent"><html:link forward="logoff"><img border="0" src="<%= request.getContextPath() %>/images/logout.gif" alt="<bean:message key="logout" bundle="IMAGE_RESOURCES" />"></html:link></td>
	</tr>
</table>	