<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<style>

/*	Filipe Varela / keoshi.com
 *  Joao Carvalho
 */

/* Typography */

.all {
	min-height: 750px;
}

.symbol,#period ul li a {
	font-family: 'Entypo';
	text-decoration: none;
}

/* General */

ul {
	list-style: none;
}

/* Structure */

#sidebar {
	margin-right: 10px;
	max-height: 75%;
	min-height: 70px;
	overflow-y: auto;
	overflow-x: hidden;
}

@media (max-width: 992px) {
  .affix {
    position: initial;
  }
}

/* Period */
#period {
	background: #f9f9f9;
	border: 1px solid #cccccc;
	margin-bottom: 32px;
	border-radius: 4px;
	box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.08), 0px 0px 0px 1px
		rgba(255, 255, 255, 0.6) inset;
}

#period header {
	padding: 15px 14px;
	border-bottom: 1px solid #cccccc;
	border-top-left-radius: 4px;
	border-top-right-radius: 4px;
	cursor: pointer;
	text-shadow: 0px 1px 0px rgba(255, 255, 255, 0.5);
	box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.08), 0px 1px 3px
		rgba(255, 255, 255, 0.6) inset;
	display: block;
	margin-top: 0;
}

#period header h2 span {
	padding-left: 10px;
}

#period header span {
	color: #666;
}

#period header a.edit-period {
	color: #848484;
	font-size: 12px;
	font-weight: bold;
	float: right !important;
	margin-top: -14px;
	text-decoration: none;
	border-bottom: none;
}

#period header a.edit-period .symbol {
	font-size: 2em;
	color: #848484;
	float: left;
	margin: -12px 4px 0 0;
	vertical-align: -2px;
}

#period header a:hover.edit-period,
#period header a:hover.edit-period .symbol
	{
	color: #333;
}

#period ul li {
	border-top: 1px solid #e6e6e6;
	padding: 6px 20px 6px 16px;
	overflow: hidden;
}

#period ul li:first-child {
	border: none;
}

#period ul li span {
	color: #999;
}

#period ul li img, #period ul li input {
	float: right;
	display: inline-block;
	cursor: pointer;
}

#period .placeholder-tip span {
	 vertical-align: middle;
	 color: black;
}

ul.courses-list {
	padding-left: 0px;
}

/* Periods Selection */
#periods-filters {
	color: #666;
	font-size: 11px;
	text-align: right;
	text-shadow: 0px 1px 0px rgba(255, 255, 255, 0.5);
}

#periods-filters input {
	margin-right: 5px;
	vertical-align: 0px;
}

#periods-filters input,#periods-filters label {
	cursor: pointer;
}

#periods-filters span {
	display: inline-block;
	margin-left: 8px;
	margin-bottom: 8px;
	padding: 5px 5px;
}

#periods-filters span.classes,
#periods-filters span.exams,
#periods-filters span.special-exams,
#periods-filters span.extraordinary-exams,
#periods-filters span.grades, 
#periods-filters span.special-grades,
#periods-filters span.extraordinary-grades
	{
	border: 1px solid #ccc;
	box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.08), 0px 1px 1px
		rgba(255, 255, 255, 0.6) inset;
	border-radius: 4px;
}

#period-filters-cycles {
	margin-top: 8px;
}

/* Header/Selection colors */
.LESSONS header,span.classes {
	background:#9ec6de;
	background-image: linear-gradient(bottom, rgb(158,198,222) 0%, rgb(170,212,235) 100%);
	background-image: -o-linear-gradient(bottom, rgb(158,198,222) 0%, rgb(170,212,235) 100%);
	background-image: -moz-linear-gradient(bottom, rgb(158,198,222) 0%, rgb(170,212,235) 100%);
	background-image: -webkit-linear-gradient(bottom, rgb(158,198,222) 0%, rgb(170,212,235) 100%);
	background-image: -ms-linear-gradient(bottom, rgb(158,198,222) 0%, rgb(170,212,235) 100%);

	background-image: -webkit-gradient(
		linear,
		left bottom,
		left top,
		color-stop(0, rgb(158,198,222)),
		color-stop(1, rgb(170,212,235))
	);
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#aad4eb', endColorstr='#9ec6de',GradientType=0 );
}

