<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc" %>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>


<fp:select
        actionClass="org.fenixedu.academic.ui.struts.action.BolonhaManager.BolonhaManagerApplication$CompetenceCoursesManagement"/>

<f:view>
    <f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
    <f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

    <h:outputText value="<h2>#{CompetenceCourseManagement.departmentToDisplay.realName}</h2>" escape="false"/>

    <h:form>
        <h:panelGrid columns="2" style="infocell" columnClasses="infocell">
            <fc:selectOneMenu value="#{CompetenceCourseManagement.selectedDepartmentUnitID}" onchange="submit()">
                <f:selectItems value="#{CurricularCourseManagement.allowedDepartmentUnits}"/>
            </fc:selectOneMenu>
        </h:panelGrid>

        <h:panelGroup rendered="#{!empty CompetenceCourseManagement.groupMembersLabels}">
            <h:outputText
                    value="<p class='mtop15 mbottom05'><b id='members' class='highlight1'>#{bolonhaBundle['groupMembers']}</b> #{bolonhaBundle['label.group.members.explanation']}:</p>"
                    escape="false"/>
            <h:outputText value="<ul>" escape="false"/>
            <fc:dataRepeater value="#{CompetenceCourseManagement.groupMembersLabels}" var="memberLabel">
                <h:outputText value="<li>#{memberLabel}</li>" escape="false"/>
            </fc:dataRepeater>
            <h:outputText value="</ul>" escape="false"/>
        </h:panelGroup>
        <h:panelGroup rendered="#{empty CompetenceCourseManagement.groupMembersLabels}">
            <h:outputText value="<i>#{bolonhaBundle['label.empty.group.members']}</i><br/>" escape="false"/>
        </h:panelGroup>

        <h:panelGroup rendered="#{!empty CompetenceCourseManagement.departmentToDisplay}">
            <h:panelGroup rendered="#{CompetenceCourseManagement.canView}">

                <h:messages infoClass="success0" errorClass="error0" layout="table"/>
                <h:panelGroup rendered="#{empty CompetenceCourseManagement.scientificAreaUnits}">
                    <h:outputText value="<i>#{bolonhaBundle['noScientificAreaUnits']}<i><br/>" escape="false"/>
                </h:panelGroup>
                <h:panelGroup rendered="#{!empty CompetenceCourseManagement.scientificAreaUnits}">
                    <fc:dataRepeater value="#{CompetenceCourseManagement.scientificAreaUnits}" var="scientificAreaUnit">
                        <h:outputText value="<p class='mtop2 mbottom0'><strong>#{scientificAreaUnit.name}</strong></p>"
                                      escape="false"/>
                        <h:panelGroup rendered="#{empty scientificAreaUnit.subUnits}">
                            <h:outputText value="#{bolonhaBundle['noCompetenceCourseGroupUnits']}><br/>" escape="false"/>
                        </h:panelGroup>


                        <h:panelGroup rendered="#{!empty scientificAreaUnit.subUnits}">
                            <h:outputText value="<ul class='list3' style='padding-left: 2em;'>" escape="false"/>
                            <fc:dataRepeater value="#{scientificAreaUnit.subUnits}"
                                             var="competenceCourseGroupUnit">
                                <h:outputText value="<li class='tree_label' style='background-position: 0em 0.75em;'>"
                                              escape="false"/>
                                <h:outputText value="<table style='width: 100%; background-color: #fff;'><tr>" escape="false"/>
                                <h:outputText value="<td>#{competenceCourseGroupUnit.name}</td> " escape="false"/>
                                <h:outputText value="<td class='aright'>" escape="false"/>
                                <h:outputLink
                                        value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/createCompetenceCourse.faces">
                                    <h:outputFormat value="#{bolonhaBundle['create.param']}" escape="false">
                                        <f:param value=" #{bolonhaBundle['course']}"/>
                                    </h:outputFormat>
                                    <f:param name="competenceCourseGroupUnitID" value="#{competenceCourseGroupUnit.externalId}"/>
                                </h:outputLink>
                                <h:outputText value="</td></tr></table>" escape="false"/>


                                <h:dataTable value="#{competenceCourseGroupUnit.competenceCourses}" var="competenceCourse"
                                             styleClass="showinfo1 smallmargin mtop05" style="width: 100%;" rowClasses="color2"
                                             columnClasses=",aright nowrap"
                                             rendered="#{!empty competenceCourseGroupUnit.competenceCourses}">
                                    <h:column>
                                        <h:outputText value="#{competenceCourse.code} - " rendered="#{!empty competenceCourse.code}"/><h:outputText value="#{competenceCourse.name} "/>
                                        <h:outputText rendered="#{!empty competenceCourse.acronym}"
                                                      value="(#{competenceCourse.acronym}) "/>
                                        <h:outputText value="<span style='color: #aaa;'>" escape="false"/>
                                        <h:outputText rendered="#{competenceCourse.curricularStage.name == 'DRAFT'}"
                                                      value="<em style='color: #bb5;'>#{enumerationBundle[competenceCourse.curricularStage]}</em>"
                                                      escape="false"/>
                                        <h:outputText rendered="#{competenceCourse.curricularStage.name == 'PUBLISHED'}"
                                                      value="<em style='color: #569;'>#{enumerationBundle[competenceCourse.curricularStage]}</em>"
                                                      escape="false"/>
                                        <h:outputText rendered="#{competenceCourse.curricularStage.name == 'APPROVED'}"
                                                      value="<em style='color: #595;'>#{enumerationBundle[competenceCourse.curricularStage]}</em>"
                                                      escape="false"/>
                                        <h:outputText value="</span>" escape="false"/>
                                    </h:column>

                                    <h:column>
                                        <h:outputLink
                                                value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/showCompetenceCourse.faces">
                                            <h:outputText value="#{bolonhaBundle['show']}"/>
                                            <f:param name="action" value="ccm"/>
                                            <f:param name="competenceCourseID" value="#{competenceCourse.externalId}"/>
                                        </h:outputLink>
                                        <h:panelGroup rendered="#{competenceCourse.curricularStage.name != 'APPROVED'}">
                                            <h:outputText value=", "/>
                                            <h:outputLink
                                                    value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/editCompetenceCourseMainPage.faces">
                                                <h:outputText value="#{bolonhaBundle['edit']}"/>
                                                <f:param name="action" value="ccm"/>
                                                <f:param name="competenceCourseID" value="#{competenceCourse.externalId}"/>
                                            </h:outputLink>
                                            <h:outputText value=", "/>
                                            <h:outputLink rendered="#{competenceCourse.curricularStage.name != 'APPROVED'}"
                                                          value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/deleteCompetenceCourse.faces">
                                                <h:outputText value="#{bolonhaBundle['delete']}"/>
                                                <f:param name="competenceCourseID" value="#{competenceCourse.externalId}"/>
                                            </h:outputLink>
                                            <h:outputText value=", "/>
                                            <h:outputLink
                                                    value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/setCompetenceCourseBibliographicReference.faces">
                                                <h:outputText value="#{bolonhaBundle['bibliographicReference']}"/>
                                                <f:param name="action" value="add"/>
                                                <f:param name="bibliographicReferenceID" value="-1"/>
                                                <f:param name="competenceCourseID" value="#{competenceCourse.externalId}"/>
                                            </h:outputLink>
                                        </h:panelGroup>
                                    </h:column>
                                </h:dataTable>

                                <h:outputText value="</li>" escape="false"/>
                            </fc:dataRepeater>
                            <h:outputText value="</ul>" escape="false"/>
                        </h:panelGroup>
                    </fc:dataRepeater>
                </h:panelGroup>

            </h:panelGroup>

            <h:panelGroup rendered="#{!CompetenceCourseManagement.canView}">
                <h:outputText value="<br/><em>#{bolonhaBundle['notMemberInCompetenceCourseManagementGroup']}</em><br/>"
                              escape="false"/>
            </h:panelGroup>

            <h:graphicImage id="image" alt="Excel" url="/images/excel.gif"/>
            <h:outputText value="&nbsp;" escape="false"/>
            <h:outputText
                    value="<a href='#{CompetenceCourseManagement.contextPath}/bolonhaManager/competenceCourses/manageVersions.do?method=exportCompetenceCourseExecutionToExcel'>#{bolonhaBundle['course.group.studies.plan']}</a>"
                    escape="false"/>

        </h:panelGroup>
    </h:form>
    <h:panelGroup rendered="#{empty CompetenceCourseManagement.departmentToDisplay}">
        <h:outputText value="<i>#{bolonhaBundle['no.current.department.working.place']}</i><br/>" escape="false"/>
    </h:panelGroup>


</f:view>