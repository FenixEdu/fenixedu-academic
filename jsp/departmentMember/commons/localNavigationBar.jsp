<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>	
<%@ page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>

<logic:present role="DEPARTMENT_MEMBER">
	<!-- Temporary solution (until we make expectations available for all departments) DEI Code = 28 -->
	<ul>
		<li>
			<html:link page="/viewDepartmentTeachers/listDepartmentTeachers.faces">
				<bean:message key="link.departmentTeachers"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</html:link>
		</li>
		<li>
			<html:link page="/courseStatistics/viewCompetenceCourses.faces">
				<bean:message key="link.departmentCourses"/>
			</html:link>
		</li>
		<li>
			<html:link page="/viewTeacherService/viewTeacherService.faces">
				<bean:message key="link.teacherService"/>
			</html:link>
		</li>
		
		<logic:notEmpty name="UserView" property="person.vigilants">
		<li><html:link  page="/vigilancy/vigilantManagement.do?method=prepareMap"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></html:link></li>
		</logic:notEmpty>

		</ul>
		<ul style="margin-top: 1em">
  		<li>
  		
		  	<html:link page="/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume">
		  		<bean:message key="link.teacher.credits"/>
		  	</html:link>  
		</li>
		</ul>
		
		<ul style="margin-top: 1em">
		<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
		<% String deiCode = "28"; %>
		<logic:notEmpty name="userView" property="person.employee.currentDepartmentWorkingPlace">
			<logic:equal name="userView" property="person.employee.currentDepartmentWorkingPlace.code" value="<%= deiCode %>">
				<li>
					<html:link page="/expectationManagement/viewPersonalExpectation.faces">
						<bean:message key="link.personalExpectationsManagement"/>
					</html:link>
				</li>
				<li>
					<html:link page="/teacherExpectationAutoAvaliation.do?method=show">
						<bean:message key="label.autoEvaluation"/>
					</html:link>
				</li>
			</logic:equal>
		</logic:notEmpty>	

		<!-- Temporary solution until department defines criteria for access to personal expectations -->		
		<bean:define id="username" name="userView" property="person.username" type="java.lang.String" />
		<% if (username.equalsIgnoreCase("ist12023") || username.equalsIgnoreCase("ist11416")) { %>
  		<li>
		  	<html:link page="/viewDepartmentTeachersExpectations.do?method=prepare">
		  		<bean:message key="link.departmentTeachersExpectations"/>
		  	</html:link>  
		</li> 				
		<% } %>
		
		<%IUserView user = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            if (user.getPerson().hasFunctionType(net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType.ASSIDUOUSNESS_RESPONSIBLE)) {%>
			</ul>
			<ul style="margin-top: 1em">
			<li class="navheader"><bean:message key="title.assiduousnessResponsible" bundle="ASSIDUOUSNESS_RESOURCES"/></li>
			<li><html:link page="/assiduousnessResponsible.do?method=showEmployeeList">
				<bean:message key="label.employees" bundle="ASSIDUOUSNESS_RESOURCES"/>
			</html:link></li>
		<% } %>
		
		
		<%-- TeacherServiceDistribution entry point --%>
		</ul>
		<ul style="margin-top: 1em">
  		<li>
		  	<html:link page="/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution">
		  		<bean:message key="link.teacherServiceDistribution"/>
		  	</html:link>  
		</li> 				
		
	
	</ul>
	
</logic:present>
