<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
		<table width="100%">
			<tr>
				<td class="infoop">
					<bean:message key="label.program.explanation" />
				</td>
			</tr>
		</table>
<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>">	
<jsp:include page="curriculumForm.jsp"/>
</logic:notPresent>
<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" >
<h2><bean:message key="title.program"/></h2>
<table>
	<tr>
		<td><strong><bean:message key="label.program" /></strong>	
		</td>
	</tr>
	<tr>
		<td><bean:define id="program" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="program"></bean:define>
			<bean:write name="program" filter="false"/>
		</td>
	</tr>
</table>
<br />	
<html:hidden property="method" value="prepareEditProgram"/>
<div class="gen-button"><html:link page="/programManagerDA.do?method=prepareEditProgram"><bean:message key="button.edit"/>
</html:link>
</div>	 
</logic:present>