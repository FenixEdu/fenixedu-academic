<%@ page language="java" %>
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
<%--
<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>">	
<jsp:include page="curriculumForm.jsp"/>
</logic:notPresent>
<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" > --%>

<logic:present name="siteView">
<bean:define id="program" name="siteView" property="component"/>
<h2><bean:message key="title.program"/></h2>
<table>
	<tr>
		<td>
			<bean:write name="program" property="program" filter="false"/>
		</td>
	</tr>
</table>
<br/>	
<logic:notEmpty name="program" property="programEn">
<h2><bean:message key="title.program.eng"/></h2>
<table>
	<tr>
		<td>
			<bean:write name="program" property="programEn" filter="false"/>
		</td>
	</tr>
</table>
<br />	
</logic:notEmpty>

<div class="gen-button">
	<html:link page="<%= "/programManagerDA.do?method=prepareEditProgram&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
		<bean:message key="button.edit"/>
	</html:link>
</div>	 
</logic:present>