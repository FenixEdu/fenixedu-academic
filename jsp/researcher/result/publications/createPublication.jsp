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


	<p class="mvert1 breadcumbs">
		<span class="actual">
		<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 1 : </strong>
		<bean:message key="researcher.result.publication.importBibtex.publicationData" bundle="RESEARCHER_RESOURCES"/></span>
			 > 
		<span>
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 2 : </strong>
		<bean:message key="label.associatingDocumentsAndUnits" bundle="RESEARCHER_RESOURCES"/></span>
		</span>
 	</p>

	<fr:form action="/resultPublications/create.do">
		<logic:equal name="publicationBean" property="createEvent" value="false">
			<!-- Present Author -->
			<p class="mtop15 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.author"/></b></p>
			<fr:edit id="author" name="publicationBean" schema="<%= publicationBean.getParticipationSchema() %>" nested="true">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
		   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
			</fr:edit>	
			
			<!-- Present publication fields -->
			<p class="mtop1 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES"key="<%="researcher.ResultPublication.type." + publicationBean.getPublicationTypeString() %>"/></b></p>
			<fr:edit id="publicationData" name="publicationBean" schema="<%= publicationBean.getActiveSchema() %>" nested="true">
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
		
		<html:submit property="confirm">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.create"/>
		</html:submit>
		<html:cancel>
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:cancel>
	</fr:form>
</logic:present>
