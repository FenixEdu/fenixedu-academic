<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

	
<!--        ---        -->
<!-- Library Selection -->
<!--        ---        -->
<table class="thmiddle">				
	<tr>
		<td>
			<div style="float: left;">
				<h2><bean:message key="portal.library.operator"	bundle="PORTAL_RESOURCES" /></h2>
			
				<fr:form action="/libraryOperator.do?method=selectLibrary">
					<fr:edit id="select.library.bean" name="selectLibraryBean">
						<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.library.SelectLibraryBean">
							<fr:slot name="library" key="label.library"	layout="menu-select-postback" required="true">
								<fr:property name="providerClass" 
								value="net.sourceforge.fenixedu.presentationTier.renderers.providers.library.LibraryProvider" />
								<fr:property name="format" value="Biblioteca ${spaceBuilding.nameWithCampus}" />
								<fr:property name="destination" value="postback" />
							</fr:slot>
							
							<%--
							<logic:present name="selectLibraryBean" property="library">
								<fr:slot name="library.spaceInformation.capacity" key="label.library.max.capacity" readOnly="true" />
								<fr:slot name="library.attendancesCount" key="label.library.ocupation" readOnly="true" />
							</logic:present>
							--%>
				
						</fr:schema>
				
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle2 thlight thleft mbottom0" />
							<fr:property name="columnClasses" value=",,tdclear tderror1" />
						</fr:layout>
				
						<fr:destination name="postback" path="/libraryOperator.do?method=selectLibrary" />
					</fr:edit>
				</fr:form>
			</div>
		</td>
		<td>
			<div style="float: right; margin-left: 10px; margin-top: 15px;">
					<logic:present name="selectLibraryBean" property="library">
					<bean:define id="libraryIdInternal" name="selectLibraryBean" property="library.idInternal" />
					<bean:define id="graph" type="java.lang.String"><%= request.getContextPath() %>/library/libraryOperator.do?method=createAreaXYChart&libraryIdInternal=<%= libraryIdInternal %></bean:define>
					<html:img align="middle" src="<%= graph %>"/>
				</logic:present>
			</div>
		</td>
	</tr>
</table>

<div style="clear: both;"> </div>