.EXAMS header,span.exams {
	background:#d7e38c;
	background-image: linear-gradient(bottom, rgb(215,227,140) 100%, rgb(202,215,127) 0%);
	background-image: -o-linear-gradient(bottom, rgb(215,227,140) 100%, rgb(202,215,127) 0%);
	background-image: -moz-linear-gradient(bottom, rgb(215,227,140) 100%, rgb(202,215,127) 0%);
	background-image: -webkit-linear-gradient(bottom, rgb(215,227,140) 100%, rgb(202,215,127) 0%);
	background-image: -ms-linear-gradient(bottom, rgb(215,227,140) 100%, rgb(202,215,127) 0%);

	background-image: -webkit-gradient(
		linear,
		left bottom,
		left top,
		color-stop(1, rgb(215,227,140)),
		color-stop(0, rgb(202,215,127))
	);
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#d7e38c', endColorstr='#cad77f',GradientType=0 );
}

.EXAMS_SPECIAL_SEASON header,span.special-exams {
	background:#f1d67e;
	background-image: linear-gradient(bottom, rgb(254,225,139) 100%, rgb(241,214,126) 0%);
	background-image: -o-linear-gradient(bottom, rgb(254,225,139) 100%, rgb(241,214,126) 0%);
	background-image: -moz-linear-gradient(bottom, rgb(254,225,139) 100%, rgb(241,214,126) 0%);
	background-image: -webkit-linear-gradient(bottom, rgb(254,225,139) 100%, rgb(241,214,126) 0%);
	background-image: -ms-linear-gradient(bottom, rgb(254,225,139) 100%, rgb(241,214,126) 0%);

	background-image: -webkit-gradient(
		linear,
		left bottom,
		left top,
		color-stop(1, rgb(254,225,139)),
		color-stop(0, rgb(241,214,126))
	);
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#fee18b', endColorstr='#f1d67e',GradientType=0 );
}

.EXAMS_EXTRAORDINARY_SEASON header,span.extraordinary-exams {
    background:#ecf27d;
    background-image: linear-gradient(bottom, rgb(236,242,125) 100%, rgb(237,244,107) 0%);
    background-image: -o-linear-gradient(bottom, rgb(236,242,125) 100%, rgb(237,244,107) 0%);
    background-image: -moz-linear-gradient(bottom, rgb(236,242,125) 100%, rgb(237,244,107) 0%);
    background-image: -webkit-linear-gradient(bottom, rgb(236,242,125) 100%, rgb(237,244,107) 0%);
    background-image: -ms-linear-gradient(bottom, rgb(236,242,125) 100%, rgb(237,244,107) 0%);

    background-image: -webkit-gradient(
            linear,
            left bottom,
            left top,
            color-stop(1, rgb(236,242,125)),
            color-stop(0, rgb(237,244,107))
    );
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#edf46b', endColorstr='#ecf27d',GradientType=0 );
}

.GRADE_SUBMISSION header,span.grades {
	background:#ebb88e;
	background-image: linear-gradient(bottom, rgb(247,196,155) 100%, rgb(235,184,142) 0%);
	background-image: -o-linear-gradient(bottom, rgb(247,196,155) 100%, rgb(235,184,142) 0%);
	background-image: -moz-linear-gradient(bottom, rgb(247,196,155) 100%, rgb(235,184,142) 0%);
	background-image: -webkit-linear-gradient(bottom, rgb(247,196,155) 100%, rgb(235,184,142) 0%);
	background-image: -ms-linear-gradient(bottom, rgb(247,196,155) 100%, rgb(235,184,142) 0%);

	background-image: -webkit-gradient(
		linear,
		left bottom,
		left top,
		color-stop(1, rgb(247,196,155)),
		color-stop(0, rgb(235,184,142))
	);
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f7c49b', endColorstr='#ebb88e',GradientType=0 );
}

.GRADE_SUBMISSION_SPECIAL_SEASON header,span.special-grades {
	background:#f2a8a8;
	background-image: linear-gradient(bottom, rgb(242,168,168) 0%, rgb(255,181,181) 100% );
    background-image: -o-linear-gradient(bottom, rgb(242,168,168) 0%, rgb(255,181,181) 100% );
    background-image: -moz-linear-gradient(bottom, rgb(242,168,168) 0%, rgb(255,181,181) 100% );
    background-image: -webkit-linear-gradient(bottom, rgb(242,168,168) 0%, rgb(255,181,181) 100% );
    background-image: -ms-linear-gradient(bottom, rgb(242,168,168) 0%, rgb(255,181,181) 100% );
    background-image: -webkit-gradient(
		linear,
		left bottom,
		left top,
		color-stop(0, rgb(242,168,168)),
        color-stop(1, rgb(255,181,181))
	);
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffb5b5', endColorstr='#f2a8a8',GradientType=0 );
}

