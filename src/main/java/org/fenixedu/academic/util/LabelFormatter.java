package org.fenixedu.academic.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.common.base.Strings;

public class LabelFormatter implements Serializable {

    private static class Label implements Serializable {
        private final String key;

        private final String bundle;

        private final String[] args;

        public Label(String bundle, String key, String... args) {
            super();
            this.bundle = bundle;
            this.key = key;
            this.args = args;
        }

        public String getBundle() {
            return bundle;
        }

        public String getKey() {
            return key;
        }

        public boolean isUseBundle() {
            return !Strings.isNullOrEmpty(bundle);
        }

        public String[] getArgs() {
            return this.args;
        }

    }

    private final List<Label> labels;

    public LabelFormatter() {
        this.labels = new ArrayList<Label>();
    }

    public LabelFormatter(String text) {
        this();
        appendLabel(text);
    }

    public LabelFormatter(final String key, final String bundle) {
        this();
        appendLabel(key, bundle);
    }

    public LabelFormatter(final String bundle, final String key, final String... args) {
        this();
        appendLabel(bundle, key, args);
    }

    public LabelFormatter appendLabel(String text) {
        this.labels.add(new Label(null, text));

        return this;
    }

    public LabelFormatter appendLabel(String key, String bundle) {
        this.labels.add(new Label(bundle, key));

        return this;
    }

    public LabelFormatter appendLabel(String bundle, String key, String... args) {
        this.labels.add(new Label(bundle, key, args));

        return this;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        for (final Label label : getLabels()) {
            if (label.isUseBundle()) {
                result.append(BundleUtil.getString(label.getBundle(), label.getKey(), label.getArgs()));
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
