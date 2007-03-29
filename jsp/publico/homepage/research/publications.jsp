<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
	
	<h1><bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES"/></h1>
	
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error1"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<logic:notEmpty name="books">
		<p id='books' class="mtop2 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Books"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Book" toScope="request"/>
		<bean:define id="results" name="books" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="inbooks">
		<p id='inbooks' class="mtop2 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.BookParts"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.BookPart" toScope="request"/>
		<bean:define id="results" name="inbooks" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<bean:define id="hasArticles" value="false" toScope="request"/>
	<logic:notEmpty name="national-articles"> 
		<bean:define id="hasArticles" value="true" toScope="request"/>
	</logic:notEmpty>
	<logic:notEmpty name="international-articles"> 
		<bean:define id="hasArticles" value="true" toScope="request"/>
	</logic:notEmpty>	
	
	<logic:equal name="hasArticles" value="true">
		<p id='books' class="mtop2 mbottom0"/>
		<bean:define id="currentSchema" value="result.publication.presentation.Article" toScope="request"/>

		<logic:notEmpty name="international-articles">
		<p class="mtop2 mbottom0"><strong><bean:message key="label.internationalArticles" bundle="RESEARCHER_RESOURCES"/></strong></p>
		<bean:define id="results" name="international-articles" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
		</logic:notEmpty>
		
		<logic:notEmpty name="national-articles">
		<p class="mtop2 mbottom0"><strong><bean:message key="label.nationalArticles" bundle="RESEARCHER_RESOURCES"/></strong></p>
		<bean:define id="results" name="national-articles" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
		</logic:notEmpty>
	</logic:equal>
	
	<logic:notEmpty name="national-inproceedings">
		<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.NationalInproceedings"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
		<bean:define id="results" name="national-inproceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="international-inproceedings">
		<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.InternationalInproceedings"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
		<bean:define id="results" name="international-inproceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="proceedings">
		<p id='proceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Proceedings"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Proceedings" toScope="request"/>
		<bean:define id="results" name="proceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="theses">
		<p id='theses' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Theses"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Thesis" toScope="request"/>
		<bean:define id="results" name="theses" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="manuals">
		<p id='manuals' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Manuals"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Manual" toScope="request"/>
		<bean:define id="results" name="manuals" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="technicalReports">
		<p id='technicalReports' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.TechnicalReports"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.TechnicalReport" toScope="request"/>
		<bean:define id="results" name="technicalReports" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="otherPublications">
		<p id='otherPublications' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.OtherPublications"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.OtherPublication" toScope="request"/>
		<bean:define id="results" name="otherPublications" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="unstructureds">
		<logic:empty name="otherPublications">
			<p id='unstructureds' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Unstructureds"/></span></strong></p>		
		</logic:empty>
		<bean:define id="results" name="unstructureds" toScope="request"/>
		<ul>
			<logic:iterate id="result" name="results" scope="request">
	 			<bean:define id="resultId" name="result" property="idInternal"/>
				<li>
		 			<fr:view name="result" layout="values" schema="result.publication.presentation.Unstructured">
		 				<fr:layout>
		 					<fr:property name="htmlSeparator" value=", "/>
		 					<fr:property name="indentation" value="false"/>
		 				</fr:layout>
	 				</fr:view>
	 			</li>
			</logic:iterate>
		</ul>
	</logic:notEmpty>
