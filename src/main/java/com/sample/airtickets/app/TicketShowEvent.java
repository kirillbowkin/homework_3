package com.sample.airtickets.app;

import com.sample.airtickets.entity.Ticket;
import org.springframework.context.ApplicationEvent;

public class TicketShowEvent extends ApplicationEvent {
    private Ticket ticket;
    public TicketShowEvent(Object source, Ticket ticket) {
        super(source);
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
