<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<logic:present name="infoSiteTeacherInformation"> 
	<html:form action="/teacherInformation">
		<logic:messagesPresent>
		<span class="error"><!-- Error messages go here -->
			<html:errors/>
		</span>
		</logic:messagesPresent>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weeklyOcupationId" property="weeklyOcupationId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.serviceProviderRegimeId" property="serviceProviderRegimeId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeOrientationId" property="degreeOrientationId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.masterOrientationId" property="masterOrientationId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.phdOrientationId" property="phdOrientationId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.comunicationPublicationsNumberId" property="comunicationPublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.magArticlePublicationsNumberId" property="magArticlePublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorBookPublicationsNumberId" property="authorBookPublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.editorBookPublicationsNumberId" property="editorBookPublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.articlesChaptersPublicationsNumberId" property="articlesChaptersPublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<table class="listClasses" width="100%">
			<tr>
				<td>
					<p align="left"><b><bean:message key="message.teacherInformation.warning" /></b></p>
				</td>
			</tr>
		</table>
		<br/>
		<table class="infoselected" width="100%">
			<tr>
				<td width="70%"><b><bean:message key="message.teacherInformation.name" /></b>
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nome" /> </td> 
				<td width="30%"><b><bean:message key="message.teacherInformation.birthDate" /></b>
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nascimento" /> </td>	
			</tr>
			<tr>
				<td>
					<b><bean:message key="message.teacherInformation.category" /></b>
					&nbsp;
					<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeacher.infoCategory" >
						<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName" />
					</logic:notEmpty>
				</td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">1</span>
		<bean:message key="message.teacherInformation.qualifications" /></p>		
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoQualifications">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.year" bundle="GEP_RESOURCES" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.school" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsDegree" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsTitle" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsMark" bundle="GEP_RESOURCES"/></th>
				</tr>
				<logic:iterate id="infoQualification" name="infoSiteTeacherInformation" property="infoQualifications">
					<tr>
						<td class="listClasses">				
							<logic:notEmpty name="infoQualification" property="year">
								<bean:write name="infoQualification" property="year" />							
							</logic:notEmpty>			 								
							<logic:empty name="infoQualification" property="year">
								<logic:notEmpty name="infoQualification" property="date">
									<dt:format pattern="yyyy">
										<bean:write name="infoQualification" property="date.time" />							
									</dt:format>
								</logic:notEmpty>
							</logic:empty>
							<logic:empty name="infoQualification" property="year">
								<logic:empty name="infoQualification" property="date">
									--
								</logic:empty>
							</logic:empty>
						</td>												
						<td class="listClasses"><bean:write name="infoQualification" property="school" /></td>
						<td class="listClasses"><bean:write name="infoQualification" property="degree" /></td>
						<logic:notEmpty name="infoQualification" property="type">												
							<bean:define id="qualificationType" name="infoQualification" property="type.name" />
							<td class="listClasses"><bean:message name="qualificationType" bundle="ENUMERATION_RESOURCES"/></td>
						</logic:notEmpty>
						<logic:empty name="infoQualification" property="type">																			
							<td class="listClasses"><bean:write name="infoQualification" property="title"/></td>
						</logic:empty>	
						<logic:present name="infoQualification" property="mark">												
							<td class="listClasses">&nbsp;<bean:write name="infoQualification" property="mark" /></td>																
						</logic:present>
						<logic:notPresent name="infoQualification" property="mark">												
							<td class="listClasses">--</td>																
						</logic:notPresent>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoQualifications">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>			
		</logic:empty>
		<br />
		<p class="infoop"><span class="emphasis-box">2</span>
		<bean:message key="message.teacherInformation.teachingCareer" /></p>
		<div class="gen-button">
			<html:link page="<%= "/readCareers.do?careerType=" + net.sourceforge.fenixedu.domain.CareerType.TEACHING.toString() + "&amp;page=0" %>">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.years" bundle="GEP_RESOURCES" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.careerCategory" bundle="GEP_RESOURCES" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.careerPositions" bundle="GEP_RESOURCES" /></th>
				</tr>
				<logic:iterate id="infoTeachingCareer" name="infoSiteTeacherInformation" property="infoTeachingCareers">
				<tr>
					<td class="listClasses"><bean:write name="infoTeachingCareer" property="beginYear"/>
						-<bean:write name="infoTeachingCareer" property="endYear"/>
					</td>
					<td class="listClasses">
						&nbsp;
						<logic:notEmpty name="infoTeachingCareer" property="infoCategory">
							<bean:write name="infoTeachingCareer" property="infoCategory.shortName" />
						</logic:notEmpty>
					</td>
					<td class="listClasses"><bean:write name="infoTeachingCareer" property="courseOrPosition" /></td>
				</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<p class="infoop"><span class="emphasis-box">3</span>
		<bean:message key="message.teacherInformation.professionalCareer" /></p>
		<div class="gen-button">
			<html:link page="<%= "/readCareers.do?careerType=" + net.sourceforge.fenixedu.domain.CareerType.PROFESSIONAL.toString() + "&amp;page=0" %>">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoProfessionalCareers">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.years" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.entity" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.functions" bundle="GEP_RESOURCES"/></th>
				</tr>
				<logic:iterate id="infoProfessionalCareer" 
							   name="infoSiteTeacherInformation" 
							   property="infoProfessionalCareers">
					<tr>
						<td class="listClasses">
							<bean:write name="infoProfessionalCareer" property="beginYear"/>
							-<bean:write name="infoProfessionalCareer" property="endYear"/>
						</td>
						<td class="listClasses"><bean:write name="infoProfessionalCareer" property="entity" /></td>
						<td class="listClasses"><bean:write name="infoProfessionalCareer" property="function" /></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoProfessionalCareers">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<p class="infoop"><span class="emphasis-box">4</span>
			<bean:message key="message.teacherInformation.externalActivities" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<div class="gen-button">
			<html:link page="/readExternalActivities.do">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoExternalActivities">
			<br/>
			<table width="100%">	
				<logic:iterate id="infoExternalActivity" name="infoSiteTeacherInformation" property="infoExternalActivities">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="infoExternalActivity" property="activity" bundle="GEP_RESOURCES"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoExternalActivities">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<!-- TJBF & PFON -->
		<p class="infoop"><span class="emphasis-box">5</span>
		<bean:message key="message.teacherInformation.ownPublications" />
		</p>
		<div class="gen-button">
			<html:link page="/readPublications.do?typePublication=Didatic&amp;page=0">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoDidaticPublications">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<logic:iterate id="infoPublicationDidatic" name="infoSiteTeacherInformation" property="infoDidaticPublications">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="infoPublicationDidatic" property="publicationString" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoDidaticPublications">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<p class="infoop"><span class="emphasis-box">6</span>
		<bean:message key="message.teacherInformation.cientificPublications" /></p>
		<div class="gen-button">
			<html:link page="/readPublications.do?typePublication=Cientific&amp;page=0">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoCientificPublications">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<logic:iterate id="infoPublicationCientific" name="infoSiteTeacherInformation" property="infoCientificPublications">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="infoPublicationCientific" property="publicationString" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoCientificPublications">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<!-- TJBF & PFON -->
		<p class="infoop"><span class="emphasis-box">7</span>
			<bean:message key="message.teacherInformation.numberOfPublications" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.number" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.national" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.international" /></th>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.comunicationsPublications" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.comunicationNational" property="comunicationNational" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.comunicationInternational" property="comunicationInternational" /></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesPublications" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.magArticleNational" property="magArticleNational" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.magArticleInternational" property="magArticleInternational" /></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookAuthorPublications" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.authorBookNational" property="authorBookNational" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.authorBookInternational" property="authorBookInternational" /></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookEditorPublications" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.editorBookNational" property="editorBookNational" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.editorBookInternational" property="editorBookInternational" /></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesAndChaptersPublications" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.articlesChaptersNational" property="articlesChaptersNational" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.articlesChaptersInternational" property="articlesChaptersInternational" /></td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">8</span>
			<bean:message key="message.teacherInformation.serviceRegime" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="1" cellspacing="1">	
			<logic:iterate id="providerRegimeType" name="providerRegimeTypeList" scope="request">
				<tr>
					<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.serviceProviderRegimeTypeName" property="serviceProviderRegimeTypeName"
					  		  idName="providerRegimeType"
							  value="name"/>
					</td>
					<td><bean:message name="providerRegimeType" property="name" bundle="ENUMERATION_RESOURCES"/></td>
				</tr>
			</logic:iterate>			
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">9</span>
			<bean:message key="message.teacherInformation.lectureCourses" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.semester" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.lectureCourse" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsDegree" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.typeOfClass" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.numberOfClass" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.numberOfWeeklyHours" /></th>
				</tr>
				<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses">
					<logic:iterate id="infoCurricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
						<tr>
								<td class="listClasses">
									<bean:write name="infoExecutionCourse" 
												property="infoExecutionPeriod.semester" />
								</td>
								<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome" /></td>
								<td class="listClasses">
									<bean:write name="infoCurricularCourse" 
												property="infoDegreeCurricularPlan.infoDegree.nome" />
								</td>
								<td class="listClasses">&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
								<td class="listClasses">&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
								<td class="listClasses">&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
						</tr>
					</logic:iterate>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses">
			<br/>&nbsp;<br/>
			<bean:message key="message.teacherInformation.noExecutionCourseLectured" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<p class="infoop"><span class="emphasis-box">10</span>
			<bean:message key="message.teacherInformation.orientations" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<th class="listClasses-header">&nbsp;</th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.numberOfStudents" /></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.description" /></th>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.tfc" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.degreeStudentsNumber" property="degreeStudentsNumber" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.degreeDescription" property="degreeDescription" /></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.masterThesis" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.masterStudentsNumber" property="masterStudentsNumber" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.masterDescription" property="masterDescription" /></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.phdThesis" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.phdStudentsNumber" property="phdStudentsNumber" /></td>
				<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.phdDescription" property="phdDescription" /></td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">11</span>
			<bean:message key="message.teacherInformation.weeklySpendTime" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<th class="listClasses-header">&nbsp;</th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.teachers" /></th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.supportLessons" /></th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.investigation" /></th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.managementWorks" /></th>
			<th class="listClasses-header"><bean:message key="message.teacherInformation.others" /></th>
		</tr>
		<tr>
			<td class="listClasses"><bean:message key="message.teacherInformation.numberOfHours" /></td>	
			<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.lecture" property="lecture" /></td>
			<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.support" property="support" /></td>
			<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.research" property="research" /></td>
			<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.management" property="management" /></td>
			<td class="listClasses"><html:text bundle="HTMLALT_RESOURCES" altKey="text.other" property="other" /></td>
		</tr>
		</table>
		<br />		
		<p class="infoop"><span class="emphasis-box">12</span>
			<bean:message key="message.teacherInformation.managerPosition" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<bean:define id="datePattern" value="dd-MM-yyyy"/>
			<logic:notEmpty name="infoSiteTeacherInformation" property="personFunctions">		
				<tr>
					<th class="listClasses-header" style="text-align:left"><bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th class="listClasses-header" style="text-align:left"><bean:message key="label.managementPosition.unit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>										
					<th class="listClasses-header" width="10%"><bean:message key="label.managementPosition.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>					
					<th class="listClasses-header" width="10%"><bean:message key="label.managementPosition.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<logic:iterate id="personFunction" name="infoSiteTeacherInformation" property="personFunctions">
					<tr>
						<td class="listClasses" style="text-align:left">
							<bean:write name="personFunction" property="function.name"/>
						</td>
						<td class="listClasses" style="text-align:left">
							<bean:define id="unit" name="personFunction" property="function.unit"/>
							<bean:write name="unit" property="name"/>
							<logic:notEmpty name="unit" property="topUnits">
								-
								<logic:iterate id="topUnit" name="unit" property="topUnits">
									<bean:write name="topUnit" property="name"/>,							
								</logic:iterate>								
							</logic:notEmpty>
						</td>				
						<td class="listClasses">
							<bean:define id="beginDate" type="org.joda.time.YearMonthDay" name="personFunction" property="beginDate"/>
							<bean:define id="beginDateTime" ><%= beginDate.toDateTimeAtCurrentTime().toDate().getTime() %></bean:define>
							<dt:format patternId="datePattern">
								<bean:write name="beginDateTime"/>
							</dt:format>
						</td>
						<logic:notEmpty name="personFunction" property="endDate">
							<td class="listClasses">
								<bean:define id="endDate" type="org.joda.time.YearMonthDay" name="personFunction" property="endDate"/>
								<bean:define id="endDateTime" ><%= endDate.toDateTimeAtCurrentTime().toDate().getTime() %></bean:define>
								<dt:format patternId="datePattern">
									<bean:write name="endDateTime"/>
								</dt:format>
							</td>
						</logic:notEmpty>
						<logic:empty name="personFunction" property="endDate">
						-			
						</logic:empty>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="infoSiteTeacherInformation" property="personFunctions">
				<tr>
					<td colspan="4" class="listClasses"> 
						<i><bean:message key="message.managementPositions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
					</td>
				</tr>
			</logic:empty>		
		</table>
		<h3>
			<table>
				<tr align="center">	
					<td>
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
							<bean:message key="button.confirm"/>
						</html:submit>
					</td>
					<td>
						<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
							<bean:message key="label.clear"/>
						</html:reset>
					</td>
				</tr>
			</table>
		</h3>
	</html:form>
</logic:present>