<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2>
	<bean:message key="portal.library.operator" bundle="PORTAL_RESOURCES" />
</h2>

<table>
	<tr>
		<td style="vertical-align: bottom;"><fr:form
				action="/libraryOperator.do?method=selectLibrary">
				<fr:edit id="attendance" name="attendance">
					<fr:schema bundle="LIBRARY_RESOURCES"
						type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
						<fr:slot name="library" key="label.library"
							layout="menu-select-postback" required="true">
							<fr:property name="providerClass"
								value="net.sourceforge.fenixedu.presentationTier.renderers.providers.library.LibraryProvider" />
							<fr:property name="format"
								value="Biblioteca ${spaceBuilding.nameWithCampus}" />
							<fr:property name="destination" value="postback" />
						</fr:slot>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes"
							value="tstyle2 thlight thleft mbottom0" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>
					<fr:destination name="postback"
						path="/libraryOperator.do?method=selectLibrary" />
				</fr:edit>
			</fr:form></td>
		<td>
			<div style="float: right; margin-left: 10px; margin-top: 15px;">
				<logic:present name="attendance" property="library">
					<html:img align="middle"
						action="/libraryOperator.do?method=createAreaXYChart"
						paramId="library" paramName="attendance"
						paramProperty="library.externalId" />
				</logic:present>
			</div>
		</td>
</table>

