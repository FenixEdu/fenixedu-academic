<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.editPatentUseCase.title"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></h3>	
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<bean:define id="patentId" name="patent" property="idInternal"/>
		
	<fr:edit 	id="editPatent" name="patent" type="net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent" 
				schema="patent.edit"
				action="<%="/patents/patentsManagement.do?method=prepareEditPatent&resultId=" + patentId %>">
		<fr:hidden slot="modifyedBy" name="UserView" property="person.name"/>
		<fr:hidden slot="participations" name="patent" property="participations"/>
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="style1"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
   	    <fr:destination name="cancel" path="<%="/patents/patentsManagement.do?method=prepareEditPatent&resultId=" + patentId %>"/>
	</fr:edit>
</logic:present>
<br/>