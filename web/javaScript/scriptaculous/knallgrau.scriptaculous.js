
// Various extensions for prototype.js and scriptaculous
// by knallgrau.at

// note: there are still some fixes left in prototype.js concerning element-border-width

Element.BLOCK_LEVEL = ["address","blockquote","center","dl","dir","div","fieldset",
	             "form","h1-6","hr","isindex","menu","noframes","noscript",
	             "ol","p","pre","table","ul","center","dir","menu","noframes","isindex"];

Element.isBlockLevel = function(element) {
  return element.isBlockLevel = (
    Element.getStyle(element, "display") == "block" || 
    element.isBlockLevel == true || 
    Element.BLOCK_LEVEL.indexOf(element.nodeName.toLowerCase()) != -1
  ); 
}

Element.show = function() {
  for (var i = 0; i < arguments.length; i++) {
    var element = $(arguments[i]);
//knallgrau: use Element.isBlockLevel
    element.style.display = Element.isBlockLevel(element) ? 'block' : '';
  }
}

Element.getDimensions = function(element) {
  element = $(element);
  if (Element.getStyle(element, 'display') != 'none')
    return {width: element.offsetWidth, height: element.offsetHeight};

  // All *Width and *Height properties give 0 on elements with display none,
  // so enable the element temporarily
  var els = element.style;
  var originalVisibility = els.visibility;
  var originalPosition = els.position;
  els.visibility = 'hidden';
  els.position = 'absolute';
//knallgrau: use Element.isBlockLevel
  els.display = Element.isBlockLevel(element) ? 'block' : '';
  var originalWidth = element.clientWidth;
  var originalHeight = element.clientHeight;
  els.display = 'none';
  els.position = originalPosition;
  els.visibility = originalVisibility;
  return {width: originalWidth, height: originalHeight};
}

Position.getVisibleWidth = function() {
  return (window.opera) ? 
    document.body.clientWidth || document.documentElement.clientWidth || window.innerWidth
    : document.documentElement.clientWidth || window.innerWidth || document.body.clientWidth;
}

Position.getVisibleHeight = function() {
  return (window.opera) ? 
     document.body.clientHeight || document.documentElement.clientHeight || window.innerHeight
     : document.documentElement.clientHeight || window.innerHeight || document.body.clientHeight;
}

Ajax.InPlaceEditor.prototype.convertHTMLLineBreaks = function(string) {
   string = string.replace(/<br>\n/gi, "\n");
   string = string.replace(/<br>/gi, "\n").replace(/<br\/>/gi, "\n").replace(/<\/p>/gi, "\n").replace(/<p>/gi, "");
   return string;
}

// Cookie Functions
function ClientCookie() {
	if (document.cookie.length) { this.cookies = ' ' + document.cookie; }
}

ClientCookie.prototype = {
   setValue: function (key, value) {
      document.cookie = key + "=" + escape(value);
   },

   getValue: function (key) {
      if (this.cookies) {
         var start = this.cookies.indexOf(' ' + key + '=');
         if (start == -1) { return null; }
         var end = this.cookies.indexOf(";", start);
         if (end == -1) { end = this.cookies.length; }
         end -= start;
         var cookie = this.cookies.substr(start,end);
         return unescape(cookie.substr(cookie.indexOf('=') + 1, cookie.length - cookie.indexOf('=') + 1));
      }
      else { return null; }
   }
}
var Cookie = new ClientCookie();


/*
 * code taken from http://wiki.script.aculo.us/scriptaculous/show/Ajax.InPlaceEditor
 * InPlaceEditor extension that adds a 'click to edit' text when the field is 
 * empty.
 */
Ajax.InPlaceEditor.prototype.__initialize = Ajax.InPlaceEditor.prototype.initialize;
Ajax.InPlaceEditor.prototype.__getText = Ajax.InPlaceEditor.prototype.getText;
Ajax.InPlaceEditor.prototype.__onComplete = Ajax.InPlaceEditor.prototype.onComplete;
Ajax.InPlaceEditor.prototype = Object.extend(Ajax.InPlaceEditor.prototype, {

    initialize: function(element, url, options){
        this.__initialize(element,url,options)
        this.setOptions(options);
        this._checkEmpty();
    },

    setOptions: function(options){
        this.options = Object.extend(Object.extend(this.options,{
            emptyText: 'click to edit...',
            emptyClassName: 'inplaceeditor-empty'
        }),options||{});
    },

    _checkEmpty: function(){
        if( this.element.innerHTML.length == 0 ){
            this.element.appendChild(
                Builder.node('span',{className:this.options.emptyClassName},this.options.emptyText));
        }
    },

    getText: function(){
        document.getElementsByClassName(this.options.emptyClassName,this.element).each(function(child){
            this.element.removeChild(child);
        }.bind(this));
        return this.__getText();
    },

    onComplete: function(transport){
        this._checkEmpty();
        this.__onComplete(transport);
    }
});
