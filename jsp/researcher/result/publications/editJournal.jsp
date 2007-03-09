<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
<h2><bean:message key="label.editJournal" bundle="RESEARCHER_RESOURCES"/></h2>


<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"/>
<bean:define id="parameters" value="<%= "resultId=" + publicationBean.getIdInternal().toString() %>"/>

	<logic:messagesPresent message="true">
		<p>
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
		</html:messages>
		</p>
	</logic:messagesPresent>

			
	
<logic:notPresent name="issueBean">
			<!-- Choose Other Journal -->
			<logic:notPresent name="publicationBean" property="scientificJournal">
				<logic:present name="publicationBean" property="scientificJournalName">
					<div class="warning0">
						<b><bean:message key="label.attention" bundle="RESEARCHER_RESOURCES"/>:</b><br/>
						<bean:message key="label.informationForCreateMagazine" bundle="RESEARCHER_RESOURCES"/>
					</div>
				</logic:present>
				
				<div class="dinline forminline">						
				<fr:form action="/resultPublications/selectJournal.do">
				<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
				<fr:edit id="selectPublication" name="publicationBean" schema="result.publication.create.Article.selectMagazine">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thtop dinline thmiddle"/>
			        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>
					<br/>
					<html:submit property="confirm"><bean:message key="label.chooseMagazineFromList" bundle="RESEARCHER_RESOURCES"/></html:submit>
					<logic:present name="publicationBean" property="scientificJournalName">
						<html:submit property="new"><bean:message key="label.createMagazine" bundle="RESEARCHER_RESOURCES"/></html:submit>	
					</logic:present>
				</fr:form>
				
				<fr:form action="<%= "/resultPublications/showPublication.do?" + parameters%>">
					<html:submit><bean:message key="button.cancel"/></html:submit>
				</fr:form>
				</div>
			</logic:notPresent>
					
			<logic:present name="publicationBean" property="scientificJournal">
	
				<!-- Choose Volume -->
				
				<div class="infoop2">
					<bean:message key="label.chooseVolume.instructions" bundle="RESEARCHER_RESOURCES"/>
				</div>
			
				<div class="dinline forminline">	
				<fr:form action="/resultPublications/editData.do">
					<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
					<fr:edit id="selectPublication" name="publicationBean" schema="result.publication.create.Article.selectIssue">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thtop mtop1"/>
			        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
					</fr:edit>
					
					<html:submit property="confirm"><bean:message key="label.chooseIssue" bundle="RESEARCHER_RESOURCES"/></html:submit>
				</fr:form>
				<fr:form action="/resultPublications/createJournalToAssociate.do">
					<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
					<html:submit><bean:message key="label.createNewIssue" bundle="RESEARCHER_RESOURCES"/></html:submit>	
				</fr:form>
				<fr:form action="/resultPublications/prepareSelectJournal.do">					
					<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
					<html:submit><bean:message key="label.chooseNewJournal" bundle="RESEARCHER_RESOURCES"/></html:submit>	
				</fr:form>
			    <fr:form action="<%= "/resultPublications/showPublication.do?" + parameters%>">
				<html:submit><bean:message key="button.cancel"/></html:submit>
				</fr:form>
				</div>
			</logic:present>					
		</logic:notPresent>					
					
		<logic:present name="issueBean">

					<div class="dinline forminline">	
					<fr:form action="/resultPublications/createJournalToAssociate.do">
					<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
					<fr:edit id="createMagazine" name="issueBean" visible="false"/>									
	
					<logic:equal name="issueBean" property="journalAlreadyChosen" value="false">
					
					<p class="mbottom0">
						<strong><bean:message key="label.journalData" bundle="RESEARCHER_RESOURCES"/>:</strong>
					</p>

					<fr:edit id="journalInfo" name="issueBean" schema="result.publication.create.Article.createMagazine">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thright thlight thtop mtop0"/>
			        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
						<fr:destination name="invalid" path="/resultPublications/createJournalToAssociate.do"/>
					</fr:edit>

					</logic:equal>
					
					<logic:equal name="issueBean" property="journalAlreadyChosen" value="true">
						<p class="mbottom0">
							<strong><bean:message key="label.journal" bundle="RESEARCHER_RESOURCES"/>:</strong>
						</p>
						<fr:view name="issueBean" property="journal.name"/>
					</logic:equal>

					<p class="mbottom0">
						<strong><bean:message key="label.journalIssue" bundle="RESEARCHER_RESOURCES"/>:</strong>
					</p>

					<fr:edit id="issueInfo" name="issueBean" schema="result.publication.create.Article.createIssue">
						<fr:layout name="tabular">
							 <fr:property name="classes" value="tstyle5 thright thlight thtop mtop0 dinline"/>
			        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
						<fr:destination name="invalid" path="/resultPublications/createJournalToAssociate.do"/>
					</fr:edit>
					<br/><br/>
					<html:submit>
						<logic:equal name="issueBean" property="journalAlreadyChosen" value="false">
							<bean:message key="label.createJournalAndIssue" bundle="RESEARCHER_RESOURCES"/>
						</logic:equal>
						<logic:equal name="issueBean" property="journalAlreadyChosen" value="true">					
							<bean:message key="label.createNewIssue" bundle="RESEARCHER_RESOURCES"/>
						</logic:equal>
					</html:submit>
				</fr:form>
				<fr:form action="<%= "/resultPublications/showPublication.do?" + parameters%>">
					<html:submit><bean:message key="button.cancel"/></html:submit>
				</fr:form>
		   	   </div>
	</logic:present>