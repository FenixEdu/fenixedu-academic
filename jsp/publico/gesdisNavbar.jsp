<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<bean:define id="executionCourse" name="<%= SessionConstants.INFO_SITE %>" property="infoExecutionCourse"/>
<bean:define id="executionCourseName" name="executionCourse" property="sigla"/>
	
<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
<bean:define id="executionPeriodName" name="executionPeriod" property="name"/>

<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>
<bean:define id="executionYearYear" name="executionYear" property="year"/>

<div id="nav">
<h3>Navega&ccedil;&atilde;o Local</h3>	
<ul>	
<li><html:link page="/viewSite.do">
	<bean:message key="link.inicialPage"/>
</html:link></li>
<li><html:link  page="/accessAnnouncements.do">
	<bean:message key="link.announcements"/>
</html:link></li>
<li> <a href="/" onclick="houdini('seccao');return false;">Informa&ccedil;&atilde;o Curricular</a></li>
</ul>
 <dl id="seccao" style="display: none;">
            <dd><html:link page="/accessObjectives.do?method=acessObjectives">
				<bean:message key="link.objectives"/>
				</html:link></dd>
            <dd><html:link page="<%= "/accessProgram.do?method=acessProgram" + "&amp;executionCourse=" + pageContext.findAttribute("executionCourseName") + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriodName") + "&amp;executionYear=" + pageContext.findAttribute("executionYearYear") %>">
				<bean:message key="link.program"/>
				</html:link></dd>
            <dd><html:link page="<%= "/accessBibliographicReferences.do?method=viewBibliographicReference" + "&amp;executionCourse=" + pageContext.findAttribute("executionCourseName") + "&amp;executionPeriod=" + pageContext.findAttribute("executionPeriodName") + "&amp;executionYear=" + pageContext.findAttribute("executionYearYear") %>">
				<bean:message key="link.bibliography"/>
				</html:link></dd>
			<dd><html:link page="/curricularCourses.do">
				<bean:message key="link.associatedCurricularCourses"/>
				</html:link></dd>	
  </dl>
 <ul> 

<li><html:link page="/viewTimeTable.do">
		<bean:message key="link.executionCourse.timeTable"/>
</html:link></li>
<li><html:link page="/viewExecutionCourseShifts.do">
		<bean:message key="link.executionCourse.shifts"/>
</html:link></li>
</ul>
<logic:present name="<%= SessionConstants.SECTIONS %>" >
	<logic:present name="<%= SessionConstants.INFO_SECTION %>" >
	<app:generateSectionMenu name="<%= SessionConstants.SECTIONS %>" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="<%= SessionConstants.INFO_SECTION %>" />
	</logic:present>
	<logic:notPresent name="<%= SessionConstants.INFO_SECTION %>" >
	<app:generateSectionMenu name="<%= SessionConstants.SECTIONS %>" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" />
	</logic:notPresent>		
</logic:present>	
</div>
<logic:present name="<%= SessionConstants.INFO_SITE %>" property="mail" >
<logic:notEmpty name="<%= SessionConstants.INFO_SITE %>" property="mail" >	
<div id="nav">
   <h3 >Contacto</h3>	
 
  	<bean:define id="mail" name="<%=SessionConstants.INFO_SITE%>" property="mail"/>
	<html:link href="<%= "mailto:" + pageContext.findAttribute("mail") %>">
		<p class="contacto"><bean:write name="mail" /></p></html:link>
  	 
</div>
</logic:notEmpty>
</logic:present>