<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="org.fenixedu.bennu.core.domain.Bennu"%>
<%@ page language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>

<html:xhtml />

<h2><bean:message key="portal.library.operator" bundle="PORTAL_RESOURCES" /></h2>

<script type="text/javascript">
	function setManualSearch() {
		document.getElementById('cardSearch').style.display = 'none';
		document.getElementById('manualSearch').style.display = 'block';
	}

	function setCardSearch() {
		document.getElementById('manualSearch').style.display = 'none';
		document.getElementById('cardSearch').style.display = 'block';
	}
</script>

<table style="width: 90%">
	<tr>
		<td style="vertical-align: bottom;"><fr:form action="/libraryOperator.do?method=selectLibrary">
				<fr:edit id="attendance" name="attendance">
					<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
						<fr:slot name="library" key="label.library" layout="menu-select-postback" required="true">
							<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.library.LibraryProvider" />
							<fr:property name="format" value="Biblioteca \${parent.parent.presentationName}" />
							<fr:property name="destination" value="postback" />
						</fr:slot>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mbottom0" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>
					<fr:destination name="postback" path="/libraryOperator.do?method=selectLibrary" />
				</fr:edit>
			</fr:form>
		</td>
		<td>
			<div style="float: right; margin-left: 10px; margin-top: 15px;">
				<logic:present name="attendance" property="library">
					 				
					<bean:message key="label.library.ocupation" />: <%= net.sourceforge.fenixedu.domain.space.SpaceUtils.currentAttendaceCount(((net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance)request.getAttribute("attendance")).getLibrary()) %> / ${attendance.library.allocatableCapacity}
				</logic:present>
			</div>
		</td>
</table>

