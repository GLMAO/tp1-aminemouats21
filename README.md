[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/t19xNtmg)




IASD :: 27


//explication bref des modifications:
1-Ajout / clarification des interfaces

TimerService, TimeChangeProvider, TimerChangeListener : contrat clair pour le service de temps et les listeners (constantes pour SECONDE_PROP, DIXEME..., getters, start()/stop()).

2-Implémentation robuste du timerservice

DummyTimeServiceImpl : tick toutes les 100 ms, émission de DIXEME_DE_SECONDE_PROP et SECONDE_PROP, gestion minutes/heures.

Utilisation de CopyOnWriteArrayList pour stocker les listeners (évite ConcurrentModificationException).

volatile sur les champs temps + try/catch autour de chaque appel listener pour isoler les erreurs.

3-Clients / Observers

Horloge : implémente TimerChangeListener, s’inscrit au service et affiche l’heure chaque seconde via les getters du service.

CompteARebours : implémente TimerChangeListener, décrémente à chaque SECONDE_PROP, se désinscrit automatiquement quand il atteint 0. Ajout d’un constructeur optionnel pour notifier la GUI.

4-Interface GUI

CounterUpdateListener : nouvelle interface pour que les compteurs notifient la GUI.

HorlogeGUI : Swing UI affichant l’heure et la liste des compte-à-rebours ; mises à jour faites sur l’EDT (SwingUtilities.invokeLater) et stockage thread-safe (ConcurrentHashMap).

5-Main / orchestration

App.main : crée le DummyTimeServiceImpl, instancie Horloge, la HorlogeGUI, 10 CompteARebours (valeurs aléatoires), lance timer.start() puis arrête proprement.