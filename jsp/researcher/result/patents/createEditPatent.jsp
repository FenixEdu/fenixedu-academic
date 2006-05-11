<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><html:errors/></span>
<html:messages id="message" message="true" bundle="RESEARCHER_RESOURCES">
	<span class="error">
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present role="RESEARCHER">
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	<logic:equal name="action" value="createPatent" >
		<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.createPatentUseCase.title"/></h2>
	</logic:equal>
	<logic:equal name="action" value="editPatent" >
		<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.editPatentUseCase.title"/></h2>
	</logic:equal>
	<logic:equal name="action" value="createPublicationt" >
		<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.createPublicationUseCase.title"/></h2>
	</logic:equal>
	<logic:equal name="action" value="editPublication" >
		<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.editPublicationUseCase.title"/></h2>
	</logic:equal>
	
	<strong>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.common.useCase.stepOne.title"/>
	</strong>
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.createResultUseCase.step.authorsManagement"/> >
 	<strong>
 		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.common.useCase.stepTwo.title"/>
 	</strong>
 	<u><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.createResultUseCase.step.insertData"/></u>
		
	<br/>
	<br/>

	<b><bean:message bundle="RESEARCHER_RESOURCES" key="label.authors"/></b>
	<fr:view name="authorsList" layout="tabular-list">
		<fr:layout>
			<fr:property name="subLayout" value="values"/>
			<fr:property name="subSchema" value="result.authors"/>
		</fr:layout>
	</fr:view>
	
	<br/><br/>		
		
	<logic:equal name="action" value="createPatent">
		<fr:create 	id="create" type="net.sourceforge.fenixedu.domain.research.result.Patent" 
					schema="patent.create"
					action="/result/patents/patentsManagement.do?method=listPatents">
			<fr:hidden slot="authorships" multiple="true" name="authorsList"/>
		    <fr:layout name="tabular">
		        <fr:property name="classes" value="style1"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		    </fr:layout>
		    <fr:destination name="cancel" path="/result/patents/patentsManagement.do?method=listPatents"/>
		</fr:create>
	</logic:equal>
		
	<logic:equal name="action" value="editPatent">
		<fr:edit 	id="input" name="patent" type="net.sourceforge.fenixedu.domain.research.result.Patent" 
					schema="patent.create"
					action="/result/patents/patentsManagement.do?method=listPatents">
			<fr:hidden slot="authorships" multiple="true" name="authorsList"/>
		    <fr:layout name="tabular">
		        <fr:property name="classes" value="style1"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		    </fr:layout>
	   	    <fr:destination name="cancel" path="/result/patents/patentsManagement.do?method=listPatents"/>
		</fr:edit>
	</logic:equal>	
</logic:present>