<logic:present name="attendance" property="library">

	<bean:define id="libraryId" name="attendance" property="library.externalId" />
	<table style="width: 90%">
		<tr>
			<td style="vertical-align: top;">
				<h3 class="mtop2"><bean:message key="label.find.person" bundle="LIBRARY_RESOURCES" /></h3>
				<div id="cardSearch" style="<%=((LibraryAttendance) request.getAttribute("attendance")).getPersonName() != null ? "display: none" : ""%>">
					<fr:form action="/libraryOperator.do?method=searchPerson">
						<table>
							<tr><td><a href="#" onclick="setManualSearch();"><bean:message key="link.manualSearch" bundle="LIBRARY_RESOURCES" /> </a></td></tr>
							<tr>
								<td><fr:edit id="search.person" name="attendance">
										<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
											<fr:slot name="personId" key="label.search.person" />
										</fr:schema>
										<fr:layout name="tabular">
											<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05" />
											<fr:property name="columnClasses" value=",,tdclear tderror1" />
										</fr:layout>
									</fr:edit></td>
								<td><html:submit>
										<bean:message key="button.search" bundle="LIBRARY_RESOURCES" />
									</html:submit></td>
							</tr>
						</table>
					</fr:form>
				</div>
				<div id="manualSearch" style="<%=((LibraryAttendance) request.getAttribute("attendance")).getPersonName() == null ? "display: none" : ""%>">
					<a href="#" onclick="setCardSearch();"><bean:message key="link.cardSearch" bundle="LIBRARY_RESOURCES" /> </a>
					<fr:form action="/libraryOperator.do?method=advancedSearch">
						<fr:edit id="advanced.search" name="attendance">
							<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
								<fr:slot name="personType" key="label.person.type" layout="menu-select">
									<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance$RoleTypeProvider" />
									<fr:property name="defaultText" value="" />
								</fr:slot>
								<fr:slot name="personName" key="label.person.name">
									<fr:property name="size" value="50" />
								</fr:slot>
							</fr:schema>
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05" />
								<fr:property name="columnClasses" value=",,tdclear tderror1" />
							</fr:layout>
						</fr:edit>
						<html:submit>
							<bean:message key="button.search" bundle="LIBRARY_RESOURCES" />
						</html:submit>
					</fr:form>
				</div> <logic:notEmpty name="attendance" property="matches">
					<bean:define id="url">
						<%
						    LibraryAttendance attendance = (LibraryAttendance) request.getAttribute("attendance");
									StringBuilder url = new StringBuilder();
									url.append("/library/libraryOperator.do?method=advancedSearch&libraryId=");
									url.append(attendance.getLibrary().getExternalId());
									if (attendance.getPersonType() != null) {
									    url.append("&personType=");
									    url.append(attendance.getPersonTypeName());
									}
									if (attendance.getPersonName() != null) {
									    url.append("&personName=");
									    url.append(attendance.getPersonName());
									}
									out.write(url.toString());
						%>
					</bean:define>
					<p>
						<bean:message key="label.pages" />
						:
						<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" />
					</p>
					<fr:view name="attendance" property="matches">
						<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
							<fr:slot name="istUsername" key="label.person.istUsername" />
							<fr:slot name="name" key="label.person.name" />
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05" />

							<fr:property name="link(view)" value="<%= "/libraryOperator.do?method=searchPerson&libraryId=" + libraryId %>" />
							<fr:property name="param(view)" value="istUsername/personIstUsername" />
							<fr:property name="key(view)" value="link.view" />
							<fr:property name="bundle(view)" value="LIBRARY_RESOURCES" />
						</fr:layout>
					</fr:view>

					<logic:notEqual name="numberOfPages" value="1">
						<p class="mtop15">
							<bean:message key="label.pages" />
							:
							<cp:collectionPages url="<%= url %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" />
						</p>
					</logic:notEqual>
				</logic:notEmpty> <logic:present name="attendance" property="person">
					<fr:view name="attendance">
						<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
							<fr:slot name="person" key="label.person.picture" layout="view-as-image">
								<fr:property name="classes" value="column3" />
								<fr:property name="useParent" value="true" />
								<fr:property name="moduleRelative" value="false" />
								<fr:property name="contextRelative" value="true" />
								<fr:property name="imageFormat" value="/person/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=\${person.istUsername}" />
							</fr:slot>
							<fr:slot name="person.name" key="label.person.name" />
							<fr:slot name="person.istUsername" key="label.person.istUsername" />
							<fr:slot name="person.emailForSendingEmails" layout="null-as-label" key="label.card.person.email" />
							<fr:slot name="person.mobile" key="label.person.mobile" />
							
							<%-- <logic:notEmpty name="attendance" property="giafProfessionalDataSet">
								<logic:iterate id="giafProfessionalDataSet" name="attendance" property="giafProfessionalDataSet" indexId="i">
									<bean:define id="labelId" name="giafProfessionalDataSet" property="professionalCategory.categoryType"/>
									<fr:slot name="<%="giafProfessionalDataSet["+ i+"]"%>" key="<%=labelId.toString() %>" bundle="ENUMERATION_RESOURCES">
										<fr:property name="format" value="\${contractSituation.name}" />
										<logic:notEmpty name="giafProfessionalDataSet" property="personProfessionalData.person.workingPlaceUnitForAnyRoleType">
											<fr:property name="format" value="\${contractSituation.name} <br/> \${personProfessionalData.person.workingPlaceUnitForAnyRoleType.presentationName}" />
											<fr:property name="escaped" value="false" />
										</logic:notEmpty>
									</fr:slot>
								</logic:iterate>
							</logic:notEmpty> --%>
							<logic:present name="attendance" property="externalTeacherUnit">
								<fr:slot name="externalTeacherUnit.presentationName" key="label.person.externalTeacher" />
							</logic:present>
							<logic:present name="attendance" property="researcherUnit">
								<fr:slot name="researcherUnit.presentationName" key="label.person.researcher" />
							</logic:present>
							<logic:present name="attendance" property="employeeUnit">
								<fr:slot name="employeeUnit.presentationName" key="label.person.employee" />
							</logic:present>
							<logic:present name="attendance" property="person.externalResearchContract">
								<fr:slot name="person.externalResearchContract.workingUnit.presentationName" key="label.person.externalResearcher" />
							</logic:present>
							<logic:present name="attendance" property="studentRegistration">
								<fr:slot name="studentRegistration.degree.presentationName" key="label.person.student" />
							</logic:present>
							<logic:present name="attendance" property="phdProcess">
								<fr:slot name="phdProcess.phdProgram.name" key="label.person.phdStudent" />
							</logic:present>
							<logic:present name="attendance" property="alumniRegistration">
								<fr:slot name="alumniRegistration.degree.presentationName" key="label.person.alumni" />
							</logic:present>
							<logic:present name="attendance" property="invitation">
								<fr:slot name="invitation.endDate" key="label.person.invitationValidUntil"/>
								<fr:slot name="invitation.unit.name" key="label.card.unitName"/>
								<fr:slot name="invitation.responsiblePerson.name" key="label.person.responsiblePerson"/>
								<fr:slot name="invitation.responsiblePerson.istUsername" key="label.person.responsiblePerson.istID"/>
							</logic:present>
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05" />
						</fr:layout>
					</fr:view>

					<logic:present name="attendance" property="personAttendance">
						<fr:form action="/libraryOperator.do?method=exitPlace">
							<table>
								<tr>
									<td><fr:edit id="person.selectPlace" name="attendance">
											<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
												<fr:slot name="personAttendance.occupationDesctiption" key="label.person.place" readOnly="true" />
												<fr:slot name="personAttendance.entranceTime" key="label.person.entranceTime" readOnly="true" />
											</fr:schema>
											<fr:layout name="tabular">
												<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05" />
											</fr:layout>
										</fr:edit></td>
									<td><html:submit>
											<bean:message key="button.exit" bundle="LIBRARY_RESOURCES" />
										</html:submit></td>
								</tr>
							</table>
						</fr:form>
					</logic:present>
					<logic:equal name="attendance" property="full" value="false">
						<logic:notPresent name="attendance" property="personAttendance">
							<fr:form action="/libraryOperator.do?method=selectPlace">
								<table>
									<tr>
										<td><fr:edit id="person.selectPlace" name="attendance">
												<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance">
													<fr:slot name="selectedSpace" key="label.person.place" layout="menu-select">
														<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.library.LibraryAttendance$PlaceProvider" />
														<fr:property name="sortBy" value="presentationName" />
														<fr:property name="format" value="\${presentationName}" />
														<fr:property name="defaultText" value="label.space.libraryResourceNone" />
														<fr:property name="bundle" value="LIBRARY_RESOURCES" />
														<fr:property name="key" value="true" />
													</fr:slot>
												</fr:schema>
												<fr:layout name="tabular">
													<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05" />
												</fr:layout>
											</fr:edit>
										</td>
										<td><html:submit>
												<bean:message key="button.entrance" bundle="LIBRARY_RESOURCES" />
											</html:submit>
										</td>
									</tr>
								</table>
							</fr:form>
						</logic:notPresent>
					</logic:equal>
				</logic:present>
				
			</td>
			
			<td style="vertical-align: top;">
				<div style="float: right; margin-left: 120px;">
					<h3 class="mtop2"><bean:message key="label.attendances" bundle="LIBRARY_RESOURCES" />
					</h3>
					
					<fr:view name="attendance" property="libraryAttendances">
						<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.domain.space.SpaceAttendances">
							<fr:slot name="person.istUsername" key="label.person.istUsername" />
							<fr:slot name="person.firstAndLastName" key="label.person.name" />
							<fr:slot name="occupationDesctiption" key="label.space.name" />
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="order(view)" value="0" />
							<fr:property name="link(view)" value="<%="/libraryOperator.do?method=searchPerson&libraryId=" + libraryId %>" />
							<fr:property name="key(view)" value="link.view" />
							<fr:property name="param(view)" value="personIstUsername/personIstUsername" />
							<fr:property name="order(exit)" value="1" />
							<fr:property name="link(exit)" value="<%="/libraryOperator.do?method=exitPlace&libraryId=" + libraryId %>" />
							<fr:property name="key(exit)" value="link.exit" />
							<fr:property name="param(exit)" value="externalId/attendanceId" />
							<fr:property name="sortBy" value="occupationDesctiption,person.name" />
							<fr:property name="classes" value="tstyle2 thlight thleft tdcenter mtop0" />
						</fr:layout>
					</fr:view>
				</div>
			</td>
		</tr>
	</table>
</logic:present>