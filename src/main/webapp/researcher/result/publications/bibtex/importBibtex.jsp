<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex"/></h2>

<fr:form action="/bibtexManagement/ignorePublication.do">
	<p>
		<strong>
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.bibtexToProcess"/> 
			(<fr:view name="importBibtexBean" property="currentPublicationPosition"/>/<fr:view name="importBibtexBean" property="totalPublicationsReaded"/>)
		</strong>
	</p>
	<fr:edit id="importBibtexBean" visible="false" name="importBibtexBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.ImportBibtexBean"/>
	<fr:view name="importBibtexBean" property="currentBibtex"/>
	<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.ignoreBibtexPublication"/></html:submit>
</fr:form>
	
<logic:equal name="importBibtexBean" property="currentProcessedParticipators" value="false">

		<logic:notEmpty name="importBibtexBean" property="parsingErrors">
				<bean:size id="numberOfErrors" name="importBibtexBean" property="parsingErrors"/>
				<span class="error0"><bean:message key="error.importBibtex.expandingReferences" arg0="<%= numberOfErrors.toString() %>"/></span><br/>
				<logic:iterate id="error" name="importBibtexBean" property="parsingErrors">
					<fr:view name="error" property="message">
						<fr:layout>
							<fr:property name="classes" value="error0"/>
						</fr:layout>
					</fr:view>
				</logic:iterate>
		</logic:notEmpty>
		
<logic:notPresent name="importBibtexBean" property="nextAuthor">
<logic:notPresent name="importBibtexBean" property="nextEditor">
	<logic:notEqual name="importBibtexBean" property="currentProcessedParticipators" value="true">
		
			<p class="mbottom05"><bean:message key="researcher.result.publication.importBibtex.authors" bundle="RESEARCHER_RESOURCES"/></p>
			<logic:iterate id="participationBean" name="importBibtexBean" property="processedBeans" indexId="i">
			<ul class="mvert05">
				<li>
					<bean:message key="label.bibtexPerson" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="participationBean" property="bibtexPerson"/><br/>
					<bean:message key="label.name" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="participationBean" property="name"/><br/>
					<bean:message key="label.person" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="participationBean" property="personType"/>
				</li>
			</ul>
				
				<fr:form id="<%= "editBeans" + i %>" action="<%= "/bibtexManagement/editParticipation.do?index=" + i %>">
					<fr:edit name="importBibtexBean" id="<%= "importBibtexBean" + i %>" visible="false"/>

				    <p class="switchInline">
				    	<div style="margin-left: 3em; margin-top: -0.25em; margin-bottom: 1.5em;">
						<a href="<%= "javascript:document.getElementById('editBeans" + i + "').submit();"%>">
						<bean:message key="button.edit" bundle="RESEARCHER_RESOURCES"/>
						</a>
						</div>
					</p>

					<p class="switchNone" style="margin-left: 3em;">
						<html:submit><bean:message key="button.edit" bundle="RESEARCHER_RESOURCES"/></html:submit>
					</p>

				</fr:form>
			</logic:iterate>

			<script type="text/javascript" language="javascript">
			switchGlobal();
			</script>
	</logic:notEqual>
</logic:notPresent>
</logic:notPresent>

