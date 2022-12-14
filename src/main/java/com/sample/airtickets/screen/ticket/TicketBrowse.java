package com.sample.airtickets.screen.ticket;

import com.google.common.collect.ImmutableMap;
import io.jmix.core.LoadContext;
import io.jmix.ui.Notifications;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.ActionType;
import io.jmix.ui.action.ItemTrackingAction;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.LinkButton;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.navigation.UrlIdSerializer;
import io.jmix.ui.navigation.UrlRouting;
import io.jmix.ui.screen.*;
import com.sample.airtickets.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Ticket.browse")
@UiDescriptor("ticket-browse.xml")
@Route(value = "tickets/view")
public class TicketBrowse extends StandardLookup<Ticket> {

    @Autowired
    private UrlRouting urlRouting;

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        String id = UrlIdSerializer.serializeId(ticket.getId());
        urlRouting.replaceState(this, ImmutableMap.of("id", id));
    }

    private Ticket ticket;

    @Install(to = "ticketDl", target = Target.DATA_LOADER)
    private Ticket ticketDlLoadDelegate(LoadContext<Ticket> loadContext) {
        return ticket;
    }

}
