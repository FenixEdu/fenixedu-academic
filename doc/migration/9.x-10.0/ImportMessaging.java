package org.fenixedu.academic.task;
	

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.stream.StreamUtils;
import org.fenixedu.messaging.core.domain.*;
import org.fenixedu.messaging.core.ui.SenderBean;
import org.fenixedu.messaging.emaildispatch.domain.LocalEmailMessageDispatchReport;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import pt.ist.fenixedu.delegates.domain.student.Delegate;
import pt.ist.fenixedu.delegates.domain.util.email.DelegateSender;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.backend.jvstmojb.pstm.OneBoxDomainObject;


import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static pt.ist.fenixframework.FenixFramework.atomic;


public class ImportMessaging extends CustomTask {


    public static Map<Sender,JSONArray> senderMessagesMap = new HashMap<>();
    public static Map<String,String> oldMessageIdNewMessageId = new HashMap<>();
    
    @Override public Atomic.TxMode getTxMode() {
        return Atomic.TxMode.READ;
    }


    @Override public void runTask() throws Exception {


        JSONParser parser = new JSONParser();


        Object obj = parser.parse(new FileReader("/var/tmp/messaging-dump.json"));
        JSONArray jsonArray = (JSONArray) obj;


        ArrayList alljsons = new ArrayList<>(jsonArray);
        List<List<JSONObject>> partition = Lists.partition(alljsons, 2000);
        int chunk = 1;
        int totalChunks = partition.size();
        for (List<JSONObject> jsonObjects : partition) {
            taskLog("Start processing senders chunk %d of %d: size = %d\n", chunk, totalChunks, jsonObjects.size());
            atomic(() -> {
                for (JSONObject jsonObject : jsonObjects) {
                    dothings(jsonObject);
                }
            });
            taskLog("Finished processing senders chunk %d of %d\n", chunk++, totalChunks);
        }
        for (Map.Entry<Sender, JSONArray> senderJSONArrayEntry : senderMessagesMap.entrySet()) {
            atomic(() -> {
                Sender sender = senderJSONArrayEntry.getKey();
                JSONArray messages = senderJSONArrayEntry.getValue();
                messages.forEach(o -> importMessage(sender, (JSONObject) o));
            });
        }


        output("externalIdMapping.json", oldMessageIdNewMessageId.entrySet().stream().map(e -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(e.getKey(), e.getValue());
            return jsonObject;
        }).collect(StreamUtils.toJsonArray()).toString().getBytes(StandardCharsets.UTF_8));
        
