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

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>">Bachelor Programmes</a> &gt;
	National Admission Test
</div>

<div id="contextual_nav">
<h2 class="brown">On this page</h2>
	<ul>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#con">General Admission Procedure</a></li>
   </ul>
</div>
<h1>Prospective Students: BSc degree in <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></h1>
<p>Students applying to a 1st cycle (BSc degree) in <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> should enroll into one of the following programs:</p>
<ul>
	<li>Integrated Master Program (integrated cycle): 5 years course leading to a MSc degree that awards the BSc degree upon completion of the first three years;</li>

	<li>Undergraduate Program (two-cycles): 3 years course leading to a BSc degree which can be followed by a two-years Master Program towards a MSc degree.</li>
</ul>
<p>Eight Engineering programs and the Architecture program in <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> are organized as Integrated Master Programs aiming at a MSc degree. The remainder Engineering programs and Applied Mathematics follow a two-cycles model (BSc+MSc).</p>
<p>Upon completion of the first 3 academic years corresponding to the 1<sup>st</sup> cycle (180 ECTS) students are awarded with the BSc degree in Engineering or Architecture. The BSc programs provide students with training in basic sciences, such as Mathematics, Physics, Chemistry and Programming, in addition to cross-cutting skills, such as oral and written communication, management, leadership, teamwork and entrepreneurship skills, encouraging employability in some areas of the work market.</p>
<p>After 5 years of studies the graduates in Engineering or Architecture are able to conceive, innovate and develop complex projects with the acquisition of advanced scientific and technological skills inherent to the MSc degree. To be admitted in the Professional Engineering Society (Ordem dos Engenheiros) or the Professional Architecture Society (Ordem dos Arquitectos) students must have a MSc degree.</p>

<h2 id="con">Admission requirements according to the National Admission Test (Concurso Nacional de Acesso)</h2>
<h3>2008/2009 Academic Year</h3>

<p>After completion of the Secundary Education (high-school) students can apply for admission either to an integrated master program or an undergraduate program in the desired field of study. Applications should be lodged at the Admission Services of the Ministry of Science, Technology and Higher Education, according to the specific admission requirements defined for each course.</p>

<h3 class="spaced">Undergraduate Programs</h3>
	<table class="tab_lay" width="99%" cellspacing="0" summary="Information for the admissions on the 1st cycle courses (2008-2009">
		<tr>
			<th>Courses</th>
			<th>Code</th>
			<th>Campus</th>

			<th>Vacancies</th>
			<th style="width: 25%;">Intake exams</th>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/lmac?locale=en">Degree in Applied Mathematics and Computation</a></td>
			<td>1518/9345</td>
			<td>Alameda</td>			
			<td>30</td>

			<td>Mathematics + Physics and Chemistry or Mathematics + Biology and Geology</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/lerc?locale=en">Degree in Communication Networks Engineering</a></td>
			<td>1519/9746</td>
			<td>Taguspark</td>			
			<td>68</td>

			<td>Mathematics + Physics and Chemistry or Mathematics + Biology and Geology</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/lee?locale=en">Degree in Electronics Engineering</a></td>
			<td>1519/9912</td>
			<td>Taguspark</td>			
			<td>33</td>

			<td>Mathematics + Physics and Chemisty</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/leamb?locale=en">Degree in Environmental Engineering</a></td>
			<td>1518/9099</td>
			<td>Alameda</td>
			<td>35</td>

			<td>Mathematics + Physics and Chemistry or Mathematics + Biology and Geology</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/legi?locale=en">Degree in Industrial Engineering and Management</a></td>
			<td>1519/9104</td>
			<td>Taguspark</td>			
			<td>40</td>

			<td>Mathematics + Physics and Chemisty</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/leic-a?locale=en">Degree in Information Systems and Computer Engineering</a></td>
			<td>1518/9121</td>
			<td>Alameda</td>
			<td>170</td>

			<td>Mathematics + Physics and Chemistry or Mathematics + Biology and Geology</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/leic-t?locale=en">Degree in Information Systems and Computer Engineering</a></td>
			<td>1519/9121</td>
			<td>Taguspark</td>			
			<td>98</td>

			<td>Mathematics + Physics and Chemistry or Mathematics + Biology and Geology</td>
		</tr>	
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/lemat?locale=en">Degree in Materials Engineering</a></td>
			<td>1518/9096</td>
			<td>Alameda</td>
			<td>20</td>

			<td>Mathematics + Physics and Chemistry or Mathematics + Biology and Geology</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/legm?locale=en">Degree in Mining and Geological Engineering</a></td>
			<td>1518/9913</td>
			<td>Alameda</td>
			<td>15</td>

			<td>Mathematics + Physics and Chemistry or Mathematics + Biology and Geology</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/lean?locale=en">Degree in Naval Architecture and Marine Engineering</a></td>
			<td>1518/9911</td>
			<td>Alameda</td>
			<td>10</td>

			<td>Mathematics + Physics and Chemistry</td>
		</tr>
