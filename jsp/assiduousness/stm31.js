// Ver: 3.10
var st_blank=new Image();st_blank.src=st_path+"blank.gif";
var nOP=false,nOP6=false,nIE=false,nIE4=false,nIE5=false,nNN=false,nNN4=false,nNN6=false,nIM=false,nIM4=false,nSTMENU=false;var NS4=0;
detectNav();

var st_rl_id=null;
var st_cl_w,st_cl_h;
var st_cumei,st_cumbi,st_cuiti;
var st_menus=new Array();
var st_fixid=null;
var st_resHandle="document.location.reload();";
if(nNN4)	window.onresize=new Function(st_resHandle);
var st_endarg="var argtn=argt.length-1;for(i=0;i<argtn;i++) eval(\"var \"+argt[i][0]+\"=(i<argu.length)?argu[i]:argt[i][1];\");";
function getcl(){return parseInt(nNN||nOP ? window.pageXOffset : document.body.scrollLeft);}
function getct(){return parseInt(nNN||nOP ? window.pageYOffset : document.body.scrollTop);}
function getcw(){return parseInt(nNN||nOP ? window.innerWidth : document.body.clientWidth);}
function getch(){return parseInt(nNN||nOP ? window.innerHeight : document.body.clientHeight);}
function sh(mb)
{
	var ly=mb.getlayer();
	if(nNN4)
		ly.visibility='show';
	else
		ly.style.visibility='visible';
}

function hd(mb)
{
	var ly=mb.getlayer();
	if(nNN4)
		ly.visibility='hide';
	else
	{
		if(nIE5)	ly.filters["Alpha"].opacity=0;
		ly.style.visibility='hidden';
		if(nIE5)	ly.filters["Alpha"].opacity=mb.opacity;
	}
}


var st_state=["OU","OV","CK"];

var fl_ar=new Array();
fl_ar["Box in"]=0;
fl_ar["Box out"]=1;
fl_ar["Circle in"]=2;
fl_ar["Circle out"]=3;
fl_ar["Wipe up"]=4;
fl_ar["Wipe down"]=5;
fl_ar["Wipe right"]=6;
fl_ar["Wipe left"]=7;
fl_ar["Vertical blinds"]=8;
fl_ar["Horizontal blinds"]=9;
fl_ar["Checkerboard across"]=10;
fl_ar["Checkerboard down"]=11;
fl_ar["Random dissolve"]=12;
fl_ar["Split vertical in"]=13;
fl_ar["Split vertical out"]=14;
fl_ar["Split horizontal in"]=15;
fl_ar["Split horizontal out"]=16;
fl_ar["Strips left down"]=17;
fl_ar["Strips left up"]=18;
fl_ar["Strips right down"]=19;
fl_ar["Strips right up"]=20;
fl_ar["Random bars horizontal"]=21;
fl_ar["Random bars vertical"]=22;
fl_ar["Random filter"]=23;

function beginSTM()
{
	var argu=arguments;
	var argt=[["nam",""],["type","relative"],["pos_l","0"],["pos_t","0"],["flt","none"],["click_sh","false"],["click_hd","true"],["ver","300"],["hddelay","1000"],["",""]];
	var argtn=argt.length-1;
	eval(st_endarg);
	if(nam=="")
		alert("Error! You didn't input the name for the menu!!!");
	nam=nam.replace(/_/g,"`~`");
	st_cumei=nam;st_cumbi=0;st_cuiti=0;

	st_menus[st_cumei]=
	{
		bodys:		new Array(),
		mei:		st_cumei,
		hdid:		null,

		block:		"STM"+st_cumei+"_",
		z_index:	1000,
		nam:		nam.toLowerCase(),
		type:		type.toLowerCase(),
		pos:		type.toLowerCase(),
		pos_l:		parseInt(pos_l),
		pos_t:		parseInt(pos_t),
		flt:		flt.toLowerCase(),
		click_sh:	eval(click_sh.toLowerCase()),
		click_hd:	eval(click_hd.toLowerCase()),
		ver:		parseInt(ver),
		hddelay:		parseInt(hddelay),

		hideall:	hideall
	};
	var tmobj=st_menus[st_cumei];
	switch(tmobj.type)
	{
	case "absolute":
		tmobj.type="custom";
		break;
	case "custom":
	case "float":
		tmobj.pos="absolute";
		break;
	case "relative":
		if(!tmobj.pos_l&&!tmobj.pos_t)
		{
			tmobj.pos="static";
			tmobj.type="static";
		}
		break;
	case "static":
	default:
		tmobj.type="static";
		tmobj.pos="static";
		tmobj.pos_l=0;
		tmobj.pos_t=0;
		break;
	}
}

function beginSTMB()
{
	var argu=arguments;
	var argt=[
	["offset","auto"],["offset_l","0"],["offset_t","0"],["arrange","vertically"],
	["arrow",""],["arrow_w","-1"],["arrow_h","-1"],
	["spacing","3"],["padding","0"],
	["bg_cl","#ffffff"],["bg_image",""],["bg_rep","repeat"],
	["bd_cl","#000000"],["bd_sz","0"],["bd_st","none"],
	["trans","0"],["spec","Normal"],["spec_sp","50"],
	["lw_max","16"],["lh_max","16"],["rw_max","16"],["rh_max","16"],
	["bg_pos_x","0%"],["bg_pos_y","0%"],
	["ds_sz","0"],["ds_color","gray"],
	["hdsp","false"],["bd_cl_t",""],["bd_cl_r",""],["bd_cl_b",""],
	["ds_st","none"],
	["",""]];
	eval(st_endarg);
	switch(bg_rep)
	{
	case 'tile':
	case 'tiled':
		bg_rep='repeat';
		break;
	case 'free':
		bg_rep='no-repeat';
		break;
	case 'tiled by x':
		bg_rep='repeat-x';
		break;
	case 'tiled by y':
		bg_rep='repeat-y';
		break;
	default:
		break;
	}

	var oldmbi=st_cumbi;var olditi=st_cuiti;st_cumbi=st_menus[st_cumei].bodys.length;st_cuiti=0;
	var menu=st_menus[st_cumei];

	menu.bodys[st_cumbi]=
	{
		items:		new Array(),
		isit:		false,

		mei:		st_cumei,
		mbi:		st_cumbi,
		block:		"STM"+st_cumei+"_"+st_cumbi+"__",
		parit:		(st_cumbi==0 ? null : st_menus[st_cumei].bodys[oldmbi].items[olditi]),
		getlayer:	getlayerMB,
		getme:		getme,
		tmid:		null,
		curiti:		-1,
		isshow:		(st_cumbi==0&&menu.type!="custom"),
		exec_ed:	false,

		arrange:	arrange.toLowerCase(),
		offset:		offset.toLowerCase(),
		offset_l:	parseInt(offset_l),
		offset_t:	parseInt(offset_t),
		arrow:		arrow,
		arrow_w:	parseInt(arrow_w),
		arrow_h:	parseInt(arrow_h),
		spacing:	parseInt(spacing),
		padding:	parseInt(padding),
		bg_cl:		bg_cl.toLowerCase(),
		bg_image:	bg_image,
		bg_rep:		bg_rep.toLowerCase(),
		bg_pos_x:	bg_pos_x.toLowerCase(),
		bg_pos_y:	bg_pos_y.toLowerCase(),
		bd_st:		bd_st.toLowerCase(),
		bd_sz:		parseInt(bd_sz),
		bd_cl:		bd_cl.toLowerCase(),
		opacity:	100-parseInt(trans),
		spec:		spec,
		spec_sp:	parseInt(spec_sp),
		fl_type:	-1,
		lw_max:		parseInt(lw_max),
		lh_max:		parseInt(lh_max),
		rw_max:		parseInt(rw_max),
		rh_max:		parseInt(rh_max),
		ds_sz:		parseInt(ds_sz),
		ds_st:		ds_st.toLowerCase(),
		ds_color:	ds_color,
		hdsp:	eval(hdsp.toLowerCase()),

		getrect:	getrect,
		getxy:		getxy,
		moveto:		moveto,
		adjust:		adjust,
		gettx_h:	getMBTextH,
		gettx_e:	getMBTextE,
		show:		show,
		hide:		hide,
		showpop:	showpop,
		hidepop:	hidepop,
		getCSS:		getMBCSS
	};
	var tmobj=menu.bodys[st_cumbi];
	if(st_cumbi)
		tmobj.parit.submenu=tmobj;
	tmobj.z_index= st_cumbi==0 ? 1010 : tmobj.parit.parme.z_index+10;
	if(tmobj.offset=="auto")
	{
		if(st_cumbi)
			tmobj.offset=tmobj.parit.parme.arrange=="vertically" ? "right" : "down";
		else
			tmobj.offset= "down";
	}
	if(tmobj.bd_st=="none")
		tmobj.bd_sz=0;
	if(nSTMENU&&!nNN4&&bd_cl_t!="")
		tmobj.bd_cl=(bd_cl_t+" "+bd_cl_r+" "+bd_cl_b+" "+bd_cl);
	bufimg("bg_image_src",tmobj.bg_image,tmobj);
	bufimg("arrow_src",tmobj.arrow,tmobj);

	switch(spec)
	{
	case "Fade":
		tmobj.spec_init=fade_init;
		tmobj.spec_sh=fade_sh;
		tmobj.spec_hd=fade_hd;
		break;
	case "Box in":
	case "Box out":
	case "Circle in":
	case "Circle out":
	case "Wipe up":
	case "Wipe down":
	case "Wipe right":
	case "Wipe left":
	case "Vertical blinds":
	case "Horizontal blinds":
	case "Checkerboard across":
	case "Checkerboard down":
	case "Random dissolve":
	case "Split vertical in":
	case "Split vertical out":
	case "Split horizontal in":
	case "Split horizontal out":
	case "Strips left down":
	case "Strips left up":
	case "Strips right down":
	case "Strips right up":
	case "Random bars horizontal":
	case "Random bars vertical":
	case "Random filter":
		tmobj.spec_init=filter_init;
		tmobj.spec_sh=filter_sh;
		tmobj.spec_hd=filter_hd;
		break;
	case "Normal":
	default:
		tmobj.spec_init=normal_init;
		tmobj.spec_sh=normal_sh;
		tmobj.spec_hd=normal_hd;
		break;
	}
	if(!nIE||nIM)
	{
		tmobj.spec_init=normal_init;
		tmobj.spec_sh=normal_sh;
		tmobj.spec_hd=normal_hd;
	}
	tmobj.spec_init();
}

