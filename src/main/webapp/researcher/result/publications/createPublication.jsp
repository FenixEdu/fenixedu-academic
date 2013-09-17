<%@ page language="java" %> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<html:xhtml/>

<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"/>

<!-- Insert new publication -->
<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.insert"/></h2>


<logic:equal name="publicationBean" property="createJournal" value="false">
	<logic:equal name="publicationBean" property="createEvent" value="false">

		<p>Escolha o tipo de publicação e insira os dados.</p>
	
		<fr:form action="/resultPublications/create.do">
		
			<!-- Present Author -->
			<p class="mtop15 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.author"/></b></p>
			<fr:edit id="author" name="publicationBean" schema="<%= publicationBean.getParticipationSchema() %>" nested="true">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 mtop1 thlight"/>
					<fr:property name="columnClasses" value="dnone,,tdclear tderror1"/>
				</fr:layout>
		   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
				<fr:destination name="cancel" path="/resultPublications/listPublications.do"/>
			</fr:edit>	
			
			<!-- Present publication fields -->
			<p class="mtop1 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.ResultPublication.type." + publicationBean.getPublicationTypeString() %>"/></b></p>

			<logic:equal name="publicationBean" property="class.simpleName" value="ArticleBean">
			<p class="breadcumbs mvert1"><span class="actual"><bean:message key="label.1stStepInsertPublicationData" bundle="RESEARCHER_RESOURCES"/></span> > <span><bean:message key="label.2ndStepAssociateJournal" bundle="RESEARCHER_RESOURCES"/></span></p>
			</logic:equal>
			<logic:equal name="publicationBean" property="class.simpleName" value="InproceedingsBean">
			<p class="breadcumbs mvert1"><span class="actual"><bean:message key="label.1stStepInsertPublicationData" bundle="RESEARCHER_RESOURCES"/></span> > <span><bean:message key="label.2ndStepAssociateEvent" bundle="RESEARCHER_RESOURCES"/></span></p>
			</logic:equal>
			<logic:equal name="publicationBean" property="class.simpleName" value="ProceedingsBean">
			<p class="breadcumbs mvert1"><span class="actual"><bean:message key="label.1stStepInsertPublicationData" bundle="RESEARCHER_RESOURCES"/></span> > <span><bean:message key="label.2ndStepAssociateEvent" bundle="RESEARCHER_RESOURCES"/></span></p>
			</logic:equal>

			
			<logic:equal name="publicationBean" property="class.simpleName" value="ArticleBean">		
				<p class="mtop0 mbottom05 color888">
					<bean:message key="label.create.journalArticle.instructions" bundle="RESEARCHER_RESOURCES"/>
				</p>
			</logic:equal>
		
			<logic:equal name="publicationBean" property="class.simpleName" value="InproceedingsBean">		
				<p class="mtop0 mbottom05 color888">
					<bean:message key="label.create.inproceedings.instructions" bundle="RESEARCHER_RESOURCES"/>
				</p>
			</logic:equal>
			
			<logic:equal name="publicationBean" property="class.simpleName" value="ProceedingsBean">		
				<p class="mtop0 mbottom05 color888">
					<bean:message key="label.create.proceedings.instructions" bundle="RESEARCHER_RESOURCES"/>
				</p>
			</logic:equal>
			

			<fr:edit id="publicationBean" name="publicationBean" schema="<%= publicationBean.getActiveSchema() %>" nested="true">
		 	    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="tstyle5 thright thlight thtop"/>
		        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		        	<fr:property name="requiredMarkShown" value="true"/>
			    </fr:layout>
		   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
		   		<fr:destination name="typePostBack" path="/resultPublications/changeType.do"/>
			</fr:edit>
			
			
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
				<logic:notEqual name="publicationBean" property="class.simpleName" value="ArticleBean">
					<logic:notEqual name="publicationBean" property="class.simpleName" value="ProceedingsBean">
						<logic:notEqual name="publicationBean" property="class.simpleName" value="InproceedingsBean">
							<bean:message bundle="RESEARCHER_RESOURCES" key="button.insert"/>
						</logic:notEqual>
					</logic:notEqual>
				</logic:notEqual>
				<logic:equal name="publicationBean" property="class.simpleName" value="ArticleBean">
					<bean:message bundle="RESEARCHER_RESOURCES" key="button.next"/>
				</logic:equal>
				<logic:equal name="publicationBean" property="class.simpleName" value="ProceedingsBean">
					<bean:message bundle="RESEARCHER_RESOURCES" key="button.next"/>
				</logic:equal>
				<logic:equal name="publicationBean" property="class.simpleName" value="InproceedingsBean">
					<bean:message bundle="RESEARCHER_RESOURCES" key="button.next"/>
				</logic:equal>
			</html:submit>
			
			<html:cancel>
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
			</html:cancel>
			
		</fr:form>
	</logic:equal>
