/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so2;

import Transportes.Autocarro;
import Transportes.Aviao;
import Transportes.Comboio;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class SO2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Central central = new Central();

        central.Criar();
        LocalTime temporario = LocalTime.MAX;
        if (central.getTerminalFerroviario().getComboios().get(0).getChegadaPrevista().isBefore(temporario))
            temporario = central.getTerminalFerroviario().getComboios().get(0).getChegadaPrevista();
        if (central.getTerminalRodoviario().getAutocarros().get(0).getChegadaPrevista().isBefore(temporario))
            temporario = central.getTerminalRodoviario().getAutocarros().get(0).getChegadaPrevista();
        if (central.getAeroporto().getAvioes().get(0).getChegadaPrevista().isBefore(temporario))
            temporario = central.getAeroporto().getAvioes().get(0).getChegadaPrevista();

        central.startHoras(temporario.truncatedTo(ChronoUnit.HOURS));

        central.Horarios();

        ArrayList<Aviao> avioes = central.getAeroporto().getAvioes();
        ArrayList<Comboio> comboios = central.getTerminalFerroviario().getComboios();
        ArrayList<Autocarro> autocarros = central.getTerminalRodoviario().getAutocarros();

        Thread[] threadAvioes = new Thread[avioes.size()];
        Thread[] threadAutocarros = new Thread[autocarros.size()];
        Thread[] threadComboios = new Thread[comboios.size()];

        for (int i = 0; i < threadAvioes.length; i++) {
            threadAvioes[i] = new Thread(avioes.get(i), "Aviao " + avioes.get(i).getNumero());
        }

        for (int j = 0; j < threadComboios.length; j++) {
            threadComboios[j] = new Thread(comboios.get(j), "Comboio " + comboios.get(j).getNumero());
        }

        for (int k = 0; k < threadAutocarros.length; k++) {
            threadAutocarros[k] = new Thread(autocarros.get(k), "Autocarro " + autocarros.get(k).getNumero());
        }

        for (Thread thread1 : threadAvioes) {
            thread1.start();
        }

        for (Thread thread2 : threadAutocarros) {
            thread2.start();
        }

        for (Thread thread3 : threadComboios) {
            thread3.start();
        }

    }

}
