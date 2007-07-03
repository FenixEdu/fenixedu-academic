<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

	<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
	<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="degreeCurricularPlan" property="equivalencePlan"/>

	<logic:present name="entries">
	<ul class="mtop05">
		<li>
			<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID="
					+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
					+ equivalencePlan.getIdInternal() %>">
				<bean:message key="link.equivalency.add" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<p class="mtop15 mbottom05">
		<bean:message key="message.degree.module.equivalencies" bundle="APPLICATION_RESOURCES"/>:
	</p>
	
	
	

		<logic:empty name="entries">
			<em><bean:message key="message.curricular.course.has.no.equivalencies" bundle="APPLICATION_RESOURCES"/></em>
		</logic:empty>

		<table class="tstyle4 mtop05">
		<logic:iterate id="entry" type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry" indexId="n" name="entries">
			<tr>
			<td>
			<logic:notEqual name="n" value="0">
			</logic:notEqual>
			
			<logic:iterate id="degreeModuleFromList1" indexId="i1" name="entry" property="oldDegreeModules">
				<logic:notEqual name="i1" value="0">
					<strong style="padding: 0 0.5em;">
						<bean:message name="entry" property="sourceDegreeModulesOperator.name" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
				<span style="border-bottom: 1px solid #ccc;">
					<bean:write name="degreeModuleFromList1" property="name"/>
				</span>
			</logic:iterate>
			</td>
	
			<td>
				<span>==></span>
			</td>
	
			<td>		
			<logic:iterate id="degreeModuleFromList2" indexId="i2" name="entry" property="newDegreeModules">
				<logic:notEqual name="i2" value="0">
					<strong style="padding: 0 0.5em;">
						<bean:message name="entry" property="newDegreeModulesOperator.name" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
				<span style="border-bottom: 1px solid #ccc;">
					<bean:write name="degreeModuleFromList2" property="name"/>
				</span>
			</logic:iterate>
			</td>
			
			<td class="acenter nowrap">
			<logic:notEmpty name="entry" property="ectsCredits">
				<bean:write name="entry" property="ectsCredits"/> <bean:message key="label.credits" bundle="APPLICATION_RESOURCES"/>
			</logic:notEmpty>
			<logic:empty name="entry" property="ectsCredits">
				-
			</logic:empty>
			</td>
			
			<td>
			<span>
				<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=deleteEquivalency&amp;degreeCurricularPlanID="
						+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
						+ equivalencePlan.getIdInternal() + "&amp;equivalencePlanEntryID="
						+ entry.getIdInternal() %>">
					<bean:message key="link.delete" bundle="APPLICATION_RESOURCES"/>
				</html:link>
			</span>
			</td>
		</tr>
		</logic:iterate>
	</table>
	</logic:present>
