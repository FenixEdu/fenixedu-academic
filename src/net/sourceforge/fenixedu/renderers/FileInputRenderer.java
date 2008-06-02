package net.sourceforge.fenixedu.renderers;

import java.io.IOException;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlForm;
import net.sourceforge.fenixedu.renderers.components.HtmlInputFile;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.plugin.RenderersRequestProcessor;
import pt.ist.fenixWebFramework.servlets.commons.UploadedFile;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This renderer creates a file input field that allows the user to 
 * submit a file. The renderer will set the slot with an output 
 * stream that represents the file.
 * 
 * <p>
 * Example:
 * <input type="file"/>
 * 
 * @author cfgi
 */
public class FileInputRenderer extends InputRenderer {

    private String size;
    private String fileNameSlot;
    private String fileSizeSlot;
    private String fileContentTypeSlot;
    
    public String getSize() {
        return this.size;
    }

    /**
     * Selects the size of the file input field.
     * 
     * @property
     */
    public void setSize(String size) {
        this.size = size;
    }

    public String getFileContentTypeSlot() {
        return this.fileContentTypeSlot;
    }

    /**
     * When this property is specified the renderer will set the given slot
     * with the content type of the file. The slot must have the string type.
     * 
     * @property
     */
    public void setFileContentTypeSlot(String fileContentTypeSlot) {
        this.fileContentTypeSlot = fileContentTypeSlot;
    }

    public String getFileNameSlot() {
        return this.fileNameSlot;
    }

    /**
     * When this property is specified the renderer will set the given slot
     * with the name of the file as given by the user. The slot must have the 
     * string type.
     * 
     * @property
     */
    public void setFileNameSlot(String fileNameSlot) {
        this.fileNameSlot = fileNameSlot;
    }

    public String getFileSizeSlot() {
        return this.fileSizeSlot;
    }

    /**
     * When this property is specified the renderer will set the given slot
     * with the size of the file in bytes. The slot must be have the 
     * <code>long</code> type.
     * 
     * @property
     */
    public void setFileSizeSlot(String fileSizeSlot) {
        this.fileSizeSlot = fileSizeSlot;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                getInputContext().getForm().setEncoding(HtmlForm.FORM_DATA);
                
                HtmlInputFile file = new HtmlInputFile();
                file.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
       
                file.setController(new UpdateFilePropertiesController(
                        ((MetaSlot) getInputContext().getMetaObject()).getMetaObject(), 
                        getFileNameSlot(), getFileSizeSlot(), getFileContentTypeSlot()));
                file.setConverter(new FileConverter(file));
                return file;
            }

            @Override
            public void applyStyle(HtmlComponent component) {
                super.applyStyle(component);
                
                ((HtmlInputFile) component).setSize(getSize());
            }
            
        };
    }

    private static class UpdateFilePropertiesController extends HtmlController {

        private MetaObject object;
        private String fileNameSlot;
        private String fileSizeSlot;
        private String fileContentTypeSlot;

        public UpdateFilePropertiesController(MetaObject object, String fileNameSlot, String fileSizeSlot, String fileContentTypeSlot) {
            this.object = object;
            this.fileNameSlot = fileNameSlot;
            this.fileSizeSlot = fileSizeSlot;
            this.fileContentTypeSlot = fileContentTypeSlot;
        }

        @Override
        public void execute(IViewState viewState) {
            HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getControlledComponent();
            String name = component.getName();
            
            UploadedFile file = RenderersRequestProcessor.getUploadedFile(name);
            if (file != null) { // if has file
                Object object = this.object.getObject();
                
                setPropertyIgnoringErrors(object, this.fileNameSlot, file.getName());
                setPropertyIgnoringErrors(object, this.fileSizeSlot, file.getSize());
                setPropertyIgnoringErrors(object, this.fileContentTypeSlot, file.getContentType());
            }
        }
        
        private void setPropertyIgnoringErrors(Object object, String property, Object value) {
            if (property == null) {
                return;
            }
            
            try {
                PropertyUtils.setProperty(object, property, value);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    }
    
    private static class FileConverter extends Converter {

        private HtmlInputFile component;

        public FileConverter(HtmlInputFile file) {
            this.component = file;
        }

        @Override
        public Object convert(Class type, Object value) {
            String name = this.component.getName();
            UploadedFile file = RenderersRequestProcessor.getUploadedFile(name);
            
            if (file == null) {
                return null;
            }
            else {
                try {
                    return file.getInputStream();
                } catch (IOException e) {
                    throw new ConversionException("renderers.converter.file.obtain", e, true, null);
                }
            }
        }
        
    }
}
