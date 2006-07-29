<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2 align="center"><bean:message key="title.transaction.createGuides"/></h2>
<center>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<br/>
<br/>
<html:form action="/createGuideFromTransactions.do" focus="contributorNumber">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="confirmCreate"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.gratuitySituationId" property="gratuitySituationId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentId" property="studentId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.transactionsWithoutGuide" property="transactionsWithoutGuide" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	
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
