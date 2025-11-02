package org.emp.gl.clients;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

/**
 * Horloge : s'inscrit au TimerService et affiche l'heure (HH:mm:ss).
 */
public class Horloge implements TimerChangeListener {

    private final String name;
    private final TimerService timerService;
    private final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");

    public Horloge(String name, TimerService timerService) {
        this.name = name;
        this.timerService = timerService;
        if (this.timerService != null) {
            this.timerService.addTimeChangeListener(this);
        }
        System.out.println("Horloge " + name + " initialized and subscribed.");
    }

    public void afficherHeure() {
        if (timerService != null) {
            String s = String.format("%02d:%02d:%02d",
                    timerService.getHeures(),
                    timerService.getMinutes(),
                    timerService.getSecondes());
            System.out.println(name + " affiche " + s);
        }
    }

    @Override
    public void propertyChange(String prop, Object oldValue, Object newValue) {
        // On affiche l'heure à chaque seconde (SECONDE_PROP)
        if (TimerChangeListener.SECONDE_PROP.equals(prop)) {
            afficherHeure();
        }
    }

    // utilitaire si on veut se désinscrire
    public void stop() {
        if (timerService != null) timerService.removeTimeChangeListener(this);
    }
}
