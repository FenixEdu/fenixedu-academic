<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<span class="error"><html:errors/></span>

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.insertExemption" /></h2>

<html:form action="/prepareInsertExemption.do" focus="studentNumber" method="post">
	<html:hidden property="method" value="prepareInsertExemption"/>
	<html:hidden property="page" value="1"/>
	
	<table>
		<tr>
			<td><bean:message key="label.student.number" />:&nbsp;</td>
			<td><html:text property="studentNumber" size="4"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.executionYear" />:&nbsp;</td>
			<td><html:select property="executionYear">
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
				<html:submit styleClass="inputbutton">
					<bean:message key="button.continue"/> 
				</html:submit>
				<html:reset styleClass="inputbutton">
					<bean:message key="label.clear"/>
				</html:reset> 
			</td>
		</tr>
	</table>
</html:form>