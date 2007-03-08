<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

<logic:present role="RESEARCHER">		
	<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"/>

	<!-- Insert new publication -->
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.create"/></h2>
	
	
	<logic:messagesPresent message="true">
		<p>
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
		</html:messages>
		</p>
	</logic:messagesPresent>


		<logic:equal name="publicationBean" property="createJournal" value="false">
	
		<logic:equal name="publicationBean" property="class.simpleName" value="ArticleBean">
			<p class="breadcumbs"><span class="actual"><bean:message key="label.1stStepInsertPublicationData" bundle="RESEARCHER_RESOURCES"/></span> > <span><bean:message key="label.2ndStepAssociateJournal" bundle="RESEARCHER_RESOURCES"/></span></p>
		</logic:equal>

		<fr:form action="/resultPublications/create.do">
		
		<logic:equal name="publicationBean" property="createEvent" value="false">
			<!-- Present Author -->
			<logic:notEqual name="publicationBean" property="class.simpleName" value="ProceedingsBean">
			<p class="mtop15 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.author"/></b></p>
			</logic:notEqual>
			<logic:equal name="publicationBean" property="class.simpleName" value="ProceedingsBean">
			<p class="mtop15 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.editor"/></b></p>
			</logic:equal>
			<fr:edit id="author" name="publicationBean" schema="<%= publicationBean.getParticipationSchema() %>" nested="true">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
		   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
			</fr:edit>	
			
			<!-- Present publication fields -->
			<p class="mtop1 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES"key="<%="researcher.ResultPublication.type." + publicationBean.getPublicationTypeString() %>"/></b></p>
			<fr:edit id="publicationBean" name="publicationBean" schema="<%= publicationBean.getActiveSchema() %>" nested="true">
		 	    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="tstyle5 thright thlight thtop"/>
		        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			    </fr:layout>
		   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
		   		<fr:destination name="typePostBack" path="/resultPublications/changeType.do"/>
			</fr:edit>
		</logic:equal>

		<!-- Create event in case of inproceedings or proceedings -->
		<logic:equal name="publicationBean" property="createEvent" value="true">
			<!-- Present Event -->
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.createConference"/>
			<fr:edit id="createEvent" name="publicationBean" schema="result.publication.create.Event" nested="true">
		 	    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="tstyle5 thright thlight thtop"/>
		        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			    </fr:layout>
		   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
		   		<fr:destination name="input" path="/resultPublications/prepareCreate.do"/>
			</fr:edit>
			
			<!-- Present Author -->
			<p class="mtop15 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.author"/></b></p>
			<fr:view name="publicationBean" schema="<%= publicationBean.getParticipationSchema() %>">
				<fr:layout name="tabular">
	    		    <fr:property name="classes" value="tstyle5 thlight thright thtop width600px"/>
    		    	<fr:property name="columnClasses" value="width12em,,"/>
				</fr:layout>
			</fr:view>	
		
			<!-- Present publication fields -->
			<p class="mtop1 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES"key="<%="researcher.ResultPublication.type." + publicationBean.getPublicationTypeString() %>"/></b></p>
			<fr:view name="publicationBean" schema="<%= publicationBean.getActiveSchema() %>">
				<fr:layout name="tabular">
    		    	<fr:property name="classes" value="tstyle5 thlight thright thtop width600px"/>
	    	    	<fr:property name="columnClasses" value="width12em,,"/>
				</fr:layout>
			</fr:view>
		</logic:equal>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
			<logic:notEqual name="publicationBean" property="class.simpleName" value="ArticleBean">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.create"/>
			</logic:notEqual>
			<logic:equal name="publicationBean" property="class.simpleName" value="ArticleBean">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.next"/>
			</logic:equal>
			
		</html:submit>
		<html:cancel>
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:cancel>

		</fr:form>

		</logic:equal>

	<logic:equal name="publicationBean" property="createJournal" value="true">
	
			<logic:equal name="publicationBean" property="class.simpleName" value="ArticleBean">
			<p class="breadcumbs"><span><bean:message key="label.1stStepInsertPublicationData" bundle="RESEARCHER_RESOURCES"/></span> > <span class="actual"><bean:message key="label.2ndStepAssociateJournal" bundle="RESEARCHER_RESOURCES"/></span></p>
			</logic:equal>
			
			<p class="mtop1 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES"key="<%="researcher.ResultPublication.type." + publicationBean.getPublicationTypeString() %>"/></b></p>
			
			<fr:view name="publicationBean" schema="result.publication.create.Article.readOnly" >
		 	    <fr:layout name="tabular-nonNullValues">
		    	    <fr:property name="classes" value="tstyle1 thright thlight thtop"/>
		        	<fr:property name="columnClasses" value="width10em,,"/>
		        	<fr:property name="rowClasses" value=",bold,,,,,,,"/>
			    </fr:layout>
			</fr:view>

		<logic:notPresent name="issueBean">
			<logic:notPresent name="publicationBean" property="scientificJournal">
			
				<logic:present name="publicationBean" property="scientificJournalName">
					<div class="warning0">
						<p style="margin:0; padding: 0.5em 0.75em;">
							<b><bean:message key="label.attention" bundle="RESEARCHER_RESOURCES"/>:</b><br/>
							<bean:message key="label.informationForCreateMagazine" bundle="RESEARCHER_RESOURCES"/>
						</p>
					</div>
				</logic:present>
				
				
				<p><strong><bean:message key="label.articleJournal" bundle="RESEARCHER_RESOURCES"/></strong>:</p>
				<p class="color888"><bean:message key="label.chooseJournal.instructions" bundle="RESEARCHER_RESOURCES"/></p>
				<div class="dinline forminline">						
				<fr:form action="/resultPublications/createWrapper.do">
				<fr:edit id="publicationBean" name="publicationBean" schema="result.publication.create.Article.selectMagazine">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thtop thmiddle"/>
			        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>
					<br/><br/>
					<html:submit property="confirm"><bean:message key="label.chooseMagazineFromList" bundle="RESEARCHER_RESOURCES"/></html:submit>
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
			
				<p><strong><bean:message key="label.articleIssue" bundle="RESEARCHER_RESOURCES"/></strong>:</p>
				<p class="color888"><bean:message key="label.chooseIssue.instructions" bundle="RESEARCHER_RESOURCES"/></p>
				
				<div class="dinline forminline">	
				<fr:form action="/resultPublications/create.do">
					<fr:edit id="publicationBean" name="publicationBean" schema="result.publication.create.Article.selectIssue">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thtop"/>
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
			<div class="dinline forminline">	
				<fr:form action="/resultPublications/createJournal.do">
					<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
					<fr:edit id="createMagazine" name="issueBean" visible="false"/>
					
					<p class="mtop1 mbottom05"><strong><bean:message key="label.journal" bundle="RESEARCHER_RESOURCES"/></strong>:</p>
					<logic:equal name="issueBean" property="journalAlreadyChosen" value="false">
					<fr:edit id="journalInfo" name="issueBean" schema="result.publication.create.Article.createMagazine">
						<fr:layout name="tabular">
						 <fr:property name="classes" value="tstyle5 thright thlight thtop mtop05"/>
		        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
					</fr:edit>
					</logic:equal>
					<logic:equal name="issueBean" property="journalAlreadyChosen" value="true">
						<span><fr:view name="issueBean" property="journal.name"/></span>
					</logic:equal>
					
					<p class="mtop1 mbottom05"><strong><bean:message key="label.journalIssue" bundle="RESEARCHER_RESOURCES"/></strong>:</p>
					<fr:edit id="journalInfo" name="issueBean" schema="result.publication.create.Article.createIssue">
						<fr:layout name="tabular">
						 <fr:property name="classes" value="tstyle5 thright thlight thtop mtop05"/>
		        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
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

</logic:present>
