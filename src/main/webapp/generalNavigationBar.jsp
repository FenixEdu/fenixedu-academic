<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<table width="50%" align="center" cellpadding="0">
	<tr>
	    <td width="50%" nowrap="nowrap" class="navopgeral-td">
    		<html:link forward="home">Home</html:link>
	    </td>
		<td width="50%" nowrap="nowrap" class="centerContent"><html:link forward="logoff"><img border="0" src="<%= request.getContextPath() %>/images/logout.gif" alt="<bean:message key="logout" bundle="IMAGE_RESOURCES" />" /></html:link></td>
	</tr>
</table>	