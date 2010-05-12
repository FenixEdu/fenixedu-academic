<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>


<logic:present name="registrationConclusionBean">
	<strong><bean:message  key="label.phd.studyPlan" bundle="PHD_RESOURCES"/></strong>
	<br/>

	<table>
		<tr>

			<%-- view registration conclusion information --%>
			<td>
				<fr:view schema="PhdIndividualProgramProcess.view.registration.conclusion.bean" name="registrationConclusionBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight mtop10" />
					</fr:layout>
				</fr:view>
			</td>
			
			<%-- show operations --%>
			<td>
				<ul class="operations">
					<li>
						<html:link action="/phdIndividualProgramProcess.do?method=viewCurriculum" paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.view.curriculum"/>
						</html:link>
					</li>
					<li>
						<html:link action="/phdIndividualProgramProcess.do?method=prepareEditWhenStartedStudies" paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.edit.when.started.studies"/>
						</html:link>
					</li>
					<logic:notEmpty name="process" property="registration">
						<li>
							<html:link action="/phdIndividualProgramProcess.do?method=printSchoolRegistrationDeclaration&language=en" paramId="processId" paramName="process" paramProperty="externalId">
								<bean:message bundle="PHD_RESOURCES" key="label.phd.print.school.registration.declaration"/>
							</html:link>
						</li>
					</logic:notEmpty>
				</ul>
			</td>
		</tr>
	</table>
	<br/>
</logic:present>

<logic:notPresent name="registrationConclusionBean">
	<logic:notEmpty name="process" property="studyPlan">
		<strong><bean:message  key="label.phd.studyPlan" bundle="PHD_RESOURCES"/></strong>
		<table>
			<tr>
				<td>
					<fr:view schema="PhdStudyPlan.description" name="process" property="studyPlan">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle2 thlight mtop10" />
						</fr:layout>
					</fr:view>
				</td>
				<td>
					<ul class="operations">
						<logic:notEmpty name="process" property="registration">
							<li>
								<html:link action="/phdIndividualProgramProcess.do?method=printSchoolRegistrationDeclaration&language=en" paramId="processId" paramName="process" paramProperty="externalId">
									<bean:message bundle="PHD_RESOURCES" key="label.phd.print.school.registration.declaration"/>
								</html:link>
							</li>
						</logic:notEmpty>
					</ul>
				</td>
			</tr>
		</table>
		<br />
	</logic:notEmpty>
</logic:notPresent>

