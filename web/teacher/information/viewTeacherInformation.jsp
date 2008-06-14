<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<h2><bean:message key="title.teacherInformation"/></h2>

<logic:present name="infoSiteTeacherInformation"> 
	<bean:define id="datePattern" value="dd-MM-yyyy"/>

	<div class="infoop2">
		<p>
			<bean:message key="message.teacherInformation.name" /> 
			<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nome" />
		</p>
		<p>
			<bean:message key="message.teacherInformation.birthDate" /> 
			<dt:format patternId="datePattern"><bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nascimento.time" /></dt:format>
		</p>
		<p>
		<bean:message key="message.teacherInformation.category" /> 
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeacher">
			<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeacher.infoCategory">
				<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName" />
			</logic:notEmpty>
		</logic:notEmpty>
		</p>
	</div>


		<p class="infoop mtop2"><span class="emphasis-box">1</span>
		<bean:message key="message.teacherInformation.qualifications" /></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoQualifications">
			<table class="tstyle1 thlight">	
				<tr>
					<th><bean:message key="message.teacherInformation.year" /></th>
					<th><bean:message key="message.teacherInformation.school" /></th>
					<th><bean:message key="message.teacherInformation.qualificationsDegree" /></th>
					<th><bean:message key="message.teacherInformation.qualificationsTitle" /></th>
					<th><bean:message key="message.teacherInformation.qualificationsMark" /></th>
				</tr>
				<logic:iterate id="infoQualification" name="infoSiteTeacherInformation" property="infoQualifications">
					<tr>
						<td>
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
						<td><bean:write name="infoQualification" property="school" /></td>
						<td><bean:write name="infoQualification" property="degree" /></td>
						<logic:notEmpty name="infoQualification" property="type">												
							<bean:define id="qualificationType" name="infoQualification" property="type.name" />
							<td><bean:message name="qualificationType" bundle="ENUMERATION_RESOURCES"/></td>
						</logic:notEmpty>
						<logic:empty name="infoQualification" property="type">																			
							<td><bean:write name="infoQualification" property="title"/></td>
						</logic:empty>	
						<logic:present name="infoQualification" property="mark">												
							<td>&nbsp;<bean:write name="infoQualification" property="mark" /></td>																
						</logic:present>
						<logic:notPresent name="infoQualification" property="mark">												
							<td>--</td>																
						</logic:notPresent>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoQualifications">

			<p class="mvert2"><em><bean:message key="label.teachersInformation.notModified" /></em></p>

		</logic:empty>
		<br/>
		<p class="infoop mtop2"><span class="emphasis-box">2</span>
		<bean:message key="message.teacherInformation.teachingCareer" /></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<table class="tstyle1 thlight">	
				<tr>
					<th><bean:message key="message.teacherInformation.years" /></th>
					<th><bean:message key="message.teacherInformation.careerCategory" /></th>
					<th><bean:message key="message.teacherInformation.careerPositions" /></th>
				</tr>
				<logic:iterate id="infoTeachingCareer" name="infoSiteTeacherInformation" property="infoTeachingCareers">
				<tr>
					<td><bean:write name="infoTeachingCareer" property="beginYear"/>
						-<bean:write name="infoTeachingCareer" property="endYear"/>
					</td>
					<td>				
						<logic:notEmpty name="infoTeachingCareer" property="infoCategory" >
							<bean:write name="infoTeachingCareer" property="infoCategory.shortName" />
						</logic:notEmpty>
					</td>
					<td><bean:write name="infoTeachingCareer" property="courseOrPosition" /></td>
				</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoTeachingCareers">

			<p class="mvert2"><em><bean:message key="label.teachersInformation.notModified" /></em></p>
			
		</logic:empty>

		<p class="infoop mtop2"><span class="emphasis-box">3</span>
		<bean:message key="message.teacherInformation.professionalCareer" /></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoProfessionalCareers">
			<table class="tstyle1 thlight">	
				<tr>
					<th><bean:message key="message.teacherInformation.years" /></th>
					<th><bean:message key="message.teacherInformation.entity" /></th>
					<th><bean:message key="message.teacherInformation.functions" /></th>
				</tr>
				<logic:iterate id="infoProfessionalCareer" 
							   name="infoSiteTeacherInformation" 
							   property="infoProfessionalCareers">
					<tr>
						<td>
							<bean:write name="infoProfessionalCareer" property="beginYear"/>
							-<bean:write name="infoProfessionalCareer" property="endYear"/>
						</td>
						<td><bean:write name="infoProfessionalCareer" property="entity" /></td>
						<td><bean:write name="infoProfessionalCareer" property="function" /></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoProfessionalCareers">

			<p class="mvert2"><em><bean:message key="label.teachersInformation.notModified" /></em></p>

		</logic:empty>
		<br/>
		<p class="infoop mtop2"><span class="emphasis-box">4</span>
			<bean:message key="message.teacherInformation.externalActivities" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoExternalActivities">
			<table width="100%">
				<logic:iterate id="infoExternalActivity" name="infoSiteTeacherInformation" property="infoExternalActivities">
					<tr>
						<td style="text-align:left">
							<bean:write name="infoExternalActivity" property="activity" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoExternalActivities">

			<p class="mvert2"><em><bean:message key="label.teachersInformation.notModified" /></em></p>

		</logic:empty>
		<br/>
		<p class="infoop mtop2"><span class="emphasis-box">5</span>
		<bean:message key="message.teacherInformation.ownPublications" /></p>
		<!-- DELETE (replace by resultpublications)  -->
		<%--  
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoDidaticPublications">
			<table>	
				<logic:iterate id="infoPublicationDidatic" name="infoSiteTeacherInformation" property="infoDidaticPublications">
					<tr>
						<td style="text-align:left">
							<bean:write name="infoPublicationDidatic" property="publicationString" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoDidaticPublications">

			<p class="mvert2"><em><bean:message key="label.teachersInformation.notModified" /></em></p>

		</logic:empty>
		--%>
		<!-- END DELETE  -->
		<!-- DIDATIC PUBLICATIONS (Sergio Patricio & Luis Santos)-->
		<logic:notEmpty name="didaticResults">
			<table class="tstyle1 thlight">	
				<logic:iterate id="didaticResult" name="didaticResults">
					<tr>
						<td style="text-align:left">
							<bean:write name="didaticResult" property="resume"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="didaticResults">

			<p class="mvert2"><em><bean:message key="label.teachersInformation.notModified" /></em></p>

		</logic:empty>
		<!--  END DIDATIC PUBLICATIONS -->

		<p class="infoop mtop2"><span class="emphasis-box">6</span>
		<bean:message key="message.teacherInformation.cientificPublications" /></p>
		<!-- DELETE (replace by resultpublications)  -->
		<%-- 
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoCientificPublications">
			<table>	
				<logic:iterate id="infoPublicationCientific" name="infoSiteTeacherInformation" property="infoCientificPublications">
					<tr>
						<td style="text-align:left">
							<bean:write name="infoPublicationCientific" property="publicationString" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoCientificPublications">

			<p class="mvert2"><em><bean:message key="label.teachersInformation.notModified" /></em></p>

		</logic:empty>
		--%>
		<!-- END DELETE  -->
		<!-- CIENTIFIC PUBLICATIONS (Sergio Patricio & Luis Santos)-->
		<logic:notEmpty name="cientificResults">
			<table class="tstyle1 thlight">	
				<logic:iterate id="cientificResult" name="cientificResults">
					<tr>
						<td style="text-align:left">
							<bean:write name="cientificResult" property="resume"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="cientificResults">

			<p class="mvert2"><em><bean:message key="label.teachersInformation.notModified" /></em></p>

		</logic:empty>
		<!--  END CIENTIFIC PUBLICATIONS -->

		<p class="infoop mtop2"><span class="emphasis-box">7</span>
		<bean:message key="message.teacherInformation.numberOfPublications" /></p>
		<table class="tstyle1 thlight">
			<tr>
				<th><bean:message key="message.teacherInformation.number" /></th>
				<th><bean:message key="message.teacherInformation.national" /></th>
				<th><bean:message key="message.teacherInformation.international" /></th>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.comunicationsPublications" /></td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber.national" />
				</td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.articlesPublications" /></td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMagArticlePublicationsNumber.national" />
				</td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMagArticlePublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.bookAuthorPublications" /></td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoAuthorBookPublicationsNumber.national" />
				</td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoAuthorBookPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.bookEditorPublications" /></td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoEditBookPublicationsNumber.national" />
				</td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoEditBookPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.articlesAndChaptersPublications" /></td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoArticleChapterPublicationsNumber.national" />
				</td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoArticleChapterPublicationsNumber.international" />
				</td>
			</tr>
		</table>

		<p class="infoop mtop2"><span class="emphasis-box">8</span>
			<bean:message key="message.teacherInformation.serviceRegime" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<logic:present name="infoSiteTeacherInformation" property="infoServiceProviderRegime.providerRegimeType">
			<table>	
				<tr>
					<td>
						<bean:message name="infoSiteTeacherInformation" 
									  property="infoServiceProviderRegime.providerRegimeType.name" 
									  bundle="ENUMERATION_RESOURCES"/>
					</td>
				</tr>
			</table>
		</logic:present>
		<logic:notPresent name="infoSiteTeacherInformation" property="infoServiceProviderRegime.providerRegimeType">

			<p class="mvert2"><em><bean:message key="label.teachersInformation.notModified" /></em></p>
			
		</logic:notPresent>

		<p class="infoop mtop2"><span class="emphasis-box">9</span>
			<bean:message key="message.teacherInformation.lectureCourses" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table class="tstyle1 thlight">
			<tr>
				<th><bean:message key="message.teacherInformation.semester" /></th>
				<th><bean:message key="message.teacherInformation.lectureCourse" /></th>
				<th><bean:message key="message.teacherInformation.qualificationsDegree" /></th>
				<th><bean:message key="message.teacherInformation.typeOfClass" /></th>
				<th><bean:message key="message.teacherInformation.numberOfClass" /></th>
				<th><bean:message key="message.teacherInformation.numberOfWeeklyHours" /></th>
			</tr>
			<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses">
				<logic:iterate id="infoCurricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
					<tr>
							<td>
								<bean:write name="infoExecutionCourse" 
											property="infoExecutionPeriod.semester" />
							</td>
							<td><bean:write name="infoExecutionCourse" property="nome" /></td>
							<td>
								<bean:write name="infoCurricularCourse" 
											property="infoDegreeCurricularPlan.infoDegree.nome" />
							</td>
							<td>&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
							<td>&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
							<td>&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</table>

		<p class="infoop mtop2"><span class="emphasis-box">10</span>
			<bean:message key="message.teacherInformation.orientations" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table class="tstyle1 thlight">
			<tr>
				<th>&nbsp;</th>
				<th><bean:message key="message.teacherInformation.numberOfStudents" /></th>
				<th><bean:message key="message.teacherInformation.description" /></th>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.tfc" /></td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoDegreeOrientation.numberOfStudents" />
				</td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoDegreeOrientation.description" />&nbsp;
				</td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.masterThesis" /></td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMasterOrientation.numberOfStudents" />
				</td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMasterOrientation.description" />&nbsp;
				</td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.phdThesis" /></td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoPhdOrientation.numberOfStudents" />
				</td>
				<td>&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoPhdOrientation.description" />&nbsp;
				</td>
			</tr>
		</table>

		<p class="infoop mtop2"><span class="emphasis-box">11</span>
			<bean:message key="message.teacherInformation.weeklySpendTime" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table class="tstyle1 thlight">
		<tr>
			<th>&nbsp;</th>
			<th><bean:message key="message.teacherInformation.teachers" /></th>
			<th><bean:message key="message.teacherInformation.supportLessons" /></th>
			<th><bean:message key="message.teacherInformation.investigation" /></th>
			<th><bean:message key="message.teacherInformation.managementWorks" /></th>
			<th><bean:message key="message.teacherInformation.others" /></th>
		</tr>
		<tr>
			<td><bean:message key="message.teacherInformation.numberOfHours" /></td>
			<td>
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.lecture" />
			</td>
			<td>
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.support" />
			</td>
			<td>
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.research" />
			</td>
			<td>
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.management" />
			</td>
			<td>
				<bean:write name="infoSiteTeacherInformation" property="infoWeeklyOcupation.other" />
			</td>
		</tr>
		</table>

		<p class="infoop mtop2"><span class="emphasis-box">12</span>
			<bean:message key="message.teacherInformation.managerPosition" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table class="tstyle1 thlight">
			<bean:define id="datePattern" value="dd-MM-yyyy"/>
			<logic:notEmpty name="infoSiteTeacherInformation" property="personFunctions">		
				<tr>
					<th style="text-align:left"><bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th style="text-align:left"><bean:message key="label.managementPosition.unit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>										
					<th width="10%"><bean:message key="label.managementPosition.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>					
					<th width="10%"><bean:message key="label.managementPosition.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<logic:iterate id="personFunction" name="infoSiteTeacherInformation" property="personFunctions">
					<tr>
						<td style="text-align:left">
							<bean:write name="personFunction" property="function.name"/>
						</td>
						<td style="text-align:left">
							<bean:write name="personFunction" property="function.unit.presentationNameWithParents"/>	
						</td>				
						<td>
							<bean:define id="beginDate" type="org.joda.time.YearMonthDay" name="personFunction" property="beginDate"/>
							<bean:define id="beginDateTime" ><%= beginDate.toDateTimeAtCurrentTime().toDate().getTime() %></bean:define>
							<dt:format patternId="datePattern">
								<bean:write name="beginDateTime"/>
							</dt:format>
						</td>
						<logic:notEmpty name="personFunction" property="endDate">
							<td>
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
					<td colspan="4"> 
						<i><bean:message key="message.managementPositions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
					</td>
				</tr>
			</logic:empty>		
		</table>
</logic:present>
