package com.sample.airtickets.screen.ticketreservation;

import com.sample.airtickets.app.TicketService;
import com.sample.airtickets.entity.Airport;
import com.sample.airtickets.entity.Flight;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.DateField;
import io.jmix.ui.component.EntityComboBox;
import io.jmix.ui.component.ProgressBar;
import io.jmix.ui.executor.BackgroundTask;
import io.jmix.ui.executor.BackgroundTaskHandler;
import io.jmix.ui.executor.BackgroundWorker;
import io.jmix.ui.executor.TaskLifeCycle;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@UiController("TicketReservation")
@UiDescriptor("ticket-reservation.xml")
public class TicketReservation extends Screen {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private CollectionContainer<Flight> flightsDc;
    @Autowired
    private EntityComboBox<Airport> fromField;
    @Autowired
    private EntityComboBox<Airport> toField;
    @Autowired
    private DateField<LocalDate> departureDateField;
    @Autowired
    private BackgroundWorker backgroundWorker;
    @Autowired
    private Notifications notifications;
    @Autowired
    private ProgressBar searchProgress;


    @Subscribe("searchBtn")
    public void onSearchBtnClick(Button.ClickEvent event) {
        if(fromField.isEmpty() && toField.isEmpty() && departureDateField.isEmpty()) {
            notifications.create()
                    .withCaption("Please fill at least one filter field")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            return;
        }


        BackgroundTask<Void, List<Flight>> task = new SearchTask(this, fromField.getValue(), toField.getValue(), departureDateField.getValue());
        BackgroundTaskHandler<List<Flight>> taskHandler = backgroundWorker.handle(task);
        searchProgress.setVisible(true);
        taskHandler.execute();

    }

    private class SearchTask extends BackgroundTask<Void, List<Flight>> {

        private Airport from;
        private Airport to;
        private LocalDate departureDate;

        public SearchTask(Screen screen, Airport from, Airport to, LocalDate departureDate) {
            super(10, TimeUnit.MINUTES, screen);
            this.from = from;
            this.to = to;
            this.departureDate = departureDate;
        }

        @Override
        public List<Flight> run(TaskLifeCycle taskLifeCycle) throws Exception {
            return ticketService.searchFlights(fromField.getValue(), toField.getValue(), departureDateField.getValue());
        }

        @Override
        public void done(List<Flight> result) {
            flightsDc.setItems(result);
            searchProgress.setVisible(false);
        }
    }

}