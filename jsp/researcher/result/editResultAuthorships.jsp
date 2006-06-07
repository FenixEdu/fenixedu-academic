<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal" />
	<bean:define id="resultName" name="result" property="title.content" />

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	
	<%-- TITLE --%>		
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.authorships.useCaseTitle"/></h2>
	
	<%-- Authorship List --%>
	<bean:size id="listSize" name="result" property="resultAuthorships"/>
	<logic:equal name="listSize" value="1">
		<fr:view name="result" property="resultAuthorships" schema="result.authorships" layout="tabular"/>
	</logic:equal>
	<logic:notEqual name="listSize" value="1">
		<html:form action="/result/resultAuthorshipManagement">
			<html:hidden property="method" value="removeAuthorship"/>
			<bean:define id="resultId" name="result" property="idInternal" />
			<html:hidden property="resultId" value="<%= resultId.toString() %>"/>
	
			<fr:view name="result" property="resultAuthorships" schema="result.authorships" layout="tabular">
				<fr:layout>
					<fr:property name="sortBy" value="authorOrder"/>
					
					<fr:property name="link(moveUp)" value="<%= "/result/resultAuthorshipManagement.do?method=changeAuthorsOrder" + 
					        									"&resultId=" + resultId.toString() +
					        									"&offset=-1" %>"/>
					<fr:property name="param(moveUp)" value="idInternal/oid"/>
					<fr:property name="key(moveUp)" value="link.moveUp"/>
					<fr:property name="bundle(moveUp)" value="RESEARCHER_RESOURCES"/>
					<fr:property name="order(moveUp)" value="1"/>
					
					<fr:property name="link(moveDown)" value="<%= "/result/resultAuthorshipManagement.do?method=changeAuthorsOrder" +
						        								  "&resultId=" + resultId.toString() +
						        								  "&offset=1" %>"/>
					<fr:property name="param(moveDown)" value="idInternal/oid"/>
					<fr:property name="key(moveDown)" value="link.moveDown"/>
					<fr:property name="bundle(moveDown)" value="RESEARCHER_RESOURCES"/>
					<fr:property name="order(moveDown)" value="2"/>
					
					<fr:property name="checkable" value="true"/>
					<fr:property name="checkboxName" value="authorIds"/>	
					<fr:property name="checkboxValue" value="idInternal"/>
				</fr:layout>
		 	</fr:view>
	
			<html:submit styleClass="inputButton">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.remove"/>
			</html:submit>
		</html:form>
	</logic:notEqual>
	
	<logic:notPresent name="external">
		<h3>
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.authorship.addNewAuthor"/>
			<html:link page="<%="/result/resultAuthorshipManagement.do?method=prepareEditAuthorshipWithSimpleBean&external=false&resultId=" + resultId %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.internal" />
			</html:link>				
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.or" />
			<html:link page="<%="/result/resultAuthorshipManagement.do?method=prepareEditAuthorshipWithSimpleBean&external=true&resultId=" + resultId %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants.external" />
			</html:link>				
		</h3>
	</logic:notPresent>
	<logic:present name="external">
		<logic:equal name="external" value="false">
			<h3>
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.authorship.addNewInternalAuthor"/>
			</h3>
			<fr:edit id="simpleBean" name="simpleBean" action="<%="/result/resultAuthorshipManagement.do?method=createAuthorshipInternalPerson&resultId=" + resultId %>" schema="resultAuthorship.internalPerson.creation">
				<fr:destination name="invalid" path="<%="/result/resultAuthorshipManagement.do?method=prepareEditAuthorshipWithSimpleBean&external=false&resultId=" + resultId %>"/>	
				<fr:destination name="cancel" path="<%="/result/resultAuthorshipManagement.do?method=prepareEditAuthorship&resultId=" + resultId %>"/>	
			</fr:edit>
		</logic:equal>
		<logic:equal name="external" value="true">
			<h3>
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.authorship.addNewExternalAuthor"/>			
			</h3>
			<logic:present name="simpleBean">
				<fr:edit id="simpleBean" name="simpleBean" action="<%="/result/resultAuthorshipManagement.do?method=createAuthorshipExternalPerson&external=true&resultId=" + resultId %>" schema="resultAuthorship.externalPerson.simpleCreation">
					<fr:destination name="invalid" path="<%="/result/resultAuthorshipManagement.do?method=prepareEditAuthorshipWithSimpleBean&external=true&resultId=" + resultId %>"/>	
					<fr:destination name="cancel" path="<%="/result/resultAuthorshipManagement.do?method=prepareEditAuthorship&resultId=" + resultId %>"/>	
				</fr:edit>						
			</logic:present>
			<logic:present name="fullBean">
				<fr:edit id="fullBean" name="fullBean" action="<%="/result/resultAuthorshipManagement.do?method=createAuthorshipExternalPerson&resultId=" + resultId %>" schema="resultAuthorship.externalPerson.fullCreation">
					<fr:destination name="invalid" path="<%="/result/resultAuthorshipManagement.do?method=prepareEditAuthorshipWithFullBean&external=true&resultId=" + resultId %>"/>
					<fr:destination name="cancel" path="<%="/result/resultAuthorshipManagement.do?method=prepareEditAuthorship&resultId=" + resultId %>"/>	
				</fr:edit>
			</logic:present>
		</logic:equal>
	</logic:present>
	
	<br/>
	<html:link page="<%="/result/resultAuthorshipManagement.do?method=backToResult&resultId=" + resultId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
	
</logic:present>
<br/>