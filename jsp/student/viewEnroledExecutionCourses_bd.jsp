<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	

<logic:present name="infoExecutionCourses"> 
<br>
<br>
<br>
 <h2><bean:message key="title.ChooseExecutionCourse"/></h2>
<br>

<html:form action="/viewExecutionCourseProjects" method="get">

<span class="error"><html:errors/></span>
<br>

<table width="50%" cellpadding="0" border="0">	
	<tr>
	 	<td><bean:message key="label.executionCourse"/></td>
		
		<td>
		<html:select property="executionCourseCode" size="1">
    	<html:options collection="infoExecutionCourses" property="value" labelProperty="label"/>
    	</html:select>	
    	</td>
    </tr>	

</table>
<br>
<br>
<br>
<html:submit styleClass="inputbutton"><bean:message key="button.choose"/>                    		         	
</html:submit>
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>
<br>

</html:form>	

</logic:present>
<logic:notPresent name="infoExecutionCourses">
	<h2><bean:message key="message.executionCourses.not.available"/></h2>
</logic:notPresent>



   
