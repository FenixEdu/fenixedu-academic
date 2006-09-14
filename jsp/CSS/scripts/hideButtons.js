function switchDisplay(id) {
	element = document.getElementById(id);
	
	if(element.className == 'dnone') {
		element.className = 'dblock';
	} else {
		element.className = 'dnone';
	}
}

function showElement(id) {
	element = document.getElementById(id);
	
	element.className = 'dblock';
}

function hideElement(id) {
	element = document.getElementById(id);
	
	element.className = 'dnone';
}

function getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp('(^|\\s)'+searchClass+'(\\s|$)');
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}

function switchGlobal() {
	var blockElements = getElementsByClass('switchBlock');
	var inlineElements = getElementsByClass('switchInline');
	var noneElements = getElementsByClass('switchNone');
	
	for (i = 0; i < blockElements.length; i++) {
		blockElements[i].className = 'dblock';
	}
	
	for (i = 0; i < inlineElements.length; i++) {
		inlineElements[i].className = 'dinline';
	}
	
	for (i = 0; i < noneElements.length; i++) {
		noneElements[i].className = 'dnone';
	}
}




function hideButtons()
{
	if (document.getElementById("javascriptButtonID") != null) document.getElementById("javascriptButtonID").style.display="none";
	if (document.getElementById("javascriptButtonID2") != null) document.getElementById("javascriptButtonID2").style.display="none";
	if (document.getElementById("javascriptButtonID3") != null) document.getElementById("javascriptButtonID3").style.display="none";
	if (document.getElementById("javascriptButtonID4") != null) document.getElementById("javascriptButtonID4").style.display="none";
	if (document.getElementById("javascriptButtonID5") != null) document.getElementById("javascriptButtonID5").style.display="none";
	if (document.getElementById("javascriptButtonID6") != null) document.getElementById("javascriptButtonID6").style.display="none";
	if (document.getElementById("javascriptButtonID7") != null) document.getElementById("javascriptButtonID7").style.display="none";
	if (document.getElementById("id0") != null) document.getElementById("id0").style.display="block";
	if (document.getElementById("id1") != null) document.getElementById("id1").style.display="block";
	if (document.getElementById("id2") != null) document.getElementById("id2").style.display="block";
	if (document.getElementById("id3") != null) document.getElementById("id3").style.display="block";
	if (document.getElementById("id4") != null) document.getElementById("id4").style.display="block";
	if (document.getElementById("id5") != null) document.getElementById("id5").style.display="block";
	if (document.getElementById("id6") != null) document.getElementById("id6").style.display="block";
	if (document.getElementById("id7") != null) document.getElementById("id7").style.display="block";
	if (document.getElementById("id8") != null) document.getElementById("id8").style.display="block";
	if (document.getElementById("id9") != null) document.getElementById("id9").style.display="block";
	if (document.getElementById("id10") != null) document.getElementById("id10").style.display="block";
	if (document.getElementById("id11") != null) document.getElementById("id11").style.display="block";
	if (document.getElementById("id12") != null) document.getElementById("id12").style.display="block";
	if (document.getElementById("id13") != null) document.getElementById("id13").style.display="block";
	if (document.getElementById("id14") != null) document.getElementById("id14").style.display="block";
	if (document.getElementById("id15") != null) document.getElementById("id15").style.display="block";
	if (document.getElementById("id16") != null) document.getElementById("id16").style.display="block";
	if (document.getElementById("id17") != null) document.getElementById("id17").style.display="block";
	if (document.getElementById("id18") != null) document.getElementById("id18").style.display="block";
	if (document.getElementById("id19") != null) document.getElementById("id19").style.display="block";
	if (document.getElementById("id20") != null) document.getElementById("id20").style.display="block";
}
