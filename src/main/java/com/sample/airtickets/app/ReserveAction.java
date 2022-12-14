package com.sample.airtickets.app;

import com.sample.airtickets.entity.Flight;
import com.sample.airtickets.entity.Ticket;
import io.jmix.core.Metadata;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.UiEventPublisher;
import io.jmix.ui.action.ActionType;
import io.jmix.ui.action.ItemTrackingAction;
import io.jmix.ui.app.inputdialog.DialogOutcome;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.InputDialogFacet;

@ActionType("reserve")
public class ReserveAction extends ItemTrackingAction {

    private UiEventPublisher uiEventPublisher;
    private ScreenBuilders screenBuilders;
    private InputDialogFacet inputDialog;
    private Metadata metadata;
    private TicketService ticketService;
    private Flight flight;
    public ReserveAction(UiEventPublisher uiEventPublisher, Flight flight, InputDialogFacet inputDialog, Metadata metadata, TicketService ticketService, ScreenBuilders screenBuilders) {
        this.uiEventPublisher = uiEventPublisher;
        this.flight = flight;
        this.inputDialog = inputDialog;
        this.metadata = metadata;
        this.ticketService = ticketService;
        this.screenBuilders = screenBuilders;
    }

    public ReserveAction(String id) {
        super(id);
    }

    @Override
    public void actionPerform(Component component) {
        inputDialog.setDialogResultHandler(inputDialogResult -> {
            if (inputDialogResult.closedWith(DialogOutcome.OK)) {
                Ticket ticket = metadata.create(Ticket.class);
                ticket.setFlight(this.flight);
                ticket.setReservationId(inputDialogResult.getValue("reservationId"));
                ticket.setPassportNumber(inputDialogResult.getValue("passportNumber"));
                ticket.setPassengerName(inputDialogResult.getValue("passengerName"));
                ticket.setTelephone(inputDialogResult.getValue("telephone"));

                ticketService.saveTicket(ticket);
                uiEventPublisher.publishEvent(new TicketShowEvent(this, ticket));
            }
        });

        inputDialog.show();

    }
}
