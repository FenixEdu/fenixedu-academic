<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.associateTeacher" /></h2>			
<html:form action="/teacherManagerDA">
<html:hidden property="page" value="1"/>
<table>			
	<tr>
		<td class="formTD"><bean:message key="label.teacherNumber" />
		</td>
		<td><html:text  property="teacherNumber"  />
		</td>
		<td><span class="error"><html:errors /></span >
		</td>
	</tr>
</table>
<br />

<html:hidden property="method" value="associateTeacher"/>
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

<html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit >
<html:reset styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
</html:form>