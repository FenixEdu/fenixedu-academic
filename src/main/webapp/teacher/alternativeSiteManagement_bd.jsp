<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.messaging.ExecutionCourseAliasExpandingAction"%>
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>

<p>
<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<h2>
	<bean:message key="title.personalizationOptions"/>

	<span class="small pull-right">
		<html:link page="/alternativeSite.do?method=prepareImportCustomizationOptions&amp;page=0" paramId="executionCourseID" paramName="executionCourseID">
			<bean:message key="link.import.customizationOptions"/>
		</html:link>
	</span>
</h2>
<html:form action="/alternativeSite" method="post">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editCustomizationOptions"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="executionCourseID" value="${executionCourseID}" />



<div class="clearfix alert alert-warning">
	<div class="col-lg-1 text-right">
		<img src="${pageContext.request.contextPath}/images/number_1.gif">
	</div>
	<div class="col-lg-11">
		<bean:message key="message.siteandmail.information" />
	</div>
</div>


<table class="tstyle2 thlight thleft" style="width: 100%;">
<tr>
	<td style="width: 130px;">
		<bean:message key="message.siteAddress"/>:
	</td>
	<td>
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.siteAddress" property="siteAddress" size="30"/>
		<span class="error" ><!-- Error messages go here --><html:errors property="siteAddress"/></span>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.mailAddressCourse"/>:
	</td>
	<td>
		<p>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dynamicMailDistribution" property="dynamicMailDistribution" value="false"/>	  
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.mail" property="mail" size="30"/>
	
			<span class="error" ><!-- Error messages go here -->
			<html:errors property="mail"/></span>
		</p>
		<p>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.dynamicMailDistribution" property="dynamicMailDistribution" value="true"/>
			<%=ExecutionCourseAliasExpandingAction.emailAddressPrefix%>${executionCourseID}&#64;<%=FenixConfigurationManager.getConfiguration().getMailingListHostName() %>	
	
			<span class="error" ><!-- Error messages go here -->
			<html:errors property="dynamicMailDistribution"/></span>
		</p>
    </td>    
</tr>
</table>


<div class="clearfix alert alert-warning">
	<div class="col-lg-1 text-right">
		<img src="${pageContext.request.contextPath}/images/number_2.gif">
	</div>
	<div class="col-lg-11">
		<bean:message key="message.initialStatement.explanation" />
	</div>
</div>

<table class="tstyle2 thlight thleft" style="width: 100%;">
<tr>
	<td style="width: 130px;">
		<bean:message key="message.initialStatement"/>
	</td>	
	<td>
		<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.initialStatement" property="initialStatement" rows="5" cols="110"/> 
	</td>
</tr>
</table>


<div class="clearfix alert alert-warning">
	<div class="col-lg-1 text-right">
		<img src="${pageContext.request.contextPath}/images/number_3.gif">
	</div>
	<div class="col-lg-11">
		<bean:message key="message.introduction.explanation" />
	</div>
</div>


<table class="tstyle2 thlight thleft" style="width: 100%;">
<tr>
	<td style="width: 130px;">
		<bean:message key="message.introduction"/>:
	</td>	
	<td>
		<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.introduction" property="introduction" rows="5" cols="110"/>
	</td> 
</tr>
</table>


<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
		<bean:message key="button.save"/>
	</html:submit>
</p>

</html:form>
