<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.departmentAdmOffice" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.define.expectations.evaluation.groups" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></h2>

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">

	<logic:notEmpty name="executionYearBean">
		
		<fr:form action="/defineExpectationEvaluationGroups.do?method=listGroupsWithSelectedExecutionYear">
			<div class="mtop2 mbottom1">
			<bean:message key="label.common.executionYear" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>:
			<fr:edit id="executionYear" name="executionYearBean" slot="executionYear"> 
				<fr:layout name="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsToViewTeacherPersonalExpectationsProvider"/>
					<fr:property name="format" value="${year}"/>
					<fr:destination name="postback" path="/defineExpectationEvaluationGroups.do?method=listGroupsWithSelectedExecutionYear"/>
				</fr:layout>
			</fr:edit>
			<html:submit styleClass="switchNone">
				<bean:message key="label.next" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
			</html:submit>
			</div>
		</fr:form>
		
		<logic:notEmpty name="teachers">
	
			<bean:define id="executionYear" name="executionYearBean" property="executionYear"/>	
				
			<table class="tstyle2 thleft thlight mtop15">
				<tr>
					<th><bean:message key="label.appraiser.teacher.name" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>
					<th><bean:message key="label.teacher.number" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>	
					<th><bean:message key="label.teacher.category" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>				
					<th><bean:message key="label.evaluated.teachers" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>				
					<th><bean:message key="label.action" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></th>				
				</tr>
				
				<logic:iterate id="mapEntry" name="teachers">
					
					<bean:define id="teacher" name="mapEntry" property="key" />
					<bean:define id="evaluatedGroup" name="mapEntry" property="value" />
										
					<tr>
						<td>
							<bean:write name="teacher" property="person.name"/>
						</td>
						<td class="acenter">
							<bean:write name="teacher" property="teacherId"/>
						</td>
						<td class="acenter">
							<logic:notEmpty name="teacher" property="category">
								<bean:write name="teacher" property="category.name"/>
							</logic:notEmpty>
							<logic:empty name="teacher" property="category">
								--
							</logic:empty>
						</td>
						<td>
							<logic:notEmpty name="evaluatedGroup">
								<ul class="list6 nobullet">
									<logic:iterate id="group" name="evaluatedGroup">
										<li>
											<bean:write name="group" property="evaluated.person.name"/> (<bean:write name="group" property="evaluated.teacherId"/>)
										</li>																																	
									</logic:iterate>
								</ul>	
							</logic:notEmpty>
						</td>
						<td>																					
							<bean:define id="createGroupURL">/defineExpectationEvaluationGroups.do?method=manageGroups&amp;teacherID=<bean:write name="teacher" property="externalId"/>&amp;executionYearID=<bean:write name="executionYear" property="externalId"/></bean:define>														
							<html:link page="<%= createGroupURL %>">
								<bean:message key="label.manage.groups" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>
							</html:link>						
						</td>						
					</tr>
				</logic:iterate>
									
			</table>		
			
		</logic:notEmpty>
		
		<script type="text/javascript" language="javascript">
			switchGlobal();
		</script>
	
	</logic:notEmpty>

</logic:present>