<logic:present name="importBibtexBean" property="nextAuthor">

	<fr:form action="/bibtexManagement/nextParticipation.do?participationType=author">
		<fr:edit id="importBibtexBean" visible="false" name="importBibtexBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.ImportBibtexBean"/>
		
		<logic:messagesPresent message="true">
			<p class="mtop15">
				<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
						<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
				</html:messages>
			</p>
		</logic:messagesPresent>


		<h3 class="mtop15 mbottom05"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.author"/> (<bean:write name="importBibtexBean" property="currentAuthorPosition"/>/<bean:write name="importBibtexBean" property="numberOfAuthors"/>) </h3>


		<p class="mvert1 breadcumbs">
			<span class="actual">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 1 : </strong>
			<bean:message key="researcher.result.publication.importBibtex.insertAuthorsAndEditors" bundle="RESEARCHER_RESOURCES"/></span>
			 > 
			<span>
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 2 : </strong>
			<bean:message key="researcher.result.publication.importBibtex.publicationData" bundle="RESEARCHER_RESOURCES"/></span>
			</span>
	 	</p>

			<logic:notEmpty name="importBibtexBean" property="processedBeans"> 
			<p class="mbottom05"><bean:message key="researcher.result.publication.importBibtex.authors" bundle="RESEARCHER_RESOURCES"/></p>
			</logic:notEmpty>
			<logic:iterate id="participationBean" name="importBibtexBean" property="processedBeans">
			<ul class="mvert05">
				<li>
					<bean:message key="label.bibtexPerson" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="participationBean" property="bibtexPerson"/><br/>
					<bean:message key="label.name" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="participationBean" property="name"/><br/>
					<bean:message key="label.person" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="participationBean" property="personType"/>
				</li>
			</ul>
			</logic:iterate>
			
				<p class="mtop1 mbottom025"><bean:message key="label.insertAuthor" bundle="RESEARCHER_RESOURCES"/></p>
				<bean:define id="participatorBean" name="importBibtexBean" property="nextAuthor" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexParticipatorBean"/>
				<fr:edit id="author" name="importBibtexBean" property="nextAuthor" schema="<%=participatorBean.getActiveSchema()%>">
					<fr:layout name="tabular">
	    			    <fr:property name="classes" value="tstyle5 thright thlight thtop mtop025"/>
		        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
				    <fr:destination name="change.person.type" path="/bibtexManagement/changePersonType.do"/>
			   		<fr:destination name="invalid" path="/bibtexManagement/invalidSubmit.do"/>
		   		</fr:edit>

		   		<logic:equal name="needsUnit" value="true">
					<logic:equal name="participatorBean" property="externalUnit" value="true">	
						<bean:define id="unitSchema" value="bibtex.participatorExternal.externalUnit.readOnly" toScope="request"/>
					</logic:equal>
					<logic:equal name="participatorBean" property="externalUnit" value="false">						
						<bean:define id="unitSchema" value="bibtex.participatorExternal.internalUnit" toScope="request"/>
					</logic:equal>

					<bean:define id="unitSchema" name="unitSchema" type="java.lang.String"/>
					<p class="mtop05 mbottom025"><bean:message key="label.unit" bundle="RESEARCHER_RESOURCES"/>:</p>
		   			<fr:edit id="externalPerson" name="importBibtexBean" property="nextAuthor" schema="<%= unitSchema %>">
					<fr:layout name="tabular">
	    			    <fr:property name="classes" value="tstyle5 thright thlight thtop mtop025"/>
		        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>			 
				   		<fr:destination name="postBack" path="/bibtexManagement/changeUnitType.do"/>  			
		   			</fr:edit>
		   		</logic:equal>
		   		
		<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.submit"/></html:submit>
	</fr:form>
</logic:present>

<logic:notPresent name="importBibtexBean" property="nextAuthor">
	<logic:present name="importBibtexBean" property="nextEditor">
		<fr:form action="/bibtexManagement/nextParticipation.do?participationType=editor">
		<fr:edit id="importBibtexBean" visible="false" name="importBibtexBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.ImportBibtexBean"/>
			<h3 class="mtop15 mbottom05"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.editor"/> (<bean:write name="importBibtexBean" property="currentEditorPosition"/>/<bean:write name="importBibtexBean" property="numberOfEditors"/>) </h3>
			
			<p class="mvert1 breadcumbs">
				<span class="actual">
				<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 1 : </strong>
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.setAuthorsAndEditors"/></span>
					 > 
				<span>
					<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 2 : </strong>
			 	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.setPublicationData"/>
				</span>
		 	</p>
			
		<logic:notEmpty name="importBibtexBean" property="processedBeans"> 
			<p class="mbottom05"><bean:message key="researcher.result.publication.importBibtex.authors" bundle="RESEARCHER_RESOURCES"/></p>
			</logic:notEmpty>
			<logic:iterate id="participationBean" name="importBibtexBean" property="processedBeans">
			<ul class="mvert05">
				<li>
					<bean:message key="label.bibtexPerson" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="participationBean" property="bibtexPerson"/><br/>
					<bean:message key="label.name" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="participationBean" property="name"/><br/>
					<bean:message key="label.person" bundle="RESEARCHER_RESOURCES"/>: <fr:view name="participationBean" property="personType"/>
				</li>
			</ul>
			</logic:iterate>
			
			<logic:messagesPresent message="true">
				<p class="mtop15">
					<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
						<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
					</html:messages>
				</p>
			</logic:messagesPresent>
				
				<bean:message key="label.insertEditor" bundle="RESEARCHER_RESOURCES"/>
				<bean:define id="participatorBean" name="importBibtexBean" property="nextEditor" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexParticipatorBean"/>
				<fr:edit id="editor" name="importBibtexBean" property="nextEditor" schema="<%=participatorBean.getActiveSchema()%>">
					<fr:layout name="tabular">
	    			    <fr:property name="classes" value="tstyle5 thright thlight thtop mtop05"/>
		        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
				    <fr:destination name="change.person.type" path="/bibtexManagement/changePersonType.do"/>
			   		<fr:destination name="invalid" path="/bibtexManagement/invalidSubmit.do"/>
		   		</fr:edit>
		   		<logic:equal name="participatorBean" property="externalPerson" value="true">

				    <logic:equal name="needsUnit" value="true">
					<logic:equal name="participatorBean" property="externalUnit" value="true">
						<bean:define id="unitSchema" value="bibtex.participatorExternal.externalUnit.readOnly" toScope="request"/>
					</logic:equal>
					<logic:equal name="participatorBean" property="externalUnit" value="false">
						<bean:define id="unitSchema" value="bibtex.participatorExternal.internalUnit" toScope="request"/>
					</logic:equal>

					<bean:define id="unitSchema" name="unitSchema" type="java.lang.String"/>
					<p class="mtop05 mbottom025"><bean:message key="label.unit" bundle="RESEARCHER_RESOURCES"/>:</p>
		   			<fr:edit id="externalPerson" name="importBibtexBean" property="nextEditor" schema="<%= unitSchema %>">
					<fr:layout name="tabular">
	    			    <fr:property name="classes" value="tstyle5 thright thlight thtop mtop05"/>
		        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>			   			
			   		<fr:destination name="postBack" path="/bibtexManagement/changeUnitType.do"/>
		   			</fr:edit>
		   			</logic:equal>
		   		</logic:equal>
		   		
		<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.continue"/></html:submit>
	</fr:form>
	</logic:present>
