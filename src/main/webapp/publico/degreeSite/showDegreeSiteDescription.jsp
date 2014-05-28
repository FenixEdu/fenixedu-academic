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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<html:xhtml/>


<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>

<div class="breadcumbs mvert0">
	<a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
	<bean:define id="institutionUrlTeaching" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
	&nbsp;&gt;&nbsp;
	<a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
	<logic:present name="degree">
		&nbsp;&gt;&nbsp;
		<bean:write name="degree" property="sigla"/>
	</logic:present>
</div>

<!-- COURSE NAME -->

<h1>
	<logic:notEmpty name="degree" property="phdProgram">
		<bean:write name="degree" property="phdProgram.presentationName"/>
	</logic:notEmpty>
	<logic:empty name="degree" property="phdProgram">
		<bean:write name="degree" property="presentationName"/>
	</logic:empty>
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
			<logic:notEmpty name="responsibleCoordinatorTeacher" property="person.homepageWebAddress">
				<bean:define id="homepageWebAddres" name="responsibleCoordinatorTeacher" property="person.homepageWebAddress" type="java.lang.String"/>
					<a target="_blank" href="<%= homepageWebAddres %>"><bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/></a>
			</logic:notEmpty>
			<logic:empty name="responsibleCoordinatorTeacher" property="person.homepageWebAddress">
				<bean:write name="responsibleCoordinatorTeacher" property="person.nickname"/>
			</logic:empty>
			<br/>
		</p>
	</logic:iterate>
</logic:notEmpty>

<logic:notPresent name="degreeInfo">
	<p><em><bean:message bundle="DEFAULT" key="error.public.DegreeInfoNotPresent"/></em></p>
</logic:notPresent>
<logic:present name="degreeInfo">
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
        </logic:notPresent>
        <logic:present name="doNotRenderDeadLines">
            <logic:present name="announcements">
                <logic:notEmpty name="announcements">
                    <table class="box" cellspacing="0" style="float: right;">
                </logic:notEmpty>
            </logic:present>
        </logic:present>
                        <logic:present name="announcements">
                            <logic:notEmpty name="announcements">
                                <tr>
                                    <td class="box_header">
                                        <strong><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.latestAnnouncements"/></strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="box_cell">
                                        <bean:define id="degreeId" name="degree" property="externalId"/>
                                        <logic:iterate id="announcement" name="announcements">
                                            <p style="padding-bottom: 0.9em;">
                                                <fr:view name="announcement" property="lastModification" layout="no-time"/><br/> 
                                                
                                                <bean:define id="announcementId" name="announcement" property="externalId"/>
                                                <html:link page="<%= String.format("/showDegreeAnnouncements.do?method=viewAnnouncement&amp;degreeID=%s&amp;announcementId=%s", degreeId, announcementId) %>">
                                                    <fr:view name="announcement" property="subject"/>
                                                </html:link>
                                            </p>
                                        </logic:iterate>
                                        <p class="aright">
                                            <html:link page="<%= String.format("/showDegreeAnnouncements.do?method=viewAnnouncements&amp;degreeID=%s", degreeId) %>">
                                                <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.latestAnnouncements.showAll"/>
                                            </html:link>
                                        </p>
                                    </td>
                                </tr>
                            </logic:notEmpty>
                        </logic:present>
        <logic:notPresent name="doNotRenderDeadLines">
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
        <logic:present name="doNotRenderDeadLines">
            <logic:present name="announcements">
                <logic:notEmpty name="announcements">
                    <logic:empty name="degreeInfo" property="additionalInfo" >
                        <logic:empty name="degreeInfo" property="links" >
                            </table>
                        </logic:empty>
                    </logic:empty>
                </logic:notEmpty>
            </logic:present>
        </logic:present>

		<!-- ADDITIONAL INFO -->
		<logic:notEmpty name="degreeInfo" property="additionalInfo" >	
        		<logic:present name="doNotRenderDeadLines">
                <logic:notPresent name="announcements">
                    <table class="box" cellspacing="0" style="float: right;">   
                </logic:notPresent>
                <logic:present name="announcements">
                    <logic:empty name="announcements">
                        <table class="box" cellspacing="0" style="float: right;">   
                    </logic:empty>
                </logic:present>
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
                    <logic:notPresent name="announcements">
	          			<table class="box" cellspacing="0" style="float: right;">	
                    </logic:notPresent>
                    <logic:present name="announcements">
                        <logic:empty name="announcements">
                            <table class="box" cellspacing="0" style="float: right;">   
                        </logic:empty>
                    </logic:present>
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


	<!-- DESCRIPTION -->
	<logic:notEmpty name="degreeInfo" property="description" >
		<h2 class="arrow_bullet"><bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.overview"/></h2>
		<p><bean:write name="degreeInfo" property="description.content" filter="false"/></p>
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
