<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@page import="net.sourceforge.fenixedu.domain.Employee"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.DegreeFinalizationCertificate"%>
<%@page import="net.sourceforge.fenixedu.util.LanguageUtils"%>
<%@page import="org.joda.time.YearMonthDay"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<html:xhtml />
	
	<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>
	<bean:define id="registrationConclusionBean" name="registrationConclusionBean" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean"/>
	
	<table width="90%">
		<tr>
			<td align="center">
			<h2>Folha de Apuramento Final</h2>
			<br />
			<br />
			<br />
			</td>
		<tr>
			<td><b><bean:write name="registration" property="degree.presentationName"/></b></td>
		<tr>
			<td>O Aluno <b><bean:write name="registration" property="person.name"/></b>, na Matrícula nº <b><bean:write name="registration" property="number"/></b></td>
		<tr>
			<td>
				concluiu 
				<logic:equal name="registration" property="degreeType.administrativeOfficeType" value="AdministrativeOfficeType.MASTER_DEGREE">
					a parte escolar d
				</logic:equal>
				o curso de <bean:write name="registration" property="degreeType.localizedName"/> 
				acima indicado, constituído pelas seguintes unidades curriculares e classificações:
				<br/>
				<br/>
			</td>
		</tr>
	</table>
	

	<logic:equal name="registrationConclusionBean" property="curriculumForConclusion.studentCurricularPlan.boxStructure" value="true">
		<fr:view name="registrationConclusionBean" property="curriculumForConclusion">
			<fr:layout>
				<fr:property name="visibleCurricularYearEntries" value="false" />
			</fr:layout>
		</fr:view>
	</logic:equal>
	
	<logic:equal name="registrationConclusionBean" property="curriculumForConclusion.studentCurricularPlan.boxStructure" value="false">
		<bean:define id="curriculumEntries" name="registrationConclusionBean" property="curriculumForConclusion.curriculumEntries"/>
		<table class="tstyle4">
			<tr>
				<th rowspan="2" colspan="2">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.curricular.course.from.curriculum.withBreak"/>
				</th>
				<th rowspan="2" colspan="2">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.type.of.aproval.withBreak"/>
				</th>
				<th rowspan="2" colspan="2">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.curricular.course.aproved"/>
				</th>
				<th colspan="4">
					Média de Curso
				</th>
			</tr>
			<tr>
				<th>
					Classificação
				</th>
				<th colspan="2">
					Peso
				</th>
				<th style="width: 70px;">
					(Peso x Classificação)
				</th>
			</tr>
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.EnrolmentCurriculumEntry">
					<tr>
						<td><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
						<td><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>						
						<td colspan="2">
							<logic:equal name="curriculumEntry" property="enrolment.extraCurricular" value="true">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extra.curricular.course"/>
							</logic:equal>
							<logic:equal name="curriculumEntry" property="enrolment.extraCurricular" value="false">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.directly.approved"/>
							</logic:equal>
						</td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.code"/></td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td class="acenter"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.gradeValue"/></td>
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="weigthForCurriculum"/></td>
						<td class="acenter">
							<logic:empty name="curriculumEntry" property="weigthTimesGrade">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="weigthTimesGrade">
								<bean:write name="curriculumEntry" property="weigthTimesGrade"/>
							</logic:notEmpty>
						</td>
						<td class="acenter">-</td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.EquivalanteCurriculumEntry">
					<bean:size id="numberEntries" name="curriculumEntry" property="entries"/>
					<logic:iterate id="simpleEntry" name="curriculumEntry" property="entries" indexId="index">
						<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.EnrolmentCurriculumEntry">
							<tr>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
									<td rowspan="<%= numberEntries %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/></td>
								</logic:equal>
								<td>
									<logic:equal name="simpleEntry" property="enrolment.extraCurricular" value="true">
										<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extra.curricular.course"/>
									</logic:equal>
									<logic:equal name="simpleEntry" property="enrolment.extraCurricular" value="false">
										<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.directly.approved"/>
									</logic:equal>
								</td>
								<td><bean:write name="simpleEntry" property="enrolment.curricularCourse.code"/></td>
								<td><bean:write name="simpleEntry" property="enrolment.curricularCourse.name"/></td>
								<td class="acenter"><bean:write name="simpleEntry" property="enrolment.latestEnrolmentEvaluation.gradeValue"/></td>
								<td class="acenter"><bean:write name="simpleEntry" property="weigthForCurriculum"/></td>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>" class="acenter"><bean:write name="curriculumEntry" property="weigthForCurriculum"/></td>			
									<td rowspan="<%= numberEntries %>" class="acenter">
										<logic:empty name="curriculumEntry" property="weigthTimesGrade">
											-
										</logic:empty>
										<logic:notEmpty name="curriculumEntry" property="weigthTimesGrade">
											<bean:write name="curriculumEntry" property="weigthTimesGrade"/>
										</logic:notEmpty>
									</td>
								</logic:equal>
								<td class="acenter"><bean:write name="simpleEntry" property="ectsCreditsForCurriculum"/></td>
							</tr>
						</logic:equal>
					</logic:iterate>
					<logic:iterate id="simpleEntry" name="curriculumEntry" property="entries">
						<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.NotNeedToEnrolCurriculumEntry">
							<tr>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
									<td rowspan="<%= numberEntries %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/></td>
								</logic:equal>
								<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/></td>
								<td><bean:write name="simpleEntry" property="curricularCourse.code"/></td>
								<td><bean:write name="simpleEntry" property="curricularCourse.name"/></td>
								<td class="acenter">-</td>
								<td class="acenter">-</td>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>" class="acenter">-</td>
									<td rowspan="<%= numberEntries %>" class="acenter">-</td>
								</logic:equal>
								<td class="acenter"><bean:write name="simpleEntry" property="ectsCreditsForCurriculum"/></td>
							</tr>
						</logic:equal>
						<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.DismissalEntry">
							<tr>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
									<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
									<td rowspan="<%= numberEntries %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/></td>
								</logic:equal>
								<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/></td>
								<td>
									<logic:present name="simpleEntry" property="curricularCourse">
										<bean:write name="simpleEntry" property="curricularCourse.code"/>
									</logic:present>
								</td>
								<td>
									<logic:present name="simpleEntry" property="curricularCourse">
										<bean:write name="simpleEntry" property="curricularCourse.name"/>
									</logic:present>
									<logic:present name="simpleEntry" property="curriculumGroup">
										<bean:message key="label.studentDismissal.group.credits.dismissal" bundle="ACADEMIC_OFFICE_RESOURCES" /> (<bean:write name="curriculumEntry" property="curriculumGroup.name.content"/>)
									</logic:present>
								</td>
								<td class="acenter">-</td>
								<td class="acenter">-</td>
								<logic:equal name="index" value="0">
									<td rowspan="<%= numberEntries %>" class="acenter">-</td>
									<td rowspan="<%= numberEntries %>" class="acenter">-</td>
								</logic:equal>
								<td class="acenter"><bean:write name="simpleEntry" property="ectsCreditsForCurriculum"/></td>
							</tr>
						</logic:equal>
					</logic:iterate>
				</logic:equal>
			</logic:iterate>
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.NotNeedToEnrolCurriculumEntry">
					<tr>
						<td><bean:write name="curriculumEntry" property="curricularCourse.code"/></td>
						<td><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
						<td colspan="2">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/>
							<logic:equal name="curriculumEntry" property="ectsCreditsForCurriculum" value="0">
								(<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.equivalency"/>)
							</logic:equal>
						</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.DismissalEntry">
					<tr>
						<td>
							<logic:present name="curriculumEntry" property="curricularCourse">
								<bean:write name="curriculumEntry" property="curricularCourse.code"/>
							</logic:present>
						</td>
						<td>
							<logic:present name="curriculumEntry" property="curricularCourse">
								<bean:write name="curriculumEntry" property="curricularCourse.name"/>
							</logic:present>
							<logic:present name="curriculumEntry" property="curriculumGroup">
								<bean:message key="label.studentDismissal.group.credits.dismissal" bundle="ACADEMIC_OFFICE_RESOURCES" /> (<bean:write name="curriculumEntry" property="curriculumGroup.name.content"/>)
							</logic:present>
						</td>
						<td colspan="2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.not.need.to.enrol"/></td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
					</tr>
				</logic:equal>
			</logic:iterate>
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.CreditsInScientificAreaCurriculumEntry">
					<tr>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td colspan="2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.credits.in.scientific.area"/></td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
					</tr>
				</logic:equal>
			</logic:iterate>
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.CreditsInAnySecundaryAreaCurriculumEntry">
					<tr>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td colspan="2"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.credits.in.any.secondary.area"/></td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
						<td class="acenter">-</td>
					</tr>
				</logic:equal>
			</logic:iterate>
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.NotInDegreeCurriculumCurriculumEntry">
					<tr>
						<td></td>
						<td></td>
						<td colspan="2">
							<logic:equal name="curriculumEntry" property="enrolment.extraCurricular" value="true">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extra.curricular.course"/>
							</logic:equal>
						</td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.code"/></td>
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td class="acenter"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.gradeValue"/></td>						
						<td class="acenter">-</td>
						<td class="acenter"><bean:write name="curriculumEntry" property="weigthForCurriculum"/></td>
						<td class="acenter">
							<logic:empty name="curriculumEntry" property="weigthTimesGrade">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="weigthTimesGrade">
								<bean:write name="curriculumEntry" property="weigthTimesGrade"/>
							</logic:notEmpty>
						</td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<tr>
				<td colspan="8" style="text-align: right;">
					Somatórios
				</td>
				<td class="acenter">
					<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumPi"/>
				</td>
				<td class="acenter">
					<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumPiCi"/>
				</td>
			</tr>
		</table>
	</logic:equal>

	
	<%
		request.setAttribute("degreeFinalizationDate", registrationConclusionBean.getConclusionDate().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
		final Integer finalAverage = registrationConclusionBean.getFinalAverage();	
		request.setAttribute("finalAverage", finalAverage);
		request.setAttribute("degreeFinalizationGrade", DegreeFinalizationCertificate.getDegreeFinalizationGrade(finalAverage));
		request.setAttribute("degreeFinalizationEcts", String.valueOf(registrationConclusionBean.getEctsCredits()));
		request.setAttribute("creditsDescription", registration.getDegreeType().getCreditsDescription());
		
		final Employee employee = AccessControl.getPerson().getEmployee();
		final Person administrativeOfficeCoordinator = employee.getCurrentWorkingPlace().getActiveUnitCoordinator();
		request.setAttribute("administrativeOfficeCoordinator", administrativeOfficeCoordinator);
		request.setAttribute("administrativeOfficeCoordinatorGender", administrativeOfficeCoordinator.isMale() ? "" : "a");
		request.setAttribute("administrativeOfficeName", employee.getCurrentWorkingPlace().getName());
		request.setAttribute("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
	%>
	
	<table class="apura-final" width="90%" cellspacing="0" border="0">
		<tr>
			<td colspan="6" style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;">Atribuição da Média</td>
		</tr>
		<tr>
			<td style="padding: 5px;"><bean:message bundle="APPLICATION_RESOURCES" key="label.net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean.average"/></td>
			<td style="padding: 5px;"><bean:write name="registrationConclusionBean" property="average"/></td>		
			
			<logic:equal name="registration" property="degreeType.administrativeOfficeType" value="MASTER_DEGREE">
				<td width="50%" style="padding: 5px; padding-left: 15em;">O coordenador do curso,</td>
			</logic:equal>
		</tr>
		<tr>
			<td style="padding: 5px;"><bean:message bundle="APPLICATION_RESOURCES" key="label.net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean.registration.averageType"/></td>
			<bean:define id="averageType">
				AverageType.<bean:write name="registrationConclusionBean" property="registration.averageType"/>
			</bean:define>	
			<td style="padding: 5px;"><bean:message bundle="ENUMERATION_RESOURCES" key="<%=averageType%>"/></td>
		</tr>
		<tr>
			<td style="padding: 5px;">Média Final</td>
			<td style="padding: 5px;"><bean:write name="finalAverage"/> valores</td>
			<td style="padding: 5px; padding-left: 100px;">	</td>
		</tr>
		<tr>
			<td colspan="2" style="padding: 5px;"></td>
			<logic:equal name="registration" property="degreeType.administrativeOfficeType" value="MASTER_DEGREE">
				<td width="50%" style="padding: 5px; padding-left: 15em;">_____________________________</td>
			</logic:equal>
		</tr>
	</table>
	
	<br/>
	
	<table class="apura-final" width="90%" cellspacing="0" border="0">
		<tr>
			<td colspan="2" style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;">Informação</td>
		</tr>
		<tr>
			<td colspan="2" style="padding: 5px;">
				<p class="apura-pt9">
					Concluiu 
					<logic:equal name="registration" property="degreeType.administrativeOfficeType" value="AdministrativeOfficeType.MASTER_DEGREE">
						a parte escolar d
					</logic:equal>
					o <bean:write name="registrationConclusionBean" property="degreeDescription"/>
					em <bean:write name="degreeFinalizationDate"/><bean:write name="degreeFinalizationGrade"/>, 
					tendo obtido o total de <bean:write name="degreeFinalizationEcts"/><bean:write name="creditsDescription"/>.
				</p>
			</td>
		</tr>
		<tr>
			<td style="text-align: center;">
				Conferido em <bean:write name="day"/>
			</td>
			<td style="text-align: center;">
				<div class="homologo">Homologo,</div>
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td style="text-align: center;">_____________________________</td>
			<td style="text-align: center;">_____________________________</td>
		</tr>
		<tr>
			<td style="text-align: center;"></td>
			<td style="text-align: center;"><bean:write name="administrativeOfficeCoordinator" property="name"/></td>
		</tr>
		<tr>
			<td style="text-align: center;"></td>
			<td style="text-align: center;">Coordenador<bean:write name="administrativeOfficeCoordinatorGender"/> do <bean:write name="administrativeOfficeName"/></td>
		</tr>
	</table>

</logic:present>
