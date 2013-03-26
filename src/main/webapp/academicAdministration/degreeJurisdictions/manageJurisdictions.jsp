<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@ page import="net.sourceforge.fenixedu.domain.AcademicProgram" %>

<html:xhtml />

<script>
	function getDocHeight() {
	    var D = document;
	    return Math.max(
	        Math.max(D.body.scrollHeight, D.documentElement.scrollHeight),
	        Math.max(D.body.offsetHeight, D.documentElement.offsetHeight),
	        Math.max(D.body.clientHeight, D.documentElement.clientHeight)
	    );
	}
	
	$('document').ready(function () {
		$('#bachelorTogglerIcon').click(function () {
			if ($(this).attr('state') === 'expanded') {
				$(this).attr('state','collapsed');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_plus10.gif"%>");
			} else {
				$(this).attr('state','expanded');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_minus10.gif"%>");
			}
			$('#bachelorTables').slideToggle('normal');
		});
		$('#bachelorTogglerLabel').click(function () {
			$('#bachelorTogglerIcon').click();
		});
		$('#bachelorTables').toggle();
		
		$('#masterTogglerIcon').click(function () {
			if ($(this).attr('state') === 'expanded') {
				$(this).attr('state','collapsed');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_plus10.gif"%>");
			} else {
				$(this).attr('state','expanded');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_minus10.gif"%>");
			}
			$('#masterTables').slideToggle('normal');
		});
		$('#masterTogglerLabel').click(function () {
			$('#masterTogglerIcon').click();
		});
		$('#masterTables').toggle();
		
		$('#integratedMasterTogglerIcon').click(function () {
			if ($(this).attr('state') === 'expanded') {
				$(this).attr('state','collapsed');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_plus10.gif"%>");
			} else {
				$(this).attr('state','expanded');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_minus10.gif"%>");
			}
			$('#integratedMasterTables').slideToggle('normal');
		});
		$('#integratedMasterTogglerLabel').click(function () {
			$('#integratedMasterTogglerIcon').click();
		});
		$('#integratedMasterTables').toggle();
		
		$('#phdTogglerIcon').click(function () {
			if ($(this).attr('state') === 'expanded') {
				$(this).attr('state','collapsed');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_plus10.gif"%>");
			} else {
				$(this).attr('state','expanded');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_minus10.gif"%>");
			}
			$('#phdTables').slideToggle('normal');
		});
		$('#phdTogglerLabel').click(function () {
			$('#phdTogglerIcon').click();
		});
		$('#phdTables').toggle();
		
		$('#dfaTogglerIcon').click(function () {
			if ($(this).attr('state') === 'expanded') {
				$(this).attr('state','collapsed');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_plus10.gif"%>");
			} else {
				$(this).attr('state','expanded');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_minus10.gif"%>");
			}
			$('#dfaTables').slideToggle('normal');
		});
		$('#dfaTogglerLabel').click(function () {
			$('#dfaTogglerIcon').click();
		});
		$('#dfaTables').toggle();
		
		$('#specTogglerIcon').click(function () {
			if ($(this).attr('state') === 'expanded') {
				$(this).attr('state','collapsed');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_plus10.gif"%>");
			} else {
				$(this).attr('state','expanded');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_minus10.gif"%>");
			}
			$('#specTables').slideToggle('normal');
		});
		$('#specTogglerLabel').click(function () {
			$('#specTogglerIcon').click();
		});
		$('#specTables').toggle();
		
		$('#preBolognaTogglerIcon').click(function () {
			if ($(this).attr('state') === 'expanded') {
				$(this).attr('state','collapsed');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_plus10.gif"%>");
			} else {
				$(this).attr('state','expanded');
				$(this).attr('src',"<%=request.getContextPath() + "/images/toggle_minus10.gif"%>");
			}
			$('#preBolognaTables').slideToggle('normal');
		});
		$('#preBolognaTogglerLabel').click(function () {
			$('#preBolognaTogglerIcon').click();
		});
		$('#preBolognaTables').toggle();
		
		$('.unselectedOffice').mouseover(function () {
			if ($(this).attr('id') === 'alamedaButton') {
				$(this).attr('src',"<%=request.getContextPath() + "/images/alamedaButton_hover.png"%>");
				$(this).css('width','30px');
				$(this).css('margin','0px 2.5px');
			}
			if ($(this).attr('id') === 'tagusButton') {
				$(this).attr('src',"<%=request.getContextPath() + "/images/tagusButton_hover.png"%>");
				$(this).css('width','30px');
				$(this).css('margin','0px 2.5px');
			}
			if ($(this).attr('id') === 'posgradButton') {
				$(this).attr('src',"<%=request.getContextPath() + "/images/posgradButton_hover.png"%>");
				$(this).css('width','30px');
				$(this).css('margin','0px 2.5px');
			}
		});
		
		$('.unselectedOffice').mouseout(function () {
			if ($(this).attr('id') === 'alamedaButton') {
				$(this).attr('src',"<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>");
				$(this).css('width','25px');
				$(this).css('margin','5px');
			}
			if ($(this).attr('id') === 'tagusButton') {
				$(this).attr('src',"<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>");
				$(this).css('width','25px');
				$(this).css('margin','5px');
			}
			if ($(this).attr('id') === 'posgradButton') {
				$(this).attr('src',"<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>");
				$(this).css('width','25px');
				$(this).css('margin','5px');
			}
		});
		/*
		var helperYloc = parseInt($('.helper').css("top").substring(0,$('.helper').css("top").indexOf("px")))  
	    $(window).scroll(function () {  
	    	var offset = helperYloc+$(document).scrollTop()+"px";
	    	var bottom = getDocHeight() - (window.pageYOffset + window.innerHeight);
	    	if (bottom > 40) {
	    		$('.helper').animate({top:offset},{duration:500,queue:false});
	    	}
	    });
		*/
		var togglePreset = '<%= request.getAttribute("togglePreset") %>' !== 'null' ? '<%= request.getAttribute("togglePreset") %>' : 'bachelor';
		$('#'+togglePreset+'Tables').toggle();
		$('#'+togglePreset+'TogglerIcon').attr('state','expanded');
		$('#'+togglePreset+'TogglerIcon').attr('src',"<%=request.getContextPath() + "/images/toggle_minus10.gif"%>");
	});
