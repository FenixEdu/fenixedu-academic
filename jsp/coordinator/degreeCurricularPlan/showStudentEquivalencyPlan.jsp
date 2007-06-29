<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<br/>
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
<br/>
<br/>
<logic:present name="studentSearchBean" property="studentNumber">
	<logic:notPresent name="student">
		<bean:message key="message.student.does.not.exist" bundle="APPLICATION_RESOURCES"/>
	</logic:notPresent>
	<logic:present name="student">
		<logic:notPresent name="studentCurricularPlanEquivalencePlan">
			<bean:message key="message.student.does.not.have.equivalence.plan" bundle="APPLICATION_RESOURCES"/>
		</logic:notPresent>
		<logic:present name="studentCurricularPlanEquivalencePlan">
			<bean:define id="student" type="net.sourceforge.fenixedu.domain.student.Student" name="student"/>
			<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
			<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="studentCurricularPlanEquivalencePlan"/>
			<logic:present name="rootEquivalencyPlanEntryCurriculumModuleWrapper">
				<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showTable&amp;degreeCurricularPlanID="
						+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
						+ equivalencePlan.getIdInternal() + "&amp;studentNumber="
						+ student.getNumber() 
						%>">
					<bean:message key="link.equivalency.view.table" bundle="APPLICATION_RESOURCES"/>
				</html:link>
				<br/>
				<br/>
				<bean:define id="equivalencyPlanEntryCurriculumModuleWrapper" name="rootEquivalencyPlanEntryCurriculumModuleWrapper" toScope="request"/>
				<bean:define id="indentLevel" type="java.lang.String" value="0" toScope="request"/>
				<bean:define id="width" type="java.lang.String" value="70" toScope="request"/>
				<jsp:include page="showStudentEquivalencyPlanForCurriculumModule.jsp"/>
			</logic:present>
			<logic:present name="equivalencePlanEntryWrappers">
				<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID="
						+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
						+ equivalencePlan.getIdInternal() + "&amp;studentNumber="
						+ student.getNumber() 
						%>">
					<bean:message key="link.equivalency.view.plan" bundle="APPLICATION_RESOURCES"/>
				</html:link>
				<br/>
				<br/>
				<bean:define id="equivalencePlanEntryWrappers" name="equivalencePlanEntryWrappers" toScope="request"/>
				<bean:define id="indentLevel" type="java.lang.String" value="0" toScope="request"/>
				<bean:define id="width" type="java.lang.String" value="70" toScope="request"/>
				<jsp:include page="showStudentEquivalencyPlanTable.jsp"/>
			</logic:present>
		</logic:present>
	</logic:present>
</logic:present>