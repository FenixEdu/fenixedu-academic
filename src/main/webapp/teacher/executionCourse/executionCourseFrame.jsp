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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<style>
.hide {
	display: none;
}
</style>

<c:set var="base" value="${pageContext.request.contextPath}/teacher" />
<c:set var="req" value="${pageContext.request}" />

<div class="row">
	<main class="col-sm-10 col-sm-push-2">
		<ol class="breadcrumb">
			<em>${executionCourse.name} - ${executionCourse.executionPeriod.qualifiedName}
				(<c:forEach var="degree" items="${executionCourse.degreesSortedByDegreeName}"> ${degree.sigla} </c:forEach>)
			</em>
		</ol>
		<jsp:include page="${teacher$actual$page}" />
	</main>
	<nav class="col-sm-2 col-sm-pull-10" id="context">
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong>${executionCourse.prettyAcronym}</strong>
			</li>
			<c:if test="${not empty executionCourse.siteUrl}">
				<li>
				    <!-- NO_CHECKSUM --><a href="${executionCourse.siteUrl}" target="_blank">
						<bean:message key="link.executionCourseManagement.menu.view.course.page"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.personalization}">
				<li>
					<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/alternativeSite.do?method=prepareCustomizationOptions&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.personalizationOptions"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.siteArchive}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/generateArchive.do?method=prepare&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.executionCourse.archive.generate"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.summaries}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/searchECLog.do?method=prepareInit&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.executionCourse.log"/>
					</a>
				</li>
			</c:if>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong><bean:message key="label.executionCourseManagement.menu.communication"/></strong>
			</li>
			<c:if test="${professorship.permissions.announcements}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/announcementManagement.do?method=viewAnnouncements&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.announcements"/>
					</a>
				</li>
			</c:if>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/executionCourseForumManagement.do?method=viewForuns&executionCourseID='.concat(executionCourse.externalId))}">
					<bean:message key="link.teacher.executionCourseManagement.foruns"/>
				</a>
			</li>
			<c:if test="${professorship.permissions.sections}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageExecutionCourseSite.do?method=sections&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="label.executionCourseManagement.menu.sections"/>
					</a>
				</li>
			</c:if>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong><bean:message key="label.executionCourseManagement.menu.management"/></strong>
			</li>
			<c:if test="${professorship.permissions.summaries}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/summariesManagement.do?method=prepareShowSummaries&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.summaries"/>
					</a>
				</li>
			</c:if>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/teachersManagerDA.do?method=viewTeachersByProfessorship&executionCourseID='.concat(executionCourse.externalId))}">
					<bean:message key="link.teachers"/>
				</a>
			</li>
			<c:if test="${professorship.permissions.summaries}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/searchECAttends.do?method=prepare&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.students"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.planning}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageExecutionCourse.do?method=lessonPlannings&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.lessonPlannings"/>
					</a>
				</li>
			</c:if>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/markSheetManagement.do?method=evaluationIndex&executionCourseID='.concat(executionCourse.externalId))}">
					<bean:message key="link.evaluation"/>
				</a>
			</li>
			<c:if test="${professorship.permissions.groups}">
				<li>
                <a href="${base}/${executionCourse.externalId}/student-groups/show">
						<bean:message key="link.groupsManagement"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.shift}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageExecutionCourse.do?method=manageShifts&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="label.shifts"/>
					</a>
				</li>
			</c:if>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong><bean:message key="label.executionCourseManagement.menu.curricularInfo"/></strong>
			</li>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageObjectives.do?method=objectives&executionCourseID='.concat(executionCourse.externalId))}">
					<bean:message key="link.objectives"/>
				</a>
			</li>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageProgram.do?method=program&executionCourseID='.concat(executionCourse.externalId))}">
					<bean:message key="link.program"/>
				</a>
			</li>
			<c:if test="${professorship.permissions.evaluationMethod}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageEvaluationMethod.do?method=evaluationMethod&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.evaluationMethod"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.bibliografy}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageBibliographicReference.do?method=bibliographicReference&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.bibliography"/>
					</a>
				</li>
			</c:if>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong><bean:message key="label.executionCourseManagement.menu.curricularUnitsQuality"/></strong>
			</li>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/teachingInquiry.do?method=showInquiriesPrePage&executionCourseID='.concat(executionCourse.externalId))}">
					<bean:message key="link.teachingReportManagement"/>
				</a>
			</li>
			<c:if test="${professorship.responsibleFor}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/regentInquiry.do?method=showInquiriesPrePage&executionCourseID='.concat(executionCourse.externalId))}">
						<bean:message key="link.regentReportManagement"/>
					</a>
				</li>
			</c:if>
		</ul>
	</nav>
</div>


<script src="${pageContext.request.contextPath}/javaScript/latinize.min.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/jquery.lazyload.min.js"></script> 
<script>

function sortTable(table, order) {
    var asc   = order === 'asc',
    tbody = table.find('tbody');
    tbody.find('tr').sort(function(a, b) {
        if (asc) {
            return $('td:first', a).text().localeCompare($('td:first', b).text());
        } else {
            return $('td:first', b).text().localeCompare($('td:first', a).text());
        }
    }).appendTo(tbody);
}

