<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>

<bean:define id="title" name="<%= SessionConstants.PAGE_TITLE %>" scope="request" type="java.lang.String"/>
	
<h2 align="center"><bean:message key="<%= title %>"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>
<br/>
<html:form action="/payGratuity.do" focus="contributorNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="confirmPayment"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.gratuitySituationId" property="gratuitySituationId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.insuranceExecutionYearId" property="insuranceExecutionYearId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentId" property="studentId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<table border="0">
		<tr>
			<td><bean:message key="label.choose.contributor"/>&nbsp;</td>
			<td>
				<input alt="input.contributorNumber" type="text" name="contributorNumber" size="10" value=""/>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.continue"/>
	</html:submit>
</html:form>

</center>
