package org.emp.gl.clients;

/**
 * Interface utilisée par CompteARebours pour notifier la GUI.
 */
public interface CounterUpdateListener {
    void updated(String name, int value);
    void finished(String name);
}
