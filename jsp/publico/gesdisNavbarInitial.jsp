<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<div id="nav">
   <h3><bean:message key="title.navigation.local"/></h3>	
<ul>	
<li>
	<html:link page="<%= "/viewSite.do" + "?method=announcements&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
		<bean:message key="link.announcements"/>
	</html:link>
</li>
<li> <a href="/" onclick="houdini('seccao');return false;"><bean:message key="label.curricular.information"/></a></li>
</ul>
 <dl id="seccao" style="display: none;">
            <dd><html:link page="<%= "/viewSite.do?method=objectives" + "&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.objectives"/>
				</html:link></dd>
            <dd><html:link page="<%= "/viewSite.do?method=program" + "&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.program"/>
				</html:link></dd>
            <dd><html:link page="<%= "/viewSite.do?method=evaluation&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.evaluation"/>
				</html:link></dd>	
            <dd><html:link page="<%= "/viewSite.do?method=bibliography&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.bibliography"/>
				</html:link></dd>
			<dd><html:link page="<%= "/viewSite.do?method=curricularCourses&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.associatedCurricularCourses"/>
				</html:link></dd>	
  </dl>
  <ul>

<li><html:link page="<%= "/viewSite.do" + "?method=timeTable&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
		<bean:message key="link.executionCourse.timeTable"/>
</html:link></li>
<li><html:link page="<%= "/viewSite.do" + "?method=shifts&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
		<bean:message key="link.executionCourse.shifts"/>
</html:link></li>
</ul>
<bean:define id="component" name="siteView" property="commonComponent" />
<bean:define id="sections" name="component" property="sections"/>
<logic:notEmpty name="sections" >
	<logic:present name="infoSection" >
	<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="infoSection" />
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