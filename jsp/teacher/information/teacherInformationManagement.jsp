<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.teacherInformation"/></h2>

<logic:present name="infoSiteTeacherInformation"> 
	<html:form action="/teacherInformation">
		<html:hidden property="teacherId"/>
		<html:hidden property="weeklyOcupationId"/>
		<html:hidden property="serviceProviderRegimeId"/>
		<html:hidden property="method" value="edit"/>
		<table>
			<tr>
				<td><bean:message key="message.teacherInformation.name" />
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nome" /> </td> 
				<td><bean:message key="message.teacherInformation.birthDate" />
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nascimento" /> </td>	
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.category" />
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.name" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.department" /></td>	
			</tr> 
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">1</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.qualifications" /></td>
			</tr>
		</table>
		<table>
		<tr>
			<td><bean:message key="message.teacherInformation.year" /></td>
			<td><bean:message key="message.teacherInformation.school" /></td>
			<td><bean:message key="message.teacherInformation.qualificationsDegree" /></td>
			<td><bean:message key="message.teacherInformation.qualificationsCourse" /></td>
		</tr>
		<logic:iterate id="infoQualification" name="infoSiteTeacherInformation" property="infoQualifications">
		<tr>
			<td><bean:write name="infoQualification" property="year" /></td>
			<td><bean:write name="infoQualification" property="school" /></td>
			<td><bean:write name="infoQualification" property="title" /></td>
			<td><bean:write name="infoQualification" property="mark" /></td>
		</tr>
		</logic:iterate>
		<tr>
			<td>
				<div class="gen-button">
					<html:link page="<%= "/readQualifications.do"%>">
						<bean:message key="label.teacherInformation.manage" />
					</html:link>
				</div>
			</td>
		</tr>
		</table>
		<br />
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">2</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.teachingCareer" /></td>
			</tr>
		</table>
		<table>
			<tr>
				<td><bean:message key="message.teacherInformation.years" /></td>
				<td><bean:message key="message.teacherInformation.careerCategory" /></td>
				<td><bean:message key="message.teacherInformation.careerPositions" /></td>
			</tr>
			<logic:iterate id="infoTeachingCareer" name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<tr>
				<td><bean:write name="infoProfessionalCareer" property="beginDate"/>
					-<bean:write name="infoProfessionalCareer" property="endDate"/>
				</td>
				<td><bean:write name="infoTeachingCareer" property="infoCategory.name" /></td>
				<td><bean:write name="infoTeachingCareer" property="courseOrPosition" /></td>
			</tr>
			</logic:iterate>
		</table>
		<div class="gen-button">
			<html:link page="<%= "/readCareers.do?careerType=Teaching&amp;page=0" %>">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<br />
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">3</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.professionalCareer" /></td>
			</tr>
		</table>
		<table>
			<tr>
				<td><bean:message key="message.teacherInformation.years" /></td>
				<td><bean:message key="message.teacherInformation.entity" /></td>
				<td><bean:message key="message.teacherInformation.functions" /></td>
			</tr>
			<logic:iterate id="infoProfessionalCareer" 
						   name="infoSiteTeacherInformation" 
						   property="infoProfessionalCareers">
				<tr>
					<td>
						<bean:write name="infoProfessionalCareer" property="beginDate"/>
						-<bean:write name="infoProfessionalCareer" property="endDate"/>
					</td>
					<td><bean:write name="infoProfessionalCareer" property="entity" /></td>
					<td><bean:write name="infoProfessionalCareer" property="function" /></td>
				</tr>
			</logic:iterate>
		</table>
		<div class="gen-button">
			<html:link page="<%= "/readCareers.do?careerType=Professional&amp;page=0" %>">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
	</html:form>
</logic:present>
