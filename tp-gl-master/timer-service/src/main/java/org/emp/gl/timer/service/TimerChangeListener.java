package org.emp.gl.timer.service;

/**
 * Listener custom pour les changements de temps.
 */
public interface TimerChangeListener  {
    final static String DIXEME_DE_SECONDE_PROP = "dixieme";
    final static String SECONDE_PROP = "seconde";
    final static String MINUTE_PROP = "minute";
    final static String HEURE_PROP = "heure";

    /**
     * Méthode appelée par le provider quand une propriété change.
     *
     * @param prop le nom de la propriété (SECONDE_PROP, etc.)
     * @param oldValue ancienne valeur (peut être null)
     * @param newValue nouvelle valeur
     */
    void propertyChange(String prop, Object oldValue, Object newValue);
}