</script>

<style>
	.helper {
		position: fixed;
		top: 300px;
		left: 850px;
		width: 375px;
		border: 1px solid #9B9B9B;
		border-radius: 10px;
		background-color: #F9F9F9;
		-moz-box-shadow: 0 0 3px 3px #AAA;
		-webkit-box-shadow: 0 0 3px 3px #AAA;
		box-shadow: 0 0 3px 3px #AAA;
	}
	
	.helperTitle {
		padding-left: 15px;
		font-size: 14px;
		font-weight: bold;
	}
	
	.helperList {
		list-style-type: none;
	}
	
	.helperItem {
		margin-top: 10px;
		margin-bottom: 10px;
	}
	
	.helperIcons {
		width: 30px;
		vertical-align: middle;
		margin-right: 10px;
	}
	
	.helperCaption {
		font-size: 12px;
		vertical-align: middle;
	}
	
	.degreeEntryNameCell {
		padding-left: 10px !important;
		padding-right: 25px !important;
	}
	
	.degreeEntrySiglaCell {
		padding-left: 6px !important;
		padding-right: 15px !important;
	}
	
	.degreeEntryActionsCell {
		width: 120px;
		
	}
	
	.selectedOffice {
		width: 25px;
		vertical-align: middle;
		margin: 5px;
	}
	
	.unselectedOffice {
		width: 25px;
		vertical-align: middle;
		margin: 5px;
	}
	
	.expcol {
		margin-right: 5px;
		cursor: pointer;
	}
	
	.degreeOfficeTables {
		margin-left: 15px;
	}
	
	.degreeOfficeHeaders {
		margin-top: 30px;
	}
	
	.headerTitle {
		cursor: pointer;	
	}
	
	.degreeJurisdictionsTitle {
		padding-top: 40px;
	}
