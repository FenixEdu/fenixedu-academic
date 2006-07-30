<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.associateTeacher" /></h2>			
<html:form action="/teacherManagerDA">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<table>			
	<tr>
		<td class="formTD"><bean:message key="label.teacherNumber" />
		</td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber"  property="teacherNumber"  />
		</td>
		<td><span class="error"><!-- Error messages go here --><html:errors /></span >
		</td>
	</tr>
</table>
<br />

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="associateTeacher"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit >
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
</html:form>