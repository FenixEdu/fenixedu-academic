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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.student.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p>
        <span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>

<fr:messages>
</fr:messages>

<fr:form action="/createStudent.do">

    <h3 class="mtop15 mbottom025"><bean:message key="label.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
    <fr:edit id="executionDegree"
             name="executionDegreeBean"
             type="org.fenixedu.academic.dto.administrativeOffice.ExecutionDegreeBean"
             schema="choose.executionDegree.toCreateRegistration">
        <fr:destination name="degreePostBack" path="/createStudent.do?method=chooseDegreePostBack"/>
        <fr:destination name="degreeCurricularPlanPostBack" path="/createStudent.do?method=chooseDegreeCurricularPlanPostBack"/>    
        <fr:destination name="invalid" path="/createStudent.do?method=chooseExecutionDegreeInvalid"/>
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
            <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
    
    <logic:present name="executionDegreeBean" property="executionDegree">
        <h3 class="mtop1 mbottom025"><bean:message key="label.ingression.short" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
        <fr:edit name="ingressionInformationBean" id="chooseIngression" schema="ingression.information" type="org.fenixedu.academic.dto.candidacy.IngressionInformationBean">                
            <fr:destination name="agreementPostBack" path="/createStudent.do?method=chooseAgreementPostBack"/>
            <fr:destination name="ingressionPostBack" path="/createStudent.do?method=chooseIngressionPostBack"/>
            <fr:destination name="entryPhasePostBack" path="/createStudent.do?method=chooseEntryPhasePostBack"/>
            <fr:layout name="tabular" >
                <fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
                <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
            </fr:layout>
        </fr:edit>
    </logic:present>        
    
</fr:form>

<logic:present name="choosePersonBean">
    <bean:define id="firstTimeSearch" name="choosePersonBean" property="firstTimeSearch" />
    
    <script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
    </script>
    
    <script>
        $(document).ready(function() {
            $("span.choosePersonBeanForm form input").change(function() {
                $("span.choosePersonBeanForm form input[name$='firstTimeSearch']").attr("value", true);
            })
        });
    </script>
    
    <span class="choosePersonBeanForm">
    <fr:form action="/createStudent.do?method=choosePerson">
        <fr:edit id="executionDegree" name="executionDegreeBean" visible="false"  />
        <fr:edit name="ingressionInformationBean" id="chooseIngression" visible="false" />
        
        <h3 class="mtop1 mbottom025"><bean:message key="label.identification" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
        <fr:edit id="choosePerson" name="choosePersonBean" >                 
            <fr:hidden name="firstTimeSearch" slot="firstTimeSearch" />
            
            <%-- qubExtension, removed student number --%>
            <fr:schema 
                type="org.fenixedu.academic.dto.person.ChoosePersonBean" bundle="ACADEMIC_OFFICE_RESOURCES">

                <fr:slot name="name" key="label.name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
                    <fr:property name="size" value="50" />
                </fr:slot>      
                <fr:slot name="identificationNumber" key="label.identificationNumber.descriptive"
                        validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
                    <fr:property name="size" value="22" />
                    <fr:property name="maxLength" value="20" />
                </fr:slot>      
                <fr:slot name="documentType" key="label.idDocumentType.descriptive" 
                    validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
                    <fr:property name="excludedValues" value="CITIZEN_CARD" />
                </fr:slot>
                <fr:slot name="dateOfBirth" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
                    <fr:property name="size" value="12"/>
                    <fr:property name="maxLength" value="10"/>
                </fr:slot>

                <fr:slot name="person" layout="radio-select" key="label.person.similarResults" >
                    <fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.person.SearchSimilarNamesPersonsProvider" />
                    <fr:property name="eachLayout" value="values" />
                    <fr:property name="eachSchema" value="person.name.id.dateOfBirth" />        
                </fr:slot>
            </fr:schema> 
            
            <fr:layout name="tabular" >
                <fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
                <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
                <fr:property name="requiredMarkShown" value="true" />
            </fr:layout>
        </fr:edit>
        <p>
            <html:submit><bean:message key="button.continue" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
        </p>
    </fr:form>
    </span>
</logic:present>    