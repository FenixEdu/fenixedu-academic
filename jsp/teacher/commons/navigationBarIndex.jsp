<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionPeriod"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/showProfessorships.do?method=list" %>">
  		<bean:message key="link.manage.executionCourse"/>
  	</html:link>
  </li>
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/tutorSection.do" %>">
  		<bean:message key="link.teacher.tutor.operations"/>
  	</html:link>  
  </li>
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/teacherInformation.do?method=prepareEdit&amp;page=0" %>">
  		<bean:message key="link.manage.teacherInformation"/>
  	</html:link>
  </li>
 <%--
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/prepareTeacherCreditsSheet.do" %>">
  		<bean:message key="link.view.teacher.credits.sheet"/>
  	</html:link>  
  </li>--%>
<%--  <li>
  	<html:link forward="creditsManagement" paramId="teacherOID" paramName="infoTeacher" paramProperty="idInternal">
  		<bean:message key="link.manage.credits"/>
  	</html:link>
  </li> --%>
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/finalWorkManagement.do?method=chooseDegree" %>">
  		<bean:message key="link.manage.finalWork"/>
  	</html:link>
  </li>
   <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/publicationManagement.do" %>">
  		<bean:message key="link.manage.publications"/>
  	</html:link>  
  </li>
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare" %>">
  		<bean:message key="link.curriculumHistoric.consult" bundle="CURRICULUM_HISTORIC_RESOURCES" />
  	</html:link>  
  </li>
  <li>
  	<bean:define id="url"><%= request.getContextPath() %>/teacher/credits.do?method=showTeacherCredits&amp;teacherId=<bean:write name="UserView" property="person.teacher.idInternal"/>&amp;executionPeriodId=<%= ExecutionPeriod.readActualExecutionPeriod().getIdInternal() %></bean:define>
  	<html:link href="<%= url %>">
  		<bean:message key="link.credits"/>
  	</html:link>  
  </li>
  <%--<li>
    <html:link page="/tests/questionBank.do?method=manageQuestionBank">
      <bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" />
    </html:link>
  </li>
  
  <li>
    <html:link page="/tests/testModels.do?method=manageTestModels">
      <bean:message key="message.testModels.manage" bundle="TESTS_RESOURCES" />
    </html:link>
  </li>--%>
  
  <%--
  <li>
	<html:link href="<%= request.getContextPath() + "/teacher/viewOldInquiriesTeachersResults.do?method=prepare" %>">
		<bean:message key="link.view.inquiries.results" bundle="INQUIRIES_RESOURCES"/>
	</html:link>
  </li>
  --%>
  
</ul>
