package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class InvocationResult {

    private boolean success;

    private List<LabelFormatter> messages;

    public InvocationResult() {
	this.success = false;
	this.messages = new ArrayList<LabelFormatter>();
    }

    public boolean isSuccess() {
	return success;
    }

    public InvocationResult setSuccess(boolean success) {
	this.success = success;
	return this;
    }

    public InvocationResult addMesssage(final LabelFormatter message) {
	this.messages.add(message);
	return this;
    }

    public InvocationResult addMessages(final List<LabelFormatter> messages) {
	this.messages.addAll(messages);
	return this;
    }

    public InvocationResult addMessage(final String bundle, final String key, final String... args) {
	this.messages.add(new LabelFormatter(bundle, key, args));

	return this;
    }

    public InvocationResult addMessage(final String text) {
	this.messages.add(new LabelFormatter(text));

	return this;
    }

    public List<LabelFormatter> getMessages() {
	return Collections.unmodifiableList(messages);
    }

    public LabelFormatter[] getMessagesAsArray() {
	return this.messages.toArray(new LabelFormatter[] {});
    }

    public InvocationResult and(final InvocationResult other) {
	InvocationResult result = new InvocationResult();
	result.setSuccess(isSuccess() && other.isSuccess());
	result.addMessages(getMessages());
	result.addMessages(other.getMessages());

	return result;

    }

}
