<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
	

<bean:define id="personID" name="person" property="externalId"></bean:define>
<h2><bean:message key="title.manager.qualification.showQualifications" bundle="MANAGER_RESOURCES" /></h2>
<logic:notPresent name="person" property="associatedQualifications">
	<bean:write name="link.manager.qualification.notExitsQualifications"/>
</logic:notPresent>

<fr:view  name="person" property="associatedQualifications" schema="manager.qualification.showQualifications" >
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value="bgcolor3 acenter,acenter,aleft" />
			<fr:property name="headerClasses" value="acenter" />
						
			
			<fr:property name="link(edit)" value="<%=String.format("/qualification.do?method=prepareEditQualification&personID=%s",personID) %>"/>
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="param(edit)" value="externalId/qualificationId" />
			<fr:property name="bundle(edit)" value="MANAGER_RESOURCES" />
			
			<fr:property name="link(delete)" value="<%=String.format("/qualification.do?method=deleteQualification&personID=%s",personID) %>"/>
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="param(delete)" value="externalId/qualificationId" />
			<fr:property name="bundle(delete)" value="MANAGER_RESOURCES" />
	</fr:layout>		
</fr:view>	

<p>
	<html:link page="<%=String.format("/qualification.do?method=prepareCreateQualification&personID=%s",personID) %>">
		<bean:message key="link.manager.qualification.create" bundle="MANAGER_RESOURCES"/>
	</html:link>
</p>
<p>
	<html:link page="<%=String.format("/qualification.do?method=backToViewPerson&personID=%s",personID) %>">
		<bean:message key="link.manager.qualification.back" bundle="MANAGER_RESOURCES"/>
	</html:link>
</p>		


