<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h2><bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES"/></h2>

<bean:define id="url" value="<%= "/researchUnitFunctionalities.do?method=preparePublications&unitId=" + request.getParameter("unitId") %>"/>

<p class="mbottom05"><strong>Para criar uma publicação deve escolher um dos autores:</strong></p>

<fr:form action="/resultPublications/prepareCreate.do">
<fr:edit name="bean" schema="edit.research.unit.publication.bean" >
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle mvert0"/>
		<fr:property name="columnClasses" value="width6em,width35em,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>
<table class="tstyle5 mtop0 gluetop">
	<tr>
		<td class="width6em"></td>
		<td class="width35em"><html:submit><bean:message key="researcher.ResultPublication.create" bundle="RESEARCHER_RESOURCES"/></html:submit></td>
	</tr>
</table>
</fr:form>

<p>
<logic:notEmpty name="publications"> 
	
	
	<bean:define id="type" value=""/>
	
	<logic:present name="publicationType">
	<bean:define id="type" name="publicationType" type="java.lang.String"/>
	</logic:present>
		
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error1"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.view"/>:
	<html:link page="<%= url + "&amp;publicationType=books"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Books"/></html:link> | 
	<html:link page="<%= url + "&amp;publicationType=inbooks"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.BookParts"/></html:link> | 
	<html:link page="<%= url + "&amp;publicationType=articles"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Articles"/></html:link> | 
	<html:link page="<%= url + "&amp;publicationType=inproceedings"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="research.ResultPublication.Inproceedings"/></html:link> | 
	<html:link page="<%= url + "&amp;publicationType=proceedings"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Proceedings"/></html:link> | 
	<html:link page="<%= url + "&amp;publicationType=theses"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Theses"/></html:link> | 
	<html:link page="<%= url + "&amp;publicationType=manuals"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Manuals"/></html:link> | 					
	<html:link page="<%= url + "&amp;publicationType=technicalReports"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.TechnicalReports"/></html:link> | 
	<html:link page="<%= url + "&amp;publicationType=otherPublications"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.OtherPublications"/></html:link> | 
	<html:link page="<%= url + "&amp;publicationType=unstructured"%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Unstructureds"/></html:link>  					
	
	<logic:equal name="type" value="books">
	<logic:notEmpty name="books">
		<p id='books' class="mtop2 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Books"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Book" toScope="request"/>
		<bean:define id="results" name="books" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	</logic:equal>

	<logic:equal name="type" value="inbooks">	
	<logic:notEmpty name="inbooks">
		<p id='inbooks' class="mtop2 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.BookParts"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.BookPart" toScope="request"/>
		<bean:define id="results" name="inbooks" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	</logic:equal>
	
	<bean:define id="hasArticles" value="false" toScope="request"/>
	<logic:notEmpty name="national-articles"> 
		<bean:define id="hasArticles" value="true" toScope="request"/>
	</logic:notEmpty>
	<logic:notEmpty name="international-articles"> 
		<bean:define id="hasArticles" value="true" toScope="request"/>
	</logic:notEmpty>	
	
	<logic:equal name="type" value="articles">
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
	</logic:equal>
	
	<logic:equal name="type" value="inproceedings">
	<logic:notEmpty name="international-inproceedings">
		<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.InternationalInproceedings"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
		<bean:define id="results" name="international-inproceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="national-inproceedings">
		<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.NationalInproceedings"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
		<bean:define id="results" name="national-inproceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="type" value="proceedings">
	<logic:notEmpty name="proceedings">
		<p id='proceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Proceedings"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Proceedings" toScope="request"/>
		<bean:define id="results" name="proceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="type" value="theses">
	<logic:notEmpty name="theses">
		<p id='theses' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Theses"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Thesis" toScope="request"/>
		<bean:define id="results" name="theses" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="type" value="manuals">
	<logic:notEmpty name="manuals">
		<p id='manuals' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Manuals"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Manual" toScope="request"/>
		<bean:define id="results" name="manuals" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="type" value="technicalReports">
	<logic:notEmpty name="technicalReports">
		<p id='technicalReports' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.TechnicalReports"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.TechnicalReport" toScope="request"/>
		<bean:define id="results" name="technicalReports" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="type" value="otherPublications">
	<logic:notEmpty name="otherPublications">
		<p id='otherPublications' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.OtherPublications"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.OtherPublication" toScope="request"/>
		<bean:define id="results" name="otherPublications" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	</logic:equal>
	
	<logic:equal name="type" value="unstructured">
	<logic:notEmpty name="unstructureds">
		<logic:empty name="otherPublications">
			<p id='unstructureds' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Unstructureds"/></strong></span></p>		
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
	</logic:equal>
</logic:notEmpty>

<logic:empty name="publications">
	<bean:define id="unitName" name="unit" property="name" type="java.lang.String"/>
	<bean:message key="label.noPublicationsForUnit" bundle="RESEARCHER_RESOURCES" arg0="<%= unitName %>"/>
</logic:empty>
</p>