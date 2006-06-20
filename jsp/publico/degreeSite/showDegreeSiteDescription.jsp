<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="institutionUrl" type="java.lang.String">
	<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
</bean:define>
<div class="breadcumbs">
	<a href="<%= institutionUrl %>">
		<bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/>
	</a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String">
		<bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
		<bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/>
	</bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>">
		<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/>
	</a>
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
		<bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.title.coordinator"/>&nbsp; 

			<logic:notEmpty name="responsibleCoordinatorTeacher" property="person.enderecoWeb">
				<bean:define id="homepage" name="responsibleCoordinatorTeacher" property="person.enderecoWeb"/>
				<a href=" <%= homepage %>"><bean:write name="responsibleCoordinatorTeacher" property="person.nome"/></a>
			</logic:notEmpty>		
							
			<logic:empty name="responsibleCoordinatorTeacher" property="person.enderecoWeb">
				<logic:notEmpty name="responsibleCoordinatorTeacher" property="person.email">
					<bean:define id="email" name="responsibleCoordinatorTeacher" property="person.email"/>	
						<a href="mailto: <%= email %>"><bean:write name="responsibleCoordinatorTeacher" property="person.nome"/></a>											
				</logic:notEmpty>						
				<logic:empty name="responsibleCoordinatorTeacher" property="person.email">
					<bean:write name="responsibleCoordinatorTeacher" property="person.nome"/>											
				</logic:empty>						
			</logic:empty>		
							
			<br/>
	</logic:iterate>
</logic:notEmpty>

<logic:notPresent name="infoDegreeInfo">
	<p><em><bean:message bundle="DEFAULT" key="error.public.DegreeInfoNotPresent"/></em></p>
</logic:notPresent>
<logic:present name="infoDegreeInfo">
	<!-- DESCRIPTION -->
	<logic:notEmpty name="infoDegreeInfo" property="description" >			 	
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.overview"/></h2>
		<p><bean:write name="infoDegreeInfo" property="description" filter="false"/></p>
	</logic:notEmpty>
	
	<div class="col_right">
		<!-- DEADLINES -->	
		<logic:empty name="infoDegreeInfo" property="schoolCalendar">
		<logic:empty name="infoDegreeInfo" property="candidacyPeriod">
		<logic:empty name="infoDegreeInfo" property="selectionResultDeadline">
		<logic:empty name="infoDegreeInfo" property="enrolmentPeriod">
			<bean:define id="doNotRenderDeadLines" value="doNotRenderDeadLines"/>
		</logic:empty>	
		</logic:empty>	
		</logic:empty>	
		</logic:empty>
		<logic:notPresent name="doNotRenderDeadLines">
					<table class="box" cellspacing="0">
						<tr>
							<td class="box_header">
								<strong>
									<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.deadlines"/>
								</strong>
							</td>
						</tr>						
						<tr>
							<td class="box_cell">
								<p>
									<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.schoolCalendar"/>: 
									<em>
										<bean:write name="infoDegreeInfo" property="schoolCalendar" filter="false"/>										
									</em>
								</p>
							</td>
						</tr>
						<tr>
							<td class="box_cell">
								<p>
									<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.candidacyPeriod"/>: 
									<em>
										<bean:write name="infoDegreeInfo" property="candidacyPeriod" filter="false"/>
									</em>
								</p>
							</td>
						</tr>
						<tr>
							<td class="box_cell">
								<p>
									<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.selectionResultDeadline"/>: 
									<em>
										<bean:write name="infoDegreeInfo" property="selectionResultDeadline" filter="false"/>
									</em>
								</p>
							</td>
						</tr>
						<tr>
							<td class="box_cell">
								<p>
									<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.enrolmentPeriod"/>: 
									<em>
										<bean:write name="infoDegreeInfo" property="enrolmentPeriod" filter="false"/>
									</em>
								</p>
							</td>
						</tr>
			<logic:empty name="infoDegreeInfo" property="additionalInfo" >
				<logic:empty name="infoDegreeInfo" property="links" >
					</table>
				</logic:empty>
			</logic:empty>
		</logic:notPresent>
		
		<!-- ADDITIONAL INFO -->
		<logic:notEmpty name="infoDegreeInfo" property="additionalInfo" >	
			<logic:present name="doNotRenderDeadLines">
				<table class="box" cellspacing="0">	
			</logic:present>
				<tr>
					<td class="box_header"><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.additionalInfo"/></strong></td>
				</tr>						
				<tr>
					<td class="box_cell"><p><bean:write name="infoDegreeInfo" property="additionalInfo" filter="false"/></p></td>						
				</tr>
			<logic:empty name="infoDegreeInfo" property="links" >
				</table>
			</logic:empty>
		</logic:notEmpty>
		
		<!-- LINKS -->	
		<logic:notEmpty name="infoDegreeInfo" property="links" >
			<logic:present name="doNotRenderDeadLines">
				<logic:empty name="infoDegreeInfo" property="additionalInfo" >	
					<table class="box" cellspacing="0">	
				</logic:empty>
			</logic:present>
						<tr>
							<td class="box_header"><strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.links"/></strong></td>
						</tr>
						<tr>
							<td class="box_cell"><p><bean:write name="infoDegreeInfo" property="links" filter="false"/></p></td>	
						</tr>
					</table>
		</logic:notEmpty>
	</div>
	
	<!-- HISTORY -->
	<logic:notEmpty name="infoDegreeInfo" property="history" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.history"/></h2>
		<p><bean:write name="infoDegreeInfo" property="history" filter="false"/></p>
	</logic:notEmpty>

	<!-- OBJECTIVES -->
	<logic:notEmpty name="infoDegreeInfo" property="objectives" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.objectives"/></h2>
	 	<p><bean:write name="infoDegreeInfo" property="objectives" filter="false"/></p>
	</logic:notEmpty>
				  
	<!-- DESIGNED FOR -->
	<logic:notEmpty name="infoDegreeInfo" property="designedFor" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION"  key="public.degree.information.label.designedFor"/></h2>
	 	<p><bean:write name="infoDegreeInfo" property="designedFor" filter="false"/></p>
	</logic:notEmpty>
				  
	<!-- PROFESSIONAL EXITS -->
	<logic:notEmpty name="infoDegreeInfo" property="professionalExits" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.professionalExits"/></h2>
		<p><bean:write name="infoDegreeInfo" property="professionalExits" filter="false"/></p>  
	</logic:notEmpty>

	<!-- OPERATIONAL REGIME -->
	<logic:notEmpty name="infoDegreeInfo" property="operationalRegime" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.operationalRegime"/></h2>
		<p><bean:write name="infoDegreeInfo" property="operationalRegime" filter="false"/></p>  
	</logic:notEmpty>

	<!-- GRATUITY -->
	<logic:notEmpty name="infoDegreeInfo" property="gratuity" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.gratuity"/></h2>
		<p><bean:write name="infoDegreeInfo" property="gratuity" filter="false"/></p>  
	</logic:notEmpty>

	<logic:empty name="infoDegreeInfo" property="description">
	<logic:empty name="infoDegreeInfo" property="history">
	<logic:empty name="infoDegreeInfo" property="objectives">
	<logic:empty name="infoDegreeInfo" property="designedFor">
	<logic:empty name="infoDegreeInfo" property="professionalExits">
	<logic:empty name="infoDegreeInfo" property="operationalRegime">
	<logic:empty name="infoDegreeInfo" property="gratuity">	
	<logic:empty name="infoDegreeInfo" property="additionalInfo">
	<logic:empty name="infoDegreeInfo" property="links">
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
