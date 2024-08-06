package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.presentation.modelDto.TransferenciaDto;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BanelcoService {

    public boolean transferirEntreBancos(TransferenciaDto transferenciaDto) {
        Random random = new Random();
        int numeroAleatorio = random.nextInt();
        return numeroAleatorio % 2 == 0;
    }
    
}
