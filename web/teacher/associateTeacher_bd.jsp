<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.associateTeacher" /></h2>			
<html:form action="/teacherManagerDA">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<table>			
		<tr>
			<td class="formTD"><bean:message key="text.username" bundle="HTMLALT_RESOURCES"/>
			</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId"  property="teacherId"  />
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
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:cancel>
</html:form>