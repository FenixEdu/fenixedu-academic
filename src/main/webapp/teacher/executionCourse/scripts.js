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

    $('#editGroup').click(function (e) {
        $('#secondaryEditGroup').toggleClass('active');
        $('.editAttends').toggleClass('hide');
        $("#checkAllAdd").addClass('active');
        $("#checkAllAdd").trigger('click');
    	if(!$('.editAttends').hasClass("hide")){
			$('html, body').animate({
				scrollTop: $("#addStudentTable").offset().top
			}, 200);
    	}
    	if($('.editAttends').hasClass("hide")){
		    $(".removeCheckbox").each(function () {
	            if (this.checked) {
	            	changeRow($(this),"danger");
	                $(this).prop('checked',false);
	            }
		    });
	    }
        checkboxStatusChecker();
    });
    
    $('#secondaryEditGroup').click(function (e) {
    	$('#secondaryEditGroup').toggleClass('active');
        $('#editGroup').toggleClass('active');
        $('.editAttends').toggleClass('hide');
        $("#checkAllAdd").addClass('active');
        $("#checkAllAdd").trigger('click');
    	if(!$('.editAttends').hasClass("hide")){
			$('html, body').animate({
				scrollTop: $("#addStudentTable").offset().top
			}, 200);
    	}
    	if($('.editAttends').hasClass("hide")){
			$('html, body').animate({
				scrollTop: $("#editGroup").offset().top
			}, 200);
		    $(".removeCheckbox").each(function () {
	            if (this.checked) {
	            	changeRow($(this),"danger");
	                $(this).prop('checked',false);
	            }
		    });
	    }
        e.preventDefault();
        checkboxStatusChecker();
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