</logic:equal>

<logic:equal name="publicationBean" property="createJournal" value="true">

		<logic:equal name="publicationBean" property="class.simpleName" value="ArticleBean">
		<p class="breadcumbs"><span><bean:message key="label.1stStepInsertPublicationData" bundle="RESEARCHER_RESOURCES"/></span> > <span class="actual"><bean:message key="label.2ndStepAssociateJournal" bundle="RESEARCHER_RESOURCES"/></span></p>
		</logic:equal>
		
		<p class="mtop1 mbottom05"><b><bean:message key="label.articleData" bundle="RESEARCHER_RESOURCES"/></b></p>
		<fr:view name="publicationBean" schema="result.publication.create.Article.readOnly" >
	 	    <fr:layout name="tabular-nonNullValues">
	    	    <fr:property name="classes" value="tstyle1 thright thlight thtop mtop05"/>
	        	<fr:property name="columnClasses" value="width10em,,"/>
	        	<fr:property name="rowClasses" value=",bold,,,,,,,"/>
		    </fr:layout>
		</fr:view>

	<logic:notPresent name="issueBean">
		<logic:notPresent name="publicationBean" property="scientificJournal">
			<p class="mbottom05"><strong><bean:message key="label.articleJournal" bundle="RESEARCHER_RESOURCES"/></strong></p>

			<p class="color888 mtop05 mbottom1"><bean:message key="label.chooseJournal.instructions" bundle="RESEARCHER_RESOURCES"/></p>
			
			<logic:present name="publicationBean" property="scientificJournalName">
				<div class="warning0 mvert1">
					<p style="margin:0; padding: 0.5em 0.75em;">
						<b><bean:message key="label.attention" bundle="RESEARCHER_RESOURCES"/>:</b><br/>
						<bean:message key="label.informationForCreateMagazine" bundle="RESEARCHER_RESOURCES"/>
					</p>
				</div>
			</logic:present>				
			
			<div class="dinline forminline">						
				<fr:form action="/resultPublications/createWrapper.do">
					<fr:edit id="publicationBean" name="publicationBean" schema="result.publication.create.Article.selectMagazine">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
				        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
							<fr:property name="requiredMarkShown" value="true"/>
						</fr:layout>
					</fr:edit>
					<br/><br/>
					<html:submit><bean:message key="label.chooseMagazineFromList" bundle="RESEARCHER_RESOURCES"/></html:submit>
					<logic:present name="publicationBean" property="scientificJournalName">
						<html:submit property="new"><bean:message key="label.createMagazine" bundle="RESEARCHER_RESOURCES"/></html:submit>	
					</logic:present>
				</fr:form>
				
				<fr:form action="/resultPublications/listPublications.do">
					<html:submit><bean:message key="button.cancel"/></html:submit>
				</fr:form>
			</div>
		</logic:notPresent>

		<logic:present name="publicationBean" property="scientificJournal">
			<p><strong><bean:message key="label.articleIssue" bundle="RESEARCHER_RESOURCES"/></strong></p>
			<p class="color888 mtop05 mbottom1"><bean:message key="label.chooseIssue.instructions" bundle="RESEARCHER_RESOURCES"/></p>
			<div class="dinline forminline">	
				<fr:form action="/resultPublications/create.do">
					<fr:edit id="publicationBean" name="publicationBean" schema="result.publication.create.Article.selectIssue">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thright thlight thtop dinline mtop05"/>
							<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
					</fr:edit>
					<br/><br/>
					<html:submit property="confirm"><bean:message key="label.chooseIssue" bundle="RESEARCHER_RESOURCES"/></html:submit>
				</fr:form>
				<fr:form action="/resultPublications/createJournal.do">
					<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
					<html:submit><bean:message key="label.createNewIssue" bundle="RESEARCHER_RESOURCES"/></html:submit>	
				</fr:form>
				<fr:form action="/resultPublications/listPublications.do">
					<html:submit><bean:message key="button.cancel"/></html:submit>
				</fr:form>
			</div>
		</logic:present>					
	</logic:notPresent>					

	<logic:present name="issueBean">					
			<logic:messagesPresent message="true">
				<p>
				<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
					<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
				</html:messages>
				</p>
	   	    </logic:messagesPresent>
		<div class="dinline forminline">	
			<fr:form action="/resultPublications/createJournal.do">
				<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
				<fr:edit id="createMagazine" name="issueBean" visible="false"/>
				
				<p class="mtop15 mbottom025"><strong><bean:message key="label.journal" bundle="RESEARCHER_RESOURCES"/></strong></p>
				<logic:equal name="issueBean" property="journalAlreadyChosen" value="false">
				<fr:edit id="journalInfo" name="issueBean" schema="result.publication.create.Article.createMagazine">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thtop mtop025"/>
						<fr:property name="requiredMarkShown" value="true"/>
	        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
					<fr:destination name="invalid"  path="/resultPublications/createJournal.do"/>
				</fr:edit>

				</logic:equal>
				<logic:equal name="issueBean" property="journalAlreadyChosen" value="true">
					<span><fr:view name="issueBean" property="journal.name"/></span>
				</logic:equal>
				
				<p class="mtop15 mbottom05"><strong><bean:message key="label.journalIssue" bundle="RESEARCHER_RESOURCES"/></strong></p>

				<bean:define id="issueSchema" value="result.publication.create.Article.createIssue" type="java.lang.String"/>
				<logic:equal name="issueBean" property="specialIssue" value="true">
					<bean:define id="issueSchema" value="result.publication.create.Article.createSpecialIssue" type="java.lang.String"/>
				</logic:equal>
				
				<fr:edit id="issueInfo" name="issueBean" schema="<%= issueSchema %>">
					<fr:layout name="tabular">
					 <fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
	        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	        		 <fr:property name="requiredMarkShown" value="true"/>
					</fr:layout>
					<fr:destination name="postBack" path="/resultPublications/changeSpecialIssueInCreation.do"/>
					<fr:destination name="invalid"  path="/resultPublications/createJournal.do"/>
				</fr:edit>
				<br/><br/>
				<html:submit>
				<logic:equal name="issueBean" property="journalAlreadyChosen" value="false">
				<bean:message key="label.insertJournalArticle" bundle="RESEARCHER_RESOURCES"/>
				</logic:equal>
				<logic:equal name="issueBean" property="journalAlreadyChosen" value="true">					
				<bean:message key="label.createNewIssue" bundle="RESEARCHER_RESOURCES"/>
				</logic:equal>
				</html:submit>
			</fr:form>
		   <fr:form action="/resultPublications/listPublications.do">
			<html:submit><bean:message key="button.cancel"/></html:submit>
	   	   </fr:form>
	   	   </div>
