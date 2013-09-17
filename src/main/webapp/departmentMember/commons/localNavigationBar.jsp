<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>

<logic:present role="DEPARTMENT_MEMBER">

	<ul>	
		<li class="navheader"><bean:message key="label.teacher"/></li>
		<li>
		  	<html:link page="/credits.do?method=showTeacherCredits">
		  		<bean:message key="link.teacher.credits"/> (RSD)
		  	</html:link>  
		</li>
		<li>
		  	<html:link page="/credits.do?method=showTeacherManagementFunctions">
		  		<bean:message key="label.teacher.details.functionsInformation"/>
		  	</html:link>  
		</li>
	</ul>
	<ul>	
		<li class="navheader"><bean:message key="title.department"/></li>
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
	<%--        
	        <li>
    	        <html:link page="/viewInquiriesResults.do?method=chooseDegreeCurricularPlan">
        	        <bean:message key="title.inquiries.results" bundle="INQUIRIES_RESOURCES"/>
            	</html:link>
        	</li>
	--%>
		<logic:notEmpty name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.vigilantWrappers">
			<li>
				<html:link page="/vigilancy/vigilantManagement.do?method=prepareMap">
					<bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/>
				</html:link>
			</li>
		</logic:notEmpty>
  		<%--<li>
		  	<html:link page="/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume">
		  		<bean:message key="link.teacher.credits"/>
		  	</html:link>  
		</li>--%>
		<li>
			<html:link page="/exportCredits.do?method=prepareExportDepartmentCourses">
				<bean:message key="label.executionCourses.types" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/departmentForum.do?method=prepare">
				<bean:message key="link.foruns"/>
			</html:link>
		</li>
	</ul> 
	<% 
	net.sourceforge.fenixedu.domain.Department department =  AccessControl.getPerson().getTeacher().getCurrentWorkingDepartment();
	if (AccessControl.getPerson().getTeacher() != null && department!=null && department.isCurrentUserCurrentDepartmentPresident()){ %>
	<ul>
		<li class="navheader">
			<bean:message key="link.teacher.creditsSupervision" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</li>
		<li>
		  	<html:link page="/creditsReductions.do?method=showReductionServices">
		  		<bean:message key="label.credits.creditsReduction" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		  	</html:link>  
		</li>
	</ul>
	<ul>
		<li class="navheader">
			<bean:message key="label.teacher.evaluation.title" bundle="RESEARCHER_RESOURCES"/>
		</li>

		<li>
			<html:link page="/teacherEvaluation.do?method=viewManagementInterface">
				<bean:message key="label.teacher.evaluation.management.title" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>	
	</ul>
	<% } %>
	<!-- Temporary solution (until we make expectations available for all departments) DEI Code = 28 -->
	<ul style="margin-top: 1em">
		<bean:define id="userView" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>"/>
		<% String deiCode = "28"; %>
		<logic:notEmpty name="userView" property="person.teacher">
			<logic:notEmpty name="userView" property="person.teacher.currentWorkingDepartment">	
				<logic:equal name="userView" property="person.teacher.currentWorkingDepartment.code" value="<%= deiCode %>">
					
					<li class="navheader"><bean:message key="title.accompaniment" bundle="DEPARTMENT_MEMBER_RESOURCES"/></li>
					<li>
						<html:link page="/personalExpectationManagement.do?method=viewTeacherPersonalExpectations">
							<bean:message key="link.personalExpectationsManagement"/>
						</html:link>
					</li>				
					<li>
						<html:link page="/teacherExpectationAutoAvaliation.do?method=show">
							<bean:message key="label.autoEvaluation"/>
						</html:link>
					</li>
					<li>
						<html:link page="/evaluateExpectations.do?method=chooseTeacher">
							<bean:message key="label.evaluate.expectations"/>
						</html:link>
					</li>
					<li>
						<html:link page="/listTeachersPersonalExpectations.do?method=listTeachersPersonalExpectations">
							<bean:message key="label.see.teachers.personal.expectations"/>
						</html:link>
					</li>																
				</logic:equal>
			</logic:notEmpty>
		</logic:notEmpty>	
	</ul>	
	<% IUserView user = (IUserView) userView; 
	 if (user.getPerson().hasTeacher() && user.getPerson().getTeacher().hasAnyExecutionCourseAudits()) { %>
		<ul style="margin-top: 1em">
			<li class="navheader"><bean:message key="link.inquiry.audit" bundle="INQUIRIES_RESOURCES"/></li>
			<li>
				<html:link page="/qucAudit.do?method=showAuditProcesses">
					<bean:message key="link.inquiry.auditProcesses" bundle="INQUIRIES_RESOURCES"/>
				</html:link>
			</li>
		</ul>
	<% } %>
		
	<% if (user.getPerson().hasFunctionType(net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType.PRESIDENT,
   		    net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum.MANAGEMENT_FUNCTION)) { %>	
		<ul>
			<li class="navheader"><bean:message key="title.department.quc"/></li>
			<li>
				<html:link page="/viewQucResults.do?method=resumeResults">
					<bean:message key="label.quc.results"/>
				</html:link>
			</li>
		</ul>		
	<% } %>		
			
	<logic:notEmpty name="userView" property="person.teacher">
		<logic:notEmpty name="userView" property="person.teacher.currentWorkingDepartment">	
			<bean:define id="unit" name="userView" property="person.teacher.currentWorkingDepartment.departmentUnit"/>
			<bean:define id="unitID" name="unit" property="externalId"/>
			<bean:define id="unitExternalId" name="unit" property="externalId"/>
			
			<ul>	
				<li class="navheader"><fr:view name="unit" property="acronym"/></li>
				<li>
					<html:link page="<%= "/sendEmailToDepartmentGroups.do?method=prepare&" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/messaging") + "&unitExternalId=" + unitExternalId %>">
						<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
					</html:link>
				</li>	
				<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
					<li>
						<html:link page="<%= "/departmentFunctionalities.do?method=configureGroups&unitId=" + unitID %>"><bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
						</html:link>
					</li>
				</logic:equal>
				<li>
					<html:link page="<%= "/departmentFunctionalities.do?method=showProjects&unitId=" + unitID %>"><bean:message key="label.showProjects" bundle="RESEARCHER_RESOURCES"/>
					</html:link>
				</li>
				<li>
					<html:link page="<%= "/departmentFunctionalities.do?method=manageFiles&unitId=" + unitID %>"><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></html:link>
				</li>						
			</ul>
		
			<logic:notEmpty name="unit" property="allSubUnits">
				<ul>
				<logic:iterate id="subUnit" name="unit" property="allSubUnits">
					<logic:equal name="subUnit" property="scientificAreaUnit"  value="true">
						<logic:equal name="subUnit" property="currentUserMemberOfScientificArea" value="true">
							<bean:define id="subUnitID" name="subUnit" property="externalId"/>
							<bean:define id="subUnitExternalID" name="subUnit" property="externalId"/>
							<li class="navheader"><fr:view name="subUnit" property="name"/></li>
									<ul>
										<li>
											<html:link page="<%= "/sendEmailToDepartmentGroups.do?method=prepare&" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/messaging") + "&unitExternalId=" + subUnitExternalID %>">
												<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
											 </html:link>
										 </li>	
										  <logic:equal name="subUnit" property="currentUserAbleToDefineGroups" value="true">
										  <li>
											 <html:link page="<%= "/departmentFunctionalities.do?method=configureGroups&unitId=" + subUnitID %>"><bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
											 </html:link>
										  </li>
										  </logic:equal>
										  <li><html:link page="<%= "/departmentFunctionalities.do?method=manageFiles&unitId=" + subUnitID %>"><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></html:link></li>						
									</ul>							
						</logic:equal>
					</logic:equal>
				</logic:iterate>
				</ul>
			</logic:notEmpty>		
		</logic:notEmpty>
	</logic:notEmpty>
				
	<ul style="margin-top: 1em">
  		<li>
		  	<html:link page="/tsdProcess.do?method=prepareTSDProcess">
		  		<bean:message key="link.teacherServiceDistribution"/>
		  	</html:link>  
		</li> 							
	</ul>
		
		

</logic:present>
