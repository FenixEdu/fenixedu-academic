<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>



	<span class="error"><!-- Error messages go here --><html:errors /></span>

    <html:form action="/chooseStudent.do?method=choose">
	   <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<table>	
	       <!-- Student Number -->
	       <tr>
	         <td><bean:message key="label.student.number"/></td>
	         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.number" property="number"/></td>
	         </td>
	       </tr>
		</table>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok"/>
	</html:form>
