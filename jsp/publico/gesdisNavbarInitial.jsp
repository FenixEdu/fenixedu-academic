<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<div id="nav">
   <h3>Navega&ccedil;&atilde;o Local</h3>	
<ul>	
<li><html:link  page="/accessAnnouncements.do">
	<bean:message key="link.announcements"/>
</html:link></li>
<li><html:link page="/accessTeachers.do">
	<bean:message key="link.teachers"/>
</html:link></li>
<li><html:link page="/viewTimeTable.do">
		<bean:message key="link.executionCourse.timeTable"/>
</html:link></li>
<li><html:link page="/viewExecutionCourseShifts.do">
		<bean:message key="link.executionCourse.shifts"/>
</html:link></li>
<li> <a href="/" onclick="houdini('seccao');return false;">Informa&ccedil;&atilde;o Curricular</a></li>
</ul>
 <dl id="seccao" style="display: none;">
            <dd><html:link page="/accessObjectives.do?method=acessObjectives">
				<bean:message key="link.objectives"/>
				</html:link></dd>
            <dd><html:link page="/accessProgram.do?method=acessProgram">
				<bean:message key="link.program"/>
				</html:link></dd>
            <dd><html:link page="/accessBibliographicReferences.do?method=viewBibliographicReference">
				<bean:message key="link.bibliography"/>
				</html:link></dd>
			<dd><html:link page="/curricularCourses.do">
				<bean:message key="link.associatedCurricularCourses"/>
				</html:link></dd>	
  </dl>
<logic:present name="<%= SessionConstants.SECTIONS %>" >
	<ul>
<logic:iterate id="section" name="<%= SessionConstants.SECTIONS %>" indexId="index">

<%-- <logic:match name="section" property="class" value="List">
	<dl id="seccao" style="display: none;">
	 <logic:iterate id="subSection" name="section" indexId="subIndex"> 
		 <dd><dd>item</dd></dd>
	</logic:iterate>	
	</dl>
</logic:match>	
<logic:notMatch name="section" property="class" value="List">
<ul><li><html:link page="/viewSection.do" indexed="true" >
	 <bean:write name="section" property="name"/> 
	secção
</html:link></li></ul>	
</logic:notMatch> --%>


<li><html:link page="/viewSection.do" indexed="true">section</html:link></li>
</logic:iterate>
</ul>	
</logic:present>		
</div>