<table>
	<tr>
		<td style="vertical-align: top;"><logic:present name="attendance"
				property="library">
				<h3 class="mtop2">
					<bean:message key="label.find.person" bundle="LIBRARY_RESOURCES" />
				</h3>

				<fr:form action="/libraryOperator.do?method=searchPerson">
					<table>
						<tr>
							<td><fr:edit id="search.person" name="attendance">
									<fr:schema bundle="LIBRARY_RESOURCES"
										type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
										<fr:slot name="personId" key="label.search.person" />
									</fr:schema>
									<fr:layout name="tabular">
										<fr:property name="classes"
											value="tstyle2 thlight thleft mtop05 mbottom05" />
										<fr:property name="columnClasses" value=",,tdclear tderror1" />
									</fr:layout>
								</fr:edit>
							</td>
							<td><html:submit>
									<bean:message key="button.search" bundle="LIBRARY_RESOURCES" />
								</html:submit>
							</td>
						</tr>
					</table>
				</fr:form>

				<logic:present name="attendance" property="person">
					<fr:view name="attendance">
						<fr:schema bundle="LIBRARY_RESOURCES"
							type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
							<fr:slot name="person" key="label.person.picture"
								layout="view-as-image">
								<fr:property name="classes" value="column3" />
								<fr:property name="useParent" value="true" />
								<fr:property name="moduleRelative" value="false" />
								<fr:property name="contextRelative" value="true" />
								<fr:property name="imageFormat"
									value="/person/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=${person.istUsername}" />
							</fr:slot>
							<fr:slot name="person.name" key="label.person.name" />
							<fr:slot name="person.istUsername" key="label.person.istUsername" />
							<logic:present name="attendance" property="teacherUnit">
								<fr:slot name="teacherUnit.presentationName"
									key="label.person.teacher" />
							</logic:present>
							<logic:present name="attendance" property="researcherUnit">
								<fr:slot name="researcherUnit.presentationName"
									key="label.person.researcher" />
							</logic:present>
							<logic:present name="attendance" property="grantOwnerUnit">
								<fr:slot name="grantOwnerUnit.presentationName"
									key="label.person.grantOwner" />
							</logic:present>
							<logic:present name="attendance" property="employeeUnit">
								<fr:slot name="employeeUnit.presentationName"
									key="label.person.employee" />
							</logic:present>
							<logic:present name="attendance"
								property="person.externalResearchContract">
								<fr:slot
									name="person.externalResearchContract.workingUnit.presentationName"
									key="label.person.externalResearcher" />
							</logic:present>
							<logic:present name="attendance" property="studentRegistration">
								<fr:slot name="studentRegistration.degree.presentationName"
									key="label.person.student" />
							</logic:present>
							<logic:present name="attendance" property="alumniRegistration">
								<fr:slot name="alumniRegistration.degree.presentationName"
									key="label.person.alumni" />
							</logic:present>
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes"
								value="tstyle2 thlight thleft mtop05 mbottom05" />
						</fr:layout>
					</fr:view>

					<fr:form action="/libraryOperator.do">
						<input type="hidden" name="method" />
						<table>
							<tr>
								<td><fr:edit id="person.edit.libraryCardNumber"
										name="attendance">
										<fr:schema bundle="LIBRARY_RESOURCES"
											type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
											<fr:slot name="personLibraryCardNumber"
												key="label.person.libraryCardNumber" />
										</fr:schema>
										<fr:layout name="tabular">
											<fr:property name="classes"
												value="tstyle2 thlight thleft mtop05 mbottom05" />
										</fr:layout>
									</fr:edit></td>
								<td>
									<p>
										<html:submit disabled="true"
											onclick="this.form.method.value='generateCardNumber';">
											<bean:message key="button.generateLibraryNumber"
												bundle="LIBRARY_RESOURCES" />
										</html:submit>
										<html:submit
											onclick="this.form.method.value='saveCardNumber';">
											<bean:message key="button.save" bundle="LIBRARY_RESOURCES" />
										</html:submit>
									</p>
								</td>
							</tr>
						</table>
					</fr:form>

					<logic:present name="attendance" property="personAttendance">
						<fr:form action="/libraryOperator.do?method=exitPlace">
							<table>
								<tr>
									<td><fr:edit id="person.selectPlace" name="attendance">
											<fr:schema bundle="LIBRARY_RESOURCES"
												type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
												<fr:slot
													name="selectedSpace.spaceInformation.presentationName"
													key="label.person.place" readOnly="true" />
												<fr:slot name="personAttendance.entranceTime"
													key="label.person.entranceTime" readOnly="true" />
											</fr:schema>
											<fr:layout name="tabular">
												<fr:property name="classes"
													value="tstyle2 thlight thleft mtop05 mbottom05" />
											</fr:layout>
										</fr:edit></td>
									<td><html:submit>
											<bean:message key="button.exit" bundle="LIBRARY_RESOURCES" />
										</html:submit></td>
								</tr>
							</table>
						</fr:form>
					</logic:present>
					<logic:notPresent name="attendance" property="personAttendance">
						<fr:form action="/libraryOperator.do?method=selectPlace">
							<table>
								<tr>
									<td><fr:edit id="person.selectPlace" name="attendance">
											<fr:schema bundle="LIBRARY_RESOURCES"
												type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
												<fr:slot name="selectedSpace" key="label.person.place"
													layout="menu-select" required="true">
													<fr:property name="providerClass"
														value="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance$PlaceProvider" />
													<fr:property name="sortBy"
														value="spaceInformation.presentationName" />
													<fr:property name="format"
														value="${spaceInformation.presentationName}" />
												</fr:slot>
											</fr:schema>
											<fr:layout name="tabular">
												<fr:property name="classes"
													value="tstyle2 thlight thleft mtop05 mbottom05" />
											</fr:layout>
										</fr:edit>
									</td>
									<td><html:submit>
											<bean:message key="button.entrance"
												bundle="LIBRARY_RESOURCES" />
										</html:submit>
									</td>
								</tr>
							</table>
						</fr:form>
					</logic:notPresent>
				</logic:present>

			</logic:present></td>

		<td style="vertical-align: top;"><logic:present name="attendance"
				property="library">
				<div style="float: right; margin-left: 120px;">
					<bean:define id="libraryId" name="attendance"
						property="library.externalId" />
					<h3 class="mtop2">
						<bean:message key="label.attendances" bundle="LIBRARY_RESOURCES" />
					</h3>

					<fr:view name="attendance" property="library.containedSpacesSet">
						<fr:schema bundle="LIBRARY_RESOURCES"
							type="net.sourceforge.fenixedu.domain.space.Space">
							<fr:slot name="spaceInformation.presentationName"
								key="label.space.name" />
							<fr:slot name="currentAttendance" key="label.occupant"
								layout="null-as-label">
								<fr:property name="label" value=" - " />
								<fr:property name="subLayout" value="values" />
								<fr:property name="subSchema" value="library.personWithUsername" />
							</fr:slot>
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="order(view)" value="0" />
							<fr:property name="link(view)"
								value="<%="/libraryOperator.do?method=searchPerson&libraryId=" + libraryId%>" />
							<fr:property name="key(view)" value="link.view" />
							<fr:property name="param(view)"
								value="currentAttendance.personIstUsername/personIstUsername" />
							<fr:property name="visibleIf(view)" value="withLibraryOccupation" />
							<fr:property name="order(exit)" value="1" />
							<fr:property name="link(exit)"
								value="<%="/libraryOperator.do?method=exitPlace&libraryId=" + libraryId%>" />
							<fr:property name="key(exit)" value="link.exit" />
							<fr:property name="param(exit)"
								value="currentAttendance.externalId/attendanceId" />
							<fr:property name="visibleIf(exit)" value="withLibraryOccupation" />
							<fr:property name="sortBy"
								value="spaceInformation.presentationName" />
							<fr:property name="classes"
								value="tstyle2 thlight thleft tdcenter mtop0" />
						</fr:layout>
					</fr:view>
				</div>
			</logic:present>
		</td>
	</tr>
</table>