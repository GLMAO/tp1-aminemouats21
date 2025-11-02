package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface graphique Swing :
 * - affiche l'heure (HH:mm:ss) chaque seconde,
 * - affiche la liste des compte-à-rebours et leurs valeurs,
 * - met à jour en temps réel via CounterUpdateListener.
 */
public class HorlogeGUI implements TimerChangeListener, CounterUpdateListener {

    private TimerService timerService;
    private JFrame frame;
    private JLabel timeLabel;
    private DefaultListModel<String> listModel;
    private JList<String> list;
    // Map thread-safe pour stocker l'état courant des compteurs par nom
    private final Map<String, Integer> counters = new ConcurrentHashMap<>();
    private final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");

    public HorlogeGUI(TimerService timerService) {
        this.timerService = timerService;

        // Créer UI sur l'EDT
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Horloge & Comptes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout(8, 8));

            // Zone d'affichage de l'heure
            timeLabel = new JLabel("--:--:--", SwingConstants.CENTER);
            timeLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
            frame.add(timeLabel, BorderLayout.NORTH);

            // Liste des compte-à-rebours
            listModel = new DefaultListModel<>();
            list = new JList<>(listModel);
            list.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
            JScrollPane sp = new JScrollPane(list);
            sp.setBorder(BorderFactory.createTitledBorder("Compte(s) en cours"));
            frame.add(sp, BorderLayout.CENTER);

            frame.setSize(360, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // S'inscrire comme listener pour recevoir les ticks (pour l'heure)
        if (this.timerService != null) {
            this.timerService.addTimeChangeListener(this);
        } else {
            throw new IllegalArgumentException("TimerService ne peut pas être null pour la GUI");
        }
    }

    // TimerChangeListener : on actualise l'heure à chaque seconde
    @Override
    public void propertyChange(String prop, Object oldValue, Object newValue) {
        if (TimerChangeListener.SECONDE_PROP.equals(prop)) {
            final String time = String.format("%02d:%02d:%02d",
                    timerService.getHeures(),
                    timerService.getMinutes(),
                    timerService.getSecondes());
            // Mettre à jour Swing sur l'EDT
            SwingUtilities.invokeLater(() -> {
                if (timeLabel != null) timeLabel.setText(time);
            });
        }
    }

    // CounterUpdateListener : CompteARebours appelle updated / finished
    @Override
    public void updated(String name, int value) {
        counters.put(name, value);
        refreshListModel();
    }

    @Override
    public void finished(String name) {
        counters.remove(name);
        refreshListModel();
    }

    // Reconstruit le DefaultListModel à partir de la map
    private void refreshListModel() {
        SwingUtilities.invokeLater(() -> {
            if (listModel == null) return;
            listModel.clear();
            counters.forEach((k, v) -> listModel.addElement(k + " : " + v));
        });
    }

    // méthode utilitaire si l'on veut fermer proprement la GUI (optionnel)
    public void dispose() {
        SwingUtilities.invokeLater(() -> {
            if (frame != null) frame.dispose();
        });
        if (this.timerService != null) {
            this.timerService.removeTimeChangeListener(this);
        }
    }
}
