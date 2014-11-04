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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="org.fenixedu.academic.domain.person.RoleType"%><html:xhtml />

<h2><bean:message key="link.executionCourse.log" bundle="APPLICATION_RESOURCES"/></h2>

<logic:empty name="logsList">
	<div class="infoop6">
		<bean:message key="link.personInformation.noLog" bundle="APPLICATION_RESOURCES"/>
	</div>
</logic:empty>

<p class="mtop2">
	<html:link page= "/findPerson.do?method=viewPerson" paramName="person" paramId="personID" paramProperty="externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>

<logic:notEmpty name="logsList">

<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/dataTables/media/js/jquery.dataTables.js"%>"></script>
	
		<script type="text/javascript">
			$(document).ready(function() {
	    		$('.results').dataTable( {
	    			"iDisplayLength": 25,
	    			"sPaginationType": "full_numbers",
	    			"oLanguage" : {
	    				"sProcessing": "A processar...",
	    				"sLengthMenu": "Mostrar _MENU_ registos",
	    				"sZeroRecords": "Não foram encontrados registos",
	    				"sInfo": "_START_ - _END_ de _TOTAL_",
	    				"sInfoEmpty": "0 - 0 de 0",
	    				"sInfoFiltered": "(filtrado de _MAX_ total de registos)",
	    				"sInfoPostFix": "",
	    				"sSearch": "Procura",
	    				"sFirst": "Primeiro",
	    				"sPrevious": "Anterior",
	    				"sNext": "Seguinte",
	    				"sLast": "Último"
	    			},
	    			"aaSorting": false
	    		}
			 );
			});
		</script>
		
		<style type="text/css" title="currentStyle">
			@import "<%= request.getContextPath() + "/javaScript/dataTables/media/css/demo_table.css" %>";
			.dataTables_wrapper {
				position: relative;
				min-height: 302px;
				_height: 302px;
				clear: none;
			}
			tr.odd td.sorting_2 {
				background-color : #FAFAFA !important;
			}
		</style>
	
	<fr:view name="logsList">
		<fr:schema type="org.fenixedu.academic.domain.PersonInformationLog" bundle="APPLICATION_RESOURCES">
			<fr:slot name="person" layout="null-as-label">
				<fr:property name="subSchema" value="person.photo" />
				<fr:property name="subLayout" value="values"/>
			</fr:slot>
			<fr:slot name="username" key="label.username" >
		    	<fr:property name="classes" value="nobullet noindent"/>  
	    	</fr:slot>
	    	<fr:slot name="personName" key="label.name">
		      	<fr:property name="classes" value="nobullet noindent"/>  
	    	</fr:slot>
	    	<fr:slot name="whenDateTime" bundle="ENUMERATION_RESOURCES" key="DATE">
		     	<fr:property name="classes" value="nobullet noindent"/>   
	    	</fr:slot>
	    	<fr:slot name="description">
	        	<fr:property name="classes" value="nobullet noindent"/>   
		    </fr:slot>	
	    </fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tstyle9 mtop05 results resultsLeft" />
			<fr:property name="columnClasses" value="acenter,acenter,acenter ,acenter ,acenter" />
			<fr:property name="renderCompliantTable" value="true"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

