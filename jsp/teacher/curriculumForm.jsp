<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	
		
<html:form action="/curriculumManagerDA">
			<html:hidden property="page" value="1"/>	
<h3><bean:message key="title.objectives" />	</h3>			
<table>		
	
	<tr>
		<td><h2><bean:message key="label.generalObjectives" />	</h2>
		</td>
		<td><html:textarea  property="generalObjectives" />
		</td>
		<td> <span class="error" ><html:errors property="generalObjectives"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.operacionalObjectives" /></h2>
		</td>
		<td><html:textarea  property="operacionalObjectives" />
		</td>
		<td> <span class="error" ><html:errors property="operacionalObjectives"/></span>	
		</td>
	</tr>
</table>
<h3><bean:message key="title.program" />	</h3>	
<table>		
<tr>
		<td><h2><bean:message key="label.program" />	</h2>
		</td>
		<td><html:textarea  property="program" />
		</td>
		<td> <span class="error" ><html:errors property="program"/></span>	
		</td>
	</tr>
</table>	
<h3>	
<table>
	<tr>
		<td>
			<html:reset  styleClass="inputbutton">
          <bean:message key="label.clear"/>
    </html:reset>
		</td>
		<td>
			
			 <html:hidden property="method" value="insertCurriculum"/>
    <html:submit styleClass="inputbutton" >
	<bean:message key="button.save"/>
	</html:submit>
		</td>
		
	</tr>
</table></h3>	
</html:form>