package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.AnimationCallback;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.PathBuilder;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael.Rect;

public class FenixLoadingScreenWidget extends Raphael{
    
    int width;
    int height;
    double innerProd;
    String title;
    String subtitle;
    int[] xImportsFromAI;
    int[] yImportsFromAI;
    double[] xHeart;
    double[] yHeart;
    Circle[] dots;
    Circle[] heart;
    double radius;
    
    private FenixLoadingScreenWidget(int width, int height) {
	super(width,height);
    }

    public FenixLoadingScreenWidget(int width, int height, String title, String subtitle) {
	this(width, height);
	this.width = width;
	this.height = height;
	this.title = title;
	this.subtitle = subtitle;
	innerProd = Math.sqrt(width*width + height*height);
	radius = innerProd*0.003;
	//drawOutline();
	populateCoordinates();
	drawRooster();
	animateRooster(40);
	heartBeat();
    }
    
    private void drawOutline() {
	final Rect outline = new Rect(0.0, 0.0, width-1, height-1);
	outline.attr("stroke", "black");
	outline.attr("stroke-width", 1);
	
	final Circle center = new Circle(width/2, height/2, radius*2);
	center.attr("fill","#000");
	center.attr("stroke-width", 0.0);
    }
    
    private int getFontSize(double reference) {
	return (int) (innerProd * (reference * 1.0) / 100.0);
    }
    
