<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.insertExemption" /></h2>

<html:form action="/readStudent" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readStudent"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<table>
		<tr>
			<td><bean:message key="label.student.number" />:&nbsp;</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" property="studentNumber" size="10"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.executionYear" />:&nbsp;</td>
			<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYear" property="executionYear">
				<html:options collection="executionYears" property="value" labelProperty="label" />
			</html:select></td>
		</tr>
		<tr>
			<td colspan="2">
			&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.continue"/> 
				</html:submit>
				<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
					<bean:message key="label.clear"/>
				</html:reset> 
			</td>
		</tr>
	</table>
</html:form>