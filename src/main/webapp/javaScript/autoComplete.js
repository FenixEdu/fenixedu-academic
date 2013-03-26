/**
*    Json key/value autocomplete for jQuery 
*    Provides a transparent way to have key/value autocomplete
*    Copyright (C) 2008 Ziadin Givan www.CodeAssembly.com  
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Lesser General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU Lesser General Public License
*    along with this program.  If not, see http://www.gnu.org/licenses/
*    
*    Examples 
*	 $("input#example").autocomplete("autocomplete.php");//using default parameters
*	 $("input#example").autocomplete("autocomplete.php",{minChars:3,timeout:3000,validSelection:false,parameters:{'myparam':'myvalue'},before : function(input,text) {},after : function(input,text) {}});
*    minChars = Minimum characters the input must have for the ajax request to be made
*	 timeOut = Number of miliseconds passed after user entered text to make the ajax request   
*    validSelection = If set to true then will invalidate (set to empty) the value field if the text is not selected (or modified) from the list of items.
*    parameters = Custom parameters to be passed
*    after, before = a function that will be caled before/after the ajax request
*/
jQuery.fn.autocomplete = function(url, settings ) 
{
	return this.each( function()//do it for each matched element
	{
		//this is the original input
		var textInput = jQuery(this);
		//create a new hidden input that will be used for holding the return value when posting the form, then swap names with the original input
		textInput.after('<input type=hidden name="' + textInput.attr("name") + '"/>').attr("name", textInput.attr("name") + "_text");
		var valueInput = jQuery(this).next();
		//create the ul that will hold the text and values
		valueInput.after('<div><ul class="autocomplete"><li></li></ul></div>');
		var list = valueInput.next().children();
		var oldText = '';
		var typingTimeout;
		var size = 0;
		var selected = -1;

		settings = jQuery.extend(//provide default settings
		{
			minChars : 1,
			timeout: 300,
			width: null,
			after : null,
			before : null,
			select : null,
			error: null,
			cleanSelection: null,
			validSelection : true,
			parameters : {'inputName' : valueInput.attr('name'), 'inputId' : textInput.attr('id')}
		} , settings);

		function getData(text)
		{
			window.clearInterval(typingTimeout);
			
			if (text != oldText && (settings.minChars != null && text.length >= settings.minChars))
			{
				clear();
				
				if (settings.before != null) 
				{
					settings.before(textInput,text);
				}
				textInput.addClass('autocomplete-loading');
				settings.parameters.value = text;
				jQuery.ajax({ 
                    url: url, 
                    data: settings.parameters,
                    dataType: 'json', 
                    success: function(data) { 
							
        					var items = '';
        					if (data)
        					{
        						size = data.length;
        						for (i = 0; i < data.length; i++)//iterate over all options
        						{
        						  items += '<li id="' + data[i]['oid'] + '" name="' + data[i]['description'] + '">' + highlightData(text,data[i]['description'])  + '</li>'
        						
        						  list.html(items);
        						  //on mouse hover over elements set selected class and on click set the selected value and close list
        						  list.show().children().
        						  hover(function() { jQuery(this).addClass("selected").siblings().removeClass("selected");}, function() { jQuery(this).removeClass("selected") } ).
        						  click(function () { 
        							valueInput.val( jQuery(this).attr('name') );
        							textInput.val( jQuery(this).text() ); 
        							if (settings.select != null) {
        								settings.select(textInput,text,jQuery(this));
        							}
        							clear(); 
        							selectNextInput();
        						  	});
        						}
        						//clear the previous list if the new one has no elements
        						if(size == 0) {
        							list.html('<li></li>');
        						}
        						if (settings.after != null) 
        						{
        							settings.after(textInput,text);
        						}
        					}
        					textInput.removeClass('autocomplete-loading');
					}, 
					error: function (XMLHttpRequest, textStatus, errorThrown) { 
						textInput.removeClass('autocomplete-loading');
						if (settings.error != null) {
							settings.error(textInput,text);
						}
					} 
				}); 

				oldText = text;
			}
		}
		
		function highlightData(input, text) {
			
			inputParts = input.split(' ');
			for (j = 0; j < inputParts.length; j++) {
				if (inputParts[j].length>1) {
					text = text.replace(new RegExp("(" + inputParts[j] + ")" ,"gi"), "<strong>$1</strong>");
				}
			}
			return text;
		}
		
		function clear()
		{
			list.hide();
			size = 0;
			selected = -1;
			oldText = '';
		}	
				
		function selectNextInput() {
			var nextRow = textInput.parents("tr").next("tr")
			if (nextRow.size() != 0) {
				var nextInput = nextRow.find(":input:visible:first");
				nextInput.focus();
			}
			else {
			// table ended let's try to find the submit button
				var submitButton = textInput.parents("table").next(":input[type=submit]:visible:first");
				submitButton.focus();
			}				
		}
		
		textInput.focus(function focus(e) {
			var currentWidth = settings.width != null ? settings.width : jQuery(e.currentTarget).width();
			if (list.width() != currentWidth) {
				list.css({ width: currentWidth});
			}
			list.show();
		});
		
		textInput.blur( function(e) {
			 window.setTimeout(function() { list.hide(); },'300');
		});
		
		textInput.keydown(function(e) 
		{
			window.clearInterval(typingTimeout);
			if(e.which == 27)//escape
			{
				clear();
			} 
			
			else if (e.which == 46 || e.which == 8)//delete and backspace
			{
				valueInput.val('');
				if(settings.cleanSelection != null) {
					settings.cleanSelection(textInput,textInput.val());
				}
				if (textInput.val().length > 3) {
					typingTimeout = window.setTimeout(function() { getData(textInput.val()) },settings.timeout);
				}
				else {
					clear();
				}
			}
			else if(e.which == 13)//enter 
			{ 
				if ( list.css("display") == "none")
				{ 
					valueInput.parents("form").submit();
				} else
				{
					
					if (settings.select != null) {
						selectedItem = jQuery(list.children()[selected]);
						settings.select(textInput,textInput.val(),selectedItem);
						valueInput = textInput.next();
						valueInput.val(selectedItem.attr('name'));
					}
					
					clear();
					selectNextInput();
				}
				e.preventDefault();
				return false;
			}
			else if (e.which == 9)  
			// Tab
			{
				if (list.css("display") == "block") 
				{
					// when the suggestion list is open
					// and tab is pressed we'll select 
					// the value that is highlighted, if
					// none is, then we'll select the 1st.
					if (selected == -1) {
						selected = 0;
					}
					textInput.val( list.children().removeClass('selected').eq(selected).addClass('selected').text() );	        
					selectedItem = jQuery(list.children()[selected]);
					settings.select(textInput,textInput.val(),selectedItem);
					valueInput = textInput.next();
					valueInput.val(selectedItem.attr('name'));
					clear();
					selectNextInput();
					e.preventDefault();
				}

			}
			else if(e.which == 40  || e.which == 38)//move up, down 
			{
			  switch(e.which) 
			  {
				case 40: 
				  selected = selected >= size - 1 ? 0 : selected + 1; break;
				case 38:
				  selected = selected <= 0 ? size - 1 : selected - 1; break;
				default: break;
			  }
			  //set selected item and input values
			  textInput.val( list.children().removeClass('selected').eq(selected).addClass('selected').text() );	        
			  valueInput.val( list.children().eq(selected).attr('value') );
			} else 
			{ 
				//invalidate previous selection
				if (settings.validSelection) valueInput.val('');
				typingTimeout = window.setTimeout(function() { getData(textInput.val()) },settings.timeout);
			}
		});
	});
};
