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
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.longName" /></td>
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
					<html:link page="/readQualifications.do">
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
			<html:link page="/readCareers.do?careerType=Teaching&amp;page=0">
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
			<html:link page="/readCareers.do?careerType=Professional&amp;page=0">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<br />
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">4</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.serviceRegime" />
					<%--<bean:write name="" property="" />--%>
					<!-- VER ##################### -->
					<bean:message key="label.doublePoint" />
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td>
					<logic:iterate id="providerRegimeType" name="providerRegimeTypeList" scope="request">
						<bean:message name="providerRegimeType" property="name" bundle="ENUMERATION_RESOURCES"/>
						<html:radio property="serviceProviderRegimeTypeName"
								idName="providerRegimeType"
								value="name"/><br />
					</logic:iterate>
				</td>
			</tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">5</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.externalActivities" />
					<%--<bean:write name="" property="" />--%>
					<bean:message key="label.doublePoint" />
				</td>
			</tr>
		</table>
		<table>
			<logic:iterate id="infoExternalActivity" name="infoSiteTeacherInformation" property="infoExternalActivities">
				<tr>
					<td><bean:write name="infoExternalActivity" property="activity" /></td>
				</tr>
			</logic:iterate>
		</table>
		<div class="gen-button">
			<html:link page="/readExternalActivities.do">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<br />
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">6</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.numberOfPublications" /></td>
			</tr>
		</table>
		<table>
			<tr>
				<td><bean:message key="message.teacherInformation.number" /></td>
				<td><bean:message key="message.teacherInformation.national" /></td>
				<td><bean:message key="message.teacherInformation.international" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.comunicationsPublications" /></td>
				<!-- VER ################# -->
				<%--<td><bean:write name="" property="" /></td>
				<td><bean:write name="" property="" /></td>--%>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.articlesPublications" /></td>
				<!-- VER ################# -->
				<%--<td><bean:write name="" property="" /></td>
				<td><bean:write name="" property="" /></td>--%>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.bookAuthorPublications" /></td>
				<!-- VER ################# -->
				<%--<td><bean:write name="" property="" /></td>
				<td><bean:write name="" property="" /></td>--%>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.bookEditorPublications" /></td>
				<%--<td><bean:write name="" property="" /></td>
				<td><bean:write name="" property="" /></td>--%>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.articlesAndChaptersPublications" /></td>
				<%--<td><bean:write name="" property="" /></td>
				<td><bean:write name="" property="" /></td>--%>
			</tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">7</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.ownPublications" /></td>
			</tr>
		</table>
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr> <!-- VER ------------- -->
				<%--<td><html:textarea name="" property=""/></td> --%>
			</tr>
		</table>
		<%--<table> <!-- VER: Passar a usar a tabela de publicações -->
			<logic:iterate id="" name="" property="">
				<tr>
					<td><bean:write name="" property="" /></td>
				</tr>
			</logic:iterate>
		</table>--%>
	<%--<div class="gen-button"> <!-- VER ESTE LINK -->
			<html:link page="<%= "/summariesManager.do?method=prepareInsertSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>--%>
		<br />
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">8</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.cientificPublications" /></td>
			</tr>
		</table>
	<%--<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td><html:textarea name="" property=""/></td> 
			</tr>
		</table>--%>
		<!-- VER ACTUALIZAR AS PUBLICACOES -->
	<%--<table>
			<logic:iterate id="" name="" property="">
			<tr>
				<td><bean:write name="" property="" /></td>
			</tr>
			</logic:iterate>
		</table>--%>
		<%--<div class="gen-button">
				<html:link page="<%= "/summariesManager.do?method=prepareInsertSummary&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
					<bean:message key="label.teacherInformation.manage" />
				</html:link>
			</div>--%>
		<br />
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">9</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.lectureCourses" />
					<%--<bean:write name="" property="" />--%>
					<bean:message key="label.doublePoint" />
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td><bean:message key="message.teacherInformation.semester" /></td>
				<td><bean:message key="message.teacherInformation.lectureCourse" /></td>
				<td><bean:message key="message.teacherInformation.qualificationsCourse" /></td>
				<td><bean:message key="message.teacherInformation.typeOfClass" /></td>
				<td><bean:message key="message.teacherInformation.numberOfClass" /></td>
				<td><bean:message key="message.teacherInformation.numberOfWeeklyHours" /></td>
			</tr>
			<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoExecutionCourses">
				<tr>
					<%--<td><bean:write name="infoExecutionCourse" property="" /></td>--%>
						<td><bean:write name="infoExecutionCourse" property="nome" /></td>
				<%-- VER ##############		<td><bean:write name="infoExecutionCourse" property="" /></td>
						<td><bean:write name="infoExecutionCourse" property="" /></td>
						<td><bean:write name="infoExecutionCourse" property="" /></td>
						<td><bean:write name="infoExecutionCourse" property="" /></td>--%>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">10</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.orientations" />
					<%--<bean:write name="" property="" />--%>
					<bean:message key="label.doublePoint" />
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td></td>
				<td><bean:message key="message.teacherInformation.numberOfStudents" /></td>
				<td><bean:message key="message.teacherInformation.description" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.tfc" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.masterThesis" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.phdThesis" /></td>
				<td></td>
				<td></td>
			</tr>
		</table>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">11</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.weeklySpendTime" />
					<%--<bean:write name="" property="" />--%>
					<bean:message key="label.doublePoint" />
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td></td>
				<td><bean:message key="message.teacherInformation.teachers" /></td>
				<td><bean:message key="message.teacherInformation.supportLessons" /></td>
				<td><bean:message key="message.teacherInformation.investigation" /></td>
				<td><bean:message key="message.teacherInformation.managementWorks" /></td>
				<td><bean:message key="message.teacherInformation.others" /></td>
			</tr>
		</table>
		<tr>
			<td><bean:message key="message.teacherInformation.numberOfHours" /></td>
		<%-- 	VER CREDITOS	
			<td><bean:write name="" property="" /></td>
			<td><bean:write name="" property="" /></td> --%>
			<td><bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.research" /></td>
			<td><bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.management" /></td>
			<td><bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.other" /></td>
		</tr>
		<br />
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="50px"><span class="emphasis-box">12</span></td>
				<td class="infoop"><bean:message key="message.teacherInformation.managerPosition" />
					<%--bean:write name="" property="" />--%>
					<bean:message key="label.doublePoint" />
				</td>
			</tr>		
		</table>
		<table>
			<tr>
				<%--	<td><bean:write name="" property=""/></td>--%>
			</tr>
		</table>
		<br />
		<table>
			<tr>
				<td><bean:message key="message.teacherInformation.actualizationDate" /> &nbsp;
					<%--<bean:write name="" property="" />--%>
				</td>
			</tr>
		</table>
		<h3>
			<table>
				<tr align="center">	
					<td>
						<html:submit styleClass="inputbutton" property="confirm">
							<bean:message key="button.save"/>
						</html:submit>
					</td>
					<td>
						<html:reset styleClass="inputbutton">
							<bean:message key="label.clear"/>
						</html:reset>
					</td>
				</tr>
			</table>
		</h3>
	</html:form>
</logic:present>
