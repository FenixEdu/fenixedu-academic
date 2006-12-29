<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.messaging.ExecutionCourseAliasExpandingAction"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherAdministrationViewerDispatchAction"%>

<p>
<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<h2><bean:message key="title.personalizationOptions"/></h2>
<logic:present name="siteView"> 
<html:form action="/alternativeSite" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editCustomizationOptions"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<bean:define id="bodyComponent" name="siteView" property="component"/>
<bean:define id="objectCode"><%= pageContext.findAttribute("objectCode").toString() %></bean:define>

<p class="mvert15">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
	<html:link page="/alternativeSite.do?method=prepareImportCustomizationOptions&amp;page=0" paramId="objectCode" paramName="objectCode">
		<bean:message key="link.import.customizationOptions"/>
	</html:link>
</p>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop"><img src="<%= request.getContextPath() %>/images/number_1.gif" alt="<bean:message key="number_1" bundle="IMAGE_RESOURCES" />" />
		</td>
		<td class="infoop"><bean:message key="message.siteandmail.information" />
		</td>
	</tr>
</table>


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
			<%=ExecutionCourseAliasExpandingAction.emailAddressPrefix%><%=request.getParameter("objectCode")%>&#64;<%=TeacherAdministrationViewerDispatchAction.mailingListDomainConfiguration() %>	
	
			<span class="error" ><!-- Error messages go here -->
			<html:errors property="dynamicMailDistribution"/></span>
		</p>
    </td>    
</tr>
</table>



<table class="mtop2" width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop">
		<img src="<%= request.getContextPath() %>/images/number_2.gif" alt="<bean:message key="number_2" bundle="IMAGE_RESOURCES" />" />
	</td>
	<td class="infoop">
		<bean:message key="message.initialStatement.explanation" />
	</td>
</tr>
</table>



<table class="tstyle2 thlight thleft" style="width: 100%;">
<tr>
	<td style="width: 130px;">
		<bean:message key="message.initialStatement"/>
	</td>	
	<td>
		<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.initialStatement" bundle="HTMLALT_RESOURCES" name="bodyComponent" property="initialStatement" rows="10" cols="56"/> 
	</td>
</tr>
</table>


<table class="mtop2" width="100%" cellpadding="0" cellspacing="0">
<tr>
	<td class="infoop"><img src="<%= request.getContextPath() %>/images/number_3.gif" alt="<bean:message key="number_3" bundle="IMAGE_RESOURCES" />" />
	</td>
	<td class="infoop">
	  <bean:message key="message.introduction.explanation" />
	</td>
</tr>
</table>


<table class="tstyle2 thlight thleft" style="width: 100%;">
<tr>
	<td style="width: 130px;">
		<bean:message key="message.introduction"/>:
	</td>	
	<td>
		<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.introduction" bundle="HTMLALT_RESOURCES" name="bodyComponent" property="introduction" rows="10" cols="56"/>
	</td> 
</tr>
</table>


<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
		<bean:message key="button.save"/>
	</html:submit>

	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</p>

</html:form>
</logic:present>