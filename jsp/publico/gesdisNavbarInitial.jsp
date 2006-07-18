<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="curricularCourses" name="component" property="associatedDegrees" />
<div id="nav">
   <h3><bean:message key="title.navigation.local"/></h3>	
<ul>	
<li>

	<html:link page="<%= "/viewSite.do" + "?method=announcements&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>">
		<bean:message key="link.announcements"/>
	</html:link>
</li>
<li>
	<html:link page="<%= "/viewSite.do" + "?method=summaries&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)  %>">
		<bean:message key="link.summaries.public"/>
	</html:link>
</li>
<li>
	<html:link page="<%= "/viewSite.do?method=evaluationMethod&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>">
				<bean:message key="link.evaluationMethod"/>
	</html:link>
</li>
<li>
	<html:link page="<%= "/viewSite.do?method=bibliography&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>">
				<bean:message key="link.bibliography"/>
	</html:link>
</li>
<li> <a href="/" onclick="houdini('seccao');return false;"><bean:message key="label.curricular.information"/></a></li>
</ul>
 <dl id="seccao" style="display: none;">
<logic:iterate id="curricularCourse" name="curricularCourses">
		<bean:define id="curricularCourseId" name="curricularCourse" property="idInternal" />
	<dd><html:link page="<%= "/viewSite.do?method=curricularCourse&amp;objectCode=" + pageContext.findAttribute("objectCode") +"&amp;ccCode=" +  pageContext.findAttribute("curricularCourseId") + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>">
				<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>-<bean:write name="curricularCourse" property="name"/>
	</html:link></dd>
</logic:iterate>
 </dl>
<ul>

<li><html:link page="<%= "/viewSite.do" + "?method=timeTable&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>">
		<bean:message key="link.executionCourse.timeTable"/>
</html:link></li>
<li><html:link page="<%= "/viewSite.do" + "?method=shifts&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>">
		<bean:message key="link.executionCourse.shifts"/>
</html:link></li>

<li><html:link page="<%= "/viewSite.do" + "?method=evaluation&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>">
		<bean:message key="link.evaluation"/>
</html:link></li>

<li><html:link page="<%= "/viewSite.do" + "?method=viewExecutionCourseProjects&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>">
		<bean:message key="link.groups"/>
</html:link></li>


</ul>
<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="sections" name="component" property="sections"/>
<logic:notEmpty name="sections" >
	<logic:present name="infoSection" >
		<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="infoSection"/>
	</logic:present>
	<logic:notPresent name="infoSection" >
		<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" />
	</logic:notPresent>		
</logic:notEmpty>	
</div>
<logic:notEmpty name="component" property="mail" >	
<bean:define id="siteMail"	name="component" property="mail" />
<div id="nav">
   <h3 ><bean:message key="label.contact"/></h3>	
 	
	<html:link href="<%= "mailto:" + pageContext.findAttribute("siteMail") %>">
		<p class="contacto"><bean:write name="siteMail" /></p></html:link>
  	 
</div>
</logic:notEmpty>