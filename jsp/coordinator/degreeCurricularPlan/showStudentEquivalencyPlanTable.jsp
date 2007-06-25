<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="student" type="net.sourceforge.fenixedu.domain.student.Student" name="student"/>
<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>


<logic:present name="equivalencePlanEntryWrappers">
	<div style="background: #fafafa; border: 2px solid #eee; padding: 1em; color: #555;">
		<logic:iterate id="entry" type="net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.EquivalencyPlanEntryWrapper" indexId="n" name="equivalencePlanEntryWrappers">
			<bean:define id="equivalencePlanEntry" name="entry" property="equivalencePlanEntry"/>

			<% boolean strikeText = false; %>

			<logic:equal name="entry" property="removalEntry" value="true">
				<% strikeText = true; %>
			</logic:equal>

			<logic:notEqual name="n" value="0">
				<br/>
				<br/>
			</logic:notEqual>
			<% if (strikeText) { %>
				<strike>
			<% } %>
			<logic:equal name="equivalencePlanEntry" property="courseGroupEntry" value="true">
				<span style="border-bottom: 1px dotted #aaa;">
					<bean:write name="equivalencePlanEntry" property="oldCourseGroup.name"/>
				</span>
				==>
				<span style="border-bottom: 1px dotted #aaa;">
					<bean:write name="equivalencePlanEntry" property="newCourseGroup.name"/>
				</span>
			</logic:equal>
			<logic:equal name="equivalencePlanEntry" property="curricularCourseEntry" value="true">
				<logic:iterate id="curricularCourseFromList1" indexId="i1" name="equivalencePlanEntry" property="oldCurricularCourses">
					<span style="border-bottom: 1px dotted #aaa;">
						<logic:notEqual name="i1" value="0">
							<strong>
								<bean:message key="AND" bundle="ENUMERATION_RESOURCES"/>
							</strong>
						</logic:notEqual>
						<bean:write name="curricularCourseFromList1" property="name"/>
					</span>
				</logic:iterate>
				==>
				<logic:iterate id="curricularCourseFromList2" indexId="i2" name="equivalencePlanEntry" property="newDegreeModules">
					<span style="border-bottom: 1px dotted #aaa;">
						<logic:notEqual name="i2" value="0">
							<strong>
								<bean:message name="equivalencePlanEntry" property="newDegreeModulesOperator.name" bundle="ENUMERATION_RESOURCES"/>
							</strong>
						</logic:notEqual>
						<bean:write name="curricularCourseFromList2" property="name"/>
					</span>
				</logic:iterate>
			</logic:equal>
			<% if (strikeText) { %>
				</strike>
			<% } %>
			<logic:equal name="entry" property="equivalencePlanEntry.equivalencePlan.class.name" value="net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan">
				<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=deleteEquivalency&amp;degreeCurricularPlanID="
						+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanEntryID="
						+ entry.getEquivalencePlanEntry().getIdInternal() + "&amp;studentNumber="
						+ student.getNumber() %>">
					<bean:message key="link.delete"/>
				</html:link>
			</logic:equal>
			<logic:notEqual name="entry" property="equivalencePlanEntry.equivalencePlan.class.name" value="net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan">
				<logic:equal name="entry" property="removalEntry" value="true">
					<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=activate&amp;degreeCurricularPlanID="
							+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanEntryID="
							+ entry.getEquivalencePlanEntry().getIdInternal() + "&amp;studentNumber="
							+ student.getNumber() %>">
						<bean:message key="link.activate"/>
					</html:link>
				</logic:equal>
				<logic:notEqual name="entry" property="removalEntry" value="true">
					<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=deactivate&amp;degreeCurricularPlanID="
							+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanEntryID="
							+ entry.getEquivalencePlanEntry().getIdInternal() + "&amp;studentNumber="
							+ student.getNumber() %>">
						<bean:message key="link.deactivate"/>
					</html:link>
				</logic:notEqual>
			</logic:notEqual>
		</logic:iterate>
	</div>
</logic:present>