function appendSTMI()
{
	var argu=arguments;
	var argt=[
	["isimage","false"],["text",""],["align","left"],["valign","middle"],
	["image_ou",""],["image_ov",""],["image_w","-1"],["image_h","-1"],["image_b","0"],
	["type","normal"],["bgc_ou","#ffffff"],["bgc_ov","#ffffff"],
	["sep_img",""],["sep_size","1"],["sep_w","-1"],["sep_h","-1"],
	["icon_ou",""],["icon_ov",""],["icon_w","-1"],["icon_h","-1"],["icon_b","0"],
	["tip",""],["url",""],["target","_self"],
	["f_fm_ou","Arial"],["f_sz_ou","9"],["f_cl_ou","#000000"],["f_wg_ou","normal"],["f_st_ou","normal"],["f_de_ou","none"],
	["f_fm_ov","Arial"],["f_sz_ov","9"],["f_cl_ov","#000000"],["f_wg_ov","normal"],["f_st_ov","normal"],["f_de_ov","underline"],
	["bd_sz","0"],["bd_st","none"],["bd_cl_r_ou","#000000"],["bd_cl_l_ou","#000000"],["bd_cl_r_ov","#000000"],["bd_cl_l_ov","#000000"],
	["bd_cl_t_ou",""],["bd_cl_b_ou",""],["bd_cl_t_ov",""],["bd_cl_b_ov",""],
	["st_text",""],["bg_img_ou",""],["bg_img_ov",""],["bg_rep_ou","repeat"],["bg_rep_ov","repeat"],
	["",""]];
	eval(st_endarg);
	switch(bg_rep_ou)
	{
	case 'tile':
	case 'tiled':
		bg_rep_ou='repeat';
		break;
	case 'free':
		bg_rep_ou='no-repeat';
		break;
	case 'tiled by x':
		bg_rep_ou='repeat-x';
		break;
	case 'tiled by y':
		bg_rep_ou='repeat-y';
		break;
	default:
		break;
	}
	switch(bg_rep_ov)
	{
	case 'tile':
	case 'tiled':
		bg_rep_ov='repeat';
		break;
	case 'free':
		bg_rep_ov='no-repeat';
		break;
	case 'tiled by x':
		bg_rep_ov='repeat-x';
		break;
	case 'tiled by y':
		bg_rep_ov='repeat-y';
		break;
	default:
		break;
	}

	st_cuiti=st_menus[st_cumei].bodys[st_cumbi].items.length;
	var menu=st_menus[st_cumei];
	var body=menu.bodys[st_cumbi];

	body.items[st_cuiti]=
	{
		isit:		true,
		mei:		st_cumei,
		mbi:		st_cumbi,
		iti:		st_cuiti,
		block:		"STM"+st_cumei+"_"+st_cumbi+"__"+st_cuiti+"___",
		parme:		body,
		submenu:	null,
		getlayer:	getlayerIT,
		get_st_lay:	get_st_lay,
		getme:		getme,
		txblock:	"STM"+st_cumei+"_"+st_cumbi+"__"+st_cuiti+"___"+"TX",
		tmid:	null,

		isimage:	eval(isimage.toLowerCase()),
		text:		text,
		align:		align.toLowerCase(),
		valign:		valign.toLowerCase(),
		image:		[image_ou,image_ov,image_ov],

		image_w:	parseInt(image_w),
		image_h:	parseInt(image_h),
		image_b:	parseInt(image_b),
		type:		type.toLowerCase(),
		bg_cl:		[bgc_ou.toLowerCase(),bgc_ov.toLowerCase(),bgc_ov.toLowerCase()],
		sep_img:	sep_img,
		sep_size:	parseInt(sep_size),
		sep_w:		parseInt(sep_w),
		sep_h:		parseInt(sep_h),
		icon:		[icon_ou,icon_ov,icon_ov],
		icon_w:		parseInt(icon_w),
		icon_h:		parseInt(icon_h),
		icon_b:		parseInt(icon_b),
		tip:		tip,
		url:		url,
		target:		target,
		f_fm:		[f_fm_ou.replace(/'/g,''),f_fm_ov.replace(/'/g,''),f_fm_ov.replace(/'/g,'')],
		f_sz:		[parseInt(f_sz_ou),parseInt(f_sz_ov),parseInt(f_sz_ov)],
		f_cl:		[f_cl_ou.toLowerCase(),f_cl_ov.toLowerCase(),f_cl_ov.toLowerCase()],
		f_wg:		[f_wg_ou.toLowerCase(),f_wg_ov.toLowerCase(),f_wg_ov.toLowerCase()],
		f_st:		[f_st_ou.toLowerCase(),f_st_ov.toLowerCase(),f_st_ov.toLowerCase()],
		f_de:		[f_de_ou.toLowerCase(),f_de_ov.toLowerCase(),f_de_ov.toLowerCase()],
	
		bd_st:		bd_st.toLowerCase(),
		bd_sz:		parseInt(bd_sz),
		bd_cl_r:	[bd_cl_r_ou.toLowerCase(),bd_cl_r_ov.toLowerCase(),bd_cl_r_ov.toLowerCase()],
		bd_cl_l:	[bd_cl_l_ou.toLowerCase(),bd_cl_l_ov.toLowerCase(),bd_cl_l_ov.toLowerCase()],
		bd_cl_t:	[bd_cl_t_ou.toLowerCase(),bd_cl_t_ov.toLowerCase(),bd_cl_t_ov.toLowerCase()],
		bd_cl_b:	[bd_cl_b_ou.toLowerCase(),bd_cl_b_ov.toLowerCase(),bd_cl_b_ov.toLowerCase()],

		st_text:	st_text,
		bg_img:		[bg_img_ou,bg_img_ov,bg_img_ov],
		bg_rep:		[bg_rep_ou.toLowerCase(),bg_rep_ov.toLowerCase(),bg_rep_ov.toLowerCase()],

		getrect:	getrect,
		gettx:		getMIText,
		showpop:	shitpop,
		hidepop:	hditpop,
		getCSS:		getMICSS
	};

	var tmobj=st_menus[st_cumei].bodys[st_cumbi].items[st_cuiti];
	if(tmobj.bd_st=="none"||!tmobj.bd_sz)
	{
		tmobj.bd_sz=0;	tmobj.bd_st="none";
	}
	if(nOP)
	{
		if(tmobj.bd_st=="ridge")	tmobj.bd_st="outset";
		if(tmobj.bd_st=="groove")	tmobj.bd_st="inset";
	}
	if(tmobj.bd_st=="inset")
	{
		var tmclr=tmobj.bd_cl_l;	tmobj.bd_cl_l=tmobj.bd_cl_r;	tmobj.bd_cl_r=tmclr;	tmobj.bd_st="outset";
	}
	if(bd_cl_t_ou=="")
	{
		if("none_solid_double_dashed_dotted".indexOf(tmobj.bd_st)>=0)
			tmobj.bd_cl_r=tmobj.bd_cl_l;
		if(tmobj.bd_st=="outset")
			tmobj.bd_st="solid";
		tmobj.bd_cl_t=tmobj.bd_cl_l;
		tmobj.bd_cl_b=tmobj.bd_cl_r;
	}
	tmobj.bd_cl=new Array();
	for(i=0;i<3;i++)
		tmobj.bd_cl[i]=tmobj.bd_cl_t[i]+" "+tmobj.bd_cl_r[i]+" "+tmobj.bd_cl_b[i]+" "+tmobj.bd_cl_l[i];
	if(tmobj.type=="sepline")
		bufimg("sep_img_src",tmobj.sep_img,tmobj);
	else
	{
		for(i=0;i<3;i++)
		{
			bufimg("icon_"+i,tmobj.icon[i],tmobj);
			if(tmobj.isimage)
				bufimg("image_"+i,tmobj.image[i],tmobj);
			if(tmobj.bg_img[i]!="")
				bufimg("bgimg_"+i,tmobj.bg_img[i],tmobj);
		}
	}
}

function endSTMB()
{
	var tmobj=st_menus[st_cumei].bodys[st_cumbi].parit;
	if(tmobj)
	{
		st_cumei=tmobj.mei;
		st_cumbi=tmobj.mbi;
		st_cuiti=tmobj.iti;
	}
}

function endSTM()
{
	if(nSTMENU)
	{
		if(!nNN4)
			createCSS();
		var menu=st_menus[st_cumei];
		var menuHTML="";
		for(mbi=0;mbi<menu.bodys.length;mbi++)
		{
			var body=menu.bodys[mbi];
			var bodyHTML=body.gettx_h();
			bodyHTML+=(body.arrange=="vertically" ? "" : (nNN4 ? "<TR HEIGHT=100%>" : "<TR ID="+body.block+"TR>"));
			for(iti=0;iti<body.items.length;iti++)
			{
				var item=body.items[iti];
				var itemHTML="";
				itemHTML+=(body.arrange=="vertically" ? (nNN4 ? "<TR HEIGHT=100%>" : "<TR ID="+item.block+"TR>") : "");
				itemHTML+=item.gettx();
				itemHTML+=(body.arrange=="vertically" ? "</TR>" : "");
				bodyHTML+=itemHTML;
			}
			bodyHTML+=(body.arrange=="vertically" ? "" : "</TR>");
			bodyHTML+=body.gettx_e();
			menuHTML+=bodyHTML;
		}
		document.open();
		document.write(menuHTML);
		document.close();
	}
	else
	{
		var menu=st_menus[st_cumei];
		var me_ht="";
		var mbi=0;
		if(menu.bodys.length>0)
		{
			var body=menu.bodys[mbi];
			me_ht+="<TABLE BORDER=0 CELLSPACING=0 CELLPADDING="+body.bd_sz;
			if(body.bd_sz) me_ht+=" BGCOLOR="+body.bd_cl;
			me_ht+="><TR><TD>";
			me_ht+="<TABLE BORDER=0 CELLSPACING=0 CELLPADDING="+body.spacing;
			if(body.bg_cl!="transparent") me_ht+=" BGCOLOR="+body.bg_cl;
			me_ht+=">";
			if(body.arrange!="vertically") me_ht+="<TR>";
			
			for(iti=0;iti<body.items.length;iti++)
			{
				var item=body.items[iti];
				var s="";
				if(body.arrange=="vertically") s+="<TR>";
				s+="<TD NOWRAP WIDTH=1 HEIGHT=100%>";
				s+="<TABLE BORDER=0 WIDTH=100% HEIGHT=100% CELLSPACING=0 CELLPADDING="+(item.type=="sepline" ? 0 : item.bd_sz);
				if(item.bd_sz) s+=" BGCOLOR="+item.bd_cl_l[0];
				s+="><TR><TD WIDTH=100%>";
				s+="<TABLE BORDER=0 WIDTH=100% HEIGHT=100% CELLSPACING=0 CELLPADDING="+(item.type=="sepline" ? 0 : body.padding);
				if(body.arrange=="vertically") s+=" WIDTH=100%";
				if(item.type!="sepline") s+=addquo(item.tip);
				if(item.bg_cl[0]!="transparent") s+=" BGCOLOR="+item.bg_cl[0];
				s+="><TR>";

				if(item.type=="sepline")
				{
					s+="<TD NOWRAP VALIGN=TOP"+
						" HEIGHT="+(body.arrange=="vertically" ? item.sep_size : "100%")+
						" WIDTH="+(body.arrange=="vertically" ? "100%" : item.sep_size)+
						" STYLE=\"font-size:0pt;\""+
						">";
					s+=createIMG(item.sep_img,"",item.sep_w,item.sep_h,0);
					s+="</TD>";
				}
				else
				{
					if(body.lw_max&&(body.arrange=="vertically"||item.icon_w))
					{
						s+="<TD ALIGN=CENTER VALIGN=MIDDLE";
						s+=getwdstr(this);
						s+=">";
						s+=createIMG(item.icon[0],"",item.icon_w,item.icon_h,item.icon_b);
						s+="</TD>";
					}
					s+="<TD WIDTH=100% NOWRAP ALIGN="+item.align+" VALIGN="+item.valign+">";
					s+="<A STYLE=\"";
					s+="font-family:"+item.f_fm[0]+";";
					s+="font-size:"+item.f_sz[0]+"pt;";
					s+="color:"+item.f_cl[0]+";";
					s+="font-weight:"+item.f_wg[0]+";";
					s+="font-style:"+item.f_st[0]+";";
					s+="text-decoration:"+item.f_de[0]+";\"";
					s+=" HREF="+addquo(item.url=="" ? "javascript:;" : item.url); 
					s+=" TARGET="+item.target;
					s+=">";
					if(item.isimage)
						s+=createIMG(item.image[0],"",item.image_w,item.image_h,item.image_b);
					else
					{
						s+="<IMG SRC=\""+st_blank.src+"\" WIDTH=1 HEIGHT=1 BORDER=0 ALIGN=ABSMIDDLE>";
						s+=item.text;
					}
					s+="</A>";
					s+="</TD>";
					if(body.arrow_w)
					{
						s+="<TD NOWRAP ALIGN=CENTER VALIGN=MIDDLE>";
						s+=createIMG((item.submenu ? body.arrow : st_blank.src),"",body.arrow_w,body.arrow_h,0);
						s+="</TD>";
					}
				}
				s+="</TR></TABLE></TD></TR></TABLE>";
				s+=body.arrange=="vertically" ? "</TD></TR>" : "</TR>";
				me_ht+=s;
			}
			if(body.arrange!="vertically") me_ht+="</TR>";
			me_ht+="</TABLE></TD></TR></TABLE>";
		}
		document.open();
		document.write(me_ht);
		document.close();
	}
}

function getMBTextH()
{
	if(nNN4)
	{
		var s=(this.mbi||st_menus[this.mei].pos=="absolute" ? "<LAYER" : "<ILAYER");
		if(this.mbi==0&&st_menus[this.mei].pos=="absolute")
			s+=(" LEFT="+st_menus[this.mei].pos_l+" TOP="+st_menus[this.mei].pos_t);
		s+=" VISIBILITY="+(this.mbi==0&&st_menus[this.mei].type!="custom" ? "show" : "hide");
		s+=" ID="+this.block;
		s+=" Z-INDEX="+this.z_index;
		s+=">";
		s+="<LAYER ID="+this.block+"IN";
		s+=">";
		s+="<TABLE BORDER=0 CELLPADDING="+this.bd_sz+" CELLSPACING=0";
		if(this.bd_sz)
			s+=" BGCOLOR="+this.bd_cl;
		s+="><TR><TD>";
		s+="<TABLE BORDER=0 CELLPADDING="+this.spacing+" CELLSPACING=0";
		if(this.bg_image!="")
			s+=" BACKGROUND=\""+this.bg_image+"\"";
		if(this.bg_cl!="transparent")
			s+=" BGCOLOR="+this.bg_cl;
		s+=" ID="+this.block;
		s+=">";
		return s;
	}
	else
	{
		var stdiv="position:"+(this.mbi ? "absolute" : st_menus[this.mei].pos)+";";
		if(this.mbi==0)
		{
			stdiv+=("float:"+st_menus[this.mei].flt+";");
			stdiv+=("left:"+st_menus[this.mei].pos_l+"px;");
			stdiv+=("top:"+st_menus[this.mei].pos_t+"px;");
		}	
		stdiv+="z-index:"+this.z_index+";";
		stdiv+="visibility:hidden;";
//		stdiv+="visibility:"+(this.mbi==0&&st_menus[this.mei].type!="custom" ? "visible" : "hidden")+";";
		
		var s="";
		if(!nOP)
		{
			s+="<DIV ID="+this.block;
			s+=" STYLE='";
			if(nIE)
				s+="width:0px;";
			s+=stdiv;
			s+="'>";
		}
		s+="<TABLE CELLSPACING=0";
		s+=" ID="+(nOP ? this.block : (this.block+"TB"));
		s+=" CELLPADDING="+this.spacing;
		s+=" STYLE='";
		if(this.ds_st=="simple")
			s+="margin:"+this.ds_sz+"px;";
		if(nOP)
			s+=stdiv;
		s+="'>";

		return s;
	}
}

function getMBTextE()
{
	if(nNN4)
		return (this.mbi==0&&this.pos!="absolute") ? "</TABLE></TD></TR></TABLE></LAYER></ILAYER>" : "</TABLE></TD></TR></TABLE></LAYER></LAYER>";
	else if(nOP)
		return "</TABLE>";
	else
		return "</TABLE></DIV>";
}

function getMIText()
{
	if(nNN4)
	{
		var s="<TD WIDTH=1>";
		s+="<FONT STYLE='font-size:1pt;'><ILAYER ID="+this.block;
		s+="><LAYER ID="+this.block+"IN>";
		
		for(i=0;i<3;i++)
		{
			if(this.type=="sepline"&&i)
				break;

			s+="<LAYER ID="+this.block+st_state[i];
			s+=" Z-INDEX=10";
			s+=" VISIBILITY="+(i ? "HIDE" : "SHOW")+">";
			s+="<TABLE BORDER=0 CELLPADDING="+(this.type=="sepline" ? 0 : this.bd_sz)+" CELLSPACING=0 WIDTH=100%";
			if(this.bd_sz)
				s+=" BGCOLOR="+this.bd_cl_l[i];
			s+="><TR><TD WIDTH=100%>";
			s+="<TABLE BORDER=0 CELLPADDING="+(this.type=="sepline" ? 0 : this.parme.padding)+" CELLSPACING=0";
			if(this.bg_cl[i]!="transparent")
				s+=" BGCOLOR="+this.bg_cl[i];
			s+=" TITLE="+addquo(this.type!="sepline" ? this.tip : "");
			s+=" WIDTH=100%><TR>";

			if(this.type=="sepline")
			{
				s+="<TD NOWRAP VALIGN=TOP"+
					" HEIGHT="+(this.parme.arrange=="vertically" ? this.sep_size : "100%")+
					" WIDTH="+(this.parme.arrange=="vertically" ? "100%" : this.sep_size)+
					" STYLE=\"font-size:0pt;\""+
					">";
				s+=createIMG(this.sep_img,this.block+"LINE",this.sep_w,this.sep_h,0);
				s+="</TD>";
			}
			else
			{
				if(this.parme.lw_max&&(this.parme.arrange=="vertically"||this.icon_w))
				{
					s+="<TD ALIGN=CENTER VALIGN=MIDDLE";
					s+=getwdstr(this);
					s+=">";
					s+=createIMG(this.icon[i],this.block+"ICON",this.icon_w,this.icon_h,this.icon_b);
					s+="</TD>";
				}

				s+="<TD WIDTH=100% NOWRAP ALIGN="+this.align+" VALIGN="+this.valign+">";
				s+="<FONT FACE='"+this.f_fm[i]+"' STYLE=\"";
				s+="font-size:"+this.f_sz[i]+"pt;";
				s+="color:"+this.f_cl[i]+";";
				s+="font-weight:"+this.f_wg[i]+";";
				s+="font-style:"+this.f_st[i]+";";
				s+="text-decoration:"+this.f_de[i]+";";
				s+="\">";
				if(this.isimage)
					s+=createIMG(this.image[i],this.block+"IMG",this.image_w,this.image_h,this.image_b);
				else
				{
					s+="<IMG SRC=\""+st_blank.src+"\" WIDTH=1 HEIGHT=1 BORDER=0 ALIGN=ABSMIDDLE>";
					s+=this.text;
				}
				s+="</FONT>";
				s+="</TD>";

				if(this.parme.arrow_w)
				{
					s+="<TD NOWRAP ALIGN=CENTER VALIGN=MIDDLE>";
					s+=createIMG((this.submenu ? this.parme.arrow : st_blank.src),this.block+"ARROW",this.parme.arrow_w,this.parme.arrow_h,0);
					s+="</TD>";
				}
			}

			s+="</TR></TABLE>";
			s+="</TD></TR></TABLE>";
			s+="</LAYER>";
			
			if(this.type!="sepline")
			{
				s+="<LAYER ID="+this.block+st_state[i]+"M";
				s+=" Z-INDEX=20";
				s+=">";
				s+="</LAYER>";
			}
		}

		s+="</LAYER></ILAYER></FONT>";
		s+="</TD>";
		return s;
	}
	else
	{
		var s="";

		s+="<TD STYLE='border-style:none;border-width:0px;' NOWRAP VALIGN=TOP";
		s+=" ID="+this.parme.block+this.iti;
		s+=">";
		if(!nOP)
		{
			s+="<DIV ID="+this.block;
			s+=">";
		}
		s+="<TABLE CELLSPACING=0";

		if(!nOP)
		{
			s+=" HEIGHT=100%";
			s+=" STYLE='border-style:solid;border-width:0px;'";
		}
		if(this.parme.arrange=="vertically")
			s+=" WIDTH=100%";
		s+=" ID="+(nOP ? this.block : (this.block+"TB"));
		s+=" TITLE="+addquo(this.type!="sepline" ? this.tip : "");
		s+=" CELLPADDING="+(this.type!="sepline" ? this.parme.padding : "0");
		s+="><TR ID="+this.block+"TR>";

		if(this.type=="sepline")
		{
			s+="<TD STYLE='border-style:none;border-width:0px;' NOWRAP VALIGN=TOP"+
				" ID="+this.block+"MTD"+
				" HEIGHT="+(this.parme.arrange=="vertically" ? this.sep_size : "100%")+
				" WIDTH="+(this.parme.arrange=="vertically" ? "100%" : this.sep_size)+
				">";
			s+=createIMG(this.sep_img,this.block+"LINE",this.sep_w,this.sep_h,0);
			s+="</TD>";
		}
		else
		{
			if(this.parme.lw_max&&(this.parme.arrange=="vertically"||this.icon_w))
			{
				s+="<TD STYLE='border-style:none;border-width:0px;' NOWRAP ALIGN=CENTER VALIGN=MIDDLE HEIGHT=100%";
				s+=" ID="+this.block+"LTD";
				s+=getwdstr(this);
				s+=">";
				s+=createIMG(this.icon[0],this.block+"ICON",this.icon_w,this.icon_h,this.icon_b);
				s+="</TD>";
			}
			else if(this.parme.arrange=="vertically")
				s+="<TD ID="+this.block+"LLTD WIDTH=3><IMG SRC=\""+st_blank.src+"\" WIDTH=1 ID="+this.block+"LLTDI></TD>";

			s+="<TD STYLE='border-style:none;border-width:0px;' NOWRAP HEIGHT=100%";
			s+=" ID="+this.block+"MTD";
			s+=" ALIGN="+this.align;
			s+=" VALIGN="+this.valign+">";
			s+="<FONT ID="+this.txblock+">";
			if(this.isimage)
				s+=createIMG(this.image[0],this.block+"IMG",this.image_w,this.image_h,this.image_b);
			else
				s+=this.text;
			s+="</FONT>";
			s+="</TD>";

			if(this.parme.arrow_w)
			{
				s+="<TD STYLE='border-style:none;border-width:0px;' NOWRAP";
				s+=" ID="+this.block+"RTD";
				s+=" WIDTH="+(this.parme.arrow_w+2);
				s+=" ALIGN=CENTER VALIGN=MIDDLE HEIGHT=100%>";
				s+=createIMG((this.submenu ? this.parme.arrow : st_blank.src),this.block+"ARROW",this.parme.arrow_w,this.parme.arrow_h,0);
				s+="</TD>";
			}
			else if(this.parme.arrange=="vertically")
				s+="<TD ID="+this.block+"RRTD WIDTH=3><IMG SRC=\""+st_blank.src+"\" WIDTH=1 ID="+this.block+"RRTDI></TD>";
		}
		
		s+="</TR></TABLE>";
		if(!nOP)
			s+="</DIV>";
		s+="</TD>";
		return s;
	}
}

function createCSS()
{
	var menuCSS="<STYLE TYPE='text/css'>\n";
	var menu=st_menus[st_cumei];
	for(mbi=0;mbi<menu.bodys.length;mbi++)
	{
		var body=menu.bodys[mbi];
		menuCSS+=body.getCSS();
		for(iti=0;iti<body.items.length;iti++)
		{
			var item=body.items[iti];
			menuCSS+=item.getCSS();
		}
	}
	menuCSS+="</STYLE>";
	document.open();
	document.write(menuCSS);
	document.close();
}

function getMBCSS()
{
	var s="";
	
	if(nIE&&!nIM)
	{
		s+="#"+this.block+"\n{\n";
		s+="filter:Alpha(opacity="+this.opacity+")";
		if(this.ds_st=="simple")
			s+=" dropshadow(color="+this.ds_color+",offx="+this.ds_sz+",offy="+this.ds_sz+",positive=1)";
		if(this.fl_type>=0)
			s+=" revealTrans(Transition="+this.fl_type+",Duration="+this.duration+")";
		s+=";\n";
		s+="}\n";
	}
	
	s+="#"+(nOP ? this.block : (this.block+"TB"))+"\n{\n";
	s+="\tborder-style:"+this.bd_st+";\n";
	s+="\tborder-width:"+this.bd_sz+"px;\n";
	s+="\tborder-color:"+this.bd_cl+";\n";
	s+="\tbackground-color:"+(this.bg_cl)+";\n";
	if(this.bg_image!="")
	{
		s+="\tbackground-image:url("+this.bg_image+");\n";
		s+="\tbackground-repeat:"+this.bg_rep+";\n";
//		s+="\tbackground-position:"+this.bg_pos_x+" "+this.bg_pos_y+";\n";
	}
	s+="}\n";
	return s;
}

function getMICSS()
{
	var s="#"+this.block+"\n{\n";
	if(this.type!="sepline")
	{
		s+="\tborder-style:"+this.bd_st+";\n";
		s+="\tborder-width:"+this.bd_sz+"px;\n";
		s+="\tborder-color:"+this.bd_cl[0]+";\n";
		if(this.bg_img[0]!="")
		{
			s+="\tbackground-image:url("+this.bg_img[0]+");\r\n";
			s+="\tbackground-repeat:"+this.bg_rep[0]+";\r\n";
		}
	}
	s+="\tbackground-color:"+this.bg_cl[0]+";\n";
	s+="\tcursor:"+getcursor(this)+";\n";
	s+="}\n";
	s+="#"+this.txblock+"\n{\n";
	s+="\tcursor:"+getcursor(this)+";\n";
	s+="\tfont-family:"+this.f_fm[0]+";\n";
	s+="\tfont-size:"+this.f_sz[0]+"pt;\n";
	s+="\tcolor:"+this.f_cl[0]+";\n";
	s+="\tfont-weight:"+this.f_wg[0]+";\n";
	s+="\tfont-style:"+this.f_st[0]+";\n";
	s+="\ttext-decoration:"+this.f_de[0]+";\n";
	s+="}\n";
	return s;
}

function doitov()
{
	var e=nIE ? event : arguments[0];
	var obj=this;
	var id=this.id;
	var mei=(id.substring(3,id.indexOf('_')));
	var mbi=eval(id.substring(id.indexOf('_')+1,id.indexOf('__')));
	var iti=eval(id.substring(id.indexOf('__')+2,id.indexOf('___')));
	var it=st_menus[mei].bodys[mbi].items[iti];

	if(!it.parme.isshow||(!nNN&&(e.fromElement&&e.fromElement.id&&e.fromElement.id.indexOf(it.block)>=0)))
		return ;
	if(nNN4)
		it.getlayer().document.layers[0].captureEvents(Event.MOUSEDOWN|Event.CLICK);

	if(st_menus[it.mei].hdid)
	{
		clearTimeout(st_menus[it.mei].hdid);
		st_menus[it.mei].hdid=null;
	}

	
	var curiti=it.parme.curiti;
	var curit=null;
	if(curiti>=0)
		curit=it.parme.items[curiti];
	if(it.mbi==0&&st_menus[it.mei].click_sh&&(curiti<0||(curiti>=0&&(!curit.submenu||!curit.submenu.isshow))))
	{
		if(curiti!=it.iti)
		{
			if(curiti>=0)
			{
				shitst(curit,0);
				it.parme.curiti=-1;
			}
			shitst(it,1);
			it.parme.curiti=it.iti;
		}
	}
	else
	{
		if(it.parme.curiti!=it.iti)
		{
			if(it.parme.curiti>=0)
			{
				it.parme.items[it.parme.curiti].hidepop();
				it.parme.curiti=-1;
			}
			it.showpop();
			it.parme.curiti=it.iti;
		}
		else
		{
			if(it.submenu&&it.submenu.isshow==false)
			{
				shitst(it,2);
				it.submenu.showpop();
			}
		}
	}
	if(it.st_text!="")
		window.status=it.st_text;
}

function doitou()
{
	var e=nIE ? event : arguments[0];
	var obj=this;
	var id=this.id;
	var mei=(id.substring(3,id.indexOf('_')));
	var mbi=eval(id.substring(id.indexOf('_')+1,id.indexOf('__')));
	var iti=eval(id.substring(id.indexOf('__')+2,id.indexOf('___')));
	var it=st_menus[mei].bodys[mbi].items[iti];

	if(!it.parme.isshow||(!nNN&&(e.toElement&&e.toElement.id&&e.toElement.id.indexOf(it.block)>=0)))
		return ;
	if(nNN4)
		it.getlayer().document.layers[0].releaseEvents(Event.MOUSEDOWN|Event.CLICK);

	if(!it.submenu||!it.submenu.isshow)
	{
		shitst(it,0);
		it.parme.curiti=-1;
	}
	window.status="";
}

function doitck()
{
	var e=nIE ? event : arguments[0];
	var obj=this;
	var id=this.id;
	var mei=(id.substring(3,id.indexOf('_')));
	var mbi=eval(id.substring(id.indexOf('_')+1,id.indexOf('__')));
	var iti=eval(id.substring(id.indexOf('__')+2,id.indexOf('___')));
	var it=st_menus[mei].bodys[mbi].items[iti];

	if(it.mbi==0&&st_menus[it.mei].click_sh&&it.submenu)
	{
		if(!it.submenu.isshow)
		{
			shitst(it,2);
			it.submenu.showpop();
		}
		else
		{
			shitst(it,1);
			it.submenu.hidepop();
		}
	}
	else
	{
		if(!it.submenu)
			shitst(it,1);
		if(it.url!="")
			window.open(it.url,it.target);
	}
}

function doitmd()
{
	var e=nIE ? event : arguments[0];
	var obj=this;
	var id=this.id;
	var mei=(id.substring(3,id.indexOf('_')));
	var mbi=eval(id.substring(id.indexOf('_')+1,id.indexOf('__')));
	var iti=eval(id.substring(id.indexOf('__')+2,id.indexOf('___')));
	var it=st_menus[mei].bodys[mbi].items[iti];

	shitst(it,2);
}

function getme()
{
	return st_menus[this.mei];
}

function getlayerMB()
{
	return nNN4 ? document.layers[this.block] : getob(this.block);
}

function getlayerIT()
{
	return nNN4 ? this.parme.getlayer().document.layers[0].document.layers[this.block] : getob(this.block);
}

function get_st_lay()
{
	if(this.type=="sepline")
		return null;
	var st_arr=new Array();
	for(i=0;i<3;i++)
	{
		st_arr[st_arr.length]=this.getlayer().document.layers[0].document.layers[this.block+st_state[i]];
		st_arr[st_arr.length]=this.getlayer().document.layers[0].document.layers[this.block+st_state[i]+"M"];
	}
	return st_arr;
}

function addquo(n)
{
	return "\""+n+"\"";
}

function getob(id)
{
	if(nNN6)
		return document.getElementById(id);
	else if(!nNN4)
		return document.all[id];
	else
	{
		alert("The code for NN4 can't call this function directly!");
		return null;
	}
}

function getrect()
{
	if(nNN4)
	{
		var obj=this.getlayer();
		return [obj.pageX,obj.pageY,obj.clip.width,obj.clip.height];
	}
	else
	{
		var l,t,w,h;
		var obj=this.getlayer();
		w=parseInt(obj.offsetWidth);
		h=parseInt(obj.offsetHeight);
		l=0;t=0;
		while(obj)
		{
			l+=parseInt(obj.offsetLeft);
			t+=parseInt(obj.offsetTop);
			obj=obj.offsetParent;
		}
		return [l,t,w,h];
	}
}

function getxy()
{
	var x=this.offset_l;
	var y=this.offset_t;
	var subrc=this.getrect();
	if(this.mbi==0)
	{
		if(st_menus[this.mei].type=="custom")
			return [st_menus[this.mei].pos_l,st_menus[this.mei].pos_t];
		else if(st_menus[this.mei].type=="float")
			return [getcl()+st_menus[this.mei].pos_l,getct()+st_menus[this.mei].pos_t];
		else
			return [subrc[0],subrc[1]];
	}
	var itrc=this.parit.getrect();
	var bdrc=this.parit.parme.getrect();
	switch(this.offset)
	{
		case "left":
			x+=itrc[0]-subrc[2];
			y+=itrc[1];
			break;
		case "up":
			x+=itrc[0];
			y+=itrc[1]-subrc[3];
			break;
		case "right":
			x+=itrc[0]+itrc[2];
			y+=itrc[1];
			break;
		case "down":
			x+=itrc[0];
			y+=itrc[1]+itrc[3];
			break;
		case "auto":
		default:
			break;

	}
	if(nIE&&dtdExist(this))
		return [x,y];
	else
		return this.adjust([x,y]);
}

function moveto(xy)
{
	if(xy!=null&&(this.mbi||st_menus[this.mei].pos=="absolute"))
	{
		if(nNN4)
			this.getlayer().moveToAbsolute(xy[0],xy[1]);
		else
		{
			this.getlayer().style.left=xy[0];
			this.getlayer().style.top=xy[1];
		}
	}
}

function adjust(xy)
{
	var rc=this.getrect();
	var tx=xy[0];
	var ty=xy[1];
	var c_l=getcl();
	var c_t=getct();
	var c_r=c_l+getcw();
	var c_b=c_t+getch();
	if(tx+rc[2]>c_r)
		tx=c_r-rc[2];
	tx=tx>c_l ? tx : c_l;
	if(ty+rc[3]>c_b)
		ty=c_b-rc[3];
	ty=ty>c_t ? ty : c_t;
	return [tx,ty];
}

function ckPage()
{
	var st_or_w=st_cl_w;
	var st_or_h=st_cl_h;
	var st_or_l=st_cl_l;
	var st_or_t=st_cl_t;
	st_cl_w=getcw();
	st_cl_h=getch();
	st_cl_l=getcl();
	st_cl_t=getct();
	if(nOP&&(st_cl_w-st_or_w||st_cl_h-st_or_h))
		eval(st_resHandle);
	else if(st_cl_l-st_or_l||st_cl_t-st_or_t)
	{
		var mei;
		for(mei in st_menus)
		{
			var menu=st_menus[mei];
			if(menu&&menu.bodys.length&&menu.type=="float")
			{
				var mb=menu.bodys[0];
				menu.bodys[0].moveto([st_cl_l+menu.pos_l,st_cl_t+menu.pos_t]);
				if(nIE)
					mov_sh(mb,st_cl_l-st_or_l,st_cl_t-st_or_t);
			}
		}
	}
	else if(nIE)
	{
		var mei;
		for(mei in st_menus)
		{
			var menu=st_menus[mei];
			if(menu&&menu.bodys.length&&menu.type=="float"&&menu.bodys[0].ds_sz&&!menu.bodys[0].ds_arr)
			{
				var mb=menu.bodys[0];
				if(mb.ds_st=="complex")
					add_sh(mb,mb.getlayer(),mb.ds_color,mb.ds_sz);
			}
		}
	}
}

function createIMG(src,id,width,height,border)
{
	var s="<IMG SRC=";
	s+=addquo(src);
	if(id!="")
		s+=" ID="+id;
	if(width&&height)
	{
		if(width>0)
			s+=" WIDTH="+width;
		if(height>0)
			s+=" HEIGHT="+height;
	}
	s+=" BORDER="+border+">";
	return s;
}

function fixmenu()
{
	var mei;
	for(mei in st_menus)
	{
		var menu=st_menus[mei];
		var mbi;
		for(mbi=0;mbi<menu.bodys.length;mbi++)
		{
			var body=menu.bodys[mbi];
			if(body.arrange!="vertically")
			{
				var iti=0;
				var fixit=getob(body.block+iti);
				if(!fixit)	return;
				var h=parseInt(fixit.offsetHeight);
				if(!nIE&&!h)	return;
				for(iti=0;iti<body.items.length;iti++)
				{
					var item=body.items[iti];
					if(nOP||item.type=="sepline"||nIE)	item.getlayer().style.height=h-2*body.spacing;
					else	item.getlayer().style.height=h-2*body.spacing-2*item.bd_sz;
				}
			}
			else if(nIE||nOP)
			{
				for(iti=0;iti<body.items.length;iti++)
				{
					var item=body.items[iti];
					if(item.type!="sepline")
					{
						var fixit=getob(body.block+iti);
						if(!fixit)	return;
						var it=item.getlayer();
						if(!it)	return;
						var h=it.offsetHeight;
						var w=fixit.offsetWidth;
						if(!nIE&&(!h||!w))	return;
						if(h)
							item.getlayer().style.height=h;
						if(w)	
							if(nOP)	item.getlayer().style.width=w-2*body.spacing;
					}
				}
			}
		}
	}
	for(mei in st_menus)
	{
		var menu=st_menus[mei];
		if(menu.type!="custom")
			menu.bodys[0].getlayer().style.visibility="visible";
	}
	clearInterval(st_fixid);
	st_fixid=null;
}

function shitst(it,nst)
{
	if(nNN4)
	{
		var st_lay=it.get_st_lay();
		for(i=0;i<3;i++)
		{
			st_lay[i*2+1].resizeTo(st_lay[i*2].clip.width,st_lay[i*2].clip.height);
			st_lay[i*2].visibility=(i==nst ? "show" : "hide");
			st_lay[i*2+1].visibility=(i==nst ? "show" : "hide");
		}
	}
	else
	{
		var obj=it.getlayer();
		obj.style.backgroundColor=it.bg_cl[nst];
		obj.style.borderColor=it.bd_cl[nst];
		if(it.bg_img[nst]!="")
		{
			obj.style.backgroundImage="url("+it.bg_img[nst]+")";
			obj.style.backgroundRepeat=it.bg_rep[nst];
		}
		var tmp=getob(it.txblock).style;
		if(it.f_fm[0]!=it.f_fm[1]||it.f_fm[0]!=it.f_fm[2])
			tmp.fontFamily=it.f_fm[nst];
		tmp.fontSize=it.f_sz[nst]+"pt";
		tmp.color=it.f_cl[nst];
		tmp.fontWeight=it.f_wg[nst];
		tmp.fontStyle=it.f_st[nst];
		tmp.textDecoration=it.f_de[nst];
		
		if(it.icon[0]!=it.icon[1]||it.icon[0]!=it.icon[2])
		{
			tmp=getob(it.block+"ICON");
			if(tmp)
				tmp.src=it.icon[nst];
		}
		if(it.isimage&&(it.image[0]!=it.image[1]||it.image[0]!=it.image[2]))
		{
			tmp=getob(it.block+"IMG");
			if(tmp)
				tmp.src=it.image[nst];
		}
	}
}

function dombov()
{
	var e=nIE ? event : arguments[0];
	var obj=this;
	var id=this.id;
	var mei=(id.substring(3,id.indexOf('_')));
	var mbi=eval(id.substring(id.indexOf('_')+1,id.indexOf('__')));
	var mb=st_menus[mei].bodys[mbi];

	if(!mb.isshow||(!nNN&&(e.fromElement&&e.fromElement.id&&e.fromElement.id.indexOf(mb.block)>=0)))
		return ;
	if(st_menus[mb.mei].hdid)
	{
		clearTimeout(st_menus[mb.mei].hdid);
		st_menus[mb.mei].hdid=null;
	}
}

function dombou()
{
	var e=nIE ? event : arguments[0];
	var obj=this;
	var id=this.id;
	var mei=(id.substring(3,id.indexOf('_')));
	var mbi=eval(id.substring(id.indexOf('_')+1,id.indexOf('__')));
	var mb=st_menus[mei].bodys[mbi];

	if(!mb.isshow||(!nNN&&(e.toElement&&e.toElement.id&&e.toElement.id.indexOf(mb.block)>=0)))
		return ;

	if(st_menus[mb.mei].hdid)
	{
		clearTimeout(st_menus[mb.mei].hdid);
		st_menus[mb.mei].hdid=null;
	}
	st_menus[mb.mei].hdid=setTimeout("st_menus['"+mb.mei+"'].hideall();",st_menus[mb.mei].hddelay);
}

function show()
{
	this.exec_ed=false;
	if(nNN4)
	{
		this.moveto(this.getxy());
		sh(this);
	}
	else
	{
		if(this.tmid)
		{
			clearTimeout(this.tmid);
			this.tmid=null;
		}
		this.spec_sh();
	}
	this.isshow=true;
	this.exec_ed=true;
}

function hide()
{
	this.exec_ed=false;
	if(nNN4)
		hd(this);
	else
	{
		if(this.tmid)
		{
			clearInterval(this.tmid);
			this.tmid=null;
		}
		this.spec_hd();
	}
	this.isshow=false;
	this.exec_ed=true;
}

function showpop()
{
	this.show();
}

function shitpop()
{
	if(this.submenu)
	{
		if(!this.submenu.isshow)
		{
			this.submenu.showpop();
		}
		shitst(this,2);
	}
	else
		shitst(this,1);
}

function hditpop()
{
	if(this.submenu&&this.submenu.isshow)
		this.submenu.hidepop();
	shitst(this,0);
}

function hidepop()
{
	if(this.curiti>=0)
	{
		var tmp=this.items[this.curiti].submenu;
		if(tmp&&tmp.isshow)
		{
			tmp.hidepop();
		}
		shitst(this.items[this.curiti],0);
		this.curiti=-1;
	}
	this.hide();
}

function hideall()
{
	var mb=this.bodys[0];
	if(mb.isshow)
	{
		if(mb.curiti>=0)
		{
			mb.items[mb.curiti].hidepop();
			mb.curiti=-1;
		}
		if(this.type=="custom")
			mb.hide();
	}
	this.hdid=null;
}

function dtdExist(me)
{
	var obj;
	for(obj=me.getlayer().offsetParent;obj;obj=obj.offsetParent)
	{
		if(obj==document.body)
			return false;
	}
	return true;
}

function setupEvent(menu)
{
	var ly;
	for(mbi=0;mbi<menu.bodys.length;mbi++)
	{
		var body=menu.bodys[mbi];
		ly=nNN4 ? body.getlayer().document.layers[0] : body.getlayer();
		ly.onmouseover=dombov;
		ly.onmouseout=dombou;
		for(iti=0;iti<body.items.length;iti++)
		{
			var item=body.items[iti];
			if(item.type!="sepline")
			{
				do
				{
					ly=nNN4 ? item.getlayer().document.layers[0] : item.getlayer();
				}while(!ly);
				ly.onmouseover=doitov;
				ly.onmouseout=doitou;
				ly.onmousedown=doitmd;
				ly.onclick=doitck;
			}
		}
	}
}

function bufimg(obj,sr,tmobj)
{
	if(sr!="")
	{
		eval("tmobj."+obj+"=new Image();");
		eval("tmobj."+obj+".src=\""+sr+"\";");
	}
}

function normal_init()
{
}

function normal_sh()
{
	this.tmid=null;
	this.moveto(this.getxy());
	sh(this);
	if(nIE&&this.ds_st=="complex")
		add_sh(this,this.getlayer(),this.ds_color,this.ds_sz);
}

function normal_hd()
{
	this.tmid=null;
	if(nIE)
		del_sh(this);
	hd(this);
}

function fade_init()
{
	this.current=0;
	this.step=parseInt(this.opacity*this.spec_sp/200);
	if(this.step<=0)
		this.step++;
}

function fade_sh()
{
	this.tmid=null;
	if(this.exec_ed)
	{
		this.current+=this.step;
		if(this.current>this.opacity)
			this.current=this.opacity;
	}
	this.getlayer().filters["Alpha"].opacity=this.current;
	if(!this.exec_ed)
	{
		this.moveto(this.getxy());
		sh(this);
	}
	if(this.current!=this.opacity)
		this.tmid=setTimeout("st_menus['"+this.mei+"'].bodys["+this.mbi+"].spec_sh();",50);
	else if(this.ds_st=="complex")
		add_sh(this,this.getlayer(),this.ds_color,this.ds_sz);
}

function fade_hd()
{
	this.tmid=null;
	if(this.exec_ed)
	{
		this.current-=this.step;
		if(this.current<0||!this.hdsp)
			this.current=0;
	}
	else
		del_sh(this);
	this.getlayer().filters["Alpha"].opacity=this.current;
	if(!this.current)
		hd(this);
	else
		this.tmid=setTimeout("st_menus['"+this.mei+"'].bodys["+this.mbi+"].spec_hd();",50);
}

function filter_init()
{
	this.fl_type=fl_ar[this.spec];
	this.duration=10/this.spec_sp;
	if(this.duration<0.1)
		this.duration=0.1;
}

function filter_sh()
{
	this.tmid=null;
	if(this.exec_ed)
	{
		if(this.getlayer().filters["revealTrans"].Status==0)
			add_sh(this,this.getlayer(),this.ds_color,this.ds_sz);
		else
			this.tmid=setTimeout("st_menus['"+this.mei+"'].bodys["+this.mbi+"].spec_sh();",10);
	}
	else
	{
		var fl_obj=this.getlayer().filters["revealTrans"];
		fl_obj.apply();
		this.moveto(this.getxy());
		sh(this);
		fl_obj.play();
		if(this.ds_st=="complex")
			this.tmid=setTimeout("st_menus['"+this.mei+"'].bodys["+this.mbi+"].spec_sh();",10);
	}
}

function filter_hd()
{
	this.tmid=null;
	del_sh(this);
	var fl_obj=this.getlayer().filters["revealTrans"];
	if(this.hdsp)	fl_obj.apply();
	hd(this);
	if(this.hdsp)	fl_obj.play();
}

function showFloatMenuAt(mei,x,y)
{
	if(nSTMENU)
	{
		var menu=st_menus[mei.replace(/_/g,"`~`")];
		if(menu&&menu.type=="custom"&&menu.bodys.length&&!menu.bodys[0].isshow)
		{
			movetoex(menu,[x,y]);
			menu.bodys[0].show();
		}
	}
}

function movetoex(menu,xy)
{
	menu.pos_l=xy[0];
	menu.pos_t=xy[1];
}

function getcursor(it)
{
	if(nNN6)
		return "default";
	return it.type!="sepline"&&((it.mbi==0&&st_menus[it.mei].click_sh&&it.submenu)||it.url!="") ? "hand" : "default";
}

function getwdstr(obj)
{
	if(obj.parme.arrange=="vertically")
	{
		if(obj.parme.lw_max>0)
			return " WIDTH="+obj.parme.lw_max;
		else
			return "";
	}
	else
	{
		if(obj.icon_w>0)
			return " WIDTH="+obj.icon_w;
		else
			return "";
	}
}

function add_sh(mb,el,color,size)
{
	var i,rect,rs,opacity;
	mb.ds_arr=new Array();
	_l=el.style.posLeft;_t=el.style.posTop;_w=el.offsetWidth;_h=el.offsetHeight;
	for (i=size; i>0; i--)
	{
		if(_w-1-i<=0)
			continue;
		var j;
		for(j=0;j<2;j++)
		{
			rect = document.createElement('div');
			rs = rect.style;
			rs.fontSize="0px";
			rs.position = 'absolute';
			rs.left=(j?(_l+i):(_l+_w-1))+"px";
			rs.top=(j?(_t+_h-1):(_t+i))+"px";
			rs.width=(j?(_w-1-i):(1+i))+"px";
			rs.height=(j?(1+i):(_h))+"px";
			rs.zIndex = el.style.zIndex-i;
			rs.backgroundColor = color;
			opacity = 1 - i / (i + 1);
			rs.filter = 'alpha(opacity=' + (mb.opacity * opacity) + ')';
			el.insertAdjacentElement('afterEnd', rect);
			mb.ds_arr[mb.ds_arr.length]=rect;
		}
	}
}

function del_sh(mb)
{
	var i;
	if(mb.ds_arr)
	{
		for(i=mb.ds_arr.length-1;i>=0;i--)
			mb.ds_arr[i].removeNode(true);
		mb.ds_arr[i]=null;
	}
}

function mov_sh(mb,dx,dy)
{
	if(mb.ds_arr)
		for(i=mb.ds_arr.length-1;i>=0;i--)
		{
			mb.ds_arr[i].style.left=mb.ds_arr[i].style.posLeft+dx+"px";
			mb.ds_arr[i].style.top=mb.ds_arr[i].style.posTop+dy+"px";
		}
}

function detectNav()
{
	nOP=navigator.userAgent.indexOf("Opera")!=-1;
	if(nOP) nOP6=navigator.userAgent.indexOf("Opera 6")!=-1;
	else
	{
		nIE=document.all ? true : false;
		if(nIE)
		{
			nIM=navigator.appVersion.indexOf("Mac")!=-1;
			nIE4=(eval(navigator.appVersion.substring(0,1)>=4));
			nIE5=navigator.appVersion.indexOf("MSIE 5.0")>=0;
			nIM4=nIM&&nIE4;
		}
		else
		{
			nNN4=document.layers&&navigator.appVersion.substring(0,1)=="4" ? true : false;
			if(!nNN4)
				nNN6=(document.getElementsByTagName("*") && navigator.userAgent.indexOf("Gecko")!=-1);
			nNN=nNN4||nNN6;
		}
	}
	nSTMENU=nOP6||nIE4||nNN;
}

window.onload=st_onload;
function st_onload()
{
	if(nSTMENU)
	{
		var i;
		for(i in st_menus)
			setupEvent(st_menus[i]);

		st_cl_w=getcw();
		st_cl_h=getch();
		st_cl_l=getcl();
		st_cl_t=getct();
		if(st_rl_id)
			clearInterval(st_rl_id);
		st_rl_id=setInterval("ckPage();",50);
		if(!nNN4)
		{
			if(st_fixid)
				clearInterval(st_fixid);
			st_fixid=setInterval("fixmenu();",50);
		}
	}
}