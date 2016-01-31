package com.semjournals.rest.resources;

import com.google.gson.Gson;
import com.semjournals.adapter.AccountAdapter;
import com.semjournals.adapter.JournalAdapter;
import com.semjournals.model.Account;
import com.semjournals.model.Journal;
import com.semjournals.rest.util.UploadUtil;
import com.semjournals.service.JournalService;
import com.semjournals.service.SubscriptionService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/journals/")
public class JournalResource extends AbstractResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listJournals() {

        List<JournalAdapter> journalAdapterList = new ArrayList<>();

        List<Journal> journalList;
        if (userUtil.isCurrentUserAdmin()) {
            journalList = new JournalService().list();
        } else {
            journalList = new JournalService().listActive();
        }

        journalList.forEach(journal -> {
            JournalAdapter journalAdapter = new JournalAdapter(journal);
            journalAdapter.setSubscribed(checkHasSubscriber(journal, userUtil.getCurrentAccount()));
            journalAdapterList.add(journalAdapter);
        });

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapterList));
    }

    @GET
    @Path("/{journalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJournalById(final @PathParam("journalId") String journalId) {
        JournalAdapter journalAdapter = new JournalAdapter(new JournalService().get(journalId));

        if (!journalAdapter.isActive() && !userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

    @POST
    @Path("/upload/{journalName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail,
                               final @PathParam("journalName") String journalName) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        String fileName = journalName + ".pdf";
        JournalService service = new JournalService();

        String uploadedFileLocation = "journals/" + fileName;

        Journal journal = new Journal(userUtil.getCurrentAccount(), journalName, true);
        JournalAdapter journalAdapter = new JournalAdapter(journal);

        try {
            new JournalAdapter(service.create(journalAdapter));
            new UploadUtil().writeToFile(uploadedInputStream, uploadedFileLocation);
        } catch (IllegalArgumentException e) {
            return status(Response.Status.CONFLICT);
        } catch (Exception e) {
            return status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return ok();
    }

    @PUT
    @Path("/{journalId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJournal(final @PathParam("journalId") String journalId, JournalAdapter updateJournalAdapter) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        JournalService service = new JournalService();

        updateJournalAdapter.setId(journalId);
        JournalAdapter journalAdapter;
        try {
            journalAdapter = new JournalAdapter(service.update(updateJournalAdapter));
        } catch (IllegalArgumentException e) {
            return status(Response.Status.CONFLICT);
        }

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

    @DELETE
    @Path("/{journalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJournal(final @PathParam("journalId") String journalId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        new JournalService().delete(journalId);

        return ok();
    }

    @PUT
    @Path("/{journalId}/active")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateJournal(final @PathParam("journalId") String journalId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        JournalService service = new JournalService();
        Journal journal = service.get(journalId);
        journal.setActive(true);

        journal = service.update(new JournalAdapter(journal));
        JournalAdapter journalAdapter = new JournalAdapter(journal);

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

    @DELETE
    @Path("/{journalId}/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateJournal(final @PathParam("journalId") String journalId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        JournalService service = new JournalService();
        Journal journal = service.get(journalId);
        journal.setActive(false);

        journal = service.update(new JournalAdapter(journal));
        JournalAdapter journalAdapter = new JournalAdapter(journal);

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

    @GET
    @Path("/{journalId}/subscribers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubscribers(final @PathParam("journalId") String journalId) {
        if (!userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        JournalService service = new JournalService();
        Journal journal = service.get(journalId);
        JournalAdapter journalAdapter = new JournalAdapter(journal);

        Set<AccountAdapter> subscriberSet = journal.getSubscribers().stream().map(AccountAdapter::new).collect(Collectors.toSet());
        journalAdapter.setSubscribers(subscriberSet);

        Gson gson = new Gson();
        return ok(gson.toJson(journalAdapter));
    }

    @PUT
    @Path("/{journalId}/subscribers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscribeJournal(final @PathParam("journalId") String journalId) {
        if (userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        JournalService service = new JournalService();
        Journal journal = service.get(journalId);

        new SubscriptionService().subscribe(userUtil.getCurrentAccount(), journal);

        return ok();
    }

    @DELETE
    @Path("/{journalId}/subscribers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unsubscribeJournal(final @PathParam("journalId") String journalId) {
        if (userUtil.isCurrentUserAdmin()) {
            return status(Response.Status.FORBIDDEN);
        }

        JournalService service = new JournalService();
        Journal journal = service.get(journalId);

        new SubscriptionService().unsubscribe(userUtil.getCurrentAccount(), journal);

        return ok();
    }

    private boolean checkHasSubscriber(Journal journal, Account account) {
        final boolean[] hasAccount = {false};
        journal.getSubscribers().forEach((subscriber) -> {
            if (subscriber.getId().equalsIgnoreCase(account.getId())) {
                hasAccount[0] = true;
            }
        });
        return hasAccount[0];
    }
}