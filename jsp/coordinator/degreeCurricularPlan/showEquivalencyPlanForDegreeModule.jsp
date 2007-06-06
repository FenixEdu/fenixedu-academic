<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="degreeCurricularPlan" property="equivalencePlan"/>

<logic:equal name="degreeModule" property="leaf" value="true">

	<bean:define id="curricularCourse" type="net.sourceforge.fenixedu.domain.CurricularCourse" name="degreeModule"/>

	<bean:define id="indentLevel" type="java.lang.String" name="indentLevel"/>
	<bean:define id="width" type="java.lang.String" name="width"/>

	<div class="indent<%= indentLevel %>">
		<table class="showinfo3 mvert0" style="width: <%= width %>em;">
			<tr>
				<td>
					<bean:write name="degreeModule" property="name"/>
				</td>
				<td class="highlight2 smalltxt" align="center" style="width: 14em;">
					<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID="
							+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
							+ equivalencePlan.getIdInternal() + "&amp;curricularCourseID="
							+ curricularCourse.getIdInternal() %>">
						<bean:message key="link.equivalency.add"/>
					</html:link>
				</td>
				<td class="smalltxt" align="right" style="width: 22em;">
					<%
						java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry> curricularCourseEquivalencePlanEntries = curricularCourse.getNewCurricularCourseEquivalencePlanEntry(equivalencePlan);
						request.setAttribute("curricularCourseEquivalencePlanEntries", curricularCourseEquivalencePlanEntries);
					%>					
					<bean:size id="numElements" name="curricularCourseEquivalencePlanEntries"/>
					<logic:equal name="numElements" value="0">
						<span style="color: #888">
							<bean:message key="message.curricular.course.has.no.equivalencies"/>
						</span>
					</logic:equal>
					<logic:iterate id="curricularCourseEquivalencePlanEntry" type="net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry" name="curricularCourseEquivalencePlanEntries">
						(
						<logic:iterate id="oldCurricularCourse" indexId="i" type="net.sourceforge.fenixedu.domain.CurricularCourse" name="curricularCourseEquivalencePlanEntry" property="oldCurricularCoursesSortedByName">
							<logic:notEqual name="i" value="0">
								+
							</logic:notEqual>
							<bean:write name="oldCurricularCourse" property="name"/>
						</logic:iterate>
						)
						<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=deleteEquivalency&amp;degreeCurricularPlanID="
								+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
								+ equivalencePlan.getIdInternal() + "&amp;curricularCourseEquivalencePlanEntryID="
								+ curricularCourseEquivalencePlanEntry.getIdInternal() %>">
							<bean:message key="link.delete"/>
						</html:link>
						<br/>
					</logic:iterate>
				</td>
			</tr>
		</table>
	</div>
</logic:equal>
<logic:notEqual name="degreeModule" property="leaf" value="true">

	<bean:define id="indentLevel" type="java.lang.String" name="indentLevel"/>
	<bean:define id="width" type="java.lang.String" name="width"/>

	<div style="padding-left: <%= indentLevel %>em;">
		<table class="showinfo3 mvert0" style="width: <%= width %>em;">
			<tr class="bgcolor2">
				<th class="aleft" colspan="3">
					<bean:write name="degreeModule" property="name"/>
				</th>
			</tr>
		</table>
	</div>

	<logic:iterate id="context" name="degreeModule" property="childContextsSortedByDegreeModuleName">
		<bean:define id="degreeModule" name="context" property="childDegreeModule" toScope="request"/>
		<% 
			Integer newIndentLevel = Integer.valueOf(Integer.parseInt(indentLevel) + 3);
			Integer newWidth = Integer.valueOf(Integer.parseInt(width) - 3);
		%>
		<bean:define id="indentLevel" type="java.lang.String" value="<%= newIndentLevel.toString() %>" toScope="request"/>
		<bean:define id="width" type="java.lang.String" value="<%= newWidth.toString() %>" toScope="request"/>
		<jsp:include page="showEquivalencyPlanForDegreeModule.jsp"/>
	</logic:iterate>
</logic:notEqual>
