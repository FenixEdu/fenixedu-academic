<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="title.application.name.secondCycle" bundle="CANDIDATE_RESOURCES"/>
</div>

<div id="contextual_nav">
<h2 class="brown">In this page</h2>
	<ul>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#desc">Description</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#vag">Vacancies</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#praz">Deadlines</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#prop">Tuition fees</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#critsel">Selection criteria</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#docnec">Documentation needed</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#emol">Application fees</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#faq">FAQ</a></li>
    	<li><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>"><b>Submit Application</b></a></li>
    	<li><a href="<%= fullPath + "?method=prepareApplicationAccessRecovery" %>"><b>Recover Access</b></a></li>
   </ul>
</div>

<h1><bean:message key="title.application.name.secondCycle" bundle="CANDIDATE_RESOURCES"/></h1>


<h2 id="desc">Description</h2>

<p>Prospective students can apply for a Master - 2nd cycle program, aiming at a MSc degree, if one of the following conditions are met:</p>

<ul>
	<li>Hold a BSc degree (1st cycle) in the field of Science and Technology (exception due to the Integrated Master Degree in Architecture for which is mandatory to hold a BSc degree in Architecture);</li>
	<li>Hold either a scholar, scientific or professional curriculum, which guarantees to be able to complete the MSc program they wish to enroll.</li>
</ul>

<p>&nbsp;</p>
<p>&nbsp;</p>
<h2 id="vag">Vacancies</h2>
<table class="tab_lay" width="70%" cellspacing="0" summary="Informations about the Master Programmes (2009-2010)">
	<tr>	
		<th>Course</th>
		<th>Campus</th>
		<th>Vacancies</th>

	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/ma?locale=en">Master Degree in Architecture</a></td>
		<td>Alameda</td>
		<td>5</td>
	</tr>
	<tr>
		<td><a href="https://fenix.ist.utl.pt/cursos/mbionano?locale=en">Master Degree in Bioengineering and Nanosystems</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="https://fenix.ist.utl.pt/cursos/mbiotec?locale=en">Master Degree in Biotechnology</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr >

		<td><a href="http://fenix.ist.utl.pt/meaer?locale=en">Master Degree in Aerospace Engineering</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mebiol?locale=en">Master Degree in Biological Engineering</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>

	<tr>
		<td><a href="http://fenix.ist.utl.pt/mebiom?locale=en">Master Degree in Biomedical Engineering</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meq?locale=en">Master Degree in Chemical Engineering</a></td>
		<td>Alameda</td>
		<td>20</td>

	</tr>
	<tr >
		<td><a href="http://fenix.ist.utl.pt/mq?locale=en">Master Degree in Chemistry</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">

		<td><a href="http://fenix.ist.utl.pt/mec?locale=en">Master Degree in Civil Engineering</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr >
		<td><a href="http://fenix.ist.utl.pt/merc?locale=en">Master Degree in Communication Networks Engineering</a></td>
		<td>Taguspark</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mee?locale=en">Master Degree in Electronics Engineering</a></td>
		<td>Taguspark</td>
		<td>15</td>
	</tr>	
	<tr >

		<td><a href="http://fenix.ist.utl.pt/meec?locale=en">Master Degree in Electrical and Computer Engineering</a></td>
		<td>Alameda</td>
		<td>90</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meamb?locale=en">Master Degree in Environmental Engineering</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr>
		<td><a href="https://fenix.ist.utl.pt/mefarm?locale=en">Master Degree in Pharmaceutical Engineering</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/megi?locale=en">Master Degree in Industrial Engineering and Management</a></td>
		<td>Taguskpark</td>
		<td>30</td>
	</tr>
	<tr >
		<td><a href="http://fenix.ist.utl.pt/meic-a?locale=en">Master Degree in Information Systems and Computer Engineering</a></td>
		<td>Alameda</td>
		<td>30</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meic-t?locale=en">Master Degree in Information Systems and Computer Engineering</a></td>
		<td>Taguspark</td>
		<td>20</td>
	</tr>

	<tr >
		<td><a href="http://fenix.ist.utl.pt/memat?locale=en">Master Degree in Materials Engineering</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mma?locale=en">Master Degree in Mathematics and Applications</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr >
		<td><a href="http://fenix.ist.utl.pt/memec?locale=en">Master Degree in Mechanical Engineering</a></td>
		<td>Alameda</td>
		<td>35</td>

	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/megm?locale=en">Master Degree in Mining and Geological Engineering</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr >

		<td><a href="http://fenix.ist.utl.pt/mean?locale=en">Master Degree in Naval Architecture and Marine Engineering</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meft?locale=en">Master Degree in Physics Engineering</a></td>
		<td>Alameda</td>

		<td>30</td>
	</tr>
	<tr>
		<td><a href="http://www.civil.ist.utl.pt/nispt/mit/ctis/">Master Degree in Complex Transport Infrastructure Systems (MIT-Portugal)</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://www.civil.ist.utl.pt/?locale=en">Master in Urban Studies and Territorial Management</a></td>
		<td>Alameda</td>
		<td>25</td>
	</tr>

