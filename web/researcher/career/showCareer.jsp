<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.career.title.label"/></h2>

<bean:define id="userView" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>"/>

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
	        <fr:hidden slot="person" name="userView" property="person"/>
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
