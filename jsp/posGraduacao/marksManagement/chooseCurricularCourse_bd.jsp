<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<span class="error"><html:errors/></span>
<logic:present name="curricularCourses">
<h2><bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" /></h2>
<br />
<html:form action="/marksManagementDispatchAction?method=showMarksManagementMenu">
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
	<html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
</html:form>
</logic:present>