</style>

<bean:define id="alamedaEid" name="alamedaOffice" property="externalId"/>
<bean:define id="tagusEid" name="tagusOffice" property="externalId"/>
<bean:define id="posgradEid" name="posgradOffice" property="externalId"/>

<h2 class="degreeJurisdictionsTitle"><bean:message key="label.degreeJurisdiction.title" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<div class="helper">
	<p class="helperTitle">Secretarias Acad&eacute;micas</p>
	<ul class="helperList">
		<li class="helperItem"><img class="helperIcons" src="<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>" /><span class="helperCaption"><bean:write name="alamedaOffice" property="unit.name" /></span></li>
		<li class="helperItem"><img class="helperIcons" src="<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>" /><span class="helperCaption"><bean:write name="tagusOffice" property="unit.name" /></span></li>
		<li class="helperItem"><img class="helperIcons" src="<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>" /><span class="helperCaption"><bean:write name="posgradOffice" property="unit.name" /></span></li>
	</ul>
</div>

<h3 class="degreeOfficeHeaders"><img id="bachelorTogglerIcon" state="collapsed" class="expcol" src="<%=request.getContextPath() + "/images/toggle_plus10.gif"%>" /><span id="bachelorTogglerLabel" class="headerTitle"><bean:message key="label.degreeJurisdiction.bachelors" bundle="ACADEMIC_OFFICE_RESOURCES" /></span></h3>
<div id="bachelorTables"><table class="tstyle1 thleft degreeOfficeTables">
	<tr>
		<th><bean:message key="label.degreeJurisdiction.degreeName" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th><bean:message key="label.degreeJurisdiction.degreeSigla" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th></th>
	</tr>
	<logic:iterate id="bachelors" name="degrees" property="bachelors">
		<tr>
			<td class="degreeEntryNameCell"><bean:write name="bachelors" property="name" /></td>
			<td class="degreeEntrySiglaCell"><bean:write name="bachelors" property="sigla" /></td>
			<td class="degreeEntryActionsCell">
				<% 	AcademicProgram academicProgram = (AcademicProgram) bachelors;
					if (academicProgram.getAdministrativeOffice().getExternalId().equals(alamedaEid)) { 
				%>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)bachelors).getExternalId() + "&amp;officeOid=" + alamedaEid + "&amp;togglePreset=bachelor" %>'>
						<img id="alamedaButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(tagusEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)bachelors).getExternalId() + "&amp;officeOid=" + tagusEid + "&amp;togglePreset=bachelor" %>'>
						<img id="tagusButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(posgradEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)bachelors).getExternalId() + "&amp;officeOid=" + posgradEid + "&amp;togglePreset=bachelor" %>'>
						<img id="posgradButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>" />
					</a>
				<%  } %>
			</td>
		</tr>
	</logic:iterate>
</table></div>


