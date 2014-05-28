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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<bean:define id="personId" name="person" property="externalId" />

<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
    <p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>

<%-- create --%>
<logic:notPresent name="qualification"> 
	<h2><bean:message key="title.manager.qualification.createQualification" bundle="MANAGER_RESOURCES" /></h2>
	
	<fr:create id="qualification"  action="<%="/qualification.do?method=backToShowQualifications&personID=" + personId %>" 
	type="net.sourceforge.fenixedu.domain.Qualification" 
	schema="manager.qualification.qualification">
	   <fr:hidden  slot="person" name="person"/>
	   <fr:layout name="tabular-editable">
	       <fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
	       <fr:property name="columnClasses" value=",,tdclear tderror1" />
	   </fr:layout>
	  
	   <fr:destination name="cancel" path="<%="/qualification.do?method=backToShowQualifications&personID"+personId%>" />
	</fr:create>
</logic:notPresent>

<%-- edit --%>
<logic:present name="qualification">
	<h2><bean:message key="title.manager.qualification.editQualification" bundle="MANAGER_RESOURCES" /></h2> 
	<fr:edit id="qualification" name="qualification"  action="<%=String.format("/qualification.do?method=backToShowQualifications&personID=%s", personId) %>" 
	type="net.sourceforge.fenixedu.domain.Qualification" 
	schema="manager.qualification.qualification">
	  
	   <fr:layout name="tabular-editable">
	       <fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
	       <fr:property name="columnClasses" value=",,tdclear tderror1" />
	   </fr:layout>
	  
	   <fr:destination name="cancel" path="<%="/qualification.do?method=backToShowQualifications&personID"+personId%>" />
	</fr:edit>
</logic:present>







	




