<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.LEEC.enrollment"/></h2>
<span class="error"><html:errors/></span>
<html:form action="/curricularCoursesEnrollment">
	<html:hidden property="method" value="prepareEnrollmentChooseAreas" />
	<html:hidden property="page" value="2" />
	<html:hidden property="studentNumber" />
	<html:hidden property="studentName" value="<%=pageContext.findAttribute("studentName").toString()%>"/>
	<html:hidden property="executionPeriod" value="<%=pageContext.findAttribute("executionPeriod").toString()%>"/>
	<html:hidden property="executionYear" value="<%=pageContext.findAttribute("executionYear").toString()%>"/>
	<html:hidden property="studentCurricularPlanId" value="<%=pageContext.findAttribute("studentCurricularPlanId").toString()%>"/>
	<logic:present name="executionDegreeId">
		<html:hidden property="executionDegreeId" value="<%=pageContext.findAttribute("executionDegreeId").toString()%>"/>
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
				<br />
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
				<bean:message key="label.student.enrollment.specializationArea" />
			</td>
			<td>
				<br />
				<html:select property="specializationArea">
					<html:option value="" key="label.student.enrollment.no.area">
						<bean:message key="label.student.enrollment.no.area"/>
					</html:option>
					<html:options collection="infoBranches" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.secondaryArea" />
			</td>
			<td>
				<html:select property="secondaryArea">
					<html:option value="" key="label.student.enrollment.no.area">
						<bean:message key="label.student.enrollment.no.area"/>
					</html:option>
					<html:options collection="infoBranches" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br/>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.student.modify"/>
	</html:submit>
</html:form>