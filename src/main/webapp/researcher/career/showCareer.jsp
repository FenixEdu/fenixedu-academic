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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.career.title.label"/></h2>

<div class="infoop2">
    <p></p><bean:message bundle="RESEARCHER_RESOURCES" key="label.career.purpose.help"/><p>
</div>

<logic:notPresent name="creating">
<logic:notPresent name="editCareer">
<logic:notPresent name="deleteCareer">
<ul>
    <li>
        <html:link page="/career/careerManagement.do?method=prepareCreateCareer">
            <bean:message key="label.create.career" bundle="RESEARCHER_RESOURCES"/>
        </html:link>
    </li>
</ul>
</logic:notPresent>
</logic:notPresent>
</logic:notPresent>

<span class="error0">
	<html:messages id="message" message="true">
    	<bean:write name="message" />
	</html:messages>
</span>

<logic:present name="creating">
    <fr:form action="/career/careerManagement.do?method=showCareer">
	    <fr:create id="createCareer" schema="researcher.career" type="net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer">
	        <fr:hidden slot="person" name="LOGGED_USER_ATTRIBUTE" property="person"/>
	        <fr:layout name="tabular">
		        <fr:property name="classes" value="tstyle5 thlight thright"/>
		        <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	        </fr:layout>
	    </fr:create>
	    <p>
		<html:submit>
			<bean:message bundle="COMMON_RESOURCES" key="button.create" />
		</html:submit>
		<html:cancel>
			<bean:message bundle="COMMON_RESOURCES" key="button.cancel" />
		</html:cancel>
		</p>
	</fr:form>
</logic:present>

<logic:present name="editCareer">
    <fr:edit id="editCareer" name="editCareer" schema="researcher.career" action="/career/careerManagement.do?method=showCareer" />
</logic:present>

<logic:present name="deleteCareer">
    <p>
        <bean:message bundle="RESEARCHER_RESOURCES" key="label.career.delete.confirm" />
		<span class="color888"><fr:view name="deleteCareer" property="beginYear"/><logic:present name="deleteCareer" property="endYear" ><bean:message key="label.until" bundle="RESEARCHER_RESOURCES"/> <fr:view name="deleteCareer" property="endYear"/></span></logic:present>, 
		<fr:view name="deleteCareer" property="function"/> (<fr:view name="deleteCareer" property="entity"/>)?
        
        <fr:form action="/career/careerManagement.do?method=deleteCareer">
            <fr:edit id="deleteCareer" name="deleteCareer" visible="false">
                <fr:destination name="cancel" path="/career/careerManagement.do?method=showCareer" />
            </fr:edit>
	        <html:submit>
	            <bean:message bundle="COMMON_RESOURCES" key="button.yes" />
	        </html:submit>
	        <html:cancel>
	            <bean:message bundle="COMMON_RESOURCES" key="button.no" />
	        </html:cancel>
        </fr:form>
    </p>
</logic:present>

<logic:notPresent name="creating">
<logic:notPresent name="editCareer">
<logic:notEmpty name="career">
	<p class="mtop15"><strong><bean:message key="label.career.list" bundle="RESEARCHER_RESOURCES"/></strong></p>

    <fr:view name="career" schema="researcher.career">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="link(edit)" value="/career/careerManagement.do?method=prepareEditCareer" />
			<fr:property name="param(edit)" value="externalId/id" />
			<fr:property name="key(edit)" value="link.edit" />
			<fr:property name="bundle(edit)" value="COMMON_RESOURCES" />
			<fr:property name="link(delete)" value="/career/careerManagement.do?method=prepareDeleteCareer" />
			<fr:property name="param(delete)" value="externalId/id" />
			<fr:property name="key(delete)" value="link.delete" />
			<fr:property name="bundle(delete)" value="COMMON_RESOURCES" />
			<fr:property name="groupLinks" value="true" />
			<fr:property name="linkGroupSeparator" value=", " />
		</fr:layout>
    </fr:view>
</logic:notEmpty>

<logic:empty name="career">
    <p class="mtop15">
        <em><bean:message key="label.no.career.in.person" bundle="RESEARCHER_RESOURCES"/>.</em>
    </p>
</logic:empty>
</logic:notPresent>
</logic:notPresent>