<h3 class="degreeOfficeHeaders"><img id="masterTogglerIcon" state="collapsed" class="expcol" src="<%=request.getContextPath() + "/images/toggle_plus10.gif"%>" /><span id="masterTogglerLabel" class="headerTitle"><bean:message key="label.degreeJurisdiction.masters" bundle="ACADEMIC_OFFICE_RESOURCES" /></span></h3>
<div id="masterTables"><table class="tstyle1 thleft degreeOfficeTables">
	<tr>
		<th><bean:message key="label.degreeJurisdiction.degreeName" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th><bean:message key="label.degreeJurisdiction.degreeSigla" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th></th>
	</tr>
	<logic:iterate id="masters" name="degrees" property="masters">
		<tr>
			<td class="degreeEntryNameCell"><bean:write name="masters" property="name" /></td>
			<td class="degreeEntrySiglaCell"><bean:write name="masters" property="sigla" /></td>
			<td class="degreeEntryActionsCell">
				<% 	AcademicProgram academicProgram = (AcademicProgram) masters;
					if (academicProgram.getAdministrativeOffice().getExternalId().equals(alamedaEid)) { 
				%>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)masters).getExternalId() + "&amp;officeOid=" + alamedaEid + "&amp;togglePreset=master" %>'>
						<img id="alamedaButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(tagusEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)masters).getExternalId() + "&amp;officeOid=" + tagusEid + "&amp;togglePreset=master" %>'>
						<img id="tagusButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(posgradEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)masters).getExternalId() + "&amp;officeOid=" + posgradEid + "&amp;togglePreset=master" %>'>
						<img id="posgradButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>" />
					</a>
				<%  } %>
			</td>
		</tr>
	</logic:iterate>
</table></div>


<h3 class="degreeOfficeHeaders"><img id="integratedMasterTogglerIcon" state="collapsed" class="expcol" src="<%=request.getContextPath() + "/images/toggle_plus10.gif"%>" /><span id="integratedMasterTogglerLabel" class="headerTitle"><bean:message key="label.degreeJurisdiction.integratedMasters" bundle="ACADEMIC_OFFICE_RESOURCES" /></span></h3>
<div id="integratedMasterTables"><table class="tstyle1 thleft degreeOfficeTables">
	<tr>
		<th><bean:message key="label.degreeJurisdiction.degreeName" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th><bean:message key="label.degreeJurisdiction.degreeSigla" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th></th>
	</tr>
	<logic:iterate id="integratedMasters" name="degrees" property="integratedMasters">
		<tr>
			<td class="degreeEntryNameCell"><bean:write name="integratedMasters" property="name" /></td>
			<td class="degreeEntrySiglaCell"><bean:write name="integratedMasters" property="sigla" /></td>
			<td class="degreeEntryActionsCell">
				<% 	AcademicProgram academicProgram = (AcademicProgram) integratedMasters;
					if (academicProgram.getAdministrativeOffice().getExternalId().equals(alamedaEid)) { 
				%>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)integratedMasters).getExternalId() + "&amp;officeOid=" + alamedaEid + "&amp;togglePreset=integratedMaster" %>'>
						<img id="alamedaButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(tagusEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)integratedMasters).getExternalId() + "&amp;officeOid=" + tagusEid + "&amp;togglePreset=integratedMaster" %>'>
						<img id="tagusButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(posgradEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)integratedMasters).getExternalId() + "&amp;officeOid=" + posgradEid + "&amp;togglePreset=integratedMaster" %>'>
						<img id="posgradButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>" />
					</a>
				<%  } %>
			</td>
		</tr>
	</logic:iterate>
</table></div>