.GRADE_SUBMISSION_EXTRAORDINARY_SEASON header,span.extraordinary-grades {
    background:#de98f2;
    background-image: linear-gradient(bottom, rgb(222,152,242) 0%, rgb(216,123,242) 100% );
    background-image: -o-linear-gradient(bottom, rgb(222,152,242) 0%, rgb(216,123,242) 100% );
    background-image: -moz-linear-gradient(bottom, rgb(222,152,242) 0%, rgb(216,123,242) 100% );
    background-image: -webkit-linear-gradient(bottom, rgb(222,152,242) 0%, rgb(216,123,242) 100% );
    background-image: -ms-linear-gradient(bottom, rgb(222,152,242) 0%, rgb(216,123,242) 100% );
    background-image: -webkit-gradient(
            linear,
            left bottom,
            left top,
            color-stop(0, rgb(222,152,242)),
            color-stop(1, rgb(216,123,242))
    );
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#d87bf2', endColorstr='#de98f2',GradientType=0 );
}

/* Edit Periods */
.period-edit {
	padding: 30px 0 30px 30px;
	border-bottom: 1px solid #cccccc;
}

.period-edit fieldset {
	margin-bottom: 15px;
}

.period-edit fieldset legend {
	display: none;
}

.editar-periodo label {
	display: block;
	font-weight: bold;
	margin-bottom: 5px;
}

.editar-periodo input {
	font-size: 0.9em;
	color: #666;
	margin: 0px;
	padding: 6px 6px;
	border: 1px solid #d6d6d6;
	outline: none;
	box-shadow: 0px 0px 6px rgba(255, 255, 255, 0), 0px 1px 2px
		rgba(0, 0, 0, 0.1) inset;
	border-radius: 4px;
}

.editar-periodo input:disabled.confirmar,
.editar-periodo input:disabled.confirmar:hover,
.editar-periodo input:disabled.confirmar:focus,
.editar-periodo input:disabled.confirmar:active {
	background:#c9d7e5;
	box-shadow:none;
}


.editar-periodo input:focus {
	color: #333;
	border: 1px solid rgb(158, 198, 222);
	box-shadow: 0px 0px 6px rgba(158, 198, 222, 0.6), 0px 1px 2px
		rgba(0, 0, 0, 0.2) inset;
}

.editar-periodo select {
	font-size: 0.9em;
	color: #666;
	margin: 0px;
	border: 1px solid #d6d6d6;
	outline: none;
}

.editar-periodo select:focus {
	color: #333;
	border: 1px solid rgb(158, 198, 222);
	box-shadow: 0px 0px 6px rgba(158, 198, 222, 0.6), 0px 1px 2px
		rgba(0, 0, 0, 0.2) inset;
}

.period-edit .data-bloco {
	margin-right: 20px;
	display: inline-block;
}

.period-edit .links-periodo {
	font-size: 0.85em;
	float: right;
	margin-right: 30px;
	margin-top: -10px;
}

.period-edit .symbol {
	color: #999;
	font-size: 2.2em;
	line-height: 0.1em;
	vertical-align: -4px;
	margin-left: 6px;
}

.period-edit .symbol a {
	color: #999;
	text-decoration: none;
}

.period-edit .symbol a:hover {
	color: #105c93;
}

.editar-periodo input.confirmar,.editar-periodo input.cancelar {
	margin-top: 10px;
	font: 1em sans-serif;
	padding: 8px 20px;
	color: white;
	border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.2) rgba(0, 0, 0, 0.4);
	cursor: pointer;
	text-shadow: 0px -1px 0px rgba(0, 0, 0, 0.3);
	box-shadow: 0px 0px 6px rgba(255, 255, 255, 0.2) inset, 0px 1px 4px
		rgba(0, 0, 0, 0.3);
}

.editar-periodo input.confirmar {
	background-color: rgb(16, 92, 147);
	background-image: -webkit-linear-gradient(top, rgb(142, 174, 202),
		rgb(101, 138, 175) );
}

.editar-periodo input.cancelar {
	margin-left: 15px;
	background-color: rgb(16, 92, 147);
	background-image: -webkit-linear-gradient(top, rgb(210, 210, 210),
		rgb(150, 150, 150) );
}

