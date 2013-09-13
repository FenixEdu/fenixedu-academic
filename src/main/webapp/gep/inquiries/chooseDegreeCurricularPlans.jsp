<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>

<p><em><bean:message key="title.inquiries.GEP" bundle="INQUIRIES_RESOURCES"/></em></p>
<h2><bean:message key="title.inquiries.course.evaluation" bundle="INQUIRIES_RESOURCES"/></h2>
<h3 class="mbottom1"><bean:message key="title.inquiries.student.inquiry" bundle="INQUIRIES_RESOURCES"/></h3>


<span class="error"><!-- Error messages go here --><html:errors /></span>


<logic:present name='<%= InquiriesUtil.DEGREE_CURRICULAR_PLANS_LIST %>'>
		<p class="mtop15 mbottom05">
			<bean:message key="title.inquiries.edit.reminder" bundle="INQUIRIES_RESOURCES"/>
		</p>
		
	<html:form action="/sendEmailReminder">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="sendEmails" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		 
<table class="tstyle5 thlight thleft mtop05">
	<tr>
		<th><bean:message key="title.inquiries.reminder.subject" bundle="INQUIRIES_RESOURCES"/></th>
		<td>
			<bean:define id="subject">
				<bean:message key="message.inquiries.email.reminder.subject" bundle="INQUIRIES_RESOURCES"/>
			</bean:define>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.bodyTextSubject" property="bodyTextSubject" size="50" value='<%= subject %>' />
		</td>
	</tr>
	<tr>
		<th><bean:message key="title.inquiries.reminder.body" bundle="INQUIRIES_RESOURCES"/></th>
		<td>
			<bean:define id="bodyIntro">
				<bean:message key="message.inquiries.email.reminder.body.intro" bundle="INQUIRIES_RESOURCES"/>
			</bean:define>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.bodyTextIntro" property="bodyTextIntro" styleClass="reminder" rows="12" cols="90" value='<%= bodyIntro %>' />
		</td>
	</tr>
	<tr>
		<th></th>
		<td><bean:message key="title.inquiries.reminder.unevaluated.courses" bundle="INQUIRIES_RESOURCES"/></td>
	</tr>
	<tr>
		<th></th>
		<td>
			<bean:define id="bodyEnd">
				<bean:message key="message.inquiries.email.reminder.body.end" bundle="INQUIRIES_RESOURCES"/>
			</bean:define>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.bodyTextEnd" property="bodyTextEnd" styleClass="reminder" rows="7" cols="90" value='<%= bodyEnd %>' />
		</td>
	</tr>
</table>

		
			



	<p class="mbottom05 mtop2">
		<bean:message key="title.inquiries.choose.curricular.plans" bundle="INQUIRIES_RESOURCES"/>
	</p>

		<table class="tstyle1 thleft mtop05">
			<tr>
				<th>
					Tipo de Curso
				</th>
				<th>
					<bean:message key="table.header.curricular.plan" bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th>
					<bean:message key="table.header.acronym" bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th>
				</th>
			</tr>
			
			<logic:iterate id="degreeCurricularPlan" name='<%= InquiriesUtil.DEGREE_CURRICULAR_PLANS_LIST %>' type="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan">
				<%--<bean:define id="degreeType">
					<bean:write name="degreeCurricularPlan" property="infoDegree.degreeType.name" />	
				</bean:define>--%>
					<tr>
						<td>
							<bean:message name="degreeCurricularPlan" property="infoDegree.degreeType.name" bundle="ENUMERATION_RESOURCES"/>
						</td>
						<td>
							<bean:write name="degreeCurricularPlan" property="infoDegree.nome" /> - <bean:write name="degreeCurricularPlan" property="name" />
						</td>
						<td>
							<bean:write name="degreeCurricularPlan" property="infoDegree.sigla" />
						</td>
						<td>
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.degreeCurricularPlanIds" property="degreeCurricularPlanIds">
								<bean:write name="degreeCurricularPlan" property="externalId" />
							</html:multibox>
						</td>
					</tr>
			</logic:iterate>
		</table>		



	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.inquiries.send.email.reminder" bundle="INQUIRIES_RESOURCES"/>
		</html:submit>
	</p>

	</html:form>

</logic:present>
