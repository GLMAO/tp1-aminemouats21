package org.emp.gl.core.launcher;

import org.emp.gl.clients.CompteARebours;
import org.emp.gl.clients.Horloge;
import org.emp.gl.clients.HorlogeGUI;
import org.emp.gl.time.service.impl.DummyTimeServiceImpl;
import org.emp.gl.timer.service.TimerService;

import java.util.Random;

public class App {

    public static void main(String[] args) throws InterruptedException {
        TimerService timer = new DummyTimeServiceImpl();

        // (optionnel) horloge console
        new Horloge("H1", timer);

        // GUI — prend en charge l'heure et l'affichage des compteurs
        HorlogeGUI gui = new HorlogeGUI(timer);

        // un compte à rebours fixe
        new CompteARebours("C-fixe", timer, 20, gui);

        // 10 comptes aléatoires entre 10 et 20, en fournissant la GUI comme listener
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int start = 10 + rnd.nextInt(11); // 10..20
            new CompteARebours("C-" + (i + 1), timer, start, gui);
        }

        timer.start();

        // Laisser tourner ; la GUI reste ouverte. Ici on attend 60s puis on arrête
        Thread.sleep(60000);

        timer.stop();
        System.out.println("Timer stopped. Exiting.");

        // Fermer proprement la GUI
        gui.dispose();
    }
}
