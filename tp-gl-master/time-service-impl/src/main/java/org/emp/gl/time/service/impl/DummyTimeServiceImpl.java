package org.emp.gl.time.service.impl;

import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

/**
 * Implémentation du TimerService.
 * Envoie un événement "dixieme" toutes les 100ms et "seconde" toutes les 1000ms.
 */
public class DummyTimeServiceImpl implements TimerService {

    private final List<TimerChangeListener> listeners = new CopyOnWriteArrayList<>();
    private final Timer timer = new Timer("DummyTimeService-Timer", true);

    private volatile int heures;
    private volatile int minutes;
    private volatile int secondes;
    private volatile int dixieme;

    private volatile boolean running = false;

    @Override
    public void addTimeChangeListener(TimerChangeListener pl) {
        if (pl != null) listeners.add(pl);
    }

    @Override
    public void removeTimeChangeListener(TimerChangeListener pl) {
        listeners.remove(pl);
    }

    @Override
    public int getMinutes() { return minutes; }
    @Override
    public int getHeures() { return heures; }
    @Override
    public int getSecondes() { return secondes; }
    @Override
    public int getDixiemeDeSeconde() { return dixieme; }

    @Override
    public void start() {
        if (running) return;
        running = true;
        LocalTime now = LocalTime.now();
        heures = now.getHour();
        minutes = now.getMinute();
        secondes = now.getSecond();
        dixieme = 0;

        // schedule every 100ms
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // increment dixième
                int oldDix = dixieme;
                dixieme++;
                if (dixieme >= 10) {
                    dixieme = 0;
                    int oldSec = secondes;
                    secondes++;
                    if (secondes >= 60) {
                        secondes = 0;
                        int oldMin = minutes;
                        minutes++;
                        if (minutes >= 60) {
                            minutes = 0;
                            heures = (heures + 1) % 24;
                            firePropertyChange(TimerChangeListener.HEURE_PROP, null, heures);
                        }
                        firePropertyChange(TimerChangeListener.MINUTE_PROP, null, minutes);
                    }
                    firePropertyChange(TimerChangeListener.SECONDE_PROP, oldSec, secondes);
                }
                firePropertyChange(TimerChangeListener.DIXEME_DE_SECONDE_PROP, oldDix, dixieme);
            }
        }, 0, 100L);
    }

    @Override
    public void stop() {
        if (!running) return;
        running = false;
        timer.cancel();
        listeners.clear();
    }

    private void firePropertyChange(String prop, Object oldValue, Object newValue) {
        for (TimerChangeListener l : listeners) {
            try {
                l.propertyChange(prop, oldValue, newValue);
            } catch (Exception ex) {
                // On ne laisse pas une exception d'un listener casser le timer
                ex.printStackTrace();
            }
        }
    }
}