</logic:notPresent>
<%-- 
	<fr:form action="/bibtexManagement/setParticipations.do">
		<fr:edit id="importBibtexBean" visible="false" name="importBibtexBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.ImportBibtexBean"/>

		<b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 1 : </b>
		<u><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.setAuthorsAndEditors"/></u> >
		<b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 2 : </b>
	 	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.setPublicationData"/>
	
		<!-- Set Participations -->
		<logic:notEmpty name="importBibtexBean" property="currentAuthors">
			<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.authors"/></h3>
			<logic:iterate id="author" name="importBibtexBean" property="currentAuthors">
				<bean:define id="participatorBean" name="author" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexParticipatorBean"/>
				<fr:edit id="<%=participatorBean.getBibtexPerson()%>" nested="true" name="author" schema="<%=participatorBean.getActiveSchema()%>">
					<fr:layout name="tabular">
	    			    <fr:property name="classes" value="tstyle1 thright thlight thtop"/>
		        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
			   		<fr:destination name="invalid" path="/bibtexManagement/invalidSubmit.do"/>
		   		</fr:edit>
			</logic:iterate>
		</logic:notEmpty>
		
		<logic:notEmpty name="importBibtexBean" property="currentEditors">
			<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.editors"/></h3>
			<logic:iterate id="editor" name="importBibtexBean" property="currentEditors">
				<bean:define id="participatorBean" name="editor" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.BibtexParticipatorBean"/>
				<fr:edit id="<%=participatorBean.getBibtexPerson()%>" nested="true" name="editor" schema="<%=participatorBean.getActiveSchema()%>">
				    <fr:layout name="tabular">
			    	    <fr:property name="classes" value="tstyle1 thright thlight thtop"/>
			        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
					<fr:destination name="invalid" path="/bibtexManagement/invalidSubmit.do"/>
		   		</fr:edit>
			</logic:iterate>
		</logic:notEmpty>

		<i><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.warningParticipants"/></i><br/>
		<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.continue"/></html:submit>
	</fr:form>
	
	--%>
</logic:equal>
	
