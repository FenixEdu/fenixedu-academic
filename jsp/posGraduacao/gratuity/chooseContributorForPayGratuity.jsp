<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.masterDegree.utils.SessionConstants" %>

<bean:define id="title" name="<%= SessionConstants.PAGE_TITLE %>" scope="request" type="java.lang.String"/>
	
<h2 align="center"><bean:message key="<%= title %>"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>
<br/>
<html:form action="/payGratuity.do" focus="contributorNumber">
	<html:hidden property="method" value="confirmPayment"/>
	<html:hidden property="gratuitySituationId" />
	<html:hidden property="insuranceExecutionYearId" />
	<html:hidden property="studentId" />
	<html:hidden property="page" value="1"/>
	<table border="0">
		<tr>
			<td><bean:message key="label.choose.contributor"/>&nbsp;</td>
			<td>
				<input type="text" name="contributorNumber" size="10" value=""/>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.continue"/>
	</html:submit>
</html:form>

</center>
