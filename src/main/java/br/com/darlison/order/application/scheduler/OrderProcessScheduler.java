package br.com.darlison.order.application.scheduler;

import br.com.darlison.order.domain.port.usecase.ProcessOrderUseCase;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderProcessScheduler {

    private final ProcessOrderUseCase useCase;

    @Scheduled(cron = "0 */1 * * * *") // a cada 1 minuto
    public void processPendingOrders() {
        System.out.println("Start pending order process.");

        useCase.processPendingOrders();

        System.out.println("Finish pending order process.");
    }

}
