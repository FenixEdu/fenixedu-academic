<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="student" type="net.sourceforge.fenixedu.domain.student.Student" name="student"/>
<bean:define id="equivalencyPlanEntryCurriculumModuleWrapper" name="equivalencyPlanEntryCurriculumModuleWrapper"/>
<bean:define id="curriculumModule" type="net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule" name="equivalencyPlanEntryCurriculumModuleWrapper" property="curriculumModule"/>
<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="studentCurricularPlanEquivalencePlan"/>
<bean:define id="indentLevel" type="java.lang.String" name="indentLevel"/>
<bean:define id="width" type="java.lang.String" name="width"/>

<logic:equal name="curriculumModule" property="leaf" value="true">
	<logic:equal name="curriculumModule" property="enrolment" value="true">
	
		<bean:define id="enrolment" type="net.sourceforge.fenixedu.domain.Enrolment" name="curriculumModule"/>
	
		<div class="indent<%= indentLevel %>">
			<table class="showinfo3 mvert0" style="width: <%= width %>em;">
				<tr>
					<td>
						<logic:equal name="enrolment" property="enrolment" value="true">
							<logic:notEmpty name="enrolment" property="code"><bean:write name="enrolment" property="code"/> - </logic:notEmpty>
						</logic:equal>
						<bean:write name="enrolment" property="name"/>
					</td>
					<td class="highlight2 smalltxt" align="center" style="width: 14em;">
						<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID="
								+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
								+ equivalencePlan.getIdInternal() + "&amp;curriculumModuleID="
								+ curriculumModule.getIdInternal() + "&amp;studentNumber="
									+ student.getNumber() %>">
							<bean:message key="link.equivalency.add" bundle="APPLICATION_RESOURCES"/>
						</html:link>
					</td>
					<td class="smalltxt" align="right" style="width: 22em;">
						<logic:empty name="equivalencyPlanEntryCurriculumModuleWrapper" property="equivalencePlanEntriesToApply">
							<span style="color: #888">
								<bean:message key="message.curricular.course.has.no.equivalencies" bundle="APPLICATION_RESOURCES"/>
							</span>
						</logic:empty>
						<logic:notEmpty name="equivalencyPlanEntryCurriculumModuleWrapper" property="equivalencePlanEntriesToApply">
							<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showTable&amp;degreeCurricularPlanID="
									+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
									+ equivalencePlan.getIdInternal() + "&amp;curriculumModuleID="
									+ curriculumModule.getIdInternal() + "&amp;studentNumber="
									+ student.getNumber() 
									%>">
								<bean:message key="link.equivalencies.for.curricular.course.view" bundle="APPLICATION_RESOURCES"/>
							</html:link>
						</logic:notEmpty>
					</td>
				</tr>
			</table>
		</div>
		
	</logic:equal>
</logic:equal>
<logic:notEqual name="curriculumModule" property="leaf" value="true">

	<bean:define id="curriculumGroup" type="net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup" name="curriculumModule"/>

	<div style="padding-left: <%= indentLevel %>em;">
		<table class="showinfo3 mvert0" style="width: <%= width %>em;">
			<tr class="bgcolor2">
				<th class="aleft">
					<bean:write name="curriculumGroup" property="name"/>
				</th>
				<th class="smalltxt" align="center" style="width: 14em;">
					<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID="
							+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
							+ equivalencePlan.getIdInternal() + "&amp;curriculumModuleID="
							+ curriculumModule.getIdInternal() + "&amp;studentNumber="
								+ student.getNumber() %>">
						<bean:message key="link.equivalency.add" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</th>
				<th class="smalltxt" align="right" style="width: 22em;">
					<logic:empty name="equivalencyPlanEntryCurriculumModuleWrapper" property="equivalencePlanEntriesToApply">
						<span style="color: #888">
							<bean:message key="message.curricular.course.has.no.equivalencies" bundle="APPLICATION_RESOURCES"/>
						</span>
					</logic:empty>
					<logic:notEmpty name="equivalencyPlanEntryCurriculumModuleWrapper" property="equivalencePlanEntriesToApply">
						<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showTable&amp;degreeCurricularPlanID="
								+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
								+ equivalencePlan.getIdInternal() + "&amp;curriculumModuleID="
								+ curriculumModule.getIdInternal() + "&amp;studentNumber="
								+ student.getNumber() 
								%>">
							<bean:message key="link.equivalencies.for.curricular.course.view" bundle="APPLICATION_RESOURCES"/>
						</html:link>
					</logic:notEmpty>
				</th>
			</tr>
		</table>
	</div>

	<logic:iterate id="child" name="equivalencyPlanEntryCurriculumModuleWrapper" property="children">
		<bean:define id="equivalencyPlanEntryCurriculumModuleWrapper" name="child" toScope="request"/>
		<% 
			Integer newIndentLevel = Integer.valueOf(Integer.parseInt(indentLevel) + 3);
			Integer newWidth = Integer.valueOf(Integer.parseInt(width) - 3);
		%>
		<bean:define id="indentLevel" type="java.lang.String" value="<%= newIndentLevel.toString() %>" toScope="request"/>
		<bean:define id="width" type="java.lang.String" value="<%= newWidth.toString() %>" toScope="request"/>
		<jsp:include page="showStudentEquivalencyPlanForCurriculumModule.jsp"/>
	</logic:iterate>
</logic:notEqual>
