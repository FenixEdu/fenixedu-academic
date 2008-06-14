<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrolment.without.rules" bundle="DEGREE_ADM_OFFICE"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>
<html:form action="/courseEnrolmentWithoutRulesManagerDA" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readEnrollments"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.userType" property="userType"/>
	<logic:present name="degreeType">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" value="<%= pageContext.findAttribute("degreeType").toString()%>"/>
	</logic:present>
	<table>
		<tr>
			<td><bean:message key="label.choose.student"/>&nbsp;</td>
			<td>
				<input alt="input.studentNumber" type="text" name="studentNumber" size="6" maxlength="6"/>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.choose.year.execution"/>&nbsp;</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriod" property="executionPeriod" >
					<html:optionsCollection name="executionPeriods"/>
				</html:select>
			</td>				
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.submit.student"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="button.clean"/>
	</html:reset>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='exit';this.form.submit();">
		<bean:message key="button.cancel"/>
	</html:cancel>			
</html:form>