.editar-periodo input.confirmar:hover,
.editar-periodo input.confirmar:focus,
.editar-periodo input.cancelar:hover,
.editar-periodo input.cancelar:focus
	{
	box-shadow: 0px 0px 10px rgba(255, 255, 255, 0.8) inset, 0px 1px 4px
		rgba(0, 0, 0, 0.3);
}

.editar-periodo input.confirmar:active,.editar-periodo input.cancelar:active
	{
	box-shadow: 0px 1px 4px 1px rgba(0, 0, 0, 0.5) inset, 0px 1px 4px
		rgba(0, 0, 0, 0.3);
}

.links-periodo a {
	text-decoration: none;
}

.links-periodo a.eliminar {
	color: #cc3333;
	margin-left: 10px;
}

.links-periodo a:hover {
	text-decoration: underline;
}

/* Sidebar */
.selected {
	background-color: #ECB;
}

#sidebar {
	background: #fafafa;
	border: 1px solid #cccccc;
	margin-bottom: 32px;
	border-radius: 4px;
	box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.08), 0px 0px 0px 1px
		rgba(255, 255, 255, 0.6) inset;
}

#sidebar ul {
	margin: 10px 0 16px;
	text-indent: 14px;
	line-height: 24px;
}

#sidebar li {
	display: block;
	cursor: pointer;
}

#sidebar li:hover {
	background: rgba(255, 180, 23, 0.2);
}

#years-filters {
	padding: 14px;
	font-size: 11px;
	line-height: 22px;
}

#years-filters span {
	margin-right: 16px;
	display: inline-block;
}

#years-filters input {
	vertical-align: 0px;
}

.draggable_course {
	padding: 5px;
	display: block;
	cursor: pointer;
}

.draggable_course:hover {
	background: rgba(255, 180, 23, 0.2);
}

/* Misc */
.period-edit,ul.courses-list,.hide {
	display: none;
}

/* Transitions */
a,input,.symbol {
	-webkit-transition: all 0.15s ease;
}

</style>

<link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/jqTheme/ui.all.css">

<script src="<%= request.getContextPath()%>org.joda.time.DateTimeorg.joda.time.DateTime/javaScript/jquery/jquery-ui.js"></script>