    public void populateCoordinates() {
	int i = 0;
	
	xImportsFromAI = new int[89];
	xImportsFromAI[0] = 217;
	xImportsFromAI[1] = 196;
	xImportsFromAI[2] = 177;
	xImportsFromAI[3] = 166;
	xImportsFromAI[4] = 154;
	xImportsFromAI[5] = 147;
	xImportsFromAI[6] = 150;
	xImportsFromAI[7] = 157;
	xImportsFromAI[8] = 169;
	xImportsFromAI[9] = 182;
	xImportsFromAI[10] = 203;
	xImportsFromAI[11] = 231;
	xImportsFromAI[12] = 256;
	xImportsFromAI[13] = 283;
	xImportsFromAI[14] = 303;
	xImportsFromAI[15] = 322;
	xImportsFromAI[16] = 326;
	xImportsFromAI[17] = 322;
	xImportsFromAI[18] = 313;
	xImportsFromAI[19] = 303;
	xImportsFromAI[20] = 304;
	xImportsFromAI[21] = 327;
	xImportsFromAI[22] = 350;
	xImportsFromAI[23] = 351;
	xImportsFromAI[24] = 353;
	xImportsFromAI[25] = 354;
	xImportsFromAI[26] = 356;
	xImportsFromAI[27] = 359;
	xImportsFromAI[28] = 366;
	xImportsFromAI[29] = 377;
	xImportsFromAI[30] = 374;
	xImportsFromAI[31] = 356;
	xImportsFromAI[32] = 335;
	xImportsFromAI[33] = 321;
	xImportsFromAI[34] = 311;
	xImportsFromAI[35] = 293;
	xImportsFromAI[36] = 279;
	xImportsFromAI[37] = 270;
	xImportsFromAI[38] = 274;
	xImportsFromAI[39] = 287;
	xImportsFromAI[40] = 308;
	xImportsFromAI[41] = 335;
	xImportsFromAI[42] = 363;
	xImportsFromAI[43] = 389;
	xImportsFromAI[44] = 410;
	xImportsFromAI[45] = 435;
	xImportsFromAI[46] = 452;
	xImportsFromAI[47] = 464;
	xImportsFromAI[48] = 471;
	xImportsFromAI[49] = 478;
	xImportsFromAI[50] = 471;
	xImportsFromAI[51] = 450;
	xImportsFromAI[52] = 427;
	xImportsFromAI[53] = 434;
	xImportsFromAI[54] = 454;
	xImportsFromAI[55] = 472;
	xImportsFromAI[56] = 489;
	xImportsFromAI[57] = 505;
	xImportsFromAI[58] = 515;
	xImportsFromAI[59] = 498;
	xImportsFromAI[60] = 475;
	xImportsFromAI[61] = 454;
	xImportsFromAI[62] = 434;
	xImportsFromAI[63] = 426;
	xImportsFromAI[64] = 425;
	xImportsFromAI[65] = 424;
	xImportsFromAI[66] = 423;
	xImportsFromAI[67] = 422;
	xImportsFromAI[68] = 444;
	xImportsFromAI[69] = 464;
	xImportsFromAI[70] = 473;
	xImportsFromAI[71] = 463;
	xImportsFromAI[72] = 444;
	xImportsFromAI[73] = 432;
	xImportsFromAI[74] = 412;
	xImportsFromAI[75] = 391;
	xImportsFromAI[76] = 370;
	xImportsFromAI[77] = 349;
	xImportsFromAI[78] = 335;
	xImportsFromAI[79] = 314;
	xImportsFromAI[80] = 294;
	xImportsFromAI[81] = 296;
	xImportsFromAI[82] = 292;
	xImportsFromAI[83] = 276;
	xImportsFromAI[84] = 261;
	xImportsFromAI[85] = 243;
	xImportsFromAI[86] = 224;
	xImportsFromAI[87] = 222;
	xImportsFromAI[88] = 227;
	for(i=0; i<xImportsFromAI.length; i++) {
	    xImportsFromAI[i] += 30;
	}
	
	yImportsFromAI = new int[89];
	yImportsFromAI[0] = 175;
	yImportsFromAI[1] = 197;
	yImportsFromAI[2] = 220;
	yImportsFromAI[3] = 243;
	yImportsFromAI[4] = 264;
	yImportsFromAI[5] = 288;
	yImportsFromAI[6] = 315;
	yImportsFromAI[7] = 341;
	yImportsFromAI[8] = 365;
	yImportsFromAI[9] = 390;
	yImportsFromAI[10] = 411;
	yImportsFromAI[11] = 419;
	yImportsFromAI[12] = 409;
	yImportsFromAI[13] = 398;
	yImportsFromAI[14] = 387;
	yImportsFromAI[15] = 367;
	yImportsFromAI[16] = 344;
	yImportsFromAI[17] = 321;
	yImportsFromAI[18] = 299;
	yImportsFromAI[19] = 273;
	yImportsFromAI[20] = 249;
	yImportsFromAI[21] = 257;
	yImportsFromAI[22] = 267;
	yImportsFromAI[23] = 292;
	yImportsFromAI[24] = 315;
	yImportsFromAI[25] = 340;
	yImportsFromAI[26] = 363;
	yImportsFromAI[27] = 384;
	yImportsFromAI[28] = 403;
	yImportsFromAI[29] = 423;
	yImportsFromAI[30] = 446;
	yImportsFromAI[31] = 459;
	yImportsFromAI[32] = 465;
	yImportsFromAI[33] = 452;
	yImportsFromAI[34] = 427;
	yImportsFromAI[35] = 439;
	yImportsFromAI[36] = 458;
	yImportsFromAI[37] = 481;
	yImportsFromAI[38] = 503;
	yImportsFromAI[39] = 527;
	yImportsFromAI[40] = 547;
	yImportsFromAI[41] = 558;
	yImportsFromAI[42] = 566;
	yImportsFromAI[43] = 569;
	yImportsFromAI[44] = 565;
	yImportsFromAI[45] = 549;
	yImportsFromAI[46] = 530;
	yImportsFromAI[47] = 509;
	yImportsFromAI[48] = 487;
	yImportsFromAI[49] = 462;
	yImportsFromAI[50] = 440;
	yImportsFromAI[51] = 427;
	yImportsFromAI[52] = 415;
	yImportsFromAI[53] = 396;
	yImportsFromAI[54] = 394;
	yImportsFromAI[55] = 390;
	yImportsFromAI[56] = 385;
	yImportsFromAI[57] = 376;
	yImportsFromAI[58] = 359;
	yImportsFromAI[59] = 359;
	yImportsFromAI[60] = 359;
	yImportsFromAI[61] = 359;
	yImportsFromAI[62] = 359;
	yImportsFromAI[63] = 337;
	yImportsFromAI[64] = 314;
	yImportsFromAI[65] = 289;
	yImportsFromAI[66] = 266;
	yImportsFromAI[67] = 240;
	yImportsFromAI[68] = 227;
	yImportsFromAI[69] = 214;
	yImportsFromAI[70] = 193;
	yImportsFromAI[71] = 175;
	yImportsFromAI[72] = 162;
	yImportsFromAI[73] = 143;
	yImportsFromAI[74] = 128;
	yImportsFromAI[75] = 112;
	yImportsFromAI[76] = 112;
	yImportsFromAI[77] = 112;
	yImportsFromAI[78] = 131;
	yImportsFromAI[79] = 138;
	yImportsFromAI[80] = 149;
	yImportsFromAI[81] = 172;
	yImportsFromAI[82] = 196;
	yImportsFromAI[83] = 212;
	yImportsFromAI[84] = 226;
	yImportsFromAI[85] = 242;
	yImportsFromAI[86] = 246;
	yImportsFromAI[87] = 222;
	yImportsFromAI[88] = 196;
	for(i=0; i<yImportsFromAI.length; i++) {
	    yImportsFromAI[i] += 21;
	}
	
	xHeart = new double[24];
	xHeart[0] = 386;
	xHeart[1] = 388.667;
	xHeart[2] = 390.667;
	xHeart[3] = 390;
	xHeart[4] = 385.334;
	xHeart[5] = 380.667;
	xHeart[6] = 379.333;
	xHeart[7] = 383.667;
	xHeart[8] = 390.666;
	xHeart[9] = 398;
	xHeart[10] = 402.667;
	xHeart[11] = 410;
	xHeart[12] = 416;
	xHeart[13] = 423.333;
	xHeart[14] = 427.667;
	xHeart[15] = 431.333;
	xHeart[16] = 431.666;
	xHeart[17] = 429;
	xHeart[18] = 424.333;
	xHeart[19] = 418.334;
	xHeart[20] = 411.333;
	xHeart[21] = 406;
	xHeart[22] = 399;
	xHeart[23] = 393;
	for(i=0; i<xHeart.length; i++) {
	    xHeart[i] += 30;
	}

	
	yHeart = new double[24];
	yHeart[0] = 440.667;
	yHeart[1] = 432.333;
	yHeart[2] = 426;
	yHeart[3] = 418;
	yHeart[4] = 411.667;
	yHeart[5] = 406;
	yHeart[6] = 397.667;
	yHeart[7] = 389.667;
	yHeart[8] = 386;
	yHeart[9] = 389.333;
	yHeart[10] = 396.333;
	yHeart[11] = 394.667;
	yHeart[12] = 389;
	yHeart[13] = 390;
	yHeart[14] = 395.667;
	yHeart[15] = 402.333;
	yHeart[16] = 410;
	yHeart[17] = 416.333;
	yHeart[18] = 422;
	yHeart[19] = 427.334;
	yHeart[20] = 431.333;
	yHeart[21] = 436.001;
	yHeart[22] = 439;
	yHeart[23] = 440.333;
	for(i=0; i<yHeart.length; i++) {
	    yHeart[i] -= 21;
	}
    }
    
    
    public void drawRooster() {
	dots = new Circle[xImportsFromAI.length];
	for(int i=0; i<xImportsFromAI.length; i++) {
	    Circle dot = new Circle(xImportsFromAI[i], height - yImportsFromAI[i], radius);
	    dot.attr("fill","#AEE0F2");
	    dot.attr("stroke-width", 0.00001);
	    
	    dots[i] = dot;
	}
	
	heart = new Circle[xHeart.length];
	for(int k=0; k<xHeart.length; k++) {
	    Circle heartP = new Circle(xHeart[k], yHeart[k], radius);
	    heartP.attr("fill","#ED1C24");
	    heartP.attr("stroke-width", 0.00001);
	    
	    heart[k] = heartP;
	}
	
	if(title != null) {
	    final Text caption = new Text(width/2, height*0.85, title);
	    caption.attr("font-size", getFontSize(2) + "px");
	    caption.attr("font-family", "sans-serif");
	    caption.attr("font-style", "italic");
	    caption.attr("font-weight", "bold");
	    caption.attr("fill","#35697C");
	}
	
	if(subtitle != null) {
	    final Text subcaption = new Text(width/2, height*0.9, subtitle);
	    subcaption.attr("font-size", getFontSize(1.8) + "px");
	    subcaption.attr("font-family", "sans-serif");
	    subcaption.attr("fill","#58ADCC");
	}
    }
    
