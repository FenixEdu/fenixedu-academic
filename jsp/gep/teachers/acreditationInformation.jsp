<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<logic:present name="infoSiteTeachersInformation">
	<logic:iterate id="infoSiteTeacherInformation" name="infoSiteTeachersInformation">
		<h2><bean:message key="label.acred.teacherInfo" bundle="GEP_RESOURCES"/></h2>
		<h2>
			<bean:message key="label.acred.degree" bundle="GEP_RESOURCES"/>
			<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>
		</h2>
		<table class="infoselected" width="100%">
			<tr>
				<td width="70%"><bean:message key="label.acred.teacherInformation.name" bundle="GEP_RESOURCES"/>
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nome" /> </td> 
				<td width="30%"><bean:message key="label.acred.teacherInformation.birthDate" bundle="GEP_RESOURCES"/>
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nascimento" /> </td>	
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.category" bundle="GEP_RESOURCES"/>
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName" /></td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">1</span>
		<bean:message key="message.teacherInformation.qualifications" bundle="GEP_RESOURCES"/></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<tr>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.year" bundle="GEP_RESOURCES" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.school" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsDegree" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsTitle" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsMark" bundle="GEP_RESOURCES"/></td>
			</tr>
			<logic:iterate id="infoQualification" name="infoSiteTeacherInformation" property="infoQualifications">
				<tr>
					<td class="listClasses">
						<dt:format pattern="dd-MM-yyyy">
							<bean:write name="infoQualification" property="date.time" />
						</dt:format>
					</td>
					<td class="listClasses"><bean:write name="infoQualification" property="school" /></td>
					<td class="listClasses"><bean:write name="infoQualification" property="degree" /></td>
					<td class="listClasses"><bean:write name="infoQualification" property="title" /></td>
					<td class="listClasses">&nbsp;<bean:write name="infoQualification" property="mark" /></td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">2</span>
		<bean:message key="message.teacherInformation.teachingCareer" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<tr>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.years" bundle="GEP_RESOURCES" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.careerCategory" bundle="GEP_RESOURCES" /></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.careerPositions" bundle="GEP_RESOURCES" /></td>
			</tr>
			<logic:iterate id="infoTeachingCareer" name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<tr>
				<td class="listClasses"><bean:write name="infoTeachingCareer" property="beginYear"/>
					-<bean:write name="infoTeachingCareer" property="endYear"/>
				</td>
				<td class="listClasses"><bean:write name="infoTeachingCareer" property="infoCategory.shortName" /></td>
				<td class="listClasses"><bean:write name="infoTeachingCareer" property="courseOrPosition" /></td>
			</tr>
			</logic:iterate>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">3</span>
		<bean:message key="message.teacherInformation.professionalCareer" /></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<tr>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.years" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.entity" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.functions" bundle="GEP_RESOURCES"/></td>
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
		<br />
		<p class="infoop"><span class="emphasis-box">4</span>
			<bean:message key="message.teacherInformation.serviceRegime" bundle="GEP_RESOURCES" />
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
			<tr>
				<td>
					<logic:present name="infoSiteTeacherInformation" property="infoServiceProviderRegime.providerRegimeType">
						<bean:message name="infoSiteTeacherInformation" 
									  property="infoServiceProviderRegime.providerRegimeType.name" 
									  bundle="ENUMERATION_RESOURCES"/>
				  	</logic:present>
				</td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">5</span>
			<bean:message key="message.teacherInformation.externalActivities" bundle="GEP_RESOURCES"/>
		</p>
		<br />	
		<table width="100%">	
			<logic:iterate id="infoExternalActivity" name="infoSiteTeacherInformation" property="infoExternalActivities">
				<tr>
					<td class="listClasses" style="text-align:left">
						<bean:write name="infoExternalActivity" property="activity" bundle="GEP_RESOURCES"/>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">6</span>
				<bean:message key="message.teacherInformation.numberOfPublications" bundle="GEP_RESOURCES"/>
		</p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.number" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.national" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses-header"><bean:message key="message.teacherInformation.international" bundle="GEP_RESOURCES"/></td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.comunicationsPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMagArticlePublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMagArticlePublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookAuthorPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoAuthorBookPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoAuthorBookPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookEditorPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoEditBookPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoEditBookPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesAndChaptersPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoArticleChapterPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoArticleChapterPublicationsNumber.international" />
				</td>
			</tr>
		</table>
		<br/>
		<p class="infoop">
			<span class="emphasis-box">7</span>
			<bean:message key="message.teacherInformation.ownPublications" />
		</p>
		<table width="100%" border="0" cellspacing="1">	
			<tr>
				<td style="text-align:left">
					<logic:iterate id="infoOldPublication" name="infoSiteTeacherInformation" property="infoOldDidacticPublications">
						<bean:write name="infoOldPublication" property="publication" />
						<br/>
						<br/>
					</logic:iterate>
				</td>
			</tr>
		<br />
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">8</span>
			<bean:message key="message.teacherInformation.cientificPublications" />
		</p>
		<table id="ects" width="100%" border="0" cellspacing="1">	
			<tr>
				<td style="text-align:left">
					<logic:iterate id="infoOldPublication" name="infoSiteTeacherInformation" property="infoOldCientificPublications">
						<bean:write name="infoOldPublication" property="publication" />
						<br/>
						<br/>
					</logic:iterate>
				</td>
			</tr>
			<br />
		</table>
		<br />
	</logic:iterate>
</logic:present>