</logic:present>

</logic:equal>

<!-- Create event in case of inproceedings or proceedings -->
<logic:equal name="publicationBean" property="createEvent" value="true">
	<logic:equal name="publicationBean" property="class.simpleName" value="InproceedingsBean">
	<p class="breadcumbs"><span><bean:message key="label.1stStepInsertPublicationData" bundle="RESEARCHER_RESOURCES"/></span> > <span class="actual"><bean:message key="label.2ndStepAssociateEvent" bundle="RESEARCHER_RESOURCES"/></span></p>
	</logic:equal>
	<logic:equal name="publicationBean" property="class.simpleName" value="ProceedingsBean">
	<p class="breadcumbs"><span><bean:message key="label.1stStepInsertPublicationData" bundle="RESEARCHER_RESOURCES"/></span> > <span class="actual"><bean:message key="label.2ndStepAssociateEvent" bundle="RESEARCHER_RESOURCES"/></span></p>
	</logic:equal>

	<p class="mtop1 mbottom05"><b><bean:message key="label.publicationData" bundle="RESEARCHER_RESOURCES"/></b></p>
	
	<!-- Shows conference article  -->
	<logic:present name="eventEditionBean">
		<bean:define id="publicationSchema" name="publicationBean" property="activeSchema" type="java.lang.String" />
		<fr:view name="publicationBean" schema="<%= publicationSchema %>">
	 	    <fr:layout name="tabular-nonNullValues">
	    	    <fr:property name="classes" value="tstyle1 thright thlight thtop mtop05"/>
	        	<fr:property name="columnClasses" value="width10em,,"/>
	        	<fr:property name="rowClasses" value=",bold,,,,,,,"/>
		    </fr:layout>
	     </fr:view>
	     
	     <div class="dinline forminline">
			<fr:form action="/resultPublications/createEvent.do">
				<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
				
				<!-- Select event, using autocomplete -->
				<logic:equal name="eventEditionBean" property="selectEventState" value="true">

					<p class="mbottom05"><strong><bean:message key="label.publicationEvent" bundle="RESEARCHER_RESOURCES"/></strong></p>
					
					<p class="color888 mtop05 mbottom1"><bean:message key="label.chooseEvent.firstInstructions" bundle="RESEARCHER_RESOURCES"/></p>

					<logic:present name="eventEditionBean" property="eventName">
						<div class="warning0">
							<p style="margin:0 0 1em 0; padding: 0.5em 0.75em;">
								<b><bean:message key="label.attention" bundle="RESEARCHER_RESOURCES"/>:</b><br/>
								<bean:message key="label.informationForCreateEvent" bundle="RESEARCHER_RESOURCES"/>
							</p>
						</div>
					</logic:present>
					
					<fr:edit id="eventEditionBean" name="eventEditionBean" schema="result.publication.select.Event">
						<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
					        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					        	<fr:property name="requiredMarkShown" value="true"/>
							</fr:layout>
						<fr:destination name="invalid" path="/resultPublications/prepareCreateEvent.do"/>
					</fr:edit>
					<br/><br/>
					<html:submit property="goToNextStep"><bean:message key="label.chooseEventFromList" bundle="RESEARCHER_RESOURCES"/></html:submit>
					<logic:present name="eventEditionBean" property="eventName">
						<html:submit property="createNewEvent"><bean:message key="label.createEvent" bundle="RESEARCHER_RESOURCES"/></html:submit>	
					</logic:present>
				</logic:equal>
				 
				 <!-- Select event edition, using autocomplete -->
				 <logic:equal name="eventEditionBean" property="selectEventEditionState" value="true">
				 	<p class="mbottom05"><strong><bean:message key="label.articleEventEdition" bundle="RESEARCHER_RESOURCES"/></strong></p>
					<p class="color888 mtop05 mbottom1"><bean:message key="label.chooseEventEdition.instructions" bundle="RESEARCHER_RESOURCES"/></p>
					
					<fr:edit id="eventEditionBean" name="eventEditionBean" schema="result.publication.select.EventEdition">
						<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
					        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					        	<fr:property name="requiredMarkShown" value="true"/>
							</fr:layout>
						<fr:destination name="invalid" path="/resultPublications/prepareCreateEvent.do"/>			
					</fr:edit>
					<br/><br/>
					<html:submit property="goToNextStep"><bean:message key="label.chooseEventEditionFromList" bundle="RESEARCHER_RESOURCES"/></html:submit>
				 </logic:equal>
		 
				<!-- Create new event --> 
			 	<logic:equal name="eventEditionBean" property="newEventState" value="true">
			 		<p><strong><bean:message key="label.event" bundle="RESEARCHER_RESOURCES"/></strong></p>
				 	<p class="color888"><bean:message key="label.newEvent.instructions" bundle="RESEARCHER_RESOURCES"/></p>
				 	
			 		<fr:edit id="eventCreationBean" name="eventEditionBean" schema="result.publication.createEvent">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thright thlight thtop mtop0"/>
	        				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	        				<fr:property name="requiredMarkShown" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="/resultPublications/prepareCreateEvent.do"/>			
					</fr:edit>

					<p class="mtop15"><strong><bean:message key="label.eventEdition" bundle="RESEARCHER_RESOURCES"/></strong></p>
				 	<p class="color888"><bean:message key="label.newEventEdition.instructions" bundle="RESEARCHER_RESOURCES"/></p>

					<fr:edit id="eventEditionBean" name="eventEditionBean" schema="result.publication.create.NewEventEdition">
						<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
					        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					        	<fr:property name="requiredMarkShown" value="true"/>
							</fr:layout>
						<fr:destination name="invalid" path="/resultPublications/prepareCreateEvent.do"/>			
					</fr:edit>
					<br/><br/>
					<html:submit property="goToNextStep"><bean:message key="label.insertArticle" bundle="RESEARCHER_RESOURCES"/></html:submit>
			 	</logic:equal>

			 	<!-- Create new event edition --> 
			 	<logic:equal name="eventEditionBean" property="newEventEditionState" value="true">
			 		<p><strong><bean:message key="label.event" bundle="RESEARCHER_RESOURCES"/></strong></p>
			 		<p><span><fr:view name="eventEditionBean" property="event.name" /></span></p>
			 		
					<p class="mtop15"><strong><bean:message key="label.eventEdition" bundle="RESEARCHER_RESOURCES"/></strong></p>
				 	<p class="color888 mbottom1"><bean:message key="label.newEventEdition.instructions" bundle="RESEARCHER_RESOURCES"/></p>
				 	
				 	<fr:edit id="eventEditionBean" name="eventEditionBean" schema="result.publication.create.NewEventEdition">
						<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
					        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					        	<fr:property name="requiredMarkShown" value="true"/>
							</fr:layout>
						<fr:destination name="invalid" path="/resultPublications/prepareCreateEvent.do"/>			
					</fr:edit>
					<br/><br/>
				 	<html:submit property="goToNextStep"><bean:message key="label.createEventEdition" bundle="RESEARCHER_RESOURCES"/></html:submit>
			 	</logic:equal>
			</fr:form>

			<!-- Auxiliary form to create a new event edition (presents a button in select event edition state) -->
			<logic:equal name="eventEditionBean" property="selectEventEditionState" value="true">
				<fr:form action="/resultPublications/createEvent.do">
					<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
					<fr:edit id="eventEditionBean" name="eventEditionBean" visible="false"/>
					<html:submit property="createNewEventEdition">
						<bean:message key="label.createEventEdition" bundle="RESEARCHER_RESOURCES"/>
					</html:submit>
				</fr:form>
			</logic:equal>
			<fr:form action="/resultPublications/listPublications.do">
				<html:submit><bean:message key="button.cancel"/></html:submit>
			</fr:form>
		</div>
	</logic:present>
</logic:equal>
