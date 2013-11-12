<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<h2><bean:message key="title.associateTeacher" /></h2>		

<div class="infoop2">
<bean:message key="label.teacher.auth" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="HTMLALT_RESOURCES"/>
</div>
	
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