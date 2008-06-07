<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
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
			<li>
				<html:link page="/vigilancy/vigilantManagement.do?method=prepareMap">
					<bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/>
				</html:link>
			</li>
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
			<bean:define id="userView" name="<%= pt.ist.fenixWebFramework.servlets.filters.USER_SESSION_ATTRIBUTE %>" scope="session"/>
			<% String deiCode = "28"; %>
			
			<logic:notEmpty name="userView" property="person.employee.currentDepartmentWorkingPlace">	
				<logic:equal name="userView" property="person.employee.currentDepartmentWorkingPlace.code" value="<%= deiCode %>">
					
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
		</ul>	
		<%
			IUserView user = (IUserView) userView;
        	if (user.getPerson().hasFunctionType(net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType.ASSIDUOUSNESS_RESPONSIBLE,
    		    net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum.ASSIDUOUSNESS_STRUCTURE)) {
        %>
		<ul style="margin-top: 1em">
			<li class="navheader"><bean:message key="title.assiduousnessResponsible" bundle="ASSIDUOUSNESS_RESOURCES"/></li>
			<li>
				<html:link page="/assiduousnessResponsible.do?method=showEmployeeList">
					<bean:message key="label.employees" bundle="ASSIDUOUSNESS_RESOURCES"/>
				</html:link>
			</li>
		</ul>
		<%
			} 
		%>
	
		
		<logic:notEmpty name="userView" property="person.employee.currentDepartmentWorkingPlace">	
		<bean:define id="unit" name="userView" property="person.employee.currentDepartmentWorkingPlace.departmentUnit"/>
		<bean:define id="unitID" name="unit" property="idInternal"/>
		
		<ul>	
		<li class="navheader"><fr:view name="unit" property="acronym"/></li>
			<ul>
				<li>
					<html:link page="<%= "/sendEmailToDepartmentGroups.do?method=prepare&unitId=" + unitID %>">
						<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
					 </html:link>
				 </li>	
				  <logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
				  <li>
					 <html:link page="<%= "/departmentFunctionalities.do?method=configureGroups&unitId=" + unitID %>"><bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
					 </html:link>
				  </li>
				  </logic:equal>
				  <li><html:link page="<%= "/departmentFunctionalities.do?method=manageFiles&unitId=" + unitID %>"><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></html:link></li>						
			</ul>
		</li>
		</ul>
		
			<logic:notEmpty name="unit" property="allSubUnits">
				<ul>
				<logic:iterate id="subUnit" name="unit" property="allSubUnits">
					<logic:equal name="subUnit" property="scientificAreaUnit"  value="true">
						<logic:equal name="subUnit" property="currentUserMemberOfScientificArea" value="true">
							<bean:define id="subUnitID" name="subUnit" property="idInternal"/>
							<li class="navheader"><fr:view name="subUnit" property="name"/></li>
									<ul>
										<li>
											<html:link page="<%= "/sendEmailToDepartmentGroups.do?method=prepare&unitId=" + subUnitID %>">
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
		
		<logic:notEmpty name="UserView" property="person.protocols">
			<ul style="margin-top: 1em">
			<li class="navheader"><bean:message key="title.protocolsResponsible" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></li>
			<li>
				<html:link page="/protocols.do?method=showProtocols"><bean:message key="title.protocols" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:link>
			</li>	
			</ul>
		</logic:notEmpty>
				
		<ul style="margin-top: 1em">
	  		<li>
			  	<html:link page="/tsdProcess.do?method=prepareTSDProcess">
			  		<bean:message key="link.teacherServiceDistribution"/>
			  	</html:link>  
			</li> 							
		</ul>
		


</logic:present>
