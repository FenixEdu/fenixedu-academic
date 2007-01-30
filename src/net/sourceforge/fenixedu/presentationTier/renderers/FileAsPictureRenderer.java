package net.sourceforge.fenixedu.presentationTier.renderers;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.renderers.StringRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;

public class FileAsPictureRenderer extends StringRenderer {

    private String classes;

    public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	@Override
    public HtmlComponent render(Object object, Class type) {
        File file = (File) object;
        
        HtmlImage image = new HtmlImage();

        image.setSource(FileManagerFactory.getFactoryInstance().getFileManager().formatDownloadUrl(file.getExternalStorageIdentification(), file.getFilename()));
        image.setTitle(file.getDisplayName());
        
        image.setClasses(classes);
        
        return image;
    }

}