<h3 class="degreeOfficeHeaders"><img id="phdTogglerIcon" state="collapsed" class="expcol" src="<%=request.getContextPath() + "/images/toggle_plus10.gif"%>" /><span id="phdTogglerLabel" class="headerTitle"><bean:message key="label.degreeJurisdiction.phds" bundle="ACADEMIC_OFFICE_RESOURCES" /></span></h3>
<div id="phdTables"><table class="tstyle1 thleft degreeOfficeTables">
	<tr>
		<th><bean:message key="label.degreeJurisdiction.degreeNameDea" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th><bean:message key="label.degreeJurisdiction.degreeSigla" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th></th>
	</tr>
	<logic:iterate id="deas" name="degrees" property="deas">
		<tr>
			<td class="degreeEntryNameCell"><bean:write name="deas" property="name" /></td>
			<td class="degreeEntrySiglaCell"><bean:write name="deas" property="sigla" /></td>
			<td class="degreeEntryActionsCell">
				<% 	AcademicProgram academicProgram = (AcademicProgram) deas;
					if (academicProgram.getAdministrativeOffice().getExternalId().equals(alamedaEid)) { 
				%>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)deas).getExternalId() + "&amp;officeOid=" + alamedaEid + "&amp;togglePreset=phd" %>'>
						<img id="alamedaButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(tagusEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)deas).getExternalId() + "&amp;officeOid=" + tagusEid + "&amp;togglePreset=phd" %>'>
						<img id="tagusButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(posgradEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)deas).getExternalId() + "&amp;officeOid=" + posgradEid + "&amp;togglePreset=phd" %>'>
						<img id="posgradButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>" />
					</a>
				<%  } %>
			</td>
		</tr>
	</logic:iterate>
	<tr>
		<th><bean:message key="label.degreeJurisdiction.degreeNamePhd" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th><bean:message key="label.degreeJurisdiction.degreeSigla" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th></th>
	</tr>
	<logic:iterate id="phds" name="degrees" property="phds">
		<tr>
			<td class="degreeEntryNameCell"><bean:write name="phds" property="name" /></td>
			<td class="degreeEntrySiglaCell"><bean:write name="phds" property="acronym" /></td>
			<td class="degreeEntryActionsCell">
				<% 	AcademicProgram academicProgram = (AcademicProgram) phds;
					if (academicProgram.getAdministrativeOffice().getExternalId().equals(alamedaEid)) { 
				%>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)phds).getExternalId() + "&amp;officeOid=" + alamedaEid + "&amp;togglePreset=phd" %>'>
						<img id="alamedaButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(tagusEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)phds).getExternalId() + "&amp;officeOid=" + tagusEid + "&amp;togglePreset=phd" %>'>
						<img id="tagusButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(posgradEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)phds).getExternalId() + "&amp;officeOid=" + posgradEid + "&amp;togglePreset=phd" %>'>
						<img id="posgradButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>" />
					</a>
				<%  } %>
			</td>
		</tr>
	</logic:iterate>
</table></div>


<h3 class="degreeOfficeHeaders"><img id="dfaTogglerIcon" state="collapsed" class="expcol" src="<%=request.getContextPath() + "/images/toggle_plus10.gif"%>" /><span id="dfaTogglerLabel" class="headerTitle"><bean:message key="label.degreeJurisdiction.dfas" bundle="ACADEMIC_OFFICE_RESOURCES" /></span></h3>
<div id="dfaTables"><table class="tstyle1 thleft degreeOfficeTables">
	<tr>
		<th><bean:message key="label.degreeJurisdiction.degreeName" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th><bean:message key="label.degreeJurisdiction.degreeSigla" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th></th>
	</tr>
	<logic:iterate id="dfas" name="degrees" property="dfas">
		<tr>
			<td class="degreeEntryNameCell"><bean:write name="dfas" property="name" /></td>
			<td class="degreeEntrySiglaCell"><bean:write name="dfas" property="sigla" /></td>
			<td class="degreeEntryActionsCell">
				<% 	AcademicProgram academicProgram = (AcademicProgram) dfas;
					if (academicProgram.getAdministrativeOffice().getExternalId().equals(alamedaEid)) { 
				%>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)dfas).getExternalId() + "&amp;officeOid=" + alamedaEid + "&amp;togglePreset=dfa" %>'>
						<img id="alamedaButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(tagusEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)dfas).getExternalId() + "&amp;officeOid=" + tagusEid + "&amp;togglePreset=dfa" %>'>
						<img id="tagusButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(posgradEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)dfas).getExternalId() + "&amp;officeOid=" + posgradEid + "&amp;togglePreset=dfa" %>'>
						<img id="posgradButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>" />
					</a>
				<%  } %>
			</td>
		</tr>
	</logic:iterate>
</table></div>


