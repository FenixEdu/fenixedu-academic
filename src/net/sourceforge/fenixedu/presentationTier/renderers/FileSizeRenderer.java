package net.sourceforge.fenixedu.presentationTier.renderers;

import java.text.MessageFormat;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class FileSizeRenderer extends OutputRenderer{

	private boolean shortUnit;
	
	public boolean isShortUnit() {
		return shortUnit;
	}

	/**
	 * Chooses if the file size unit will be displayed in short name or not,
	 * as in <em>b</em> VS <em>bytes</em>.
     * 
	 * @property 
	 */
	public void setShortUnit(boolean shortUnit) {
		this.shortUnit = shortUnit;
	}


	@Override
	protected Layout getLayout(Object object, Class type) {
		
		return new Layout() {

            @Override
			public HtmlComponent createComponent(Object object, Class type) {
				if (object == null) {
                    return new HtmlText("");
                }
                
				Number number = (Number) object;
				long size = number.longValue();
				String message;
				
				if (size / 1024 == 0) {
					message = MessageFormat.format("{0,number}" + ((isShortUnit()) ? "B" : "Bytes"), size);
				}
				else {
					if (size / (1024 * 1024) == 0) {
						message = MessageFormat.format("{0,number,#}" + ((isShortUnit()) ? "KB" : "KBytes"), size/1024.0);
					}
					else {
						if (size / (1024 * 1024 * 1024) == 0) {
							message = MessageFormat.format("{0,number,#.#}" + ((isShortUnit()) ? "MB" : "MBytes"), size/(1024*1024.0));
						}
						else {
							message = MessageFormat.format("{0,number,#.##}" + ((isShortUnit()) ? "GB" : "GBytes"), size/(1024*1024*1024.0));
						}
					}
				}
				
				return new HtmlText(message);
			}
		};
	}

}