<logic:equal name="importBibtexBean" property="currentProcessedParticipators" value="true">
	<bean:define id="publicationData" name="importBibtexBean" property="currentPublicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"/>	
	<fr:form action="/bibtexManagement/createPublicationWrapper.do">
	<fr:edit id="importBibtexBean" visible="false" name="importBibtexBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.ImportBibtexBean"/>

	<logic:notPresent name="issueBean"> 
	<logic:notPresent name="eventBean">

	<!-- Set Publication data -->
	<h3 class="mtop15 mbottom05"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.publicationData"/></h3>

	<p class="mvert1 breadcumbs">
	<span>
	<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 1 : </strong>
	<bean:message key="researcher.result.publication.importBibtex.insertAuthorsAndEditors" bundle="RESEARCHER_RESOURCES"/></span>
	 > 
	<span class="actual">
	<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 2 : </strong>
		<bean:message key="researcher.result.publication.importBibtex.publicationData" bundle="RESEARCHER_RESOURCES"/></span>
	</span>
 	</p>
		 	
			<logic:messagesPresent message="true">
				<p>
					<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
						<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
					</html:messages>
				</p>
			</logic:messagesPresent>
		 	
			<fr:edit nested="true" id="publicationData" name="importBibtexBean" property="currentPublicationBean" schema="<%=publicationData.getActiveSchema()%>">
		    <fr:layout name="tabular">
	    	    <fr:property name="classes" value="tstyle5 thright thlight thtop mtop05"/>
	        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	        	<fr:property name="requiredMarkShown" value="true"/>
		    </fr:layout>
				<fr:destination name="invalid" path="/bibtexManagement/invalidSubmit.do"/>
	   		</fr:edit>
	   	
  	
		<bean:define id="label" value="button.create"/>
		<logic:equal name="importBibtexBean" property="currentPublicationBean.class.simpleName" value="ArticleBean">
			<bean:define id="label" value="button.next"/>
		</logic:equal>
		<logic:equal name="importBibtexBean" property="currentPublicationBean.class.simpleName" value="ProceedingsBean">
			<bean:define id="label" value="button.next"/>
		</logic:equal>
		<logic:equal name="importBibtexBean" property="currentPublicationBean.class.simpleName" value="InproceedingsBean">
			<bean:define id="label" value="button.next"/>
		</logic:equal>
		<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="<%= label %>"/></html:submit>
	</logic:notPresent>
	</logic:notPresent>

	<logic:present name="eventBean">
		<!-- Create event in case of inproceedings or proceedings -->
		
		<fr:edit id="createEvent"  name="eventBean" visible="false"/>
		<%-- 
		<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.publicationData"/></h3>
			<fr:view name="importBibtexBean" property="currentPublicationBean" schema="<%=publicationData.getActiveSchema()%>">
				<fr:layout name="tabular">
		    	    <fr:property name="classes" value="tstyle5 thlight thright thtop width600px"/>
    				<fr:property name="columnClasses" value="width12em,,"/>
				</fr:layout>
   		</fr:view>
   			--%>
		<logic:equal name="publicationData" property="createEvent" value="true">

			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.createConference"/>
			<fr:edit id="createNewEvent" name="importBibtexBean" property="currentPublicationBean" schema="result.publication.create.Event" nested="true">
			    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="tstyle5 thright thlight thtop"/>
		        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		        	<fr:property name="requiredMarkShown" value="true"/>
			    </fr:layout>
		   		<fr:destination name="invalid" path="/bibtexManagement/invalidSubmit.do"/>
		   		<fr:destination name="input" path="/bibtexManagement/invalidSubmit.do"/>
			</fr:edit>
			<fr:edit id="createEventEdition" name="eventBean" schema="result.publication.create.NewEventEdition" nested="true">
			    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="tstyle5 thright thlight thtop"/>
		        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		        	<fr:property name="requiredMarkShown" value="true"/>
			    </fr:layout>
		   		<fr:destination name="invalid" path="/bibtexManagement/invalidSubmit.do"/>
		   		<fr:destination name="input" path="/bibtexManagement/invalidSubmit.do"/>
			</fr:edit>
		</logic:equal>
		<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.create"/></html:submit>
	</logic:present>		
	</fr:form>

	<logic:present name="issueBean">
					<logic:messagesPresent message="true">
				<p>
				<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
					<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
				</html:messages>
				</p>
	   	    </logic:messagesPresent>
		<div class="dinline forminline">	
			<fr:form action="/bibtexManagement/createJournalWorkFlow.do">
				<fr:edit id="importBibtexBean" visible="false" name="importBibtexBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex.ImportBibtexBean"/>
				<fr:edit id="createMagazine" name="issueBean" visible="false"/>
				
				<p class="mtop1 mbottom05"><strong><bean:message key="label.journal" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
				<logic:equal name="issueBean" property="journalAlreadyChosen" value="false">
				<fr:edit id="journalInfo" name="issueBean" schema="result.publication.create.Article.createMagazine">
					<fr:layout name="tabular">
					 <fr:property name="classes" value="tstyle5 thright thlight thtop mtop05"/>
	        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	        		<fr:property name="requiredMarkShown" value="true"/>
					</fr:layout>
				</fr:edit>

				</logic:equal>
				<logic:equal name="issueBean" property="journalAlreadyChosen" value="true">
					<span><fr:view name="issueBean" property="journal.name"/></span>
				</logic:equal>
				
				<p class="mtop1 mbottom05"><strong><bean:message key="label.journalIssue" bundle="RESEARCHER_RESOURCES"/>:</strong></p>

				<bean:define id="issueSchema" value="result.publication.create.Article.createIssue" type="java.lang.String"/>
				<logic:equal name="issueBean" property="specialIssue" value="true">
					<bean:define id="issueSchema" value="result.publication.create.Article.createSpecialIssue" type="java.lang.String"/>
				</logic:equal>
				
				<fr:edit id="issueInfo" name="issueBean" schema="<%= issueSchema %>">
					<fr:layout name="tabular">
					 <fr:property name="classes" value="tstyle5 thright thlight thtop mtop05"/>
	        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	        		<fr:property name="requiredMarkShown" value="true"/>
					</fr:layout>
					<fr:destination name="postBack" path="/bibtexManagement/changeSpecialIssueInImport.do"/>
					<fr:destination name="invalid"  path="/bibtexManagement/createJournalWorkFlow.do"/>
				</fr:edit>
				<br/>		
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
