package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.HtmlInputFile;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.plugin.RenderersRequestProcessor;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.fileupload.FileItem;

public class FileValidator extends HtmlValidator {

    private boolean required;
    
    private String maxSize;
    private String acceptedExtensions;
    private String acceptedTypes; 
    
    private Object[] arguments;
    
    public FileValidator(Validatable component) {
        super(component);
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getAcceptedExtensions() {
        return this.acceptedExtensions;
    }

    /**
     * The list file extensions allowed separated by a comma. For example
     * <code>"gif,jpg,png"</code>. If the extension is specified then it 
     * has priority over the accepted types. If the file has no extension
     * then the accepted types are used if provided.
     */
    public void setAcceptedExtensions(String acceptedExtensions) {
        this.acceptedExtensions = acceptedExtensions;
    }

    public String getAcceptedTypes() {
        return this.acceptedTypes;
    }

    /**
     * List of accepted types separated by a comma. For example 
     * <code>"image/gif,image/jpg,image/png"</code>. You can
     * also use wildcards like <code>"image/*"</code>.
     */
    public void setAcceptedTypes(String acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
    }

    public String getMaxSize() {
        return this.maxSize;
    }

    /**
     * The maximum size allowed for files. You can specify the units using
     * the suffix <code>"MB"</code>, <code>"KB"</code>, or <code>"B"</code>. 
     * If no units are given the value is considered to be in bytes.
     */
    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    private long convertedMaxSize() {
        String maxSize = getMaxSize().trim().toLowerCase();
        long value;
        long multiplier = 1;
        
        if (maxSize.matches("[0-9]+(m|k)?b")) {
            int index;
            
            index = maxSize.indexOf("mb");
            if (index != -1) {
                multiplier = 1024 * 1024;
            }
            else {
                index = maxSize.indexOf("kb");
                if (index != -1) {
                    multiplier = 1024;
                }
                else {
                    index = maxSize.indexOf("b");
                }
            }
            
            value = Long.parseLong(maxSize.substring(0, index));
        }
        else {
            value = Long.parseLong(maxSize);
        }

        return value * multiplier;
    }
    
    @Override
    protected String getResourceMessage(String message) {
        if (this.arguments == null || this.arguments.length == 0) {
            return super.getResourceMessage(message);
        }
        else {
            return RenderUtils.getFormatedResourceString(message, this.arguments);
        }
    }

    @Override
    public void performValidation() {
        HtmlInputFile file = (HtmlInputFile) getComponent();

        FileItem item = RenderersRequestProcessor.getFileItem(file.getName());
        if (item == null || isRequired()) {
            setInvalid("renderers.validator.required");
            return;
        }
        
        if (getMaxSize() != null) {
            long size = item.getSize();
            long maxSize = convertedMaxSize();

            if (size > maxSize) {
                setInvalid("renderers.validator.file.size", maxSize, size);
                return;
            }
        }
        
        if (getAcceptedExtensions() != null) {
            String fileName = item.getFieldName();
            int index = fileName.lastIndexOf(".");
            
            if (index != -1) {
                String extension = fileName.substring(index + 1); 
                
                if (! getAcceptedExtensions().contains(extension)) {
                    setInvalid("renderers.validator.file.extension", getAcceptedExtensions(), extension);
                    return;
                }
                else {
                    setValid(true);
                    return;
                }
            }
        }
        
        if (getAcceptedTypes() != null) {
            if (! matchesMimeType(item.getContentType())) {
                setInvalid("renderers.validator.file.type", getAcceptedTypes(), item.getContentType());
                return;
            }
        }
        
        setValid(true);
    }

    private boolean matchesMimeType(String contentType) {
        String[] acceptedTypes = getAcceptedTypes().split(",");
        
        for (int i = 0; i < acceptedTypes.length; i++) {
            String accepted = acceptedTypes[i];
            
            if (accepted.contains("*")) {
                accepted = accepted.replace("*", ".*");
            }

            if (contentType.matches(accepted)) {
                return true;
            }
        }
        
        return false;
    }

    private void setInvalid(String message, Object ... args) {
        setMessage(message);
        this.arguments = args;
        
        setValid(false);
    }
}
