# U05-Persistente-ToDo-Liste
Erweiterung von U04-Eine-einfache-ToDo-Liste um das Speichern der erstellten Tasks mit SQLite und der Room Library

## Aufgabe

In dieser Aufgabe erweitern Sie die einfache ToDo-Listen App aus U04. Bisher gingen die erstellten Tasks nach Beenden der App verloren. Dies sollen Sie nun ändern, indem Sie mittels der [Room Persistence Library](https://developer.android.com/training/data-storage/room) die Daten in einer SQLite Datenbank speichern. Beim Starten der App sollen alle bisher gespeicherten Tasks aus der Datenbank ausgelesen und auf dem Display angezeigt werden. Beim Erstellen und Updaten eines Tasks sollen diese Änderungen nicht nur entsprechend in der ListView angezeigt, sondern auch gleich noch in der Datenbank gespeichert werden.

### Vorgaben

Im Starterpaket finden Sie einen Lösungsansatz zur bisherigen ToDo-Listen-App aus U04. Um Ihnen die Implementierung der Datenbank etwas zu erleichtern, wurde der Startercode etwas angepasst und entspricht damit nicht dem exakten Lösungsvorschlag, der bereits zu U04 veröffentlicht wurde. Folgendes wurde bereits Implementiert:
- Die benötigten Dependencies zur Nutzung der Room Persistence Library wurden in die build.gradle Datei (Modulebene) eingefügt

   ```
   implementation "androidx.room:room-runtime:2.3.0"
   annotationProcessor "androidx.room:room-compiler:2.3.0"
   ```
- Ergänzung der TaskManager-Klasse, zur Abkapselung von Daten und UI. Eine Ausführliche Erklärung finden Sie in der entsprechenden [Stream-Aufzeichnung](https://www.youtube.com/watch?v=6m_b9NZGD6w), sowie in den Code-Kommentaren.
- Ergänzung der TaskAttributeTypeConverter-Klasse. Da Room keine komplexen Datentypen (wie Date, UUID oder TaskState) speichern kann, müssen derartige Datentypen beim Schreiben in die Datenbank zunächst in primitive, und beim Auslesen wieder in den entsprechend komplexen Datentyp konvertiert werden. Diese Klasse muss nicht verändert werden. Lediglich die Room-Datenbank muss über diese Klasse mithilfe einer entsprechenden Annotation informiert werden. Einen kurze, prägnante Erklärung von TypeConvertern ist [hier](https://developer.android.com/training/data-storage/room/referencing-data) zu finden. 

## Vorgehen
Die Room Persistence Library bietet eine Abstraktionsschicht für SQLite und ermöglicht Ihnen einen einfachen Datenbankzugriff. Room besteht aus den drei Hauptkomponenten:
- [Database](https://developer.android.com/reference/kotlin/androidx/room/Database): Der Hauptzugang zur Datenbank der App.
- [DAO](https://developer.android.com/training/data-storage/room/accessing-data) (Data Access Object): Ein Interface; beinhaltet Methoden, um auf die Datenbank zugreifen zu können.
- [Entity](https://developer.android.com/training/data-storage/room/defining-data): Entspricht einer Tabelle innerhalb unserer Datenbank.


Erstellen Sie zudem eine zusätzliche Klasse "TaskDatabaseHelper", welche noch einmal explizit zur Trennung von Datenbank(-zugriff) und Code fungiert. In dieser soll die Datenbank erstellt, sowie die Methoden zum Einfügen eines neuen Tasks und zum Auslesen aller in der Datenbank gespeicherten Tasks, implementiert werden.

1. Laden Sie sich das Starterpaket herunter und verschaffen Sie sich einen Überblick über den vorgegebenen Code. Starten Sie das Projekt im Emulator, um sicherzustellen, dass Sie mit einer funktionierenden Version der App in die Implementierung starten.
2. Mit der Task-Klasse haben Sie bereits die Entität gegeben, welche in der Datenbank gespeichert werden soll. Diese Klasse repräsentiert somit eine Tabelle und jedes Klassenattribut eine Spalte in unserer SQLite Datenbank. Einen Überblick, wie unsere Datenbank aussehen soll, bietet die Tabelle unten. Damit die Room Library auch versteht, dass es sich bei Task um eine Entität handelt, muss die Klasse entsprechend annotiert werden. Beachten Sie dabei auch, dass jede Entität einen sog. **Primärschlüssel** besitzt, also ein (oder auch mehrere) Attribut(e), welches jeden Datenpunkt unserer Datenbank eindeutig identifiziert.
    |id |description|createdAt|currentState|
    |:--|:----------|:--------|:-----------|
    |1  |lernen     |11.5.2021|OPEN        |
    |2  |einkaufen  |13.5.2021|CLOSED      |
    |3  |kochen     |15.5.2021|OPEN        |


3. Implementieren Sie das DAO. Das DAO muss ein Interface (bevorzugt) oder eine abstrakte Klasse sein. Das DAO soll Methoden enthalten, die abstrakten Zugriff auf die Datenbank erlauben. Ihr DAO soll dabei drei Methoden enthalten:
    * Eine Methode zum Auslesen aller in der Datenbank gespeicherten Tasks
    * Eine Methode zum Einfügen eines einzelnen Tasks in die Datenbank
    * Eine Methode zum Updaten eines Tasks, wenn sich dessen Status verändert hat
4. Erstellen Sie eine Klasse für die Room-Datenbank. Diese darf nicht instanziiert werden können (abstract) und muss von RoomDatabase erben. Vergessen Sie auch hier nicht, die Klasse dementsprechend zu annotieren. Durch eine abstrakte Getter-Methode macht die Datenbank Ihr DAO verfügbar. (Optional: Um zu vermeiden, dass mehrere Datenbank-Instanzen gleichzeitig aktiv sind, könnten sie das [Singleton-Pattern](https://en.wikipedia.org/wiki/Singleton_pattern) verwenden.)
5. Nachdem mit Room keine komplexen Objekte, wie Date oder TaskState (Strings ausgeschlossen!) gespeichert werden können, muss für derartige Daten ein [TypeConverter](https://developer.android.com/training/data-storage/room/referencing-data) eingesetzt werden. Im Startercode ist bereits ein passender TypeConverter für unsere Task-Entität zu finden. Damit Room über diesen Bescheid weiß, müssen Sie lediglich die Datenbank entsprechend annotieren.
6. Erstellen Sie nun eine Klasse "TaskDatabaseHelper", mit derer Hilfe Sie den kompletten Datenbank-Zugriff bündeln und verwalten können. In diesem Helper soll die Datenbank erstellt werden:

    ```
    AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppRoomDatabase.class, "database-name")
                          .allowMainThreadQueries()
                          .build();
    ```
   Außerdem soll die Helper-Klasse eine Methode zum Einfügen eines Tasks in die Datenbank, zum Updaten eines Tasks in der Datenbank, sowie zum Auslesen aller in der Datenbank      gespeicherten Tasks, enthalten. <br/>
   **Anmerkung:** Room unterstützt normalerweise keinen Zugriff über den Haupt-Thread, außer man ruft explizit allowMainThreadQueries() auf. Das wird allerdings NICHT empfohlen,    da sonst der UI-Thread blockiert werden könnte (Für diese Übungsaufgabe ist das noch ok, asynchrones Arbeiten sehen wir uns dann in den nächsten Übungsblättern erst an).

7. Integrieren Sie die Datenbank in ihre ToDo-App. Instanziieren Sie zunächst den TaskDatabaseHelper beim Erstellen des TaskManagers und binden Sie ihn an den passenden Stellen im Code ein. Achten Sie dabei darauf, dass nur der TaskManager Zugriff auf den TaskDatabaseHelper haben sollte! 
    * Beim Starten der App sollen alle bereits in der Datenbank gespeicherten Einträge ausgelesen und in der entsprechenden Task-ArrayList des TaskManagers gespeichert werden. Über die requestUpdate() Methode des TaskManagers können die Listener des TaskManagers explizit über diese initiale Veränderung informiert werden.
    * Beim Hinzufügen eines neuen Tasks soll dieser zunächst in der Datenbank gespeichert und dann in die entsprechende Datenstruktur geladen werden, sodass dieser mithilfe des CustomAdapters und der ListView auf dem Display angezeigt wird
    * Bei eine LongClick auf ein ListView-Element wird der Status des darunterliegenden Tasks verändert, was dementsprechend auch in der Datenbank upgedatet werden soll

## Mögliche Erweiterungen

### Löschen von Tasks
Es können aktuell zwar immer neue Tasks hinzugefügt und auch als abgeschlossen markiert werden, allerdings können bestehende Tasks nicht komplett entfernt werden. Erweitern Sie Ihren Code also dementsprechend, um einen Task sowohl von der ListView zu entfernen und ergänzen Sie eine entsprechende Datenbankabfrage, die das Löschen eines einzelnen Tasks aus der Datenbank erlaubt. Das Löschen könnte z.B. implementiert werden, indem in einem einzelnen ListView-Eintrag ein zusätliches Delete-Icon mit OnClickListener ergänzt wird.

### Filtern von Tasks
Um nur offene Tasks oder Tasks eines bestimmten Datums anzeigen zu lassen, können sie eine Art Filterfunktion implementieren. Im simpelsten Fall können unterschiedliche Buttons dazu genutzt werden, nur bestimmte Tasks anzeigen zu lassen. Dazu müssen entsprechende Datenbankabfragen im DAO, sowie entsprechende Methoden in Ihrem RoomDatabaseHelper ergänzt werden.

## Screenshots der Anwendung

|  Erstellte Tasks bleiben nun auch nach Beenden der App erhalten und werden automatisch beim nächsten Start in die ListView geladen   |
|:------:|
| ![Screenshots der finalen ToDo-App](./docs/screenshot_to_do_liste.png)   |
