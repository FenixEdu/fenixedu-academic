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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />

<em>Portal do Delegado</em>
<h2><bean:message key="link.yearDelegateInquiries" bundle="DELEGATES_RESOURCES"/></h2>

<p>
Bem vindo à nova interface dos QUC para os Delegados!
Aqui tens acesso ao quadro com o resumo dos QUC de todas as unidades curriculares do teu ano. Se os alunos de uma determinada unidade curricular  indicaram algum problema, deves explicar o motivo que causou o baixo resultado, de forma a que o corpo docente possa corrigir o que correu mal.
O número de perguntas que necessita de comentários teus está indicada entre parêntesis. Para além de essas perguntas, podes sempre para cada unidade curricular preencher comentários opcionais. O que escreves deve ser a opinião geral dos teus colegas de ano e não apenas a tua opinião. Fala com os teus colegas!
<br/>
Caso o problema não seja resolvido pelo corpo docente, os teu comentários são muito importantes para que o Coordenador de Curso, o Departamento e o Conselho Pedagógico analisem o problema.
Participa!
</p>

<h3 class="mtop15"><bean:write name="executionDegree" property="degree.sigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<fr:view name="coursesResultResume">
	<fr:layout name="delegate-inquiry-resume">
		<fr:property name="classes" value="delegate-resume"/>
	</fr:layout>
</fr:view>
<ul class="legend-general" style="margin-top: 20px;"> 
	<li><bean:message key="label.inquiry.legend" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.regular" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.toImprove" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-4">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.indequate" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><bean:message key="label.inquiry.mandatoryCommentsNumber" bundle="INQUIRIES_RESOURCES"/></li> 
</ul>

<ul class="legend-general" style="margin-top: 0px;"> 
	<li><bean:message key="label.inquiry.workLoad" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.asPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.higherThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-6">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.lowerThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
</ul> 