<logic:present name="selectLibraryBean" property="library">
	<table class="tdtop">				
		<tr>
			<td>
				<div style="float: left;">
					<bean:define id="libraryIdInternal" name="selectLibraryBean" property="library.idInternal" />
					
					<!--        ---        -->
					<!--   Person Search   -->
					<!--        ---        -->
				
					<h3 class="mtop2"><bean:message key="label.find.person"	bundle="LIBRARY_RESOURCES" /></h3>
				
					<fr:form action="/libraryOperator.do?method=searchPerson">
						<table class="thmiddle, tdmiddle">
							<tr>
								<td>
									<fr:edit id="search.person" name="selectLibraryBean">
										<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.library.SelectLibraryBean">
											<fr:slot name="personId" key="label.search.person">
												<fr:property name="size" value="25" />
												<fr:property name="destination" value="postback" />
											</fr:slot>
										</fr:schema>
							
										<fr:layout name="tabular">
											<fr:property name="classes" value="tstyle5 thlight mtop0 dinline thmiddle" />
											<fr:property name="columnClasses" value=",,tdclear tderror1" />
										</fr:layout>
							
										<fr:destination name="postback" path="/libraryOperator.do?method=searchPerson" />
									</fr:edit>
								</td>
								<td width="180">
									<html:submit>
										<bean:message key="button.search" bundle="LIBRARY_RESOURCES" />
									</html:submit>
								</td>
							</tr>
						</table>
					</fr:form>
				
					<!--           ---           -->
					<!--   Person Presentation   -->
					<!--           ---           -->
				
					<logic:present name="selectLibraryBean" property="person">
						<bean:define id="personIstUsername" name="selectLibraryBean" property="person.istUsername" /> 
						<bean:define id="personID" name="selectLibraryBean" property="person.istUsername" /> 
						<bean:define id="selectLibraryBean" name="selectLibraryBean" type="net.sourceforge.fenixedu.dataTransferObject.library.SelectLibraryBean"/> 
						
						<p/>
						<table class="tstyle2 thlight thleft mtop05 mbottom05" width="520">				
							<tr>
								<th></th>
								<td>
									<html:img align="middle"
										src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid="+personID%>"
										altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto" />
								</td>
							</tr>
							<tr>	
								<th><bean:message key="label.person.name" bundle="LIBRARY_RESOURCES"/></th>
								<td><bean:write name="selectLibraryBean" property="person.name" /></td>
							</tr>
							<tr>	
								<th><bean:message key="label.person.istUsername" bundle="LIBRARY_RESOURCES"/></th>
								<td><bean:write name="selectLibraryBean" property="person.istUsername" /></td>
							</tr>
							<% if(selectLibraryBean.getPerson().getStudent() != null && selectLibraryBean.getPerson().getStudent().getActiveRegistrations().size() > 0) { %>
								<tr>
									<th><bean:message key="label.person.degree" bundle="LIBRARY_RESOURCES"/></th>
									<td>
										<% for (int i = 0; i < selectLibraryBean.getPerson().getStudent().getActiveRegistrations().size(); i++) { %>
											<bean:define id="courseInfo" value="<%=selectLibraryBean.getPerson().getStudent().getActiveRegistrations().get(i).getDegree().getPresentationName() + " (" + 
											selectLibraryBean.getPerson().getStudent().getActiveRegistrations().get(i).getDegree().getSigla() + ")"%>"/>
											
											<% if( selectLibraryBean.getPerson().getStudent().getActiveRegistrations().size() > 1 ) { %>
												<p><bean:write name="courseInfo"/></p>
											<% } else { %>
												<bean:write name="courseInfo"/>
											<% } %>
											
										<% } %>
									</td>
								</tr>
							<% } %>
							<% if(selectLibraryBean.isPersonInside()) { %>
								<tr>
									<th><bean:message key="label.entrance.time" bundle="LIBRARY_RESOURCES"/></th>
									<td>
										<bean:define id="entranceTime" value="<%=selectLibraryBean.getPersonEntranceTime()%>"/>
										<bean:write name="entranceTime"/>
									</td>
								</tr>
							<% } %>
						</table>
						
				
						<!--               ---               -->
						<!--   Library Card Number (MODIFY)  -->
						<!--               ---               -->
					
						<logic:equal name="selectLibraryBean" property="editableCardNumber" value="true">
							<fr:form
							action="<%= "/libraryOperator.do?method=editPersonLibraryCardNumber&" + "personIstUsername=" + personIstUsername + "&libraryIdInternal=" + libraryIdInternal %>">
								<table class="mvert15 thmiddle tdmiddle">
									<tr>
										<td>
											<fr:view name="selectLibraryBean" property="person">
												<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.library.SelectLibraryBean" bundle="LIBRARY_RESOURCES">
													<fr:slot name="libraryCard.cardNumber" key="label.person.library.number" />
												</fr:schema>
												<fr:layout name="tabular">
													<fr:property name="classes" value="tstyle2 thlight mtop0 mbottom0" />
												</fr:layout>
											</fr:view>
										</td>
										
										<td></td><td></td><td>
											<html:submit>
												<bean:message key="button.modify" bundle="LIBRARY_RESOURCES" />
											</html:submit>
										</td>
									</tr>
								</table>
							</fr:form>
						</logic:equal>
			
						<!--                ---                -->
						<!--   Library Card Number (ADD/EDIT)  -->
						<!--                ---                -->
						
						<logic:equal name="selectLibraryBean" property="editCardNumberError" value="true">
							<p><em class="error0 mtop5"><bean:message key="error.edit.library.card.number"	bundle="LIBRARY_RESOURCES" /></em></p>
						</logic:equal>
					
						<logic:equal name="selectLibraryBean" property="editableCardNumber" value="false">
							<fr:form action="<%= "/libraryOperator.do?&personIstUsername=" + personIstUsername + "&libraryIdInternal=" + libraryIdInternal %>">
								<input type="hidden" name="method" />
								
								<table class="mvert15 thmiddle tdmiddle">
									<tr>
										<td>
											<fr:edit id="set.library.card.number" name="selectLibraryBean">
												<fr:schema bundle="LIBRARY_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.library.SelectLibraryBean">
													<fr:slot name="libraryCardNumber" key="label.person.library.number" required="true">
														<fr:property name="size" value="11" />
														<fr:property name="destination" value="postback" />
													</fr:slot>
												</fr:schema>
						
												<fr:layout name="tabular">
													<fr:property name="classes" value="tstyle5 thlight mtop0 mbottom0 dinline" />
													<fr:property name="columnClasses" value=",,tdclear tderror1" />
												</fr:layout>
						
												<fr:destination name="postback"	path="/libraryOperator.do?method=setPersonLibraryCardNumber" />
											</fr:edit>
										</td>
										
										<td>
											<html:submit onclick="this.form.method.value='setPersonLibraryCardNumber';">
												<bean:message key="button.save" bundle="LIBRARY_RESOURCES" />
											</html:submit>
											
											<logic:present name="selectLibraryBean" property="person.libraryCard.cardNumber">
												<html:cancel onclick="this.form.method.value='cancelEditPersonLibraryCardNumber';">
													<bean:message key="button.cancel" bundle="LIBRARY_RESOURCES" />
												</html:cancel>
											</logic:present>
										</td>
									</tr>
								</table>
							</fr:form>
						</logic:equal>
						
						<!--         ---         -->
						<!--   Entrance / Exit   -->
						<!--         ---         -->
						
						<p>
						<logic:equal name="selectLibraryBean" property="personInside" value="true">
							<fr:form
								action="<%= "/libraryOperator.do?method=studentExit&" + "personIstUsername=" + personIstUsername + "&libraryIdInternal=" + libraryIdInternal %>">
								<html:submit>
									<bean:message key="button.exit" bundle="LIBRARY_RESOURCES" />
								</html:submit>
							</fr:form>
						</logic:equal> 
							
						<logic:equal name="selectLibraryBean" property="personInside" value="false">
							<fr:form
								action="<%= "/libraryOperator.do?method=studentEntrance&" + "personIstUsername=" + personIstUsername + "&libraryIdInternal=" + libraryIdInternal %>">
								<% if(selectLibraryBean.getLibrary().hasEnoughSpace()) { %>
									<html:submit>
										<bean:message key="button.entrance" bundle="LIBRARY_RESOURCES" />
									</html:submit>
								<% } else { %>
									<html:submit disabled="true">
										<bean:message key="button.entrance" bundle="LIBRARY_RESOURCES" />
									</html:submit>
								<% } %>
			
							</fr:form>
						</logic:equal>
						</p>
					</logic:present>
					
					<!--       ---       -->
					<!--   Search Error  -->
					<!--       ---       -->
					
					<logic:present name="selectLibraryBean" property="personId">
						<logic:notPresent name="selectLibraryBean" property="person">
							<em class="error0"><bean:message key="error.search.person"	bundle="LIBRARY_RESOURCES" /></em>
						</logic:notPresent>
					</logic:present>
				</div>
					
				</td>
				<!--         ---         -->
				<!--   Attendances List  -->
				<!--         ---         -->
				<td>				
				
				<logic:greaterThan name="selectLibraryBean" property="attendancesCount" value="0">
					<div style="float: right; margin-left: 120px;">	
						<h3 class="mtop2"><bean:message key="label.attendances"	bundle="LIBRARY_RESOURCES" /></h3>
						
						<fr:view name="selectLibraryBean" property="attendances">
							<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.library.SelectLibraryBean" bundle="LIBRARY_RESOURCES">
								<fr:slot name="person.istUsername" key="label.person.istUsername"/>
								<fr:slot name="person.firstAndLastName" key="label.person.name"/>
								<%-- <fr:slot name="entranceInfo" key="label.entrance.time"/> --%>
							</fr:schema>
							
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle2 thlight thleft tdcenter mtop0" />
								<fr:property name="sortBy" value="person.istUsername" />
								<fr:property name="link(view)" value="<%= "/libraryOperator.do?method=showPerson&personIstUsername=%s&libraryIdInternal=" + libraryIdInternal %>"/>
						        <fr:property name="key(view)" value="link.view"/>
					            <fr:property name="param(view)" value="person.istUsername/personIstUsername"/>
							</fr:layout>
						</fr:view>
					</div>
				</logic:greaterThan>
			</td>
		</tr>
	</table>
</logic:present>

<div style="clear: both;"> </div>