<script type="text/javascript">

	var selectedYears = new Array(1, 1, 1, 1, 1, 1, 1);
	
	function removeFunction() {
		if(!confirm("Tem a certeza que pretende remover o curso seleccionado?"))
			return;
		var parent = $(this).parents().eq(2);
		var size = parent.children('.courses-list').children().size() - 1;
		updateSize(parent, size);
		parent.find(".saveButton").show();
		showLeaveWarning();
		$(this).parent().fadeOut(300, function() { $(this).remove(); });
	}
	
	function dropFunction(event, ui) {
		if(!$(ui.draggable).hasClass("course-dragging"))
			return;
				
		var availableYears = getYears(parseInt($(ui.draggable).find("#availableYears").html()));
		
		if(availableYears.length == 0) {
			alert("Seleccione pelo menos um ano válido para o curso escolhido!");
			return;
		}
				
		var oid = $(ui.draggable).children('#oid').html();
		
		var oids = $(this).children(".courses-list").children().find("#oid");

		for(i = 0; i < oids.length; i++) {
			if(oids.get(i).innerHTML == oid) {
				return;
			}
		}
		
		var name = $(ui.draggable).children('#presentationName').html();
		
		var years = getYearText(availableYears);
		
		$(this).children('.courses-list').show();
		$(this).children('.courses-list').
			prepend(
				'<li><div id="oid" style="display:none">' + oid + '</div>'
				+ '<div id="years" style="display:none">' + availableYears + '</div>'
				+ name + ' <span>- ' +
				years + '</span><img src="${pageContext.request.contextPath}/images/iconRemoveOff.png" alt="remove"/>');
		var size = $(this).children('.courses-list').children().size();
		updateSize(this, size);
		$(this).find(".courses-list li img").first().click(removeFunction);
		if(!$(this).hasClass("newObject")) {
			$(this).find(".saveButton").show();
			showLeaveWarning();
		} else {
			$(this).find("input[name=createPeriod]").attr("disabled", false);
		}
	}
	
	function getYears(maxYears) {
		var years = new Array();
		
		if(selectedYears[6]) {
			years.push(-1);
		} else {
			for(i = 0; i < maxYears; i++) {
				if(selectedYears[i])
					years.push((i+1));
			}
		}
				
		return years;
	}
	
	function getYearText(years) {
		if(years.indexOf(-1) >= 0)
			return 'Todos os anos';
		
		var returnStr = '';
		
		for(i in years) {
			if(returnStr.length > 0)
				returnStr += ', ';
			returnStr += years[i]  + 'º';
		}
		
		if(returnStr.length == 2)
			returnStr += ' Ano';
		else
			returnStr += ' Anos';
		
		return returnStr;
	}
	
	
	function updateSize(element, size) {
		$(element).children("#header").children('#size').html("Total: " + size + " cursos");
	}
	
	function showLeaveWarning() {
		$(window).bind('beforeunload', function(){
		    return "Tem alterações não guardadas. Tem a certeza que pretende sair da página?";
		});
		$("form").submit(function(){
		    $(window).unbind("beforeunload");
		});
	}
	function addDateFunction() {
		var newPrototype = $("#date-prototype").clone();
		newPrototype.removeAttr("id");
		makeDatePicker(newPrototype.find(".startDate"), ".endDate", "minDate");
		makeDatePicker(newPrototype.find(".endDate"), ".startDate", "maxDate");
		newPrototype.removeClass("hide");
		newPrototype.children(".add-date").click(addDateFunction);
		newPrototype.children(".remove-date").click(removeDateFunction);
		$(this).parents().eq(1).append(newPrototype);
	}
	
	function removeDateFunction() {
		$(this).parent().remove();
	}
	
	function makeDatePicker(element, otherName, date) {
		element.datepicker({
			defaultDate: "+1w",
			changeMonth: true,
			numberOfMonths: 2,
			showButtonPanel: true,
			showAnim: 'drop',
			dateFormat: 'dd/mm/yy',
			onSelect: function( selectedDate ) {
				$(this).parent().parent().find(otherName).datepicker( "option", date, selectedDate );
			}
		});
	}
	
	function changeType(element, index) {
		element.parents().eq(5).
			removeClass(selectedTypeValue[index]).
			addClass(element.val()); 
		selectedTypeValue[index] = element.val()
	}
	
	function prepareToSubmit(element, id) {
						
		var intervals = element.find("#period .period-edit .datas .date");
		
		var ret = "";
		
		for(i = 0; i < intervals.size(); i++) {
			var interval = $(intervals.get(i));

			var startDate = interval.find(".startDate").val();
			var endDate = interval.find(".endDate").val();
			
			if(startDate.length == 0 || endDate.length == 0)
				continue;
			
			if(ret.length > 0) {
				ret += ";";
			}
			ret += startDate + "," + endDate;
		}
		
		element.children(".intervals").val(ret);
	}
	
	function prepareToCreate(element) {
		prepareToSubmit(element);
		prepareEditPeriod(element);
		if(element.children(".courses").val() == "") {
			alert("Por favor seleccione pelo menos um curso!");
			return false;
		}
		return true;
	}
	
	function deletePeriod(element, index) {
		if(!confirm("Tem a certeza que pretende apagar este período? Todos os cursos associados ficarão sem período associado."))
			return;
		
		var form = element.parents().eq(3);
		
		form.append('<input type="hidden" name="removePeriod" value="' + index +'" />');
		form.submit();
	}
	
	function prepareEditPeriod(element, index) {
		
		var OIDs = '';
		
		var oidhtml = element.find(".courses-list li");
		
		for(i = 0; i < oidhtml.length; i++) {
			if(OIDs.length > 0)
				OIDs += ';';
			
			var elem = $(oidhtml.get(i));

			OIDs += elem.find("#oid").html() + ":" + elem.find("#years").html();
			
		}
						
		element.children(".courses").val(OIDs);
		
	}
	
	function duplicatePeriodFunction(element, id) {
		$("#duplicate-dialog").find("input[value=" + selectedTypeValue[id] + "]").attr('disabled',true);
		$("#duplicate-dialog").find("input[name=toDuplicateId]").val(id);
		$('#duplicate-dialog').modal();
	}
	
	function getDocHeight() {
	    var D = document;
	    return Math.max(
	        Math.max(D.body.scrollHeight, D.documentElement.scrollHeight),
	        Math.max(D.body.offsetHeight, D.documentElement.offsetHeight),
	        Math.max(D.body.clientHeight, D.documentElement.clientHeight)
	    );
	}
	
	
	$(document).ready(
			function() {
				var sidebarYloc = parseInt($('#sidebar').css("top").substring(0,$('#sidebar').css("top").indexOf("px")))  
			    $(window).scroll(function () {  
			    	var offset = sidebarYloc+$(document).scrollTop()+"px";
			    	var bottom = getDocHeight() - (window.pageYOffset + window.innerHeight);
			    	if (bottom > 100) {
			    		$('#sidebar').animate({top:offset},{duration:500,queue:false});
			    	}
			    });  

				$('.edit-period').click(function(event) {
					$(this).parent().parent().children('.period-edit').toggle();
					event.stopPropagation();
				});

				$('.period').children('header').not(".edit-period").click(function() {
					$(this).parent().find('ul.courses-list').slideToggle('slow');
				});
				
				
				$('.period').droppable({
					drop: dropFunction
				});

				$(".draggable_course").draggable({
					revert : 'invalid',
					helper: 'clone',
					appendTo: 'body',
					start: function() {
						$(this).addClass("course-dragging");
					}
				});

				$("#periods-classes").change(function() {
					$(".LESSONS").fadeToggle('fast');
				});
				
				$("#periods-exams").change(function() {
					$(".EXAMS").fadeToggle('fast');
				});
				
				$("#periods-specialExams").change(function() {
					$(".EXAMS_SPECIAL_SEASON").fadeToggle('fast');
				});
				
				$("#periods-specialGrades").change(function() {
					$(".GRADE_SUBMISSION_SPECIAL_SEASON").fadeToggle('fast');
				});

                $("#periods-extraordinaryExams").change(function() {
                    $(".EXAMS_EXTRAORDINARY_SEASON").fadeToggle('fast');
                });

                $("#periods-extraordinaryGrades").change(function() {
                    $(".GRADE_SUBMISSION_EXTRAORDINARY_SEASON").fadeToggle('fast');
                });
				
				$("#periods-grades").change(function() {
					$(".GRADE_SUBMISSION").fadeToggle('fast');
				});
				
				$("#first-year").change(function(){
					if(!$(this).is(":checked")) {
						$("input[name=all-year]").attr("checked", false);
						selectedYears[6] = false;
					}
					selectedYears[0] ^= true;
				});

				$("#second-year").change(function(){
					if(!$(this).is(":checked")) {
						$("input[name=all-year]").attr("checked", false);
						selectedYears[6] = false;
					}
					selectedYears[1] ^= true;
				});
				
				$("#third-year").change(function(){
					if(!$(this).is(":checked")) {
						$("input[name=all-year]").attr("checked", false);
						selectedYears[6] = false;
					}
					selectedYears[2] ^= true;
				});
				
				$("#fourth-year").change(function(){
					if(!$(this).is(":checked")) {
						$("input[name=all-year]").attr("checked", false);
						selectedYears[6] = false;
					}
					selectedYears[3] ^= true;
				});
				
				$("#fifth-year").change(function(){
					if(!$(this).is(":checked")) {
						$("input[name=all-year]").attr("checked", false);
						selectedYears[6] = false;
					}
					selectedYears[4] ^= true;
				});
				
				$("#sixth-year").change(function(){
					if(!$(this).is(":checked")) {
						$("input[name=all-year]").attr("checked", false);
						selectedYears[6] = false;
					}
					selectedYears[5] ^= true;
				});
				
				$("#all-year").change(function(){
					
					var checked = $(this).is(":checked");
					
					$("input[name=first-year]").attr("checked", checked);
					selectedYears[0] = checked;
					
					$("input[name=second-year]").attr("checked", checked);
					selectedYears[1] = checked;

					$("input[name=third-year]").attr("checked", checked);
					selectedYears[2] = checked;

					$("input[name=fourth-year]").attr("checked", checked);
					selectedYears[3] = checked;

					$("input[name=fifth-year]").attr("checked", checked);
					selectedYears[4] = checked;
					
					$("input[name=sixth-year]").attr("checked", checked);
					selectedYears[5] = checked;
					
					selectedYears[6] ^= true;
				});
				
				$(".courses-list li img").click(removeFunction);
				
				makeDatePicker($(".startDate").not(".prototype"), ".endDate", "minDate");
				
				makeDatePicker($(".endDate").not(".prototype"), ".startDate", "maxDate");
				
				$(".add-date").click(addDateFunction);
				
				$(".remove-date").click(removeDateFunction);
				
				$(".saveButton").hide();
				
				$(".newObject .period-edit").show();
				
				if($(".newObject").length > 0)
					showLeaveWarning();
				
			});

</script>
