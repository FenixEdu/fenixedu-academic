<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>

<link href="<%= request.getContextPath() %>/CSS/inquiries_style.css" rel="stylesheet" type="text/css" />

<p class="center">
	<bean:message key="title.inquiries.GEP" bundle="INQUIRIES_RESOURCES"/>
</p>
<h2 class="center caps">
	<bean:message key="title.inquiries.course.evaluation" bundle="INQUIRIES_RESOURCES"/>
</h2>
<h3 class="center caps">
	<bean:message key="title.inquiries.student.inquiry" bundle="INQUIRIES_RESOURCES"/>
</h3>

<br/>
<p><strong>Escolha um ou mais planos curriculares:</strong></p>

<logic:present name='<%= InquiriesUtil.DEGREE_CURRICULAR_PLANS_LIST %>'>
	<html:form action="/sendEmailReminder">
		<html:hidden property="method" value="sendEmails" />
		<%--ul style="list-style-type: none;"--%>
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="table.header.curricular.plan" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="table.header.acronym" bundle="INQUIRIES_RESOURCES"/>
				</td>
				<td class="listClasses-header" />
			</tr>
			

			<logic:iterate id="degreeCurricularPlan" name='<%= InquiriesUtil.DEGREE_CURRICULAR_PLANS_LIST %>' type="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan">
				<bean:define id="degreeType">
					<bean:write name="degreeCurricularPlan" property="infoDegree.tipoCurso.name" />	
				</bean:define>
				<c:if test="${degreeType == 'DEGREE'}">
					<tr>
						<td class="listClasses">
							<bean:write name="degreeCurricularPlan" property="infoDegree.nome" />
						</td>
						<td class="listClasses">
							<bean:write name="degreeCurricularPlan" property="infoDegree.sigla" />
						</td>
						<td class="listClasses">
							<html:multibox property="degreeCurricularPlanIds">
								<bean:write name="degreeCurricularPlan" property="idInternal" />
							</html:multibox>
						</td>
					</tr>

				</c:if>
			</logic:iterate>
			
			<tr>
				<td class="invisible" align="right" colspan="3">
					&nbsp;
				</td>
			<tr>
				<td class="invisible" align="right" colspan="3">
					<html:submit styleClass="inputbuttonSmall">
						<bean:message key="button.inquiries.send.email.reminder" bundle="INQUIRIES_RESOURCES"/>
					</html:submit>
				</td>
			</tr>
		</table>


	</html:form>

</logic:present>
