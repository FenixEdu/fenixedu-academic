<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
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
					+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
					+ equivalencePlan.getExternalId() %>">
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
						+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
						+ equivalencePlan.getExternalId() %>">
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
					<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlan" property="externalId"/>
					<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
						<bean:message key="link.equivalency.view.plan" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notPresent>
