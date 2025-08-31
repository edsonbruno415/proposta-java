package com.edson.proposta_app.agendador;

import com.edson.proposta_app.entity.Proposta;
import com.edson.proposta_app.repository.PropostaRepository;
import com.edson.proposta_app.service.NotificacaoRabbitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class PropostaSemIntegracao {

    private PropostaRepository propostaRepository;

    private NotificacaoRabbitService notificacaoRabbitService;

    private String exchange;

    public PropostaSemIntegracao(@Value("${rabbitmq.propostapendente.exchange}") String exchange,
                                 NotificacaoRabbitService notificacaoRabbitService,
                                 PropostaRepository propostaRepository
    ) {
        this.exchange = exchange;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.propostaRepository = propostaRepository;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void buscarPropostasSemIntegracao(){
        propostaRepository.findAllByIntegradaIsFalse().forEach(proposta -> {
            try{
                notificacaoRabbitService.notificar(proposta, exchange);
                atualizarProposta(proposta);
            }catch (RuntimeException ex) {
                System.out.println("Exception at buscarPropostasSemIntegracao: " + ex.getMessage());
                //logger.error(ex.getMessage());
            }
        });
    }

    public void atualizarProposta(Proposta proposta){
        proposta.setIntegrada(true);
        propostaRepository.save(proposta);
    }
}
