<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>

<div class="breadcumbs mvert0">
	<a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<bean:write name="degree" property="sigla"/>
	</logic:present>
</div>

<!-- COURSE NAME -->

<h1>
	<logic:equal name="degree" property="bolonhaDegree" value="true">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="bolonhaDegreeType.name"/>
	</logic:equal>
	<logic:equal name="degree" property="bolonhaDegree" value="false">
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/>
	</logic:equal>
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in"/>
	<logic:present name="inEnglish">
		<logic:equal name="inEnglish" value="false">
			<bean:write name="degree" property="nome"/>
		</logic:equal>
		<logic:equal name="inEnglish" value="true">
			<bean:write name="degree" property="nameEn"/>
		</logic:equal>
	</logic:present>
</h1>

<!-- CAMPUS -->
<logic:iterate id="campusIter" name="campus">
 	<h2 class="greytxt">
		<bean:write name="campusIter" property="name"/>
	</h2>
</logic:iterate>	

<!-- COORDINATORS -->
<logic:notEmpty name="responsibleCoordinatorsTeachers">
	<p><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.coordinators"/> <bean:write name="executionYear" property="year"/></strong></p>
	<logic:iterate id="responsibleCoordinatorTeacher" name="responsibleCoordinatorsTeachers">
		<p>
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.title.coordinator"/>&nbsp; 
			<%-- <bean:write name="responsibleCoordinatorTeacher" property="category.name.content"/>&nbsp; --%>

			<%-- F�NIX HOMEPAGE --%>	
			<logic:notEmpty name="responsibleCoordinatorTeacher" property="person.homepage">
				<logic:equal name="responsibleCoordinatorTeacher" property="person.homepage.activated" value="true">
					<bean:define id="userUId" name="responsibleCoordinatorTeacher" property="person.user.userUId" type="java.lang.String"/>
					<bean:define id="homepage" value="<%="../homepage/" + userUId%>" type="java.lang.String"/>
					<a target="_blank" href="<%= homepage %>"><bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/></a>										
				</logic:equal>

				<logic:equal name="responsibleCoordinatorTeacher" property="person.homepage.activated" value="false">
					<%-- PERSONAL HOMEPAGE --%>	
					<logic:notEmpty name="responsibleCoordinatorTeacher" property="person.enderecoWeb">
						<logic:equal name="responsibleCoordinatorTeacher" property="person.availableWebSite" value="true">
							<bean:define id="homepage" name="responsibleCoordinatorTeacher" property="person.enderecoWeb" type="java.lang.String"/>
							<a target="_blank" href="<%= homepage %>"><bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/></a>					
						</logic:equal>

						<logic:equal name="responsibleCoordinatorTeacher" property="person.availableWebSite" value="false">
							<%-- EMAIL OR JUST NAME --%>	
							<logic:notEmpty name="responsibleCoordinatorTeacher" property="person.email">
								<bean:define id="email" name="responsibleCoordinatorTeacher" property="person.email"/>	
								<a href="mailto: <%= email %>"><bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/></a>											
							</logic:notEmpty>						

							<%-- NAME --%>	
							<logic:empty name="responsibleCoordinatorTeacher" property="person.email">
								<bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/>											
							</logic:empty>
						</logic:equal>
					</logic:notEmpty>
				</logic:equal>
			</logic:notEmpty>

			<logic:empty name="responsibleCoordinatorTeacher" property="person.homepage">
				<%-- PERSONAL HOMEPAGE --%>	
				<logic:notEmpty name="responsibleCoordinatorTeacher" property="person.enderecoWeb">
					<logic:equal name="responsibleCoordinatorTeacher" property="person.availableWebSite" value="true">
						<bean:define id="homepage" name="responsibleCoordinatorTeacher" property="person.enderecoWeb" type="java.lang.String"/>
						<a target="_blank" href="<%= homepage %>"><bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/></a>					
					</logic:equal>

					<logic:equal name="responsibleCoordinatorTeacher" property="person.availableWebSite" value="false">
						<%-- EMAIL OR JUST NAME --%>	
						<logic:notEmpty name="responsibleCoordinatorTeacher" property="person.email">
							<bean:define id="email" name="responsibleCoordinatorTeacher" property="person.email"/>	
							<a href="mailto: <%= email %>"><bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/></a>											
						</logic:notEmpty>						

						<%-- NAME --%>	
						<logic:empty name="responsibleCoordinatorTeacher" property="person.email">
							<bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/>											
						</logic:empty>
					</logic:equal>
				</logic:notEmpty>
				
				<logic:empty name="responsibleCoordinatorTeacher" property="person.enderecoWeb">
					<%-- EMAIL OR JUST NAME --%>	
					<logic:notEmpty name="responsibleCoordinatorTeacher" property="person.email">
						<bean:define id="email" name="responsibleCoordinatorTeacher" property="person.email"/>	
						<a href="mailto: <%= email %>"><bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/></a>											
					</logic:notEmpty>						

					<%-- NAME --%>	
					<logic:empty name="responsibleCoordinatorTeacher" property="person.email">
						<bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/>											
					</logic:empty>
				</logic:empty>				
			</logic:empty>
		</p>
	</logic:iterate>
