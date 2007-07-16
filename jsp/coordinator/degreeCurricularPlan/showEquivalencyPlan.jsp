<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="title.equivalency.plan" bundle="APPLICATION_RESOURCES"/></h2>


<logic:present name="degreeCurricularPlan">
<logic:notPresent name="degreeCurricularPlan" property="equivalencePlan">

		<p class="mtop2">
			<em><bean:message key="message.no.equivalency.table.exists" bundle="APPLICATION_RESOURCES"/></em>
		</p>
		
		<p class="mbottom05"><strong><bean:message key="label.create.equivalency.table.for.degree.curricular.plan" bundle="APPLICATION_RESOURCES"/></strong></p>
		<fr:edit name="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan"
				schema="degreeCurricularPlan.createEquivalencyPlan">
		    <fr:layout>
	    	    <fr:property name="classes" value="tstyle5 thlight mtop05 thmiddle"/>
	        	<fr:property name="columnClasses" value="tdclear,,"/>
		    </fr:layout>
		</fr:edit>

</logic:notPresent>
<logic:present name="degreeCurricularPlan" property="equivalencePlan">
	<p class="mvert15">
		<bean:message key="message.equivalency.table.from.degree.curricular.plan" bundle="APPLICATION_RESOURCES"/>
		<strong class="highlight1">
			<bean:write name="degreeCurricularPlan" property="equivalencePlan.sourceDegreeCurricularPlan.presentationName"/>
		</strong>
	</p>
</logic:present>

<logic:present name="degreeCurricularPlan" property="equivalencePlan">
	<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
	<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="degreeCurricularPlan" property="equivalencePlan"/>

	<logic:notPresent name="viewTable">
		<p>
			<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showTable&amp;degreeCurricularPlanID="
					+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
					+ equivalencePlan.getIdInternal() %>">
				<bean:message key="link.equivalency.view.table" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</p>

		<logic:present name="degreeCurricularPlan" property="root">
			<bean:define id="degreeModule" name="degreeCurricularPlan" property="root" toScope="request"/>
			<bean:define id="indentLevel" type="java.lang.String" value="0" toScope="request"/>
			<bean:define id="width" type="java.lang.String" value="70" toScope="request"/>
			<jsp:include page="showEquivalencyPlanForDegreeModule.jsp"/>
		</logic:present>
	</logic:notPresent>

	<logic:present name="viewTable">
		<ul class="mbottom05">
			<li>
				<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID="
						+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
						+ equivalencePlan.getIdInternal() %>">
					<bean:message key="link.equivalency.view.plan" bundle="APPLICATION_RESOURCES"/>
				</html:link>
			</li>
		</ul>
		<logic:present name="equivalencePlanEntries">
			<bean:define id="entries" name="equivalencePlanEntries" toScope="request"/>
			<jsp:include page="showEquivalencyPlanTable.jsp"/>
		</logic:present>
		<logic:notPresent name="equivalencePlanEntries">
			<logic:notPresent name="courseGroupEquivalencePlanEntries">
				<bean:define id="entries" name="degreeCurricularPlan" property="equivalencePlan.orderedEntries" toScope="request"/>
				<jsp:include page="showEquivalencyPlanTable.jsp"/>
			</logic:notPresent>
		</logic:notPresent>
	</logic:present>
</logic:present>
</logic:present>

<logic:notPresent name="degreeCurricularPlan">
	<p class="mbottom05"><bean:message key="label.equivalency.plan.choose.degree" bundle="APPLICATION_RESOURCES"/>:</p>
	<table class="tstyle4 mtop05">
		<logic:iterate id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlans">
			<tr>
				<td>
					<bean:write name="degreeCurricularPlan" property="name"/>
				</td>
				<td>
					<bean:write name="degreeCurricularPlan" property="degree.degreeType.localizedName"/>
				</td>
				<td>
					<bean:write name="degreeCurricularPlan" property="degree.name"/>
				</td>
				<td>
					<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlan" property="idInternal"/>
					<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
						<bean:message key="link.equivalency.view.plan" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notPresent>
