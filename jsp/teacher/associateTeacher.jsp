<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	
		
			<html:form action="/teachersManagerDA">
<table>		
	
	<tr>
		<td><bean:message key="label.teacherNumber" />	
		</td>
		<td><html:text  property="teacherNumber" value="" />
		</td>
		<td>
			 <html:hidden property="method" value="associateTeacher"/>
    <html:submit >
	<bean:message key="button.save"/>
	</html:submit>
		</td>
	</tr>
	

</table>	
</html:form>