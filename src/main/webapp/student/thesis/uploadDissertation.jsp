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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2><bean:message key="title.student.thesis.upload.dissertation"/></h2>

<div class="infoop2 mvert15">
    <p>
        <bean:message key="label.student.thesis.upload.dissertation.message"/>
    </p>
</div>

<bean:define id="callbackUrl" type="java.lang.String">/thesisSubmission.do?method=prepareThesisSubmission&amp;thesisId=<bean:write name="thesis" property="externalId"/></bean:define>
<bean:define id="uploadUrl" type="java.lang.String">/thesisSubmission.do?method=uploadDissertation&amp;thesisId=<bean:write name="thesis" property="externalId"/></bean:define>
    <fr:view name="fileBean" schema="student.thesisBean.upload.dissertation">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.student.thesis.ThesisFileBean" bundle="APPLICATION_RESOURCES">
		    <fr:slot name="title" key="label.thesis.title">
		        <fr:property name="size" value="60"/>
    		</fr:slot>
    		<fr:slot name="subTitle" key="label.thesis.subtitle">
        		<fr:property name="size" value="60"/>
    		</fr:slot>
    		<fr:slot name="language" key="label.thesis.language">
        		<fr:property name="includedValues" value="pt, en"/>
    		</fr:slot>
    	</fr:schema>
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:view>
	<div>
    	<p>
    		<span class="warning0">
        		<bean:message key="label.student.thesis.upload.dissertation.message.content.warning"/>
    		</span>
    	</p>
	</div>

<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
    <p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:form action="<%= uploadUrl %>" encoding="multipart/form-data">
    <fr:edit id="dissertationFile" name="fileBean" schema="student.thesisBean.upload.dissertation">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
        
        <fr:destination name="cancel" path="<%= callbackUrl %>"/>
    </fr:edit>

	<span>
	    <bean:message key="label.student.thesis.upload.dissertation.message.content.accept.disclaimer"/>
    	<input type="checkbox" name="contentDisclaimer"/>
	</span>
	<br/>
	<br/>

    <html:submit>
        <bean:message key="button.submit"/>
    </html:submit>
    <html:cancel>
        <bean:message key="button.cancel"/>
    </html:cancel>
</fr:form>
