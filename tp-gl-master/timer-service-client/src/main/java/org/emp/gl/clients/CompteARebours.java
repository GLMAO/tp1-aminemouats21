package org.emp.gl.clients;

import org.emp.gl.clients.CounterUpdateListener;
import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Compte à rebours : décrémente à chaque seconde et se désinscrit à 0.
 * Optionnellement notifie un CounterUpdateListener (la GUI).
 */
public class CompteARebours implements TimerChangeListener {

    private final TimerService timerService;
    private final AtomicInteger value;
    private final String name;
    private final CounterUpdateListener uiListener; // peut être null

    // Constructeur original (sans GUI)
    public CompteARebours(String name, TimerService service, int start) {
        this(name, service, start, null);
    }

    // Nouveau constructeur : avec listener UI optionnel
    public CompteARebours(String name, TimerService service, int start, CounterUpdateListener uiListener) {
        this.name = name;
        this.timerService = service;
        this.value = new AtomicInteger(start);
        this.uiListener = uiListener;
        if (service != null) service.addTimeChangeListener(this);
        System.out.println("CompteARebours " + name + " initialisé à " + start);
        if (uiListener != null) {
            // notifier l'UI de la valeur initiale
            uiListener.updated(name, start);
        }
    }

    @Override
    public void propertyChange(String prop, Object oldValue, Object newValue) {
        if (TimerChangeListener.SECONDE_PROP.equals(prop)) {
            int v = value.decrementAndGet();
            System.out.println("[" + name + "] reste: " + v);
            if (uiListener != null) {
                // notifier l'UI (appel sera sur le thread du timer)
                uiListener.updated(name, v);
            }
            if (v <= 0) {
                System.out.println("[" + name + "] terminé et se désinscrit.");
                if (uiListener != null) uiListener.finished(name);
                timerService.removeTimeChangeListener(this);
            }
        }
    }
}
