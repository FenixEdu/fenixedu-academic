<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="title.student.equivalency.plan"/></h2>

<logic:present name="degreeCurricularPlan">
	<fr:edit id="net.sourceforge.fenixedu.domain.util.search.StudentSearchBean"
			name="studentSearchBean"
			type="net.sourceforge.fenixedu.domain.util.search.StudentSearchBean"
			schema="net.sourceforge.fenixedu.domain.util.search.StudentSearchBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
    	</fr:layout>
	</fr:edit>
</logic:present>

<logic:notPresent name="degreeCurricularPlan">
	<fr:edit id="net.sourceforge.fenixedu.domain.util.search.StudentSearchBeanWithDegreeCurricularPlan"
			name="studentSearchBean"
			type="net.sourceforge.fenixedu.domain.util.search.StudentSearchBean"
			schema="net.sourceforge.fenixedu.domain.util.search.StudentSearchBeanWithDegreeCurricularPlan">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
    	</fr:layout>
	</fr:edit>	
</logic:notPresent>


<logic:present name="studentSearchBean" property="studentNumber">
	<logic:notPresent name="student">
		<p>
			<em><bean:message key="message.student.does.not.exist" bundle="APPLICATION_RESOURCES"/></em>
		</p>
	</logic:notPresent>
	<logic:present name="student">
		<logic:notPresent name="studentCurricularPlanEquivalencePlan">
			<p class="mtop2">
				<em><bean:message key="message.student.does.not.have.equivalence.plan" bundle="APPLICATION_RESOURCES"/></em>
			</p>
		</logic:notPresent>
		<logic:present name="studentCurricularPlanEquivalencePlan">
			<bean:define id="student" type="net.sourceforge.fenixedu.domain.student.Student" name="student"/>
			<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
			<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="studentCurricularPlanEquivalencePlan"/>
			<logic:present name="rootEquivalencyPlanEntryCurriculumModuleWrapper">

				<p class="mvert15">
					<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showTable&amp;degreeCurricularPlanID="
							+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
							+ equivalencePlan.getExternalId() + "&amp;studentNumber="
							+ student.getNumber() 
							%>">
						<bean:message key="link.equivalency.view.table" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</p>

				<bean:define id="equivalencyPlanEntryCurriculumModuleWrapper" name="rootEquivalencyPlanEntryCurriculumModuleWrapper" toScope="request"/>
				<bean:define id="indentLevel" type="java.lang.String" value="0" toScope="request"/>
				<bean:define id="width" type="java.lang.String" value="70" toScope="request"/>
				<jsp:include page="showStudentEquivalencyPlanForCurriculumModule.jsp"/>
			</logic:present>
			<logic:present name="equivalencePlanEntryWrappers">

				<p class="mvert15">
					<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID="
							+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
							+ equivalencePlan.getExternalId() + "&amp;studentNumber="
							+ student.getNumber() 
							%>">
						<bean:message key="link.equivalency.view.plan" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</p>

				<bean:define id="equivalencePlanEntryWrappers" name="equivalencePlanEntryWrappers" toScope="request"/>
				<bean:define id="indentLevel" type="java.lang.String" value="0" toScope="request"/>
				<bean:define id="width" type="java.lang.String" value="70" toScope="request"/>

				<jsp:include page="showStudentEquivalencyPlanTable.jsp"/>
			</logic:present>
		</logic:present>
	</logic:present>
</logic:present>