package org.emp.gl.timer.service;

/**
 * TimerService : expose les getters et étend TimeChangeProvider.
 */
public interface TimerService extends TimeChangeProvider {
    int getMinutes();
    int getHeures();
    int getSecondes();
    int getDixiemeDeSeconde();
    void start();
    void stop();
}