</table>

<h2 id="praz">Deadlines</h2>
<table class="tab_simpler" summary="Prazos para candidatos em mudanÃ§a ou transferência de curso" cellspacing="0">
	<tr>
		<td class="align_r"><span class="marker">4 of May to 15 of June of 2009</span></td>
		<td><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>">Submission period</a></td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">29 of June of 2009</span></td>

		<td>Results posted</td>
	</tr>

	<tr>
		<td class="align_r"><span class="marker">29 of June to 3 of July of 2009</span></td>
		<td>Registration and enrolment</td>

	</tr>
	<tr>
		<td class="align_r"><span class="marker">29 of June to 3 of July of 2009</span></td>
		<td>Complaint Period </td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">17 of July of 2009</span></td>
		<td>Decision on complaints</td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">17 to 21 of July of 2009</span></td>
		<td>Registration of students whose complaints were accepted</td>
	</tr>
</table> 

<h2 id="prop">Tuition fees</h2>

<p>The tuition fee for the academic year of 2009/2010 for all the Master - 2nd cycle programs is <b>996.85 Euros</b>, exception is due to the "Master in Pharmaceutical Engineering" where the tuition fee is set to <b>5000 Euros</b>. For the "Master in Complex Transport Infrastructure Systems" applicants should consult the respective <a href="http://www.civil.ist.utl.pt/nispt/mit/ctis/">webpage</a>.</p>

<h2 id="critsel">Selection criteria</h2>
<p>Students applying for the Master - 2nd cycle program are selected by the coordinator considering the following criteria:</p>
<ol>
	<li>Affinity between their scientific background and the 2nd cycle programme they are applying for;</li>
	<li>Nature of the degree they hold;</li>
	<li>Classifications held along their academic history.</li>
</ol>

<p>The selection board can propose the student's conditional admission subject to attendance of a set of mandatory preliminary course units. These preliminary course units can never exceed 30 ECTS. The completion of the 2nd cycle programme requires the approval on all of the pre-defined preliminary course units. The grades obtained by the student in these preliminary units are not considered in the final classification.</p>


<h2 id="docnec">Documentation needed</h2>

<p>The following documents should be uploaded in order to complete the submission application process:</p>

<ul>
	<li>Current photo</li>
	<li>Curriculum vitae</li>
	<li>Official transcript of the candidate's university records</li>
	<li>Passport or identity card</li>
	<li>Bank transfer transcript with the payment of the application fee</li>
</ul>

<h2 id="emol">Application fees</h2>

<strong>Bank Name:</strong> Millennium BCP<br />
<strong>NIB:</strong> 0033 0000 00007920342 05<br />

<strong>IBAN:</strong> PT50 0033 0000 00007920342 05<br />
     	<strong>SWIFT/BIC:</strong> BCOMPTPL<br />
<strong>Amount:</strong> 100 euros

<p/>

<h2 id="faq">FAQ</h2>

<h3>Q1: I am a final-year student of a 1st cycle programme in <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. Do I have to do anything specific to continue my studies in the same 2nd cycle programme?</h3>
<p>No, in these cases the transition to the 2nd cycle is automatic.</p>

<h3>Q2: I am a final-year student of a 1st cycle programme at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. In which conditions is the transition to a different 2nd cycle automatic?</h3>
<p>A2: After completing the 1st cycle programme at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> you can always pursuit your studies in a different 2nd cycle programme at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> as long as both programmes are scientifically coherent. This <a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>files/ensino/coerencias_cientificas_0910.pdf">scientific coherence</a> occurs whenever the training skills of the 1st cycle respect the training needs for admission to the 2nd cycle.</p>

<h3>Q3: I am a final-year student of a 1st programme at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. What should I do if I want to pursuit my studies in a 2nd cycle programme at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> which have no scientific coherence with the 1st cycle programme?</h3>
<p>A3: You should submit your application for the 2nd cycle programme following the deadlines and regulations defined for that purpose. The application will be analysed along with the applications of external students that intend to enroll in a 2nd cycle programme at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>.</p>

<h3>Q4: I hold a pre-Bologna graduate programme from <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. May I apply for a 2nd cycle Master programme, in the same area of expertise?</h3>
<p>A4: Yes. </p>

