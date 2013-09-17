<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%-- INCOMPLETE, OBSOLETE, DELETE THIS --%>
<html:xhtml/>

<bean:define id="semester" name="executionCourse" property="executionPeriod" />
<bean:define id="executionYear" name="semester" property="executionYear" />
<bean:define id="executionCourseId" name="executionCourse" property="externalId" />

<link href="<%= request.getContextPath() + "/CSS/jqTheme/redmond/jquery-ui.css" %>" rel="stylesheet" type="text/css"></link> 

<script type="text/javascript">
	
	$(document).ready(function() {
		var tabNum = $("#tabNum").html();
		$("#tabs").tabs({ selected: parseInt(tabNum)});
	});

</script>

<span id="tabNum" style="display:none">
	<%= request.getAttribute("tabNum") %>
</span>

<h2>
	<bean:message key="title.execution.course.management.execution.course" bundle="ACADEMIC_OFFICE_RESOURCES" />: 
	<bean:write name="executionCourse" property="nome" /> - 
	<bean:write name="semester" property="name" /> -
	<bean:write name="executionYear" property="name" />
</h2>

<div id="tabs">
	<ul>
		<li><a href="#tabs-1"><bean:message key="title.execution.course.management.attributes" bundle="ACADEMIC_OFFICE_RESOURCES" /></a></li>
		<li><a href="#tabs-2"><bean:message key="title.execution.course.management.curricular.courses" bundle="ACADEMIC_OFFICE_RESOURCES" /></a></li>
		<li><a href="#tabs-3"><bean:message key="title.execution.course.management.students" bundle="ACADEMIC_OFFICE_RESOURCES" /></a></li>
		<li><a href="#tabs-4"><bean:message key="title.execution.course.management.transfer" bundle="ACADEMIC_OFFICE_RESOURCES" /></a></li>
		<li><a href="#tabs-5"><bean:message key="title.execution.course.management.separation" bundle="ACADEMIC_OFFICE_RESOURCES" /></a></li>
		<li><a href="#tabs-6"><bean:message key="title.execution.course.management.summaries" bundle="ACADEMIC_OFFICE_RESOURCES" /></a></li>		
	</ul>
	
	<div id="tabs-1">
		<fr:view name="executionCourse">
			<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionCourse" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="nome" />
				<fr:slot name="sigla" />
				<fr:slot name="entryPhase" />
				<fr:slot name="comment" />
			</fr:schema>
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
			</fr:layout>
		</fr:view>
		
		<p>
			<html:link action="<%= "/executionCourseManagement.do?method=prepareEditAttributes&executionCourseId=" + executionCourseId %>">
				<bean:message key="link.edit" bundle="APPLICATION_RESOURCES" />
			</html:link>
		</p>
	</div>
	
	<div id="tabs-2">
		<logic:messagesPresent message="true" property="error">
			<div class="error3 mbottom05" style="width: 500px;">
				<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
					<p class="mvert025"><bean:write name="messages" /></p>
				</html:messages>
			</div>
		</logic:messagesPresent>
		
	
		<bean:define id="associatedCurricularCourses" name="executionCourse" property="associatedCurricularCourses" />

		<p><strong><bean:message key="label.execution.course.management.curricular.courses" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
		
		<logic:empty name="associatedCurricularCourses">
			<p><em><bean:message key="message.execution.course.management.curricular.courses.not.associated" bundle="ACADEMIC_OFFICE_RESOURCES" />.</em></p>
		</logic:empty>
		
		<logic:notEmpty name="associatedCurricularCourses">
			<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseManagementBean"/>
			
			<bean:define id="removeLink">
				<%
				if(bean.getDegreeCurricularPlan() != null) {
				%>
					<bean:define id="degreeCurricularPlanId" name="bean" property="degreeCurricularPlan.externalId" />
					<%= String.format("/executionCourseManagement.do?method=removeSelectedCurricularCourseOnEdit&degreeCurricularPlan=%s&executionCourseId=%s&curricularCourseId=", degreeCurricularPlanId, executionCourseId) %>
				<%
				} else {
				%>
					<%= String.format("/executionCourseManagement.do?method=removeSelectedCurricularCourseOnEdit&executionCourseId=%s&curricularCourseId=", executionCourseId) %>
				<%
				}
				%>
			</bean:define>
			
			<fr:view name="associatedCurricularCourses">
				<fr:schema type="net.sourceforge.fenixedu.domain.CurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES" >
					<fr:slot name="name" />
				</fr:schema>
				
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1" />
					
					<fr:link name="remove" 
						link="<%= removeLink + "${externalId}" %>"
						label="label.remove,APPLICATION_RESOURCES" 
						confirmation="message.execution.course.management.curricular.dissociation.confirmation,ACADEMIC_OFFICE_RESOURCES" />
						
				</fr:layout>
			</fr:view>
			
		</logic:notEmpty>
		
		<p><strong><bean:message key="label.execution.course.management.curricular.course.associate" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
		
		<div>
		
			<fr:form action="/executionCourseManagement.do">
				<fr:edit id="bean" name="bean" visible="false" />
				
				<fr:edit id="bean-select" name="bean">
					<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseManagementBean"
						bundle="ACADEMIC_OFFICE_RESOURCES">	
						<fr:slot name="degreeCurricularPlan" layout="menu-select-postback">
							<fr:property name="destination" value="chooseDegreeCurricularPlanPostback" />	
							<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DegreeCurricularPlanProvider" />
							<fr:property name="format" value="${degreeType.localizedName} - ${presentationName}" />
						</fr:slot>
						
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1" />
						<fr:property name="columnClasses" value=",,tdclear" />
					</fr:layout>
					
					<fr:destination name="chooseDegreeCurricularPlanPostback"
						path="<%= "/executionCourseManagement.do?method=chooseDegreeCurricularPlanPostbackForEdit&executionCourseId=" + executionCourseId %>" />
				</fr:edit>
				
			</fr:form>
			
			<logic:notEmpty name="bean" property="degreeCurricularPlan">
				<logic:empty name="curricularCoursesToAdd">
					<p><em><bean:message key="message.execution.course.management.degree.curricular.plan.curricular.courses.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
				</logic:empty>
			
				<logic:notEmpty name="curricularCoursesToAdd">
				
					<fr:view name="curricularCoursesToAdd">
						
						<fr:schema type="net.sourceforge.fenixedu.domain.CurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES">
							<fr:slot name="name" />
						</fr:schema>
						
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1" />
							
							<fr:link name="remove" 
								link="<%= "/executionCourseManagement.do?method=addSelectedCurricularCourseOnEdit&curricularCourseId=${externalId}&executionCourseId=" + executionCourseId %>"
								label="label.add,APPLICATION_RESOURCES" />
							
						</fr:layout>
					</fr:view>
					
				</logic:notEmpty>
			</logic:notEmpty>

		</div>

	</div>
	
	<div id="tabs-3">
		
		<logic:empty name="executionCourse" property="attends">
			<p><em><bean:message key="message.execution.course.management.students.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
		</logic:empty>
	

		<logic:notEmpty name="executionCourse" property="attends">
			<logic:messagesPresent message="true" property="error">
				<div class="error3 mbottom05" style="width: 500px;">
					<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
						<p class="mvert025"><bean:write name="messages" /></p>
					</html:messages>
				</div>
			</logic:messagesPresent>
		
			<fr:view name="executionCourse" property="attends">
			
				<fr:schema type="net.sourceforge.fenixedu.domain.Attends" bundle="ACADEMIC_OFFICE_RESOURCES">
					<fr:slot name="registration.number" />
					<fr:slot name="registration.student.name" />
				</fr:schema>
			
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1" />
					
					<fr:link name="remove"
						link="<%= "/executionCourseManagement.do?method=removeAttends&attendsId=${externalId}&executionCourseId=" + executionCourseId %>"
						confirmation="message.executionCourse.remove.attends.confirmation"
						label="label.remove,APPLICATION_RESOURCES" 
						condition="ableToBeRemoved" />
						
				</fr:layout>
			</fr:view>
			
		</logic:notEmpty>
		
	</div>
	
	<style type="text/css">
		div.executionCourseTransfer div.left {
			width: 300px;
			float:left;
		}
		
		div.executionCourseTransfer div.right {
			width: 700px;
			float:right;
		}
		
		div.executionCourseTransfer div.clear {
			clear: both;
		}

		div.executionCourseTransfer div.right td.width200px select {
			width: 400px;
			float:right;
		}

	</style>
	
	<div id="tabs-4" class="executionCourseTransfer">
		<p><strong><bean:message key="label.execution.course.management.transfer.curricular.courses.and.shifts" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
		<div class="left">
			
			<fr:form action="<%= "/executionCourseManagement.do?method=transfer&executionCourseId=" + executionCourseId %>">
			
				<fr:edit id="bean" name="bean" visible="false" />
			
				<p><strong><bean:message key="label.execution.course.management.curricular.courses" bundle="ACADEMIC_OFFICE_RESOURCES" /> </strong></p>
				<logic:empty name="executionCourse" property="associatedCurricularCourses">
					<p><em><bean:message key="message.execution.course.management.execution.course.not.associated.to.any.curricular" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
				</logic:empty>
				
				<logic:notEmpty name="executionCourse" property="associatedCurricularCourses">
	
					<fr:view name="executionCourse" property="associatedCurricularCourses">
						<fr:schema type="net.sourceforge.fenixedu.domain.CurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES">
							<fr:slot name="nameI18N" />
							<fr:slot name="degreeCurricularPlan.degree.sigla" />
						</fr:schema>
						
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1" />
							<fr:property name="checkable" value="true" />
							<fr:property name="checkboxName" value="curricularCourseIdsToTransfer" />
							<fr:property name="checkboxValue" value="externalId" />
						</fr:layout>
					</fr:view>
	
				</logic:notEmpty>
				
				<p><strong><bean:message key="label.execution.course.management.shifts" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
				<logic:empty name="executionCourse" property="associatedShifts">
					<p><em><bean:message key="label.execution.course.management.associated.shifts.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
				</logic:empty>
				
				<logic:notEmpty name="executionCourse" property="associatedShifts">
	
					<fr:view name="executionCourse" property="associatedShifts">
						<fr:schema type="net.sourceforge.fenixedu.domain.Shift" bundle="ACADEMIC_OFFICE_RESOURCES">
							<fr:slot name="nome" />
							<fr:slot name="shiftTypesPrettyPrint" />
							<fr:slot name="lessonsOrderedByWeekDayAndStartTime" layout="flowLayout" >
								<fr:property name="eachLayout" value="values" />
								<fr:property name="eachSchema" value="Lesson.view" />
							</fr:slot>
						</fr:schema>
						
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1" />
							<fr:property name="checkable" value="true" />
							<fr:property name="checkboxName" value="shiftsIdsToTransfer" />
							<fr:property name="checkboxValue" value="externalId" />
						</fr:layout>
					</fr:view>
	
				</logic:notEmpty>
				
				<logic:notEmpty name="bean" property="executionCourse">
					<html:submit><bean:message key="link.execution.course.management.transfer" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
				</logic:notEmpty>
			
			</fr:form>
		</div>
		
		<div class="right">
		
			<fr:form action="/executionCourseManagement.do">
				<fr:edit id="bean" name="bean" visible="false" />
				
				<fr:edit id="bean-select" name="bean">
					<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseManagementBean"
						bundle="ACADEMIC_OFFICE_RESOURCES">	
							<fr:slot name="degreeCurricularPlan" layout="menu-select-postback">
								<fr:property name="destination" value="chooseDegreeCurricularPlanPostback" />
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DegreeCurricularPlanProvider" />
								<fr:property name="format" value="${degreeType.localizedName} - ${presentationName}" />
							</fr:slot>
						
						<logic:notEmpty name="bean" property="degreeCurricularPlan">
							<fr:slot name="curricularYear" layout="menu-select-postback">
								<fr:property name="destination" value="chooseCurricularYearPostback" />
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CurricularYearsProvider" />
								<fr:property name="format" value="${year}" />
							</fr:slot>
						</logic:notEmpty>
						
						<logic:notEmpty name="bean" property="degreeCurricularPlan">
						<logic:notEmpty name="bean" property="curricularYear">
							<fr:slot name="executionCourse" layout="menu-select-postback">
								<fr:property name="destination" value="chooseExecutionCoursePostback" />
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCoursesProvider" />
								<fr:property name="format" value="${nome}" />
							</fr:slot>
						</logic:notEmpty>
						</logic:notEmpty>
						
					</fr:schema>
					
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1" />
						<fr:property name="columnClasses" value=",width200px,tdclear tderror1" />
					</fr:layout>
					
					<fr:destination name="chooseDegreeCurricularPlanPostback"
						path="<%= "/executionCourseManagement.do?method=chooseDegreeCurricularPlanPostbackForTransfer&executionCourseId=" + executionCourseId %>" />
						
					<fr:destination name="chooseCurricularYearPostback"
						path="<%= "/executionCourseManagement.do?method=chooseCurricularYearPostbackForTransfer&executionCourseId=" + executionCourseId %>" />
					
					<fr:destination name="chooseExecutionCoursePostback"
						path="<%= "/executionCourseManagement.do?method=chooseExecutionCoursePostbackForTransfer&executionCourseId=" + executionCourseId %>" />

				</fr:edit>
				
			</fr:form>
			
			<logic:empty name="bean" property="degreeCurricularPlan">
				<p><em><bean:message key="label.execution.course.management.choose.destiny.curricular.plan" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
			</logic:empty>
			
			<logic:notEmpty name="bean" property="degreeCurricularPlan">
				<logic:empty name="bean" property="curricularYear">
					<p><em><bean:message key="label.execution.course.management.choose.curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
				</logic:empty>
				
				<logic:notEmpty name="bean" property="curricularYear">
					<logic:empty name="bean" property="executionCourse">
						<p><em><bean:message key="label.execution.course.management.choose.destiny.execution.course" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
					</logic:empty>
				</logic:notEmpty>

			</logic:notEmpty>
			
			<fr:form action="<%= "/executionCourseManagement.do?method=backTransfer&executionCourseId=" + executionCourseId %>">
				<fr:edit id="bean" name="bean" visible="false" />
				
				<logic:notEmpty name="bean" property="executionCourse">
				
					<p><strong><bean:message key="label.execution.course.management.curricular.courses" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
					<logic:empty name="bean" property="executionCourse.associatedCurricularCourses">
						<p><em><bean:message key="message.execution.course.management.execution.course.not.associated.to.any.curricular" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
					</logic:empty>
				
					<logic:notEmpty name="bean" property="executionCourse.associatedCurricularCourses">
	
						<fr:view name="bean" property="executionCourse.associatedCurricularCourses">
							
							<fr:schema type="net.sourceforge.fenixedu.domain.CurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES">
								<fr:slot name="nameI18N" />
								<fr:slot name="degreeCurricularPlan.degree.sigla" />
							</fr:schema>
							
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle1" />
								<fr:property name="checkable" value="true" />
								<fr:property name="checkboxName" value="curricularCourseIdsToTransfer" />
								<fr:property name="checkboxValue" value="externalId" />								
							</fr:layout>
							
						</fr:view>
						
						<p><strong>Turnos</strong></p>
						<fr:view name="bean" property="executionCourse.associatedShifts">
							
							<fr:schema type="net.sourceforge.fenixedu.domain.Shift" bundle="ACADEMIC_OFFICE_RESOURCES">
								<fr:slot name="nome" />
								<fr:slot name="shiftTypesPrettyPrint" />
								<fr:slot name="lessonsOrderedByWeekDayAndStartTime" layout="flowLayout" >
									<fr:property name="eachLayout" value="values" />
									<fr:property name="eachSchema" value="Lesson.view" />
								</fr:slot>
							</fr:schema>
							
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle1" />
								<fr:property name="checkable" value="true" />
								<fr:property name="checkboxName" value="shiftsIdsToTransfer" />
								<fr:property name="checkboxValue" value="externalId" />								
							</fr:layout>
							
						</fr:view>
					
					</logic:notEmpty>
					
					
				</logic:notEmpty>

				<logic:notEmpty name="bean" property="executionCourse">
					<html:submit><bean:message key="link.execution.course.management.transfer" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
				</logic:notEmpty>
				
			</fr:form>
			
		</div>
		
		<div class="clear"></div>
		
	</div>
	
	<div id="tabs-5">
		<p><strong><bean:message key="label.execution.course.management.separate.curricular.courses.and.shifts" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
		<div>
			
			<logic:notEmpty name="newExecutionCourse">
				<bean:define id="newExecutionCourseId" name="newExecutionCourse" property="externalId" />

				<p>
					<bean:message key="message.execution.course.management.course.created.with.name" bundle="ACADEMIC_OFFICE_RESOURCES" />
					<html:link action="<%= "/executionCourseManagement.do?method=viewExecutionCourse&executionCourseId=" + newExecutionCourseId %>">
						<bean:write name="newExecutionCourse" property="nome" />
					</html:link>
				</p>
			</logic:notEmpty>
			
			<fr:form action="<%= "/executionCourseManagement.do?method=separate&executionCourseId=" + executionCourseId %>">
			
				<fr:edit id="bean" name="bean" visible="false" />
			
				<p><strong><bean:message key="label.execution.course.management.curricular.courses" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
				<logic:empty name="executionCourse" property="associatedCurricularCourses">
					<p><em><bean:message key="message.execution.course.management.execution.course.not.associated.to.any.curricular" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
				</logic:empty>
				
				<logic:notEmpty name="executionCourse" property="associatedCurricularCourses">
	
					<fr:view name="executionCourse" property="associatedCurricularCourses">
						<fr:schema type="net.sourceforge.fenixedu.domain.CurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES">
							<fr:slot name="nameI18N" />
							<fr:slot name="degreeCurricularPlan.degree.sigla" />
						</fr:schema>
						
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1" />
							<fr:property name="checkable" value="true" />
							<fr:property name="checkboxName" value="curricularCourseIdsToTransfer" />
							<fr:property name="checkboxValue" value="externalId" />
						</fr:layout>
					</fr:view>
	
				</logic:notEmpty>
				
				<p><strong><bean:message key="label.execution.course.management.shifts" bundle="ACADEMIC_OFFICE_RESOURCES" /> </strong></p>
				<logic:empty name="executionCourse" property="associatedShifts">
					<p><em><bean:message key="label.execution.course.management.associated.shifts.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
				</logic:empty>
				
				<logic:notEmpty name="executionCourse" property="associatedShifts">
	
					<fr:view name="executionCourse" property="associatedShifts">
						<fr:schema type="net.sourceforge.fenixedu.domain.Shift" bundle="ACADEMIC_OFFICE_RESOURCES">
							<fr:slot name="nome" />
							<fr:slot name="shiftTypesPrettyPrint" />
							<fr:slot name="lessonsOrderedByWeekDayAndStartTime" layout="flowLayout" >
								<fr:property name="eachLayout" value="values" />
								<fr:property name="eachSchema" value="Lesson.view" />
							</fr:slot>
						</fr:schema>
						
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1" />
							<fr:property name="checkable" value="true" />
							<fr:property name="checkboxName" value="shiftsIdsToTransfer" />
							<fr:property name="checkboxValue" value="externalId" />
						</fr:layout>
					</fr:view>
	
				</logic:notEmpty>
				
				<html:submit><bean:message key="link.execution.course.management.separate" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
			
			</fr:form>
		</div>
				
	</div>
	
	<div id="tabs-6">
		<p><strong><bean:message key="label.execution.course.management.summaries" bundle="ACADEMIC_OFFICE_RESOURCES" /> </strong</p>
		
		<logic:empty name="executionCourse" property="associatedSummaries">
			<p><em><bean:message key="message.execution.course.management.summaries.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
		</logic:empty>
		
		<logic:notEmpty name="executionCourse" property="associatedSummaries">
			<fr:view name="executionCourse" property="associatedSummaries">
				
				<fr:schema type="net.sourceforge.fenixedu.domain.Summary" bundle="ACADEMIC_OFFICE_RESOURCES">
					<fr:slot name="summaryDateYearMonthDay" />
					<fr:slot name="summaryHourHourMinuteSecond" />
					<fr:slot name="summaryType" />
					<fr:slot name="title" />
				</fr:schema>
				
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1" />
					
					<fr:link name="view" label="label.view,APPLICATION_RESOURCES"
						link="<%= "/executionCourseManagement.do?method=viewSummary&summaryId=${externalId}&executionCourseId=" + executionCourseId %>" />
						
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		
	</div>
	
</div>	


