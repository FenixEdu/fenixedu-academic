<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>



	<span class="error"><html:errors/></span>

    <html:form action="/chooseStudent.do?method=choose">
	   <html:hidden property="page" value="1"/>
		<table>	
	       <!-- Student Number -->
	       <tr>
	         <td><bean:message key="label.student.number"/></td>
	         <td><html:text property="number"/></td>
	         </td>
	       </tr>
		</table>
		<br />
		<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
	</html:form>
