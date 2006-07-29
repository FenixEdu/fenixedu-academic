<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrollment" bundle="STUDENT_RESOURCES"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/curricularCoursesEnrollment">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEnrollmentChooseAreas" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentName" property="studentName" value="<%=pageContext.findAttribute("studentName").toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod" value="<%=pageContext.findAttribute("executionPeriod").toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear" value="<%=pageContext.findAttribute("executionYear").toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanId" property="studentCurricularPlanId" value="<%=pageContext.findAttribute("studentCurricularPlanId").toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />
	<logic:present name="executionDegreeId">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=pageContext.findAttribute("executionDegreeId").toString()%>"/>
	</logic:present>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<bean:define id="studentNumber"><%=pageContext.findAttribute("studentNumber")%></bean:define>
				<bean:define id="studentName"><%=pageContext.findAttribute("studentName")%></bean:define>
				<bean:define id="executionPeriod"><%=pageContext.findAttribute("executionPeriod")%></bean:define>
				<bean:define id="executionYear"><%=pageContext.findAttribute("executionYear")%></bean:define>
				<b><bean:message key="label.student.enrollment.number"/></b>
				<bean:write name="studentNumber" />&nbsp;-&nbsp;
				<bean:write name="studentName" />
				<br/>
				<b><bean:message key="label.student.enrollment.executionPeriod"/></b>
				<bean:write name="executionPeriod" />&nbsp;				
				<bean:write name="executionYear" />
			</td>
		</tr>
	</table>
	<br />
	<table>
		<tr>
			<td colspan='2' class="infoop">
				<bean:message key="message.student.enrollment.help" />
			</td>
		</tr>
		<tr>
			<td>
				<br />
				<bean:message key="label.student.enrollment.specializationArea" /> /<bean:message key="label.student.enrollment.branch"  bundle="STUDENT_RESOURCES"/>
			</td>
			<td>
				<br />
				<bean:define id="specializationAreas" name="branches" property="finalSpecializationAreas" />
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.specializationArea" property="specializationArea">
					<html:option value="" key="label.student.enrollment.no.area">
						<bean:message key="label.student.enrollment.no.area"/>
					</html:option>
					<html:options collection="specializationAreas" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.secondaryArea" />
			</td>
			<td>
				<bean:define id="secundaryAreas" name="branches" property="finalSecundaryAreas" />
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.secondaryArea" property="secondaryArea">
					<html:option value="" key="label.student.enrollment.no.area">
						<bean:message key="label.student.enrollment.no.area"/>
					</html:option>
					<html:options collection="secundaryAreas" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br/>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.student.modify"/>
	</html:submit>
</html:form>