</logic:notEmpty>

<logic:notPresent name="degreeInfo">
	<p><em><bean:message bundle="DEFAULT" key="error.public.DegreeInfoNotPresent"/></em></p>
</logic:notPresent>
<logic:present name="degreeInfo">
	<!-- DESCRIPTION -->
	<logic:notEmpty name="degreeInfo" property="description" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.overview"/></h2>
		<p><bean:write name="degreeInfo" property="description.content" filter="false"/></p>
	</logic:notEmpty>
	
		<!-- DEADLINES -->	
		<logic:empty name="degreeInfo" property="schoolCalendar">
		<logic:empty name="degreeInfo" property="candidacyPeriod">
		<logic:empty name="degreeInfo" property="selectionResultDeadline">
		<logic:empty name="degreeInfo" property="enrolmentPeriod">
			<bean:define id="doNotRenderDeadLines" value="doNotRenderDeadLines"/>
		</logic:empty>	
		</logic:empty>	
		</logic:empty>	
		</logic:empty>
		<logic:notPresent name="doNotRenderDeadLines">
					<table class="box" cellspacing="0" style="float: right;">
						<tr>
							<td class="box_header">
								<strong>
									<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.deadlines"/>
								</strong>
							</td>
						</tr>						
						<logic:notEmpty name="degreeInfo" property="schoolCalendar">
							<tr>
									<td class="box_cell">
										<p>
											<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.schoolCalendar"/>: 
											<em>
												<bean:write name="degreeInfo" property="schoolCalendar.content" filter="false"/>
											</em>
										</p>
									</td>
							</tr>
						</logic:notEmpty>
						<logic:notEmpty name="degreeInfo" property="candidacyPeriod">
							<tr>
								<td class="box_cell">
									<p>
										<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.candidacyPeriod"/>: 
										<em>
											<bean:write name="degreeInfo" property="candidacyPeriod.content" filter="false"/>
										</em>
									</p>
								</td>
							</tr>
						</logic:notEmpty>
						<logic:notEmpty name="degreeInfo" property="selectionResultDeadline">						
							<tr>
								<td class="box_cell">
									<p>
										<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.selectionResultDeadline"/>: 
										<em>
											<bean:write name="degreeInfo" property="selectionResultDeadline.content" filter="false"/>
										</em>
									</p>
								</td>
							</tr>
						</logic:notEmpty>
						<logic:notEmpty name="degreeInfo" property="enrolmentPeriod">
							<tr>
								<td class="box_cell">
									<p>
										<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.enrolmentPeriod"/>: 
										<em>
											<bean:write name="degreeInfo" property="enrolmentPeriod.content" filter="false"/>
										</em>
									</p>
								</td>
							</tr>
						</logic:notEmpty>
			<logic:empty name="degreeInfo" property="additionalInfo" >
				<logic:empty name="degreeInfo" property="links" >
					</table>
				</logic:empty>
			</logic:empty>
		</logic:notPresent>
		
		<!-- ADDITIONAL INFO -->
		<logic:notEmpty name="degreeInfo" property="additionalInfo" >	
			<logic:present name="doNotRenderDeadLines">
				<table class="box" cellspacing="0" style="float: right;">	
			</logic:present>
				<tr>
					<td class="box_header">
						<strong>
							<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.additionalInfo"/>
						</strong>
					</td>
				</tr>						
				<tr>
					<td class="box_cell">
						<bean:write name="degreeInfo" property="additionalInfo.content" filter="false"/>
					</td>						
				</tr>
			<logic:empty name="degreeInfo" property="links" >
				</table>
			</logic:empty>
		</logic:notEmpty>
		
		<!-- LINKS -->	
		<logic:notEmpty name="degreeInfo" property="links" >
			<logic:present name="doNotRenderDeadLines">
				<logic:empty name="degreeInfo" property="additionalInfo" >	
					<table class="box" cellspacing="0" style="float: right;">	
				</logic:empty>
			</logic:present>
						<tr>
							<td class="box_header">
								<strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.links"/></strong>
							</td>
						</tr>
						<tr>
							<td class="box_cell">
								<bean:write name="degreeInfo" property="links.content" filter="false"/>
							</td>	
						</tr>
					</table>
		</logic:notEmpty>

	
	<!-- HISTORY -->
	<logic:notEmpty name="degreeInfo" property="history" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.history"/></h2>
		<bean:write name="degreeInfo" property="history.content" filter="false"/>
	</logic:notEmpty>

	<!-- OBJECTIVES -->
	<logic:notEmpty name="degreeInfo" property="objectives" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.objectives"/></h2>
	 	<bean:write name="degreeInfo" property="objectives.content" filter="false"/>
	</logic:notEmpty>
				  
	<!-- DESIGNED FOR -->
	<logic:notEmpty name="degreeInfo" property="designedFor" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.designedFor"/></h2>
	 	<bean:write name="degreeInfo" property="designedFor.content" filter="false"/>
	</logic:notEmpty>
				  
	<!-- PROFESSIONAL EXITS -->
	<logic:notEmpty name="degreeInfo" property="professionalExits" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.professionalExits"/></h2>
		<bean:write name="degreeInfo" property="professionalExits.content" filter="false"/>
	</logic:notEmpty>

	<!-- OPERATIONAL REGIME -->
	<logic:notEmpty name="degreeInfo" property="operationalRegime" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.operationalRegime"/></h2>
		<bean:write name="degreeInfo" property="operationalRegime.content" filter="false"/>
	</logic:notEmpty>

	<!-- GRATUITY -->
	<logic:notEmpty name="degreeInfo" property="gratuity" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.gratuity"/></h2>
		<bean:write name="degreeInfo" property="gratuity.content" filter="false"/>
	</logic:notEmpty>

	<logic:empty name="degreeInfo" property="description">
	<logic:empty name="degreeInfo" property="history">
	<logic:empty name="degreeInfo" property="objectives">
	<logic:empty name="degreeInfo" property="designedFor">
	<logic:empty name="degreeInfo" property="professionalExits">
	<logic:empty name="degreeInfo" property="operationalRegime">
	<logic:empty name="degreeInfo" property="gratuity">	
	<logic:empty name="degreeInfo" property="additionalInfo">
	<logic:empty name="degreeInfo" property="links">
	<logic:present name="doNotRenderDeadLines">
		<p><i><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="not.available" /></i></p>
	</logic:present>
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty>
	</logic:empty>
	</logic:empty>	
	</logic:empty>	
	</logic:empty>
	</logic:empty>

	<div class="clear"></div>
</logic:present>

<p style="margin-top: 2em;">
	<span class="px10">
		<i>
			<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.responsability.information.degree" />
		</i>
	</span>
</p>
