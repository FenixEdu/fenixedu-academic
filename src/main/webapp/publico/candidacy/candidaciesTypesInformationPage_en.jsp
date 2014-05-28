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


<style type="text/css">
	
	ul.cycles {
		list-style-type: none;
		float: left;
		width: 30%;
		margin: 0;
	 	padding: 0;
		}
	
	/*
	ul.cycles li {
		background: #e2e2e2;
		padding: 10px;
		margin: 5px 0;		
		}
	*/
		
	ul.cycles li.btt_one, ul.cycles li.btt_two, ul.cycles li.btt_three, ul.cycles li.btt_four {
		text-indent: -9999px;
		margin: 5px 0;
		}	
		
	ul.cycles li.btt_one a:link, ul.cycles li.btt_one a:visited, ul.cycles li.btt_one a:hover {
		background: url(../images/candidacy/en_1.gif) no-repeat;
		width: 202px;
		height: 33px;
		display: block;
		text-decoration: none;
		border: none;
		overflow: hidden;
		}
		
			ul.cycles li.btt_one a:hover {
				background: url(../images/candidacy/h_en_1.gif) no-repeat;
				}
		
				
	ul.cycles li.btt_two a:link, ul.cycles li.btt_two a:visited, ul.cycles li.btt_two a:hover {
		background: url(../images/candidacy/en_2.gif) no-repeat;
		width: 202px;
		height: 33px;
		display: block;
		text-decoration: none;
		border: none;
		overflow: hidden;
		}
		
			ul.cycles li.btt_two a:hover {
				background: url(../images/candidacy/h_en_2.gif) no-repeat;
				}		
	
	ul.cycles li.btt_three a:link, ul.cycles li.btt_three a:visited, ul.cycles li.btt_three a:hover {
		background: url(../images/candidacy/en_3.gif) no-repeat;
		width: 202px;
		height: 33px;
		display: block;
		text-decoration: none;
		border: none;
		overflow: hidden;
		}
		
			ul.cycles li.btt_three a:hover {
				background: url(../images/candidacy/h_en_3.gif) no-repeat;
				}		
	
	ul.cycles li.btt_four a:link, ul.cycles li.btt_four a:visited, ul.cycles li.btt_four a:hover {
		background: url(../images/candidacy/en_4.gif) no-repeat;
		width: 202px;
		height: 33px;
		display: block;
		text-decoration: none;
		border: none;
		overflow: hidden;
		}
				
			ul.cycles li.btt_four a:hover {
				background: url(../images/candidacy/h_en_4.gif) no-repeat;
				}	
	
	.bolonha_diagram {
		float: left;
		display: block;
		margin-right: 4em;
		}	
	
</style>

<div class="col_right_photo"><img src="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>img/perfil/candidato_photo.jpg" alt="[Photo] Enroll at the Admissions Office" width="150" height="100" /></div>
<h1>Prospective student</h1>
<p class="greytxt">This page is aimed at providing complete application information for students interested in attending <%= net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym() %> courses.</p>
<h2>With the <%= net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym() %>, come into the best European higher education</h2>

<p>The <%= net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent() %> (<%= net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym() %>), a recognized school for excellence in Engineering, Science and Technology, implemented the Bologna Process reform in the 2006/2007 academic year.</p>
<p>This reform aims at:</p>
<ul>
	<li>Increasing students´ mobility at national and international level;</li>
	<li>Adopting a new education/learning model;</li>
	<li>Complementing students´ training in cross-cutting areas.</li>
</ul>
<p>The former 5-year undergraduate programmes (licenciaturas) have been transformed into 5-year Integrated MSc courses or into courses organized in two consecutive cycles, respectively leading to the Graduate and Master degree (3+2).</p>

<p>After completing the three first years of education, students are always awarded the graduate degree. These first three years are aimed at providing students with a sound training in basic sciences, such as Mathematics, Physics, Chemistry and Programming and in engineering sciences in a given area, in addition to cross-cutting skills, such as oral and written communication, management, leadership, teamwork and entrepreneurship.</p>
<p>The full training, which allows the students to conceive, innovate and develop comprehensive projects, takes five years to be completed, with the acquisition of advanced scientific and technological skills that are inherent to the Master degree.</p>


<div style="background: #f1f1f1; margin: 2em auto; padding: 2em 0 2em 2em; width: 90%; border: 1px solid #e2e2e2;">
	<img class="bolonha_diagram" src="../images/candidacy/quadro_en_01.gif" alt="Diagrama dos diferentes ciclos de estudo" />
	<p>Additional information on the application process can be found at:</p>
	<ul class="cycles">
		<li class="btt_one"><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>">Licenciaturas (1&ordm; Ciclo)</a></li>
		<li class="btt_two"><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/mestrados" %>">Mestrados (2&ordm; Ciclo)</a></li>
	    <li class="btt_three"><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/perfil/candidato/pd/">Forma&ccedil;&atilde;o Avan&ccedil;ada</a></li>
		<li class="btt_four"><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/perfil/candidato/fa/">Programas Doutorais (3&ordm; Ciclo)</a></li>
	</ul>

	<div style="clear: both;"></div>
</div>

<p>Under the scope of the 1<sup>st</sup> and 2<sup>nd</sup> cycle courses, the <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>, as a great European school in the area of Engineering, Science and Technology, offers a wide range of post-graduate courses, either vocational or more oriented to Research, Development and Innovation.</p>
<p>Additionally, the <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> is strongly investing in international cooperation programmes with the best schools in Europe, namely under the scope of its association to the CLUSTER network.  But <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> international programmes also include collaboration with prestigious American universities such as the MIT, CMU or UTAustin.</p>

<h2>Contacts</h2>
<h3><a href="http://nape.ist.utl.pt/">Student Support Unit (NAPE)</a></h3>
<p><strong><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> - Alameda </strong><br />

	Phone: 218 417 251 / 218 419 155 <br />
	Fax: 218 419 344
</p>
<p><strong><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> - Taguspark</strong><br />
   Phone: 214 233 545</p>
<p><a href="mailto:nape@ist.utl.pt">nape@ist.utl.pt</a></p> 

