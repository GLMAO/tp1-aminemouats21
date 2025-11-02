package org.emp.gl.timer.service;

public interface TimeChangeProvider {
    void addTimeChangeListener(TimerChangeListener pl);
    void removeTimeChangeListener(TimerChangeListener pl);
}