        taskLog("Task completed!");
    }


    private void reconfigureSender(Sender sender, SenderBean bean){
        bean.setPolicy(MessageStoragePolicy.keep(500));
        Collection<String> errors = bean.validate();
        if (errors.isEmpty()) {
            atomic(() -> {
                sender.setName(bean.getName());
                sender.setAddress(bean.getAddress());
                sender.setPolicy(MessageStoragePolicy.keepAll());
                sender.setMembers(Group.parse(bean.getMembers()));
                sender.setAttachmentsEnabled(bean.getAttachmentsEnabled());
                sender.setHtmlEnabled(bean.getHtmlEnabled());
                sender.setReplyTo(bean.getReplyTo());
                sender.setRecipients(bean.getRecipients().stream().map(Group::parse).collect(Collectors.toSet()));
                sender.setOptInRequired(bean.getOptInRequired());
            });
        }
        else {
            taskLog("Errors in System Sender reconfiguration: %s%n", errors);
        }
    }


    private Sender createSender(SenderBean bean) {
        Sender sender = null;
        bean.setPolicy(MessageStoragePolicy.keep(500));
        if (bean.validate().isEmpty()) {
            Stream<Group> recipients = bean.getRecipients().stream().map(Group::parse);
            sender = Sender
                    .from(bean.getAddress())
                    .as(bean.getName())
                    .members(Group.parse(bean.getMembers()))
                    .storagePolicy(MessageStoragePolicy.internalize(bean.getPolicy()))
                    .htmlEnabled(bean.getHtmlEnabled())
                    .replyTo(bean.getReplyTo())
                    .recipients(recipients)
                    .build();
        }
        return sender;
    }
    public void dothings(JSONObject object) {
        JSONObject bean = (JSONObject) object.get("bean");
        JSONObject relation = (JSONObject) object.get("relation");
        JSONArray messagesArray = (JSONArray) object.get("messages");
        final Iterator<JSONObject> messageIterator = messagesArray.iterator();


        //taskLog("Name: " + bean.get("name").toString());
        Gson gson = new Gson();
        SenderBean senderbean = gson.fromJson(bean.toString(), SenderBean.class);
        senderbean.setAttachmentsEnabled(false);
        senderbean.setOptInRequired(false);
        if (bean.get("name").toString().equals("Técnico Lisboa - Sistema Fénix")) {
            Sender sender = MessagingSystem.systemSender();
            reconfigureSender(sender,senderbean);
            while (messageIterator.hasNext()) {
                importMessage(sender, messageIterator.next());
            }
        } else if (!relation.isEmpty() && relation.get("type").toString().equals("delegate")) {
            String target = ((JSONArray) relation.get("target")).iterator().next().toString();
            //taskLog("Delegate: " + target);
            Delegate delegate = FenixFramework.getDomainObject(target);
            if (delegate.getUser() != null){
                DelegateSender delegateSender = new DelegateSender();
                reconfigureSender(delegateSender,senderbean);
                delegate.setSender(delegateSender);


                while (messageIterator.hasNext()) {
                    importMessage(delegateSender, messageIterator.next());
                }
            }
            else {
                taskLog("Invalid delegate: null User");
            }
        } else {
            Sender newsender = createSender(senderbean);
            if (!relation.isEmpty()) {
                String key = relation.get("type").toString();
                String target = relation.get("target").toString();
                switch (key) {
                case "coordinator":
                    //taskLog("Degree: " + target);
                    Degree degree = FenixFramework.getDomainObject(target);
                    newsender.setDegree(degree);
                    break;
                case "person":
                    //taskLog("Person: " + target);
                    Person person = FenixFramework.getDomainObject(target);
                    newsender.setPerson(person);
                    break;
                case "unit":
                    //taskLog("Unit: " + target);
                    Unit unit = FenixFramework.getDomainObject(target);
                    newsender.setUnit(unit);
                    break;
                case "course":
                    //taskLog("Course: " + target);
                    ExecutionCourse course = FenixFramework.getDomainObject(target);
                    newsender.setCourse(course);
                    break;
                }
            }
            while (messageIterator.hasNext()) {
                importMessage(newsender, messageIterator.next());
            }
        }
    }


    private void importMessage(Sender sender, JSONObject messageJson) {
        Message.MessageBuilder builder = Message.from(sender)
                .subject(messageJson.get("subject").toString())
                .textBody(messageJson.get("body").toString())
                .htmlBody(messageJson.get("html-body").toString())
                .singleBcc(extractEmailList((JSONArray) messageJson.get("bccs")))
                .to(extractGroupList((JSONArray)messageJson.get("tos")))
                .cc(extractGroupList((JSONArray)messageJson.get("ccs")))
                .bcc(extractGroupList((JSONArray)messageJson.get("recipients")));


        if (messageJson.get("replyTo") != null){
            builder.replyTo(messageJson.get("replyTo").toString());
        }


        try {


            Message importedMessage = builder.unwrapped().send();


            oldMessageIdNewMessageId.put(messageJson.get("id").toString(), importedMessage.getExternalId());
            //oldMessageIdNewMessageId.put(importedMessage.getExternalId(), importedMessage.getExternalId());


            DateTime created = DateTime.parse(messageJson.get("created").toString());
            setCreated(importedMessage, created);


//        importedMessage.setMessagingSystemFromPendingDispatch(null);
            Message_Base.getRelationMessagingSystemMessagePending().add(null, importedMessage);


            String sent = messageJson.get("sent").toString();


            if (!Strings.isNullOrEmpty(sent)) {
                LocalEmailMessageDispatchReport report = new LocalEmailMessageDispatchReport(Collections.emptyList(),1,0);
                int confirmed = jsonArraySize(messageJson, "confirmed", 1);
                report.setDeliveredCount(confirmed);
                report.setTotalCount(confirmed);
                report.setFailedCount(jsonArraySize(messageJson, "failed", 0));
                report.setFinishedDelivery(DateTime.parse(sent));
                setStartedDelivery(report, created);


                //importedMessage.setDispatchReport(report);
                Message_Base.getRelationMessageDispatchReports().add(importedMessage,report);
            }
        }catch (NullPointerException npe) {
            taskLog("npe %s %s%n", messageJson.get("id").toString(), ExceptionUtils.getFullStackTrace(npe));
        }
    }


    private int jsonArraySize(JSONObject obj, String field, int defaultValue) {
        JSONArray arr = (JSONArray) obj.get(field);
        if (arr == null) {
            return defaultValue;
        }
        return arr.size();
    }


    private void setStartedDelivery(LocalEmailMessageDispatchReport report, DateTime created) {
        try {
            Method getObjState = OneBoxDomainObject.class.getDeclaredMethod("get$obj$state", boolean.class);
            getObjState.setAccessible(true);
            Object doState = getObjState.invoke(report, true);
            Class doStateClass = Class.forName("org.fenixedu.messaging.core.domain.MessageDispatchReport_Base$DO_State");
            Field beginDateField = doStateClass.getDeclaredField("startedDelivery");
            beginDateField.setAccessible(true);
            beginDateField.set(doState, created);
        }catch (Throwable t) {
            throw new Error(t);
        }
    }


    private void setCreated(Message message, DateTime endDate)  {
        try {
            Method getObjState = OneBoxDomainObject.class.getDeclaredMethod("get$obj$state", boolean.class);
            getObjState.setAccessible(true);
            Object doState = getObjState.invoke(message, true);
            Class doStateClass = Class.forName("org.fenixedu.messaging.core.domain.Message_Base$DO_State");
            Field beginDateField = doStateClass.getDeclaredField("created");
            beginDateField.setAccessible(true);
            beginDateField.set(doState, endDate);
        }catch (Throwable t) {
            throw new Error(t);
        }
    }


    private Collection<Group> extractGroupList(JSONArray stuffArray) {
        final Iterator<JSONObject> stuffIterator = stuffArray.iterator();
        final Collection<Group> stuffList = new ArrayList<>();
        while (stuffIterator.hasNext()) {
            stuffList.add(Group.parse(stuffIterator.next().get("members").toString()));
        }
        return stuffList;
    }


    private Collection<String> extractEmailList(JSONArray stuffArray) {
        final Iterator<String> stuffIterator = stuffArray.iterator();
        final Collection<String> stuffList = new ArrayList<>();
        while (stuffIterator.hasNext()) {
            String email = stuffIterator.next();
            stuffList.add(email);
        }
        return stuffList;
    }
}