</table>

<h3 class="spaced">Integrated Master Programs</h3>
<table class="tab_lay" width="99%" cellspacing="0" summary="Information for the admission on the integrated courses (2008-2009)">
		<tr>
			<th>Courses</th>
			<th>Code</th>

			<th>Campus</th>
			<th style="white-space: nowrap;">Vacancies</th>
			<th style="width: 25%;">Intake exams</th>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/ma?locale=en">Integrated Master Degree in Architecture</a></td>
			<td>1518/9257</td>

			<td>Alameda</td>
			<td>50</td>
			<td>Mathematics + Descriptive Geometry or Design + Mathematics or Mathematics</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/meaer?locale=en">Integrated Master Degree in Aerospace Engineering</a></td>
			<td>1518/9357</td>

			<td>Alameda</td>
			<td>65</td>
			<td>Mathematics + Physics and Chemistry</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/mebiol?locale=en">Integrated Master Degree in Biological Engineering</a></td>
			<td>1518/9358</td>

			<td>Alameda</td>
			<td>65</td>
			<td>Mathematics + Physics and Chemistry or Mathematics + Biology and Geology</td>
		</tr>		
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/mebiom?locale=en">Integrated Master Degree in Biomedical Engineering</a></td>
			<td>1518/9359</td>

			<td>Alameda</td>
			<td>50</td>
			<td>Mathematics + Physics and Chemistry or Mathematics + Biology and Geology</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/mec?locale=en">Integrated Master Degree in Civil Engineering</a></td>
			<td>1518/9360</td>

			<td>Alameda</td>
			<td>185</td>
			<td>Mathematics + Physics and Chemistry</td>
		</tr>		
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/meec?locale=en">Integrated Master Degree in Electrical and Computer Engineering</a></td>
			<td>1518/9367</td>

			<td>Alameda</td>
			<td>205</td>
			<td>Mathematics + Physics and Chemistry</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/meft?locale=en">Integrated Master Degree in Physics Engineering</a></td>
			<td>1518/9458</td>

			<td>Alameda</td>
			<td>60</td>
			<td>Mathematics + Physics and Chemistry</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/memec?locale=en">Integrated Master Degree in Mechanical Engineering</a></td>	
			<td>1518/9369</td>	
			<td>Alameda</td>	
			<td>165</td>	
			<td>Mathematics + Physics and Chemistry</td>	
		</tr>

		<tr>
			<td><a href="http://fenix.ist.utl.pt/meq?locale=en">Integrated Master Degree in Chemistry</a></td>
			<td>1518/9461</td>
			<td>Alameda</td>
			<td>70</td>
			<td>Mathematics + Physics and Chemistry</td>

		</tr>
</table>	
<h3>Minimum Admission Grades</h3>
<p>The requirements to apply for the <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> Courses are the following (on a scale of 0 to 200 points):</p>
<ul>
	<li>Minimum classification of 100 points in each of the admission exams (secondary education exams carried out nationwide), except for the Degree in Applied Mathematics and Computation in which the minimum grade rate required is 120 points, and;</li>
	<li>Minimum classification of 120 points on the application grade, except for the Degree in Applied Mathematics and Computation in which the minimum classification required is 140 points. The application grade (AG) is calculated on the basis of a weight of 50% for the classification of the Secondary Education (SE) and a weight of 50% for the classification of admission exams (AE) - Calculation Formula of the Application Grade: AG = SE X 50% + AE X 50% (in other words, the arithmetical average of the final classification of the Secondary Education and the classification of the admission exams).</li>
</ul>
<p>Note: there are no pre-requisites.</p>

<p>For further information, see the <a href="http://www.dges.mctes.pt/DGES/pt">web page of the Directorate-General for Higher Education (DGES)</a>.</p>

