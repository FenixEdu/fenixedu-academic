<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	
<h3><bean:message key="title.associateTeacher" /></h3>			
<html:form action="/teacherManagerDA">
<html:hidden property="page" value="1"/>
<table>		
	
	<tr>
		<td><h2><bean:message key="label.teacherNumber" /></h2>	
		</td>
		<td><html:text  property="teacherNumber"  />
		</td>
		<td>
			 <html:hidden property="method" value="associateTeacher"/>
    <html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
	</html:submit >
		</td>
		<td><span class="error"><html:errors /></span >	</td>
		
	</tr>
	

</table>	
</html:form>