<h3 class="degreeOfficeHeaders"><img id="specTogglerIcon" state="collapsed" class="expcol" src="<%=request.getContextPath() + "/images/toggle_plus10.gif"%>" /><span id="specTogglerLabel" class="headerTitle"><bean:message key="label.degreeJurisdiction.otherDegrees" bundle="ACADEMIC_OFFICE_RESOURCES" /></span></h3>
<div id="specTables"><table class="tstyle1 thleft degreeOfficeTables">
	<tr>
		<th><bean:message key="label.degreeJurisdiction.degreeName" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th><bean:message key="label.degreeJurisdiction.degreeSigla" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th></th>
	</tr>
	<logic:iterate id="specs" name="degrees" property="specs">
		<tr>
			<td class="degreeEntryNameCell"><bean:write name="specs" property="name" /></td>
			<td class="degreeEntrySiglaCell"><bean:write name="specs" property="sigla" /></td>
			<td class="degreeEntryActionsCell">
				<% 	AcademicProgram academicProgram = (AcademicProgram) specs;
					if (academicProgram.getAdministrativeOffice().getExternalId().equals(alamedaEid)) { 
				%>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)specs).getExternalId() + "&amp;officeOid=" + alamedaEid + "&amp;togglePreset=spec" %>'>
						<img id="alamedaButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(tagusEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)specs).getExternalId() + "&amp;officeOid=" + tagusEid + "&amp;togglePreset=spec" %>'>
						<img id="tagusButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(posgradEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)specs).getExternalId() + "&amp;officeOid=" + posgradEid + "&amp;togglePreset=spec" %>'>
						<img id="posgradButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>" />
					</a>
				<%  } %>
			</td>
		</tr>
	</logic:iterate>
</table></div>


<h3 class="degreeOfficeHeaders"><img id="preBolognaTogglerIcon" state="collapsed" class="expcol" src="<%=request.getContextPath() + "/images/toggle_plus10.gif"%>" /><span id="preBolognaTogglerLabel" class="headerTitle"><bean:message key="label.degreeJurisdiction.preBologna" bundle="ACADEMIC_OFFICE_RESOURCES" /></span></h3>
<div id="preBolognaTables"><table class="tstyle1 thleft degreeOfficeTables">
	<tr>
		<th><bean:message key="label.degreeJurisdiction.degreeName" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th><bean:message key="label.degreeJurisdiction.degreeSigla" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
		<th></th>
	</tr>
	<logic:iterate id="preBologna" name="degrees" property="preBologna">
		<tr>
			<td class="degreeEntryNameCell"><bean:write name="preBologna" property="name" /></td>
			<td class="degreeEntrySiglaCell"><bean:write name="preBologna" property="sigla" /></td>
			<td class="degreeEntryActionsCell">
				<% 	AcademicProgram academicProgram = (AcademicProgram) preBologna;
					if (academicProgram.getAdministrativeOffice().getExternalId().equals(alamedaEid)) { 
				%>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)preBologna).getExternalId() + "&amp;officeOid=" + alamedaEid + "&amp;togglePreset=preBologna" %>'>
						<img id="alamedaButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/alamedaButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(tagusEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)preBologna).getExternalId() + "&amp;officeOid=" + tagusEid + "&amp;togglePreset=preBologna" %>'>
						<img id="tagusButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/tagusButton_disabled.png"%>" />
					</a>
				<%  } %>
				
				
				<%  if (academicProgram.getAdministrativeOffice().getExternalId().equals(posgradEid)) { %>
					<img class="selectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_focus.png"%>" />
				<%  } else { %>
					<a style="border-bottom: 0px;" href='<%= request.getContextPath() + "/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction&programOid=" + ((AcademicProgram)preBologna).getExternalId() + "&amp;officeOid=" + posgradEid + "&amp;togglePreset=preBologna" %>'>
						<img id="posgradButton" class="unselectedOffice" src="<%=request.getContextPath() + "/images/posgradButton_disabled.png"%>" />
					</a>
				<%  } %>
			</td>
		</tr>
	</logic:iterate>
</table></div>

