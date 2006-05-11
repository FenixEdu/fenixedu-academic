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
	<logic:equal name="action" value="createPublication" >
		<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.createPublicationUseCase.title"/></h2>
	</logic:equal>
	<logic:equal name="action" value="editPublication" >
		<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.editPublicationUseCase.title"/></h2>
	</logic:equal>
	
	<strong>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.common.useCase.stepOne.title"/>
	</strong>
	<u><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.createResultUseCase.step.authorsManagement"/></u> >
 	<strong>
 		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.common.useCase.stepTwo.title"/>
 	</strong>
 	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.createResultUseCase.step.insertData"/>
		
	<br/>
	<br/>
	
	<html:form action="/result/resultsManagement">
		<html:hidden property="method" value="authorsInserted"/>
		<bean:define id="authorsIdStr" name="authorsIdStr"/>
		<html:hidden property="authorsIdStr" value="<%= authorsIdStr.toString() %>"/>
		<bean:define id="action" name="action"/>
		<html:hidden property="action" value="<%= action.toString() %>"/>
		<bean:define id="resultId" name="resultId"/>
		<html:hidden property="resultId" value="<%= resultId.toString() %>"/>
		
		<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.authors"/></b></p>
		
		<fr:view name="authorsList" layout="tabular-list">
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="result.authors"/>
			
				<fr:property name="link(moveUp)" value="<%= "/result/resultsManagement.do?method=changeAuthorsOrder" + 
				        									"&authorsIdStr=" + authorsIdStr.toString() +
				        									"&action=" + action.toString() +
				        									"&resultId=" + resultId.toString() +
				        									"&offset=-1" %>"/>
				<fr:property name="param(moveUp)" value="idInternal/oid"/>
				<fr:property name="key(moveUp)" value="link.author.moveUp"/>
				<fr:property name="bundle(moveUp)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(moveUp)" value="1"/>
				
				<fr:property name="link(moveDown)" value="<%= 	"/result/resultsManagement.do?method=changeAuthorsOrder" +
																"&authorsIdStr=" + authorsIdStr.toString() +
					        									"&action=" + action.toString() +
					        									"&resultId=" + resultId.toString() +
					        									"&offset=1" %>"/>
				<fr:property name="param(moveDown)" value="idInternal/oid"/>
				<fr:property name="key(moveDown)" value="link.author.moveDown"/>
				<fr:property name="bundle(moveDown)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(moveDown)" value="2"/>
				
				<fr:property name="checkable" value="true"/>
				<fr:property name="checkboxName" value="authorIds"/>	
				<fr:property name="checkboxValue" value="idInternal"/>	
			</fr:layout>
		</fr:view>
		
		<html:submit styleClass="inputButton" onclick='<%= "this.form.method.value='removeAuthor'" %>'>
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.remove"/>
		</html:submit>
		<br/><br/>
		
		<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.authors.add"/></b>	
			<bean:message bundle="RESEARCHER_RESOURCES" key="label.authors.help.search.person"/></p>	

		<table>
			<tr>
				<td><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.name"/></b></td>
				<td><html:text size="20" property="nameToSearch"  /></td>
				<td>		
					<html:submit styleClass="inputButton" onclick='<%= "this.form.method.value='searchPersons'" %>'>
						<bean:message bundle="RESEARCHER_RESOURCES" key="button.search"/>
					</html:submit>
				</td>
			</tr>
		</table>
		<br/>
		
		<logic:present name="personsMatchList">
			<bean:size id="matchListSize" name="personsMatchList"/>
			<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.personsMatchList.results"/></b> <%= matchListSize.toString() %></p>
			<logic:empty name="personsMatchList">
				<bean:message bundle="RESEARCHER_RESOURCES" key="label.personsMatchList.empty"/>
			</logic:empty>
			
			<p><bean:message bundle="RESEARCHER_RESOURCES" key="label.addExternalPerson"/></p>
			<table>
				<tr>
					<td><bean:message bundle="RESEARCHER_RESOURCES" key="label.name"/></td>
					<td><html:text size="20" property="externalPersonName"/></td>
					<td>		
						<html:submit styleClass="inputButton" onclick='<%= "this.form.method.value='addExternalPerson'" %>'>
							<bean:message bundle="RESEARCHER_RESOURCES" key="button.add"/>
						</html:submit>
					</td>
				</tr>
			</table>
			<p><bean:message bundle="RESEARCHER_RESOURCES" key="label.addExternalPersonWarning"/></p>
			<br/>
			
			<logic:present name="floodedList">
				<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.moreSpecificSearch"/></b></p>
			</logic:present>
			
			<logic:notEmpty name="personsMatchList">
				<fr:view name="personsMatchList" layout="tabular-list">
					<fr:layout>
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="result.authors"/>
						
						<fr:property name="link(add)" value="<%= 	"/result/resultsManagement.do?method=insertNewAuthor" + 
						        									"&authorsIdStr=" + authorsIdStr.toString() +
						        									"&action=" + action.toString() +
						        									"&resultId=" + resultId.toString() %>"/>
						<fr:property name="param(add)" value="idInternal/oid"/>
						<fr:property name="key(add)" value="link.add"/>
						<fr:property name="bundle(add)" value="RESEARCHER_RESOURCES"/>
						<fr:property name="order(add)" value="1"/>	
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:present>
		<br/>

		<table>
			<tr>
				<td>
					<html:submit styleClass="inputButton">
						<bean:message bundle="RESEARCHER_RESOURCES" key="button.next"/>
					</html:submit>
				</td>
				<td>
					<html:submit styleClass="inputButton" onclick='<%= "this.form.method.value='cancelManagement'" %>'>
						<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
					</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
	
</logic:present>
