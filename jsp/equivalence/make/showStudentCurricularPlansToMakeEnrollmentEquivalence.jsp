<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="studentNumber" name="studentNumber" scope="request"/>
<bean:define id="degreeType" name="degreeType" scope="request"/>
<bean:define id="backLink" name="backLink" scope="request"/>
<bean:define id="infoStudentCurricularPlans" name="infoStudentCurricularPlans" scope="request"/>
<bean:size id="sizeInfoStudentCurricularPlans" name="infoStudentCurricularPlans"/>

<logic:present name="infoEquivalenceContext" scope="request">
	<bean:define id="infoEquivalenceContext" name="infoEquivalenceContext" scope="request"/>
	<bean:define id="infoStudentCurricularPlan" name="infoEquivalenceContext" property="infoStudentCurricularPlan"/>
</logic:present>

<h2><bean:message key="tilte.enrollment.equivalence"/> - <bean:message key="tilte.enrollment.equivalence.make.enrollment.equivalence"/></h2>

<span class="error"><html:errors/></span>

<logic:notEqual name="sizeInfoStudentCurricularPlans" value="0">

	<logic:present name="infoStudentCurricularPlan">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td bgcolor="#FFFFFF" class="infoselected">
					<center><b><bean:message key="message.enrollment.equivalence.info.about.chosen.student"/></b></center><br/>
					<b><bean:message key="message.enrollment.equivalence.student.number"/></b>&nbsp;<bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/><br/>
					<b><bean:message key="message.enrollment.equivalence.student.name"/></b>&nbsp;<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/><br/>
					<b><bean:message key="message.enrollment.equivalence.info.about.current.student.plan"/></b>&nbsp;
					(<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/>)&nbsp;
					<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>&nbsp;-&nbsp;
					<bean:write name="infoStudentCurricularPlan" property="startDate"/><br/>
				</td>
			</tr>
		</table>

		<br/>
	</logic:present>

	<table>
		<tr>
			<td><img src="<%= request.getContextPath() %>/images/dotist_info.gif" alt=""/></td>
			<td class="infoop">
				<strong><bean:message key="message.enrollment.equivalence.choose.student.plans.make"/></strong>
			</td>
		</tr>
	</table>

	<br/>

	<html:form action="/selectStudentCurricularPlansToMakeEnrollmentEquivalence.do">
	
		<html:hidden property="method" value="chooseStudentCurricularPlans"/>
		<html:hidden property="page" value="1"/>
		<html:hidden property="studentNumber" value="<%= pageContext.findAttribute("studentNumber").toString() %>"/>
		<html:hidden property="degreeType" value="<%= pageContext.findAttribute("degreeType").toString() %>"/>
		<html:hidden property="backLink" value="<%= pageContext.findAttribute("backLink").toString() %>"/>

		<table border="0">
			<th>&nbsp;</th>
			<th align="center"><bean:message key="message.enrollment.equivalence.from.student.plan"/></th>
			<th>&nbsp;</th>
			<th align="center"><bean:message key="message.enrollment.equivalence.to.student.plan"/></th>

			<logic:iterate id="infoStudentCurricularPlan" name="infoStudentCurricularPlans">
				<tr>
					<td align="left">
						<bean:define id="fromSCPID" name="infoStudentCurricularPlan" property="idInternal"/>
						<html:radio property="fromStudentCurricularPlanID" value="<%= pageContext.findAttribute("fromSCPID").toString() %>"/>
					</td>
					<td>
						<b><bean:message key="message.enrollment.equivalence.student.plan.degree"/>&nbsp;</b>
						(<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/>)&nbsp;
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
						<logic:present name="infoStudentCurricularPlan" property="specialization" >
							&nbsp;-&nbsp;<bean:write name="infoStudentCurricularPlan" property="specialization" />
						</logic:present>
						<br><b><bean:message key="message.enrollment.equivalence.student.plan.start.date"/>&nbsp;</b><bean:write name="infoStudentCurricularPlan" property="startDate"/>
					</td>
					<td align="left">
						<bean:define id="toSCPID" name="infoStudentCurricularPlan" property="idInternal"/>
						<html:radio property="toStudentCurricularPlanID" value="<%= pageContext.findAttribute("toSCPID").toString() %>"/>
					</td>
					<td>
						<b><bean:message key="message.enrollment.equivalence.student.plan.degree"/>&nbsp;</b>
						(<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/>)&nbsp;
						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
						<logic:present name="infoStudentCurricularPlan" property="specialization" >
							&nbsp;-&nbsp;<bean:write name="infoStudentCurricularPlan" property="specialization" />
						</logic:present>
						<br><b><bean:message key="message.enrollment.equivalence.student.plan.start.date"/>&nbsp;</b><bean:write name="infoStudentCurricularPlan" property="startDate"/>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br>
		<br>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.enrollment.equivalence.continue"/>
		</html:submit>
	
	</html:form>

</logic:notEqual>
