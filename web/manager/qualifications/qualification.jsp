<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


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







	




