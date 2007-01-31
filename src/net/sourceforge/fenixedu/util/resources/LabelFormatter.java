package net.sourceforge.fenixedu.util.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LabelFormatter implements Serializable {

    public final static String ENUMERATION_RESOURCES = "enum";
    
    public final static String APPLICATION_RESOURCES = "application";
    
    private static class Label implements Serializable {
        private String key;

        private String bundle;

        private boolean useBundle;

        public Label(String key, String bundle, boolean useBundle) {
            super();
            this.bundle = bundle;
            this.key = key;
            this.useBundle = useBundle;
        }

        public String getBundle() {
            return bundle;
        }

        public void setBundle(String bundle) {
            this.bundle = bundle;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public boolean isUseBundle() {
            return useBundle;
        }

        public void setUseBundle(boolean useBundle) {
            this.useBundle = useBundle;
        }

    }

    private List<Label> labels;

    public LabelFormatter() {
        this.labels = new ArrayList<Label>();
    }

    public LabelFormatter appendLabel(String text) {
        this.labels.add(new Label(text, null, false));

        return this;
    }

    public LabelFormatter appendLabel(String key, String bundle) {
        this.labels.add(new Label(key, bundle, true));

        return this;
    }

    @Override
    public String toString() {
        return toString(new DefaultResourceBundleProvider());
    }

    public String toString(IMessageResourceProvider messageResourceProvider) {
        final StringBuilder result = new StringBuilder();

        for (final Label label : getLabels()) {
            if (label.isUseBundle()) {
                result.append(messageResourceProvider.getMessage(label.getKey(), label.getBundle()));
            } else {
                result.append(label.getKey());
            }
        }

        return result.toString();

    }

    public List<Label> getLabels() {
        return Collections.unmodifiableList(this.labels);
    }
    
    public boolean isEmpty() {
	return this.labels.isEmpty();
    }

}
