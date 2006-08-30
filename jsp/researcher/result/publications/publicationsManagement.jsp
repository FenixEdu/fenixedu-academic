<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<style>
		table.search {
		background-color: #f5f5f5;
		border-collapse: collapse;
		}
		table.search tr td {
		border: 1px solid #fff;
		padding: 0.3em;
		}
		.leftcolumn {
		text-align: right;
		}
		
		h3.cd_heading {
		font-weight: normal;
		margin-top: 3em;
		border-top: 1px solid #e5e5e5;
		background-color: #fafafa;
		padding: 0.25em 0 0em 0.25em;
		padding: 0.5em 0.25em;
		}
		h3.cd_heading span {
		margin-top: 2em;
		border-bottom: 2px solid #fda;
		}
		
		div.cd_block {
		background-color: #fed;
		padding: 0.5em 0.5em 0.5em 0.5em;
		}
		
		table.cd {
		border-collapse: collapse;
		}
		table.cd th {
		border: 1px solid #ccc;
		background-color: #eee;
		padding: 0.5em;
		text-align: center;
		}
		table.cd td {
		border: 1px solid #ccc;
		background-color: #fff;
		padding: 0.5em;
		text-align: center;
		}
		
		p.insert {
		padding-left: 2em;
		}
		div.cd_float {
		width: 100%;
		float: left;
		padding: 0 2.5em;
		padding-bottom: 1em;
		}
		ul.cd_block {
		width: 43%;
		list-style: none;
		float: left; 
		margin: 0;
		padding: 0;
		padding: 1em;
		}
		ul.cd_block li {
		}
		ul.cd_nostyle {
		list-style: none;
		}
</style>

<logic:present role="RESEARCHER">
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></h2>

	<html:link page="/publications/publicationsManagement.do?method=prepareCreatePublication"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.insert.link" /></html:link>
	
	<logic:notEmpty name="books">
		<h3 id='books' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.books"/> </span> </h3>
		<logic:iterate id="book" name="books">
	 		<bean:define id="bookId" name="book" property="idInternal"/>
			<fr:view name="book" layout="tabular" schema="result.publication.resume.Book">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ bookId%>"/>
			</fr:view>
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ bookId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ bookId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
	  		<br /><br />
		</logic:iterate>
	</logic:notEmpty>

	<logic:notEmpty name="bookParts">
		<h3 id='booksParts' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.bookParts"/> </span> </h3>
		<logic:iterate id="bookPart" name="bookParts">
	 		<bean:define id="bookPartId" name="bookPart" property="idInternal"/>
			<fr:view name="bookPart" layout="tabular" schema="result.publication.resume.BookPart">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ bookPartId%>"/>
			</fr:view>
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ bookPartId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ bookPartId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
	  		<br /><br />
		</logic:iterate>
	</logic:notEmpty>

	<logic:notEmpty name="articles">
		<h3 id='articles' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.articles"/> </span> </h3>
		<logic:iterate id="article" name="articles">
			<bean:define id="articleId" name="article" property="idInternal"/>
			<fr:view name="article" layout="tabular" schema="result.publication.resume.Article">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ articleId%>"/>
			</fr:view>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ articleId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ articleId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>

	<logic:notEmpty name="inproceedings">
		<h3 id='inproceedings' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.inproceedings"/> </span> </h3>
		<logic:iterate id="inproceeding" name="inproceedings">
			<bean:define id="inproceedingId" name="inproceeding" property="idInternal"/>
			<fr:view name="inproceeding" layout="tabular" schema="result.publication.resume.Inproceedings">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ inproceedingId%>"/>
			</fr:view>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ inproceedingId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ inproceedingId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="proceedings">
		<h3 id='proceedings' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.proceedings"/> </span> </h3>
		<logic:iterate id="proceeding" name="proceedings">
			<bean:define id="proceedingId" name="proceeding" property="idInternal"/>
			<fr:view name="proceeding" layout="tabular" schema="result.publication.resume.Proceedings">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ proceedingId%>"/>
			</fr:view>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ proceedingId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ proceedingId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="theses">
		<h3 id='theses' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.theses"/> </span> </h3>
		<logic:iterate id="thesis" name="theses">
			<bean:define id="thesisId" name="thesis" property="idInternal"/>
			<fr:view name="thesis" layout="tabular" schema="result.publication.resume.Thesis">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ thesisId%>"/>
			</fr:view>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ thesisId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ thesisId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="manuals">
		<h3 id='manuals' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.manuals"/> </span> </h3>
		<logic:iterate id="manual" name="manuals">
			<bean:define id="manualId" name="manual" property="idInternal"/>
			<fr:view name="manual" layout="tabular" schema="result.publication.resume.Manual">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ manualId%>"/>
			</fr:view>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ manualId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ manualId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="technicalReports">
		<h3 id='technicalReports' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.technicalReports"/> </span> </h3>
		<logic:iterate id="technicalReport" name="technicalReports">
			<bean:define id="technicalReportId" name="technicalReport" property="idInternal"/>
			<fr:view name="technicalReport" layout="tabular" schema="result.publication.resume.TechnicalReport">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ technicalReportId%>"/>
			</fr:view>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ technicalReportId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ technicalReportId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="booklets">
		<h3 id='booklets' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.booklets"/> </span> </h3>
		<logic:iterate id="booklet" name="booklets">
			<bean:define id="bookletId" name="booklet" property="idInternal"/>
			<fr:view name="booklet" layout="tabular" schema="result.publication.resume.Booklet">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ bookletId%>"/>
			</fr:view>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ bookletId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ bookletId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="miscs">
		<h3 id='miscs' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.miscs"/> </span> </h3>
		<logic:iterate id="misc" name="miscs">
			<bean:define id="miscId" name="misc" property="idInternal"/>
			<fr:view name="misc" layout="tabular" schema="result.publication.resume.Misc">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ miscId%>"/>
			</fr:view>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ miscId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ miscId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>
	
	<logic:notEmpty name="unpublisheds">
		<h3 id='unpublisheds' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.unpublisheds"/> </span> </h3>
		<logic:iterate id="unpublished" name="unpublisheds">
			<bean:define id="unpublishedId" name="unpublished" property="idInternal"/>
			<fr:view name="unpublished" layout="tabular" schema="result.publication.resume.Unpublished">
			    <fr:destination name="viewPublicationDetails" path="<%="/publications/publicationsManagement.do?method=preparePublicationDetails&publicationId="+ unpublishedId%>"/>
			</fr:view>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ unpublishedId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ unpublishedId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>

	<logic:notEmpty name="unstructureds">
		<h3 id='unstructureds' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.unstructureds"/> </span> </h3>
		<logic:iterate id="unstructured" name="unstructureds">
			<bean:define id="unstructuredId" name="unstructured" property="idInternal"/>
			<fr:view name="unstructured" layout="tabular" schema="result.publication.Unstructured"/>
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ unstructuredId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.convertUnstructured" /></html:link>&nbsp;&nbsp;&nbsp;
			<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ unstructuredId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			<br /><br />
		</logic:iterate>
	</logic:notEmpty>

	<br />	<br />
	<html:link page="/publications/publicationsManagement.do?method=prepareCreatePublication"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.insert.link" /></html:link>
</logic:present>
