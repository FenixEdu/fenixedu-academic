<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.masterDegree.administrativeOffice.marksManagement" /></h2>
<br />
<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
<br />
<span class="error"><html:errors/></span>
<logic:present name="curricularCourses">
	<bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" />
	<html:form action="/marksManagementDispatchAction.do?method=showMarksManagementMenu">
	
		<table>
    	<!-- Curricular Course -->
    		<tr>
        		<td><bean:message key="label.masterDegree.administrativeOffice.curricularCourse"/>: </td>
         		<td>
            		<html:select property="curricularCourseCode">
               			<html:options collection="curricularCourses" property="idInternal" labelProperty="name"/>
             		</html:select>          
	         	</td>
    	    </tr>
		</table>
		<br />

		<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
		<html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
		<html:hidden property="curricularCourses" value="<%= pageContext.findAttribute("curricularCourses").toString() %>" />

		<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
	</html:form>
</logic:present>