function checkboxStatusChecker(){
	var addChecked = $('input[type=checkbox].addCheckbox:checked').length;
	var removeChecked = $('input[type=checkbox].removeCheckbox:checked').length;
	
	if (removeChecked == $('input[type=checkbox].removeCheckbox').length){
		$("#checkAllRemove").addClass('active');
	} else {
		$("#checkAllRemove").removeClass("active");
	}
	
	if (addChecked == $('input[type=checkbox].addCheckbox').length){
		$("#checkAllAdd").addClass("active");
	} else {
		$("#checkAllAdd").removeClass("active");
	}

	if (addChecked == 0 && removeChecked == 0){
		$(".showOnChange").addClass('hide');
	} else {
		$(".showOnChange").removeClass('hide');
	}
}

function changeRow(checkbox,status){
	var row = $(checkbox).closest('tr');
	if(row.css('display') !== 'none'){
		togglePlusGlyphSign($(checkbox).siblings('span'));
		row.toggleClass(status);
	}
}

function togglePlusGlyph(object) {
    object.toggleClass('glyphicon-plus');
    object.toggleClass('glyphicon-minus');
}

function togglePlusGlyphSign(object) {
    object.toggleClass('glyphicon-plus-sign');
    object.toggleClass('glyphicon-minus-sign');
}

$(document).ready(function () {
	if(typeof(runMe)!='undefined'){
		runMe();
	}
	
	$('.sortableTable').each(function(e) {
		sortTable($(this), 'asc');
	});
	
    $('#showPhotos').click(function () {
        $('.showPhotos').toggleClass('hide');
        $('img.lazy').trigger('visiblePhoto');
    });

    $('#editGroup').click(function () {
        $('.editAttends').toggleClass('hide');
        $("#checkAllAdd").addClass('active');
        $("#checkAllAdd").trigger('click');
       // $("#tableFilter").focus();
        checkboxStatusChecker()
    });

    $('#addStudents tr').click(function (event) {
        if (event.target.type !== 'checkbox') {
            $(':checkbox', row).trigger('click');
        }
    });

    $('.rowClickable tr').click(function (event) {
        if (event.target.type !== 'checkbox') {
            $(':checkbox', this).trigger('click');
        }
    });

    $("#checkAllRemove").click(function (event) {
        if (!$(this).hasClass('active')) {
            $(".removeCheckbox").each(function () {
                if (!this.checked) {
                	changeRow($(this),"danger");
                    $(this).prop('checked',true);
                }
            });
        } else {
            $(".removeCheckbox").each(function () {
                if (this.checked) {
                	changeRow($(this),"danger");
                    $(this).prop('checked',false);
                }
            });
        };
        checkboxStatusChecker()
    });
    
    $("#checkAllAdd").click(function (event) {
        if (!$(this).hasClass('active')) {
            $(".addCheckbox").each(function () {
                if (!this.checked) {
                	changeRow($(this),"success");
                    $(this).prop('checked',true);
                }
            });
        } else {
            $(".addCheckbox").each(function () {
                if (this.checked) {
                	changeRow($(this),"success");
                    $(this).prop('checked',false);
                }
            });
        };
        checkboxStatusChecker()
    });

    $(".addCheckbox").change(function (e) {
    	changeRow($(this),"success");
	    checkboxStatusChecker();
    });
    
    $(".removeCheckbox").change(function (e) {
    	changeRow($(this),"danger");
        checkboxStatusChecker();

    });
    
    $("img.lazy").lazyload({
 	   event : "visiblePhoto"
   });

    

    (function (document) {
        'use strict';
        var LightTableFilter = (function (Arr) {
            var _input;

            function _onInputEvent(e) {
                _input = e.target;
                var tables = document.getElementsByClassName(
                    _input.getAttribute(
                        'data-table'));
                Arr.forEach.call(tables, function (
                    table) {
                    Arr.forEach.call(table.tBodies,
                        function (tbody) {
                            Arr.forEach.call(
                                tbody.rows,
                                _filter
                            );
                        });
                });
            }

            function _filter(row) {
                var text = latinize(row.textContent).toLowerCase(),
                    val = latinize(_input.value).toLowerCase();
               if( text.indexOf(val) === -1) {
            	   row.getElementsByTagName( 'input' )[0].disabled=true;
	               row.style.display = 'none';
	            } else {
	            	row.getElementsByTagName( 'input' )[0].disabled=false;
	               row.style.display =  'table-row';
	            }
            }
            return {
                init: function () {
                    var inputs = document.getElementsByClassName(
                        'light-table-filter');
                    Arr.forEach.call(inputs, function (
                        input) {
                        input.oninput =
                            _onInputEvent;
                    });
                }
            };
        })(Array.prototype);
        document
            .addEventListener('readystatechange', function () {
                if (document.readyState === 'complete') {
                    LightTableFilter.init();
                }
            });
    })(document);
    
    
});
</script>

