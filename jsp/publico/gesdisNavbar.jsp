<%@ page language="java" %>

<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>



<div id="nav">
<h3>Navega&ccedil;&atilde;o Local</h3>	
<ul>
<li><html:link page="<%= "/viewSite.do?method=executionCourseViewer&exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
	<bean:message key="link.inicialPage"/>
</html:link></li>
<li>
	<html:link page="<%= "/accessAnnouncements.do" + "?exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
		<bean:message key="link.announcements"/>
	</html:link>
</li>
<li> <a href="/" onclick="houdini('seccao');return false;">Informa&ccedil;&atilde;o Curricular</a></li>
</ul>
 <dl id="seccao" style="display: none;">
            <dd><html:link page="<%= "/accessObjectives.do?method=acessObjectives" + "&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.objectives"/>
				</html:link></dd>
            <dd><html:link page="<%= "/accessProgram.do?method=acessProgram" + "&amp;exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.program"/>
				</html:link></dd>
			 <dd><html:link page="<%= "/viewEvaluation.do?exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.evaluation"/>
				</html:link></dd>	
            <dd><html:link page="<%= "/accessBibliographicReferences.do?exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.bibliography"/>
				</html:link></dd>
			<dd><html:link page="<%= "/curricularCourses.do?exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
				<bean:message key="link.associatedCurricularCourses"/>
				</html:link></dd>	
  </dl>
 <ul> 

<li><html:link page="<%= "/viewTimeTable.do" + "?exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
		<bean:message key="link.executionCourse.timeTable"/>
</html:link></li>
<li><html:link page="<%= "/viewExecutionCourseShifts.do" + "?exeCode=" + pageContext.findAttribute("exeCode") + "&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" + pageContext.findAttribute("eYName") %>">
		<bean:message key="link.executionCourse.shifts"/>
</html:link></li>
</ul>

<logic:present name="sections" >
	<logic:present name="infoSection" >
	<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" activeSectionName="infoSection" />
	</logic:present>
	<logic:notPresent name="infoSection" >
	<app:generateSectionMenu name="sections" path="<%=  request.getContextPath() + RequestUtils.getModuleName(request,application)%>" />
	</logic:notPresent>		
</logic:present>	
</div>
<logic:present name="siteMail"  >
<logic:notEmpty name="siteMail"  >	
<div id="nav">
   <h3 >Contacto</h3>	
 	
	<html:link href="<%= "mailto:" + pageContext.findAttribute("siteMail") %>">
		<p class="contacto"><bean:write name="siteMail" /></p></html:link>
  	 
</div>
</logic:notEmpty>
</logic:present>