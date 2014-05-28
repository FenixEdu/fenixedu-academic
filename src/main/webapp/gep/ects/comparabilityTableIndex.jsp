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
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="GEP_RESOURCES" key="title.ects.tablesManagement" /></h2>

<html:messages id="message" message="true" bundle="GEP_RESOURCES">
    <p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<fr:form action="/manageEctsComparabilityTables.do">
    <html:hidden property="method" value="filterPostback" />
    <fr:edit id="filter" name="filter">
        <fr:schema bundle="DOMAIN_RESOURCES"
            type="net.sourceforge.fenixedu.presentationTier.Action.commons.ects.EctsTableFilter">
            <fr:slot name="executionInterval" layout="menu-select" required="true" key="label.ExecutionYear.year">
                <fr:property name="format" value="${pathName}" />
                <fr:property name="providerClass"
                    value="net.sourceforge.fenixedu.presentationTier.Action.commons.ects.EctsTableFilter$ExecutionIntervalProvider" />
            </fr:slot>
            <fr:slot name="type" layout="menu-postback" required="true" key="label.ects.EctsTableType">
                <fr:destination name="/manageEctsComparabilityTables.do?method=filterPostback" />
            </fr:slot>
            <fr:slot name="level" layout="menu-select" required="true" key="label.ects.EctsTableLevel">
                <fr:property name="providerClass"
                    value="net.sourceforge.fenixedu.presentationTier.Action.commons.ects.EctsTableFilter$EctsTableLevelProvider" />
                <fr:property name="eachLayout" value="" />
            </fr:slot>
        </fr:schema>
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05" />
            <fr:property name="columnClasses" value=",,tdclear tderror1" />
        </fr:layout>
    </fr:edit>
    <html:submit onclick="this.form.method.value='viewStatus'; return true;">
        <bean:message key="button.submit" bundle="COMMON_RESOURCES" />
    </html:submit>
</fr:form>

<logic:present name="filter" property="executionInterval">
    <logic:present name="filter" property="type">
        <logic:present name="filter" property="level">
            <h3 class="mtop15 mbottom05"><fr:view name="filter" property="executionInterval.pathName" /> - <fr:view name="filter"
                property="type" /> - <fr:view name="filter" property="level" /></h3>

            <fr:form action="/manageEctsComparabilityTables.do?method=importTables" encoding="multipart/form-data">
                <table class="tstyle5 thlight mvert05 thmiddle">
                    <tr>
                        <td><fr:edit id="filter" name="filter">
                            <fr:schema bundle="GEP_RESOURCES"
                                type="net.sourceforge.fenixedu.presentationTier.Action.commons.ects.EctsTableFilter">
                                <fr:slot name="inputStream" key="label.ects.tablesManagement.importTables" required="true">
                                    <fr:property name="fileNameSlot" value="filename" />
                                    <fr:property name="onChangeEvent" value="testValidFileInput(this.id);" />
                                </fr:slot>
                            </fr:schema>
                            <fr:layout name="tabular">
                                <fr:property name="classes" value="tstylenone" />
                            </fr:layout>
                        </fr:edit></td>
                        <td><html:submit styleId="importButton" disabled="true">
                            <bean:message bundle="GEP_RESOURCES" key="button.ects.tablesManagement.importTables" />
                        </html:submit></td>
                    </tr>
                </table>
            </fr:form>
            
            <script type="text/javascript">
            	function testValidFileInput (id) {
            		var filePath = $("#"+id).val();
            		if (filePath === "") {
            			$("#importButton").attr("disabled", "disabled");
            		} else {
            			$("#importButton").removeAttr("disabled");
            		}
            	};
            </script>

            <bean:define id="interval" name="filter" property="executionInterval.resumedRepresentationInStringFormat" />
            <bean:define id="ectsType" name="filter" property="type.name" />
            <bean:define id="level" name="filter" property="level.name" />
            <bean:define id="filterArgs"
                value="<%="&amp;interval=" + interval + "&amp;type=" + ectsType + "&amp;level=" + level%>" />
            <p class="mvert05"><html:link
                action="<%="/manageEctsComparabilityTables.do?method=exportTemplate" + filterArgs%>">
                <bean:message bundle="GEP_RESOURCES" key="link.ects.tablesManagement.exportTemplate" />
            </html:link></p>

            <bean:define id="sortByArg" value="" />
            <fr:view name="status">
                <fr:schema bundle="GEP_RESOURCES"
                    type="net.sourceforge.fenixedu.domain.degreeStructure.IEctsConversionTable">
                    <logic:equal name="filter" property="type" value="ENROLMENT">
                        <logic:equal name="filter" property="level" value="COMPETENCE_COURSE">
                            <fr:slot name="targetEntity.externalId" key="label.externalId" />
                            <fr:slot name="targetEntity.departmentUnit.name" key="label.departmentUnit.name" />
                            <fr:slot name="targetEntity.name" key="label.competenceCourse.name" />
                            <fr:slot name="targetEntity.acronym" key="label.acronym" />
                            <bean:define id="sortByArg" value="targetEntity.departmentUnit.name,targetEntity.name" />
                        </logic:equal>
                        <logic:equal name="filter" property="level" value="DEGREE">
                            <fr:slot name="targetEntity.externalId" key="label.externalId" />
                            <fr:slot name="targetEntity.degreeType.localizedName" key="label.degreeType" />
                            <fr:slot name="targetEntity.name" key="label.name" />
                            <fr:slot name="curricularYear.year" key="label.curricularYear" />
                            <bean:define id="sortByArg"
                                value="targetEntity.degreeType.localizedName,targetEntity.name, curricularYear" />
                        </logic:equal>
                        <logic:equal name="filter" property="level" value="CURRICULAR_YEAR">
                            <fr:slot name="cycle" key="label.cycle" />
                            <fr:slot name="curricularYear.year" key="label.curricularYear" />
                            <bean:define id="sortByArg" value="cycle, curricularYear" />
                        </logic:equal>
                    </logic:equal>
                    <logic:equal name="filter" property="type" value="GRADUATION">
                        <logic:equal name="filter" property="level" value="DEGREE">
                            <fr:slot name="targetEntity.externalId" key="label.externalId" />
                            <fr:slot name="targetEntity.degreeType.localizedName" key="label.degreeType" />
                            <fr:slot name="targetEntity.name" key="label.name" />
                            <fr:slot name="cycle" key="label.cycle" />
                            <bean:define id="sortByArg"
                                value="targetEntity.degreeType.localizedName,targetEntity.name" />
                        </logic:equal>
                        <logic:equal name="filter" property="level" value="CYCLE">
                            <fr:slot name="cycle" key="label.cycle" />
                            <bean:define id="sortByArg" value="cycle" />
                        </logic:equal>
                    </logic:equal>
                    <fr:slot name="ectsTable.printableFormat" key="label.ectsTable" />
                </fr:schema>
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle2 mtop1" />
                    <fr:property name="sortBy" value="<%= sortByArg %>" />
                </fr:layout>
            </fr:view>
        </logic:present>
    </logic:present>
</logic:present>
