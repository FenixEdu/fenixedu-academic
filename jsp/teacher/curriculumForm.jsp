<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	
		
			<html:form action="/curriculumManagerDA">
<table>		
	
	<tr>
		<td><bean:message key="label.generalObjectives" />	
		</td>
		<td><html:text  property="generalObjectives" >
	</html:text>
		</td>
		
	</tr>
	<tr>
		<td><bean:message key="label.operacionalObjectives" />
		</td>
		<td><html:text  property="operacionalObjectives" >
	</html:text>
		</td>
		
	</tr>
	<tr>
		<td><bean:message key="label.program" />	
		</td>
		<td><html:text  property="program" >
	</html:text>
		</td>
		
	</tr>
	<tr>
		<td>
			<html:reset  styleClass="inputbutton">
          <bean:message key="label.clear"/>
    </html:reset>
		</td>
		<td>
			 <html:hidden property="method" value="insertCurriculum"/>
    <html:submit >
	<bean:message key="button.save"/>
	</html:submit>
		</td>
		
	</tr>
	
	
	
   
</table>	
</html:form>