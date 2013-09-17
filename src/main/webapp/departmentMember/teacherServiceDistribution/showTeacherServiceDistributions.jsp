<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.Person" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>
<%@page import="pt.ist.fenixWebFramework.security.UserView"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdProcessEdition"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.tsdProcessEdition"/>
	</em>
</p>



<html:form action="/tsdProcess">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareForTSDProcessEdition"/>

<p><strong><bean:write name="departmentName"/></strong></p>
<table class='tstyle5 thlight thright mtop05'>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.executionYear"/>:
	</th>
	<td>
		<html:select property="executionYear" onchange="this.form.submit();">
			<html:option value="-1"><bean:message key="label.teacherServiceDistribution.all"/></html:option>
			<html:options collection="executionYearList" property="externalId" labelProperty="year"/>
		</html:select>
	</td>
</tr>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.semester"/>:
	</th>
	<td>
		<logic:notEmpty name="executionPeriodsList">
			<html:select property="executionPeriod" onchange="this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodsList" property="externalId" labelProperty="semester"/>
			</html:select>
		</logic:notEmpty>
	</td>
</tr>
</table>

<br/>
<br/>
<logic:empty name="tsdProcessList">
<p>
	<em>
		<bean:message key="label.teacherServiceDistribution.noTSDProcesssForExecutionPeriod"/>
	</em>
</p>
</logic:empty>
<logic:notEmpty name="tsdProcessList">

	<bean:define id="deleteConfirm" type="java.lang.String">
		return confirm('<bean:message key="message.confirm.submit.deleteTSDProcess"/>')
	</bean:define>

	<b><bean:message key="label.teacherServiceDistribution.availableTSDProcesss"/>:</b>
	<br/>
	<table class='tstyle4 thlight mtop05'>
		<tr>
			<th>
				<bean:message key="label.teacherServiceDistribution.name"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.executionYear"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.semesters"/>
			</th>
			<th>
			</th>
		</tr>
		<% IUserView userView = UserView.getUser(); %>
		<% Person person = userView.getPerson(); %>
	<logic:iterate name="tsdProcessList" id="tsdProcess">	
		<tr>
		 	<td class="highlight7" align="left" width="250">
		 		<bean:define id="tsdProcessId" name="tsdProcess" property="externalId"/>
				<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
					<bean:write name="tsdProcess" property="name"/>
				</html:link>
			</td> 
			<td align="center">
				<bean:write name="tsdProcess" property="executionYear.year" />
			</td>
			<td align="center">
				<logic:iterate id="executionPeriod" name="tsdProcess" property="orderedExecutionPeriods">
					<bean:write name="executionPeriod" property="semester"/>&#186;&nbsp;
				</logic:iterate>
			</td>
			<% if(((TSDProcess) tsdProcess).getHasSuperUserPermission(person)){ %>
			<td align="center">
				<html:link page='<%= "/tsdProcess.do?method=deleteTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>' onclick='<%= deleteConfirm %>'>
					<bean:message key="label.teacherServiceDistribution.deleteTSDProcess"/>
				</html:link>
			</td>
			<% } else { %>
			<td>
			</td>
			<% } %>
		</tr>
	</logic:iterate> 
	</table>	
</logic:notEmpty> 
</html:form>

	
<br/>
<br/>
<html:link page="/tsdProcess.do?method=prepareTSDProcess">
	<bean:message key="link.back"/>
</html:link>	

