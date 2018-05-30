	package pt.ist.fenix.webapp.task;
	

	

	import com.google.api.client.repackaged.com.google.common.base.Strings;
	import com.google.common.collect.Sets;
	import com.google.gson.*;
	import org.fenixedu.academic.domain.util.Email;
	import org.fenixedu.academic.domain.util.EmailAddressList;
	import org.fenixedu.academic.domain.util.email.*;
	import org.fenixedu.bennu.core.domain.Bennu;
	import org.fenixedu.bennu.core.groups.Group;
	import org.fenixedu.bennu.scheduler.custom.CustomTask;
	import org.joda.time.DateTime;
	import pt.ist.fenixedu.delegates.domain.util.email.DelegateSender;
	import pt.ist.fenixframework.Atomic.TxMode;
	import pt.ist.fenixframework.DomainObject;
	

	import java.io.*;
	import java.nio.charset.StandardCharsets;
	import java.util.Collection;
	import java.util.Objects;
	import java.util.Set;
	import java.util.stream.Collectors;
	

	import static pt.ist.fenix.webapp.task.ExportMessaging.Property.*;
	import static pt.ist.fenix.webapp.task.ExportMessaging.Tag.*;
	

	public class ExportMessaging extends CustomTask {
	

	    private static final String PATH = "/var/tmp/messaging-dump.json";
	

	    private static SenderBean toSenderBean(Sender sender) {
	        SenderBean bean = new SenderBean();
	        bean.setName(sender.getFromName());
	        bean.setAddress(sender.getFromAddress());
	        bean.setHtmlEnabled(false); // notes: default false, true only for very specific cases
	        //bean.setPolicy(MessageStoragePolicy.keep(500));
	        bean.setMembers(sender.getMembers().getExpression());
	        Set<ReplyTo> replytos = sender.getReplyTosSet();
	        if (!replytos.isEmpty()){
	            bean.setReplyTo(toString(sender.getReplyTosSet().iterator().next()));
	        }
	        bean.setRecipients(
	            sender.getRecipientsSet().stream().map(r -> r.getMembers().getExpression()).collect(Collectors.toSet()));
	        return bean;
	    }
	

	    private static JsonElement toJson(Sender sender) {
	        JsonObject object = new JsonObject();
	        if (sender instanceof DepartmentMemberBasedSender) {
	            object.add(TYPE.key, DEPARTMENT.json);
	            object.add(TARGET.key, toJson(((UnitBasedSender) sender).getUnit()));
	        } else if (sender instanceof UnitBasedSender) {
	            object.add(TYPE.key, UNIT.json);
	            object.add(TARGET.key, toJson(((UnitBasedSender) sender).getUnit()));
	        } else if (sender instanceof CoordinatorSender) {
	            object.add(TYPE.key, COORDINATOR.json);
	            object.add(TARGET.key, toJson(((CoordinatorSender) sender).getDegree()));
	        } else if (sender instanceof PersonSender) {
	            object.add(TYPE.key, PERSON.json);
	            object.add(TARGET.key, toJson(((PersonSender) sender).getPerson()));
	        }  else if (sender instanceof ExecutionCourseSender) {
	            object.add(TYPE.key, COURSE.json);
	            object.add(TARGET.key, toJson(((ExecutionCourseSender) sender).getCourse()));
	        } else if (sender instanceof DelegateSender) {
	            object.add(TYPE.key, DELEGATE.json);
	            object.add(TARGET.key, toJson(((DelegateSender) sender).getDelegate()));
	        }
	        return object;
	    }
	

	    private static JsonElement toJson(Message message) {
	        JsonObject object = new JsonObject();
	        object.addProperty("id", message.getExternalId());
	        object.add(SUBJECT.key, toJson(message.getSubject()));
	        object.add(BODY.key, toJson(message.getBody()));
	        object.add(HTML_BODY.key, toJson(message.getHtmlBody()));
	        object.add(CREATED.key, toJson(message.getCreated()));
	        object.add(CREATOR.key, toJson(message.getPerson()));
	        object.add(SENT.key, toJson(message.getSent()));
	        object.add(BCCS.key, toJson(new EmailAddressList(message.getBccs())));
	        object.add(TOS.key, toJson(message.getTosSet()));
	        object.add(CCS.key, toJson(message.getCcsSet()));
	        object.add(RECIPIENTS.key, toJson(message.getRecipientsSet()));
	        object.add(REPLY_TOS.key, toJson(message.getReplyTosSet()));
	        object.add(EMAILS.key, toJson(message.getEmailsSet()));
	        object.add(PENDING.key, toJson(message.getRootDomainObjectFromPendingRelation() != null));
	        return object;
	    }
	

	    private static JsonElement toJson(Email email) {
	        JsonObject object = new JsonObject();
	        object.add(BCCS.key, toJson(email.getBccAddresses()));
	        object.add(CCS.key, toJson(email.getCcAddresses()));
	        object.add(TOS.key, toJson(email.getToAddresses()));
	        object.add(REPLY_TOS.key, toJson(email.getReplyTos()));
	        object.add(CONFIRMED.key, toJson(email.getConfirmedAddresses()));
	        object.add(FAILED.key, toJson(email.getFailedAddresses()));
	        object.add(PENDING.key, toJson(email.getRootDomainObjectFromEmailQueue() != null));
	        return object;
	    }
	

	    private static JsonElement toJson(EmailAddressList list) {
	        final JsonArray array = new JsonArray();
	        if (list != null) {
	            list.toCollection().stream().map(ExportMessaging::toJson).filter(Objects::nonNull).forEach(array::add);
	        }
	        return array;
	    }
	

	    private static JsonElement toJson(Recipient recipient) {
	        JsonObject object = new JsonObject();
	        object.add(NAME.key, toJson(recipient.getToName()));
	        object.add(MEMBERS.key, toJson(recipient.getMembers()));
	        return object;
	    }
	

	    private static JsonElement toJson(ReplyTo replyTo) {
	        JsonObject object = new JsonObject();
	        if (replyTo instanceof CurrentUserReplyTo) {
	            object.add(TYPE.key, USER.json);
	        } else if (replyTo instanceof PersonReplyTo) {
	            object.add(TYPE.key, PERSON.json);
	            object.add(TARGET.key, toJson(((PersonReplyTo) replyTo).getPerson()));
	        } else if (replyTo instanceof ExecutionCourseReplyTo) {
	            if (!(replyTo.getSender() instanceof ExecutionCourseSender)) {
	                return null; //XXX just venting some paranoia, should not happen
	            }
	            object.add(TYPE.key, COURSE.json);
	            //XXX redundant but left it for ease of access and completeness
	            object.add(TARGET.key, toJson(((ExecutionCourseSender) replyTo.getSender()).getCourse()));
	        } else if (replyTo instanceof ConcreteReplyTo) {
	            object.add(TYPE.key, EMAIL.json);
	            object.add(TARGET.key, toJson(replyTo.getReplyToAddress()));
	        }
	        return object;
	    }
	

	    private static String toString(ReplyTo replyTo) {
	        if (replyTo instanceof PersonReplyTo) {
	            ((PersonReplyTo) replyTo).getPerson().getEmail();
	        } else if (replyTo instanceof ExecutionCourseReplyTo) {
	            return ((ExecutionCourseSender) replyTo.getSender()).getFromAddress();
	        } else if (replyTo instanceof ConcreteReplyTo) {
	            return replyTo.getReplyToAddress();
	        }
	        return null;
	    }
	

	    private static JsonElement toJson(Group group) {
	        try {
	            return toJson(group.getExpression());
	        } catch (NullPointerException npe) {//XXX some CustomGroup has null arguments...
	            return EMPTY;
	        }
	    }
	

	    private static <T extends DomainObject> JsonElement toJson(Set<T> senders) {
	        final JsonArray array = new JsonArray();
	        senders.stream().map(ExportMessaging::toJson).filter(Objects::nonNull).forEach(array::add);
	        return array;
	    }
	

	    private static JsonElement toJson(DomainObject object) {
	        if (object == null) {
	            return EMPTY;
	        }
	        if (object instanceof Sender) {
	            return toJson((Sender) object);
	        } else if (object instanceof Message) {
	            return toJson((Message) object);
	        } else if (object instanceof Group) {
	            return toJson((Group) object);
	        } else if (object instanceof Recipient) {
	            return toJson((Recipient) object);
	        } else if (object instanceof ReplyTo) {
	            return toJson((ReplyTo) object);
	        } else if (object instanceof Email) {
	            return toJson((Email) object);
	        } else if (object instanceof EmailAddressList) {
	            return toJson((EmailAddressList) object);
	        } else {
	            return toJson(object.getExternalId());
	        }
	    }
	

	    private static JsonElement toJson(DateTime dateTime) {
	        return new JsonPrimitive(dateTime == null ? "" : dateTime.toString());
	    }
	

	    private static JsonElement toJson(Boolean bool) {
	        return new JsonPrimitive(bool);
	    }
	

	    private static JsonElement toJson(String string) {
	        return new JsonPrimitive(Strings.nullToEmpty(string));
	    }
	

	    @Override
	    public TxMode getTxMode() {
	        return TxMode.READ;
	    }
	

	    @Override
	    public void runTask() throws Exception {
	        Set<Sender> senders = Bennu.getInstance().getUtilEmailSendersSet();
	        int size = senders.size(), count = 0, tenth = (int)Math.ceil((double)size/10); //XXX imprecise
	        taskLog("Total Senders: %d\n", size);
	        if (!senders.isEmpty()) {
	            Gson gson = new GsonBuilder().create();
	            boolean skip = true;
	            try (FileOutputStream baos = new FileOutputStream(PATH); Writer writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
	                taskLog("Converting...");
	                writer.append("[");
	                writer.append("\n");
	                for (Sender sender : senders) {
	                    SenderBean bean = toSenderBean(sender);
	                    JsonObject test = new JsonObject();
	                    test.add("bean", gson.toJsonTree(bean));
	                    test.add("relation", toJson(sender));
	                    test.add("messages", toJson(sender.getMessagesSet()));
	                    writer.append(gson.toJson(test));
	                    if (count < size - 1) {
	                        writer.append(",");
	                    }
	                    writer.append("\n");
	                    if (count % tenth == 0) {
	                        taskLog("%d%% ", (count / tenth) * 10);
	                        writer.flush();
	                    }
	                    count++;
	                }
	                writer.append("]");
	                taskLog("100%\nFlushing...");
	                writer.flush();
	                taskLog("file at %s%n", PATH);
	//                output(PATH, baos.toByteArray());
	            }
	        }
	        taskLog("Task Complete");
	    }
	

	    public enum Property {
	        SENDERS, MEMBERS, SYSTEM_SENDER, ADDRESS, NAME, REPLY_TOS, MESSAGES, RECIPIENTS, TYPE, TARGET, OPTED_OUT, SUBJECT, BODY, HTML_BODY, CREATED, CREATOR, SENT, BCCS, TOS, CCS, EMAILS, PENDING, CONFIRMED, FAILED;
	

	        public final String key;
	

	        Property() {
	            this.key = name().toLowerCase().replace("_", "-");
	        }
	    }
	

	    public enum Tag {
	        COURSE, GENERIC, PERSON, UNIT, DEPARTMENT, COORDINATOR, SYSTEM, USER, EMAIL, DELEGATE;
	

	        public final JsonElement json;
	

	        Tag() {
	            json = new JsonPrimitive(name().toLowerCase().replace("_", "-"));
	        }
	    }
	

	    public static final JsonElement EMPTY = new JsonPrimitive("");
	

	    public static class SenderBean {
	        protected static final String BUNDLE = "MessagingResources";
	

	        private Boolean htmlEnabled, allPolicy, nonePolicy;
	        private String name, address, members, replyTo, policy, periodPolicy = "";
	        private int amountPolicy = -1;
	        private Collection<String> recipients, errors;
	

	

	        public Boolean getHtmlEnabled() {
	            return htmlEnabled;
	        }
	

	        public String getName() {
	            return name;
	        }
	

	        public String getAddress() {
	            return address;
	        }
	

	        public String getMembers() {
	            return members;
	        }
	

	        public Set<String> getRecipients() {
	            return recipients == null ? Sets.newHashSet() : Sets.newHashSet(recipients);
	        }
	

	        public String getReplyTo() {
	            return replyTo;
	        }
	

	        public Collection<String> getErrors() {
	            return errors;
	        }
	

	        public void setHtmlEnabled(boolean htmlEnabled) {
	            this.htmlEnabled = htmlEnabled;
	        }
	

	        public void setName(String name) {
	            this.name = name;
	        }
	

	        public void setAddress(String address) {
	            this.address = address;
	        }
	

	        public void setMembers(String members) {
	            this.members = members;
	        }
	

	        public void setRecipients(Collection<String> recipients) {
	            this.recipients = recipients;
	        }
	

	        public void setReplyTo(String replyTo) {
	            this.replyTo = replyTo;
	        }
	

	        protected void setErrors(Collection<String> errors) {
	            this.errors = errors;
	        }
	    }
	}