<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>


<logic:present name="equivalencePlanEntryWrappers">
	<div style="background: #fafafa; border: 2px solid #eee; padding: 1em; color: #555;">
		<logic:iterate id="entry" type="net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.EquivalencyPlanEntryWrapper" indexId="n" name="equivalencePlanEntryWrappers">
			<bean:define id="equivalencePlanEntry" name="entry" property="equivalencePlanEntry"/>
			<logic:notEqual name="n" value="0">
				<br/>
				<br/>
			</logic:notEqual>
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
		</logic:iterate>
	</div>
</logic:present>
