# U05-Persistente-ToDo-Liste
Erweiterung von U04-Eine-einfache-ToDo-Liste um das Speichern der erstellten Tasks mit SQLite und der Room Library

## Aufgabe

In dieser Aufgabe erweitern Sie die einfache ToDo-Listen App aus U04. Bisher gingen die erstellten Tasks nach Beenden der App verloren. Dies sollen Sie nun ändern, indem Sie mittels der `Room Persistence Library` die Daten in einer SQLite Datenbank speichern. Beim Starten der App sollen alle bisher gespeicherten Tasks aus der Datenbank ausgelesen und auf dem Display angezeigt werden. Beim Erstellen eines neuen Tasks soll dieser neben dem Anzeigen in der ListView nun zusätlich auch noch in der Datenbank gespeichert werden.

### Vorgaben

Gerne können Sie auf ihrer Implementierung der ToDo-Listen-App aus U04 weiter aufbauen. Gibt es allerdings noch Probleme in Ihrem Code oder besitzt Ihre App nicht die in U04 beschriebene Funktionsfähigkeit, empfehlen wir Ihnen, das von uns bereitgestellte Starterpaket zu nutzen (entspricht dem bereits veröffentlichten Lösungsvorschlag zu U04).
Sie sollten nun also eine App als Ausgangspunkt haben, mit der neue Tasks über einen Edittext und einen Add-Button erstellt und über einen eigenen ArrayAdapter in der ListView (oder auch RecyclerView) entsprechend angezeigt werden können. Jedes Task-Element wird durch eine Instanz der Task-Klasse abgebildet und besitzt neben einer eindeutigen ID einen kurzen Beschreibungstext, ein Erstellungsdatum und einen Status (offen vs. erledigt). Die Tasks werden sortiert in der ListView angezeigt (Sortierung nach Status und Erstellungsdatum) und über einen LongClick auf ein ListView-Element kann der Status des korrespondierenden Tasks geändert werden.

## Vorgehen
Die Room Persistence Library bietet eine Abstraktionsschicht für SQLite und ermöglicht Ihnen einen einfachen Datenbankzugriff. Room besteht aus den drei Hauptkomponenten
- Database: Der Hauptzugang zur Datenbank der App
- DAO (Data Access Object): Ein Interface; beinhaltet Methoden, um auf die Datenbank zugreifen zu können.
- Entity: Eine Tabelle innerhalb unserer Datenbank

1. Überprüfen Sie, ob Ihre App den bisherigen Anforderungen/ Vorgaben der ToDo-Liste aus U04 gerecht wird. Gegebenenfalls können Sie sich das Starterpaket herunterladen und Ihre App auf Basis dessen Basis weiterentwickeln.
2. Fügen Sie folgende Dependencies zur build.gradle Datei (auf Modulebene) hinzu, um die Room Persistence Library nutzen zu können:
implementation "androidx.room:room-runtime:2.3.0"
annotationProcessor "androidx.room:room-compiler:2.3.0"
3. Entity annotieren
 Type Converter erstellen (https://developer.android.com/training/data-storage/room/referencing-data)
4. DAO erstellen
5. Room Database erstellen
6. DatabaseHelper erstellen. Im Helper: erstellen einer Instanz der Datenbank (der Einfachheit halber mit allowMainThreadQueries); Methode zum einfügen eines neuen Tasks in die Datenbank; Methode zum auslesen aller Tasks
7. Integration des DBHelpers in die MainActivity


## Mögliche Erweiterungen

### Löschen von Tasks
Es können aktuell zwar immer neue Tasks hinzugefügt und auch als abgeschlossen markiert werden, allerdings können bestehende Tasks nicht komplett entfernt werden. Erweitern Sie Ihren Code also dementsprechend, um einen Task sowohl von der ListView zu entfernen und ergänzen Sie eine entsprechende Datenbankabfrage, die das Löschen eines einzelnen Tasks aus der Datenbank erlaubt. Das Löschen könnte z.B. implementiert werden, indem in einem einzelnen ListView-Eintrag ein zusätliches Delete-Icon mit OnClickListener ergänzt wird.

### Filtern von Tasks
Um nur offene Tasks oder Tasks eines bestimmten Datums anzeigen zu lassen, könnten sie eine Filterfunktion einbauen

## Screenshots der Anwendung

|  Einfache Lösung   |   Lösung mit CardViews für die Listenelemente    |
|:------:|:-------:|
| ![Screenshots der ToDo-App](./docs/screenshot_possible_results_simple.png)   | ![Screenshots der ToDo-App](./docs/screenshot_possible_result.png)  |
