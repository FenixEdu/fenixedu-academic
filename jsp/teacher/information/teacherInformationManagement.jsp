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
		<p class="infoop"><span class="emphasis-box">1</span>
		<bean:message key="message.teacherInformation.qualifications" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<tr>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.year" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.school" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsDegree" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsCourse" /></td>
			</tr>
			<logic:iterate id="infoQualification" name="infoSiteTeacherInformation" property="infoQualifications">
			<tr>
				<td class="listClasses"><bean:write name="infoQualification" property="year" /></td>
				<td class="listClasses"><bean:write name="infoQualification" property="school" /></td>
				<td class="listClasses"><bean:write name="infoQualification" property="title" /></td>
				<td class="listClasses"><bean:write name="infoQualification" property="mark" /></td>
			</tr>
			</logic:iterate>
		</table>
			<div class="gen-button">
				<html:link page="<%= "/readQualifications.do"%>">
					<bean:message key="label.teacherInformation.manage" />
				</html:link>
			</div>
		<br />
		<p class="infoop"><span class="emphasis-box">2</span>
		<bean:message key="message.teacherInformation.teachingCareer" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<tr>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.years" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.careerCategory" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.careerPositions" /></td>
			</tr>
			<logic:iterate id="infoTeachingCareer" name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<tr>
				<td class="listClasses"><bean:write name="infoProfessionalCareer" property="beginDate"/>
					-<bean:write name="infoProfessionalCareer" property="endDate"/>
				</td>
				<td class="listClasses"><bean:write name="infoTeachingCareer" property="infoCategory.name" /></td>
				<td class="listClasses"><bean:write name="infoTeachingCareer" property="courseOrPosition" /></td>
			</tr>
			</logic:iterate>
		</table>
		<div class="gen-button">
			<html:link page="<%= "/readCareers.do?careerType=Teaching&amp;page=0" %>">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<br />
		<p class="infoop"><span class="emphasis-box">3</span>
		<bean:message key="message.teacherInformation.professionalCareer" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<tr>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.years" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.entity" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.functions" /></td>
			</tr>
			<logic:iterate id="infoProfessionalCareer" 
						   name="infoSiteTeacherInformation" 
						   property="infoProfessionalCareers">
				<tr>
					<td class="listClasses">
						<bean:write name="infoProfessionalCareer" property="beginDate"/>
						-<bean:write name="infoProfessionalCareer" property="endDate"/>
					</td>
					<td class="listClasses"><bean:write name="infoProfessionalCareer" property="entity" /></td>
					<td class="listClasses"><bean:write name="infoProfessionalCareer" property="function" /></td>
				</tr>
			</logic:iterate>
		</table>
		<div class="gen-button">
			<html:link page="<%= "/readCareers.do?careerType=Professional&amp;page=0" %>">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<br />
		<p class="infoop"><span class="emphasis-box">4</span>
			<bean:message key="message.teacherInformation.serviceRegime" />
			<%--<bean:write name="" property="" />--%>
			<!-- VER ##################### -->
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
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
		<p class="infoop"><span class="emphasis-box">5</span>
			<bean:message key="message.teacherInformation.externalActivities" />
			<%--<bean:write name="" property="" />--%>
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<logic:iterate id="infoExternalActivity" name="infoSiteTeacherInformation" property="infoExternalActivities">
				<tr>
					<td class="listClasses"><bean:write name="infoExternalActivity" property="activity" /></td>
				</tr>
			</logic:iterate>
		</table>
		<div class="gen-button">
			<html:link page="/readExternalActivities.do">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<br />
		<p class="infoop"><span class="emphasis-box">6</span>
			<bean:message key="message.teacherInformation.numberOfPublications" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.number" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.national" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.international" /></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.comunicationsPublications" /></td>
				<!-- VER ################# -->
				<%--<td class="listClasses"><bean:write name="" property="" /></td>
				<td class="listClasses"><bean:write name="" property="" /></td>--%>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesPublications" /></td>
				<!-- VER ################# -->
				<%--<td class="listClasses"><bean:write name="" property="" /></td>
				<td class="listClasses"><bean:write name="" property="" /></td>--%>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookAuthorPublications" /></td>
				<!-- VER ################# -->
				<%--<td class="listClasses"><bean:write name="" property="" /></td>
				<td class="listClasses"><bean:write name="" property="" /></td>--%>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookEditorPublications" /></td>
				<%--<td class="listClasses"><bean:write name="" property="" /></td>
				<td class="listClasses"><bean:write name="" property="" /></td>--%>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesAndChaptersPublications" /></td>
				<%--<td class="listClasses"><bean:write name="" property="" /></td>
				<td class="listClasses"><bean:write name="" property="" /></td>--%>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">7</span>
			<bean:message key="message.teacherInformation.ownPublications" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
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
		<p class="infoop"><span class="emphasis-box">8</span>
			<bean:message key="message.teacherInformation.cientificPublications" /></p>
		<%--<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
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
		<p class="infoop"><span class="emphasis-box">9</span>
			<bean:message key="message.teacherInformation.lectureCourses" />
			<%--<bean:write name="" property="" />--%>
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.semester" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.lectureCourse" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsCourse" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.typeOfClass" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.numberOfClass" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.numberOfWeeklyHours" /></td>
			</tr>
			<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoExecutionCourses">
				<tr>
					<%--<td class="listClasses"><bean:write name="infoExecutionCourse" property="" /></td>--%>
						<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome" /></td>
				<%-- VER ##############		<td class="listClasses"><bean:write name="infoExecutionCourse" property="" /></td>
						<td class="listClasses"><bean:write name="infoExecutionCourse" property="" /></td>
						<td class="listClasses"><bean:write name="infoExecutionCourse" property="" /></td>
						<td class="listClasses"><bean:write name="infoExecutionCourse" property="" /></td>--%>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">10</span>
			<bean:message key="message.teacherInformation.orientations" />
			<%--<bean:write name="" property="" />--%>
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<td></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.numberOfStudents" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.description" /></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.tfc" /></td>
				<td class="listClasses"></td>
				<td class="listClasses"></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.masterThesis" /></td>
				<td class="listClasses"></td>
				<td class="listClasses"></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.phdThesis" /></td>
				<td class="listClasses"></td>
				<td class="listClasses"></td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">11</span>
			<bean:message key="message.teacherInformation.weeklySpendTime" />
			<%--<bean:write name="" property="" />--%>
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<td></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.teachers" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.supportLessons" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.investigation" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.managementWorks" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.others" /></td>
			</tr>
		</table>
		<tr>
			<td class="listClasses"><bean:message key="message.teacherInformation.numberOfHours" /></td>
		<%-- 	VER CREDITOS	
			<td class="listClasses"><bean:write name="" property="" /></td>
			<td class="listClasses"><bean:write name="" property="" /></td> --%>
			<td class="listClasses"><bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.research" /></td>
			<td class="listClasses"><bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.management" /></td>
			<td class="listClasses"><bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.other" /></td>
		</tr>
		<br />
		<p class="infoop"><span class="emphasis-box">12</span>
			<bean:message key="message.teacherInformation.managerPosition" />
			<%--<bean:write name="" property="" />--%>
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
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
