<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

	<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
	<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="degreeCurricularPlan" property="equivalencePlan"/>

	<logic:present name="entries">
		<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID="
				+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
				+ equivalencePlan.getIdInternal() %>">
			<bean:message key="link.equivalency.add"/>
		</html:link>
		<br/>
		<br/>
	<bean:message key="message.degree.module.equivalencies"/>:
	<div style="background: #fafafa; border: 2px solid #eee; padding: 1em; color: #555;">
		<logic:empty name="entries">
			<bean:message key="message.curricular.course.has.no.equivalencies"/>
		</logic:empty>
		<logic:iterate id="entry" type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry" indexId="n" name="entries">
			<logic:notEqual name="n" value="0">
				<br/>
				<br/>
			</logic:notEqual>
			<logic:iterate id="degreeModuleFromList1" indexId="i1" name="entry" property="oldDegreeModules">
				<span style="border-bottom: 1px dotted #aaa;">
					<logic:notEqual name="i1" value="0">
						<strong>
							<bean:message name="entry" property="sourceDegreeModulesOperator.name" bundle="ENUMERATION_RESOURCES"/>
						</strong>
					</logic:notEqual>
					<bean:write name="degreeModuleFromList1" property="name"/>
				</span>
			</logic:iterate>
			==>
			<logic:iterate id="degreeModuleFromList2" indexId="i2" name="entry" property="newDegreeModules">
				<span style="border-bottom: 1px dotted #aaa;">
					<logic:notEqual name="i2" value="0">
						<strong>
							<bean:message name="entry" property="newDegreeModulesOperator.name" bundle="ENUMERATION_RESOURCES"/>
						</strong>
					</logic:notEqual>
					<bean:write name="degreeModuleFromList2" property="name"/>
				</span>
			</logic:iterate>
			<logic:notEmpty name="entry" property="ectsCredits">
				(<bean:write name="entry" property="ectsCredits"/> <bean:message key="label.credits"/>)
			</logic:notEmpty>
			<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=deleteEquivalency&amp;degreeCurricularPlanID="
					+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
					+ equivalencePlan.getIdInternal() + "&amp;equivalencePlanEntryID="
					+ entry.getIdInternal() %>">
				<bean:message key="link.delete"/>
			</html:link>
		</logic:iterate>
	</div>
	</logic:present>
