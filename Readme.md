# U05-Persistente-ToDo-Liste

## Aufgabe

In dieser Aufgabe erweitern Sie die einfache ToDo-App aus dem letzten Übungsblatt. Bisher gingen die erstellten _Tasks_ nach Beenden der App verloren. Dies sollen Sie nun ändern, indem Sie mittels der [Room Persistence Library](https://developer.android.com/training/data-storage/room) die Daten dauerhaft in einer SQLite-Datenbank speichern. Alle neuen Aufgaben werden nicht nur im `TaskManager` gespeichert, sondern auch in der Datenbank abgelegt. Auch die Änderungen am Zustand einer Aufgabe ("offen" vs. "geschlossen") werden in den Datenbankeinträgen festgehalten. Beim Starten der App werden alle gespeicherten Aufgaben aus der Datenbank ausgelesen. Ihre App arbeitet ab diesem Zeitpunkt mit der so wiederhergestellten Aufgabenliste.

## Vorgaben

Im Starterpaket finden Sie unseren Lösungsvorschlag zur _ToDo-Listen_-App aus dem [letzten Übungsblatt](https://android-regensburg.github.io/AssignmentViewer/index.html#Android-Regensburg/U04-ToDo-Liste). Die Anwendung kann gestartet und vollständig verwendet werden. Über die `TaskManager`-Klasse werden die Aufgaben intern verwaltet. Über einen _RecyclerView_ mit entsprechendem Adapter werden die Aufgaben im UI dargestellt. **Konzentrieren Sie sich auf die Umsetzung der Datenbank als zusätzliche Schicht innerhalb der App** und integrieren Sie diese in den `TaskManager`. Dazu müssen Sie ggf. auch Änderungen an der vorgegebenen `Task`-Klasse vornehmen.

### Wichtige Stellen im Code

Die `TaskManager-Klasse` entspricht im Wesentlichen dem, was wir im Stream [Stream-Aufzeichnung](https://www.youtube.com/watch?v=6m_b9NZGD6w) implementiert haben. Der Manager soll die zentrale Stelle zur Verwaltung der Aufgabenliste darstellen. Das UI erhält vom Manager Kopien der aktuellen Aufgabenliste und die Datenbank wird innerhalb des Managers verwendet, um Änderungen an der Aufgabenliste dauerhaft zu speichern. Wo notwendig, können Sie die Funktionaliät der Klasse anhand der Code-Kommentaren nachvollziehen.

Die benötigten Dependencies zur Nutzung der _Room Persistence Library_ wurden in die `build.gradle`-Datei eingefügt. Damit ist sichergestellt, dass Sie die aus den Vorlesungsfolien und Dokumentationen bekannten Klassen und Annotationen in Ihrem eigenen Code ohne Probleme verwenden können:
   
```
def room_version = "2.4.2"
implementation "androidx.room:room-runtime:$room_version"
annotationProcessor "androidx.room:room-compiler:$room_version"
```
Wir haben eine zusätzliche Klasse `TaskAttributeTypeConverter` bereitgestellt. Da _Room_ keine komplexen Datentypen (wie `Date`, `UUID` oder unsere Enum `TaskState`) speichern kann, müssen derartige Datentypen beim Schreiben in die Datenbank zunächst in primitive umgewandelt, und beim Auslesen wieder in den entsprechend komplexen Datentyp konvertiert werden. Diese Klasse muss nicht verändert werden. Lediglich die _Room_-Datenbank muss über diese Klasse mithilfe einer entsprechenden Annotation informiert werden. Eine kurze, prägnante Erklärung von _Type Convertern_ ist [hier](https://developer.android.com/training/data-storage/room/referencing-data) zu finden. 

### Hinweise zur Nutzung von Room

Die _Room Persistence Library_ bietet eine Abstraktionsschicht für SQLite und ermöglicht Ihnen einen einfachen Datenbankzugriff. Innerhalb Ihrer Anwendung implementieren Sie dazu drei wesentliche Komponenten:

- [Entity](https://developer.android.com/training/data-storage/room/defining-data): Die Objekte, die in der App verwendet und in der Datenbank gespeichert werden sollen
- [Database](https://developer.android.com/reference/kotlin/androidx/room/Database): Die Repräsentation der SQLite-Datenbank, in der die Einträge gespeichert werden sollen
- [DAO](https://developer.android.com/training/data-storage/room/accessing-data) (Data Access Object): Ein Interface, das den Zugriff auf die Einträge in der Datenbank ermöglicht

In der Regel erstellen Sie zudem eine zusätzliche Helper-Klasse, in unserem Fall `TaskDatabaseHelper`, welche noch einmal explizit zur Trennung von Datenbank(-zugriff) und Code fungiert. In dieser wird die Datenbank erstellt, sowie in unserem Fall die Methoden zum Einfügen eines neuen Tasks und zum Auslesen aller in der Datenbank gespeicherten Tasks, implementiert werden.

## Vorgehen

### Schritt 1: Vorbereitung

Laden Sie sich das Starterpaket herunter und verschaffen Sie sich einen Überblick über den vorgegebenen Code. Starten Sie das Projekt im Emulator, um sicherzustellen, dass Sie mit einer funktionierenden Version der App in die Implementierung starten.

**Zwischenziel:** -

### Schritt 2:`Task` annotieren

Mit der `Task`-Klasse haben Sie bereits die Entität gegeben, welche in der Datenbank gespeichert werden soll. Diese Klasse repräsentiert somit einen Eintrag in der entsprechenden Tabelle, jede Instanzvariable eine potenzielle Spalte in unserer Datenbank. Einen Überblick, wie unsere Datenbank aussehen soll, bietet die Tabelle unten. Damit die _Room Library_ auch versteht, dass es sich bei Task um eine Entität handelt, muss die Klasse und Ihre Bestandteile entsprechend annotiert werden. Beachten Sie dabei auch, dass jede Entität einen sog. **Primärschlüssel** besitzt, also ein (oder auch mehrere) Attribut(e), welches jeden Datenpunkt unserer Datenbank eindeutig identifiziert.

    |id |description|createdAt|currentState|
    |:--|:----------|:--------|:-----------|
    |1  |lernen     |11.5.2021|OPEN        |
    |2  |einkaufen  |13.5.2021|CLOSED      |
    |3  |kochen     |15.5.2021|OPEN        |

**Zwischenziel:** -

### Schritt 3: Die Schnittstelle zur Room-Datenbank

Implementieren Sie das DAO (_data access object_). Das DAO muss ein Interface (bevorzugt) oder eine abstrakte Klasse sein. Das DAO soll Methoden enthalten, die abstrakten Zugriff auf die Datenbank erlauben. Ihr DAO soll dabei drei Methoden enthalten:

- Eine Methode zum Auslesen aller in der Datenbank gespeicherten Tasks
- Eine Methode zum Einfügen eines einzelnen Tasks in die Datenbank
- Eine Methode zum Updaten eines Tasks, wenn sich dessen Status verändert hat

**Zwischenziel:**

### Schritt 4: Die Datenbank

Erstellen Sie eine Klasse für die Room-Datenbank. Diese darf nicht instanziiert werden können (abstract) und muss von RoomDatabase erben. Vergessen Sie auch hier nicht, die Klasse dementsprechend zu annotieren. Durch eine abstrakte Getter-Methode macht die Datenbank Ihr DAO verfügbar. 

**Zwischenziel:** -

### Schritt 5: TypeConverter nutzen

Nachdem mit Room keine komplexen Objekte, wie Date oder TaskState (Strings ausgeschlossen!) gespeichert werden können, muss für derartige Daten ein [TypeConverter](https://developer.android.com/training/data-storage/room/referencing-data) eingesetzt werden. Im Startercode ist bereits ein passender TypeConverter für unsere Task-Entität zu finden. Damit Room über diesen Bescheid weiß, müssen Sie lediglich die Datenbank entsprechend annotieren.

**Zwischenziel:** -

### Schritt 6: Die Schnittstelle zur App

Erstellen Sie nun eine Klasse `TaskDatabaseHelper`, mit derer Hilfe Sie den kompletten Datenbank-Zugriff bündeln und verwalten können. In diesem Helper soll die Datenbank erstellt werden. Die Klasse benötigt einen `Context`, um auf die eigentliche Datenbank zugreifen zu können. Geben Sie diesen als Parameter z.B. an den Konstruktor weiter.

```
AppDatabase db = Room.databaseBuilder(context, AppRoomDatabase.class, "database-name")
                          .allowMainThreadQueries()
                          .build();
```

Außerdem soll die Helper-Klasse eine Methode zum Einfügen eines Tasks in die Datenbank, zum Updaten eines Tasks in der Datenbank, sowie zum Auslesen aller in der Datenbank gespeicherten Tasks, enthalten.

**Anmerkung:** Room unterstützt normalerweise keinen Zugriff über den Haupt-Thread, außer man ruft explizit `allowMainThreadQueries()` auf. Grundsätzlich ist das keine gute Idee und in der Android-Dokumentation wird explizit von diesem Ansatz abgeraten. Für diese Übungsaufgabe ist das noch ok, asynchrones Arbeiten sehen wir uns dann in der nächsten Woche an und können die dann gewonnenen Kenntnisse auch zur Verbesserung unseres Umgangs mit Datenbanken nutzen.

**Zwischenziel:** -

### Schritt 7: Datenbank mit restlichem Code verbinden

Integrieren Sie die Datenbank in ihre ToDo-App. Instanziieren Sie zunächst den `TaskDatabaseHelper` beim Erstellen des _TaskManagers_ und binden Sie ihn an den passenden Stellen im Code ein. Achten Sie dabei darauf, dass nur der _TaskManager_ Zugriff auf den `TaskDatabaseHelper` haben sollte! Denken Sie an dieser Stelle daran, dass der Helper den Kontext der Anwendung kennen muss, um auf die Datenbank zuzugreifen. Überlegen Sie sich, wie Sie diese Information aus der _Activity_, über den _Manager_ bis an den _Helper_ weitergeben können.

- Beim Starten der App sollen alle bereits in der Datenbank gespeicherten Einträge ausgelesen und in der entsprechenden `ArrayList` des _TaskManager_ gespeichert werden. Über die vorbereitete `requestUpdate()` Methode des _TaskManagers_ können die Listener des _TaskManagers_ explizit über die aktuell in der Aufgabenliste gespeicherten Aufgaben informiert werden.
- Beim Hinzufügen eines neuen Tasks soll dieser zunächst in der Datenbank gespeichert und dann in die entsprechende Datenstruktur geladen werden, sodass dieser mithilfe des `ArrayAdapter` und des `ListView` auf dem Display angezeigt wird
- Bei eine _LongClick_ auf ein `ListView`-Element wird der Status des darunterliegenden Tasks verändert, was dementsprechend auch in der Datenbank upgedatet werden soll

**Zwischenziel:** -

## Mögliche Erweiterungen

### Löschen von Tasks
Es können aktuell zwar immer neue Tasks hinzugefügt und auch als abgeschlossen markiert werden, allerdings können bestehende Tasks nicht komplett entfernt werden. Erweitern Sie Ihren Code also dementsprechend, um einen Task sowohl von des `ListView` zu entfernen und ergänzen Sie eine entsprechende Datenbankabfrage, die das Löschen eines einzelnen Tasks aus der Datenbank erlaubt. Das Löschen könnte z.B. implementiert werden, indem in einem einzelnen `ListView`-Eintrag ein zusätliches _Delete_-Icon mit `OnClickListener` ergänzt wird.

### Filtern von Tasks
Um nur offene Tasks oder Tasks eines bestimmten Datums anzeigen zu lassen, können sie eine Art Filterfunktion implementieren. Im simpelsten Fall können unterschiedliche Buttons dazu genutzt werden, nur bestimmte Tasks anzeigen zu lassen. Dazu müssen entsprechende Datenbankabfragen im _DAO_, sowie entsprechende Methoden in Ihrem RoomDatabaseHelper ergänzt werden.