    public void animateRooster(int index) {
	final int thisIndex = index;
	JSONObject dotFocusAnimeParams = new JSONObject();
        dotFocusAnimeParams.put("r", new JSONString(Double.toString(radius*1.8)));
        dotFocusAnimeParams.put("fill", new JSONString("#35697C"));
        
        JSONObject dotUnfocusAnimeParams = new JSONObject();
        dotUnfocusAnimeParams.put("r", new JSONString(Double.toString(radius)));
        dotUnfocusAnimeParams.put("fill", new JSONString("#AEE0F2"));
        
        JSONObject dotSemiFocusAnimeParams = new JSONObject();
        dotSemiFocusAnimeParams.put("r", new JSONString(Double.toString(radius*1.5)));
        dotSemiFocusAnimeParams.put("fill", new JSONString("#58ADCC"));
        
        final int prevIndex = thisIndex==0 ? dots.length-1 : thisIndex-1;
        final int prev2Index = prevIndex==0 ? dots.length-1 : prevIndex-1;
        final int nextIndex = (thisIndex+1)%dots.length;
        final int next2Index = (nextIndex+1)%dots.length;
        
        dots[thisIndex].animate(dotFocusAnimeParams, 100, new AnimationCallback() {
	    
	    @Override
	    public void onComplete() {
		animateRooster(nextIndex);
	    }
	});
        
        dots[prev2Index].animateWith(dots[thisIndex], dotUnfocusAnimeParams, 100);
        dots[prevIndex].animateWith(dots[thisIndex], dotSemiFocusAnimeParams, 100);
        dots[nextIndex].animateWith(dots[thisIndex], dotSemiFocusAnimeParams, 100);
        dots[next2Index].animateWith(dots[thisIndex], dotUnfocusAnimeParams, 100);
    }
    
    public void heartBeat() {
	diastole();
    }
    
    private void diastole() {
	JSONObject diastoleParams = new JSONObject();
	diastoleParams.put("opacity", new JSONString(Double.toString(0.0)));
	for(int i=1; i<heart.length; i++) {
	    heart[i].animateWith(heart[0], diastoleParams, 2000);
	}
	heart[0].animate(diastoleParams, 2000, new AnimationCallback() {

	    @Override
	    public void onComplete() {
		systole();
	    }
	    
	});
    }
    
    private void systole() {
	JSONObject diastoleParams = new JSONObject();
	diastoleParams.put("opacity", new JSONString(Double.toString(1.0)));
	for(int i=1; i<heart.length; i++) {
	    heart[i].animateWith(heart[0], diastoleParams, 2000);
	}
	heart[0].animate(diastoleParams, 2000, new AnimationCallback() {

	    @Override
	    public void onComplete() {
		diastole();
	    }
	    
	});
    }

}
