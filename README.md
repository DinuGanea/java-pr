WikiXtractor (3.0)
==============

1. Release-Notes
    1. Datenmodell
    2. Objekt Identifikation
    3. Objekt-Eigenschaften Extraktor
    4. Kommando / Task Scheduling
2. Autoren
3. Ausfuehrung
4. Logging
5. Bugs
6. Evaluation






Release-Notes
---------

### Datenmodell:

Das Datenmodell wurde um 3 neue Typen erweitert:
* Person
* City (Ort)
* Monument (Denkmal)

Diese Objekte stehen für Personen, Orte und Denkmäler welche auf Grundlage der importierten "Article"-Objekte
extrahiert werden und in der Datenbank repräsentiert.


### Objekt Identifikation:

Um die Objekte aus einem Artikel zu extrahieren, wird hauptsächlich die Information benutzt, die die Kategorien liefern.
Jedes Objekt wird von einigen Keywords identifiziert, auf denen in den Kategorien String gesucht werden.

Wenn ein dieser Keywords gematched wird, wird das Objekt entschprechend eingestuft:

* **Person:**
 "Person", "Mann" oder "Frau"
* **City:**
"Gemeinde", "Ort", "Stadt"
* **Monument:**
"Denkmal"


Monument-Objekte werden auch ohne matching von Keywords gefunden, indem auf der Seite nach dem Folgenden ````<span>````
Element gesucht wird.


    <span class="mw-headline" id="Denkmal">Denkmal</span>



### Objekt-Eigenschaften Extraktor:

Fast alle benötigte Eigenschafen eines Objektes werden durch einen trivialen Prozess gefunden. Manche Felder wie z.B.
Letzter Wohnort (Person-Objekt) haben keine streng definierte Merkale in den HTML-Dump. Die Suche muss also mithilfe von
Regex erfolgen, was aber ziemlich langsam ist, und garantiert immer nicht die Korrektheit des Ergebnisses. Eine andere
Möglichkeit wäre Entity Labeling (AI), dies ist aber einen aufendiger Prozess, der vom allem viele Ressources braucht.


* **Person**
Eine Wikipedia Seite definiert Metadata, die fasst nochmal die vorhandene Information über die entschprechende Person zusammen.
Die Metadata wird als eine Tabelle representiert, die nach der ID "#Vorlage_Personendaten" gefunden werden kann. Jede Zeile
in der Tabelle hat 2 Spalten (Eigenschaft und Wert).
* **City**
Änhlich wie für ein Person-Objekt, entählt eine Wiki-Seite eine Tabelle mit Information bezogene auf das City-Objekt.
Es wird also nach der Tabelle gesucht mit dem Selektor "table.hintergrundfarbe5.float-right tr" und dann jede Zeile
durchgelaufen. Wenn die erste Spalte in eine Zeile das Attribut "style" mit dem Wert "width: 50%;" definiert hat, ist die
Möglicherweise eine Eigenschaft für das City-Objekt.
* **Monument**
Für dieses Objekt, die einzige Möglichkeit Eigenschaften zu extrahieren ist mehrere Regex Matchings durchzuführen.
Jahr der Einweihung wird z.B. mit dem Pattern "Erbaut in den ([0-9]{4})er" gesucht, wobei der Wert für den Jahr, in die 1. Gruppe
des Matchings zu finden ist.




### Kommandos/Tasks Scheduling:

Jedes Kommando (bzw. Task) implementiert das Runnable Interface. Dies erlaubt es als ein separeter Prozess zu starten.
Dazu kennt jedes Kommando seine Abhängigkeiten (Kommandos die davor ausgeführt werden müssen), was einen
Scheduling Prozess ermöglicht. Alle Kommandos werden in eine Queue hinzugefügt, und ein nach einander gestartet.
Parallel laufen maximal 2 Prozesse. Die Queue wird durchgelaufen und für jeden Task wird gecheckt, ob die Abhängigkeiten
durch die Beendung von anderen Prozesse aufgelöst werden. Wenn es so ist, wird das Kommando dispatched.





AUTOREN
-------

##### Dinu Ganea
###### Objekt-Eigenschaften Extraktor:

        wikiXtractor.extractors.MonumentPropertiesExtractor
        wikiXtractor.extractors.CityPropertiesExtractor
        wikiXtractor.extractors.PersonPropertiesExtractor

###### Objekt-Eigenschaften Mapper:

        wikiXtractor.mapper.Mapper
        wikiXtractor.mapper.CityMapper
        wikiXtractor.mapper.PersonMapper
        wikiXtractor.mapper.MonumentMapper


###### Process Management (Multithreading Implementierung):

        wikiXtractor.worker.Worker
        wikiXtractor.scheduler.TaskScheduler



##### Ionut Urs
###### Services für Datenmodell Erweiterung:

        wikiXtractor.service.ContentEntityService
        wikiXtractor.service.CityService
        wikiXtractor.service.PersonService
        wikiXtractor.service.MonumentService



##### Sonia Rooshenas
###### Kommandos:

        wikiXtractor.api.cli.EntityBaseExtraction
        wikiXtractor.api.cli.ExecuteTasksCommand
        wikiXtractor.api.cli.CityExtractionCommand
        wikiXtractor.api.cli.PersonExtractionCommand
        wikiXtractor.api.cli.MonumentExtractionCommand


##### Sonia Rooshenas
###### Datenmodell Erweiterng:

        wikiXtractor.model.ContentEntity
        wikiXtractor.model.City
        wikiXtractor.model.Person
        wikiXtractor.model.Monument





ANWENDUNG
---------

Die JAR Datei wird ueber die Konsole ausgefuerht mit dem folgenden Befehl:

    java -Xmx4g -jar WikiXtractor-3.0.jar <command> <param_1> ... <param_n>


##### Mögliche Befehle:

* HTML Dump Datei in die Datenbank importieren (Dabei wird dei DB erstellt oder zurükgesetzt)
````<db_directory>```` - Pfad zur Datenbank
````<html_file>```` - Pfad zur HTML Dump Datei

        java -Xmx4g -jar WikiXtractor-3.0.jar createdb <db_directory> <html_file>

* Kategrie Verlinkungen zwischen Page Nodes erstellen
````<db_directory>```` - Pfad zur Datenbank

        java -Xmx4g -jar WikiXtractor-3.0.jar CategoryLinkExtraction <db_directory>


* Verweisungen zwischen Article Nodes erstellen
````<db_directory>```` - Pfad zur Datenbank

        java -Xmx4g -jar WikiXtractor-3.0.jar ArticleLinkExtraction <db_directory>


* Person, City und Monument Objekte aus Article Nodes extrahieren
````<db_directory>```` - Pfad zur Datenbank

        java -Xmx4g -jar WikiXtractor-3.0.jar EntityBaseExtraction <db_directory>


* Person-Eigenschaften extrahieren
````<db_directory>```` - Pfad zur Datenbank

        java -Xmx4g -jar WikiXtractor-3.0.jar PersonExtraction <db_directory>


* City-Eigenschaften extrahieren
````<db_directory>```` - Pfad zur Datenbank

        java -Xmx4g -jar WikiXtractor-3.0.jar CityExtraction <db_directory>


* Monument-Eigenschaften extrahieren
````<db_directory>```` - Pfad zur Datenbank

        java -Xmx4g -jar WikiXtractor-3.0.jar MonumentExtraction <db_directory>


* Information über ein Page-Objekt ausgeben
````<db_directory>```` - Pfad zur Datenbank

        java -Xmx4g -jar WikiXtractor-3.0.jar queryentity <db_directory>



* Eine Liste von Befehlen ausführen
    * Direkte Kagorien
    * Indirekte Kategorien
    * Referrer Aricles
    * Referral Articles
    * Extrahierte Objekte (Person, City, Monument) und deren Eigenschaften
    * Verweisungen auf die Extrahierte Objekte
````<db_directory>```` - Pfad zur Datenbank
````<article_name>```` - Artikle name


        java -Xmx4g -jar WikiXtractor-3.0.jar executetasks <db_directory> <article_name>


 Beispiele:

        java -Xmx4g -jar WikiXtractor-3.0.jar createdb /home/user/files/graphdb input.html
        java -Xmx4g -jar WikiXtractor-3.0.jar executetasks /home/user/files/graphdb tasks.txt
        java -Xmx4g -jar WikiXtractor-3.0.jar queryentity /home/user/files/graphdb "Shivaji"




LOGGING
-------

Exceptions und Meldungen ueber den Programmfortschritt werden sowohl in der Konsole als auch in die wikiXtractor/wikiXtractor.log
Datei geloggt. Der Log Level ist auf debug gesetzt.




BUGS
----

Es gibt noch keine bekannte Bugs!




Evaluation
----------

##### Person

        ______________________________________________________
       |               |                   |                  |
       |   Attribut    |  Absolut richtig  |    Genauigkeit   |
       |_______________|___________________|__________________|
       |  Vorname      |        20         |       1.0        |
       |  Nachname     |        20         |       1.0        |
       |  Geburtsname  |        20         |       1.0        |
       |  Geburtstag   |        18         |       0.9        |
       |  Todestag     |        19         |       0.95       |
       |  Geburtsort   |        14         |       0,7        |
       |_______________|___________________|__________________|



##### City

        ______________________________________________________
       |               |                   |                  |
       |   Attribut    |  Absolut richtig  |    Genauigkeit   |
       |_______________|___________________|__________________|
       |  Name         |        20         |       1.0        |
       |  Land         |        20         |       1.0        |
       |  Einwohnerzahl|        20         |       1.0        |
       |_______________|___________________|__________________|



##### Monument

        ______________________________________________________
       |               |                   |                  |
       |   Attribut    |  Absolut richtig  |    Genauigkeit   |
       |_______________|___________________|__________________|
       |  Name         |        20         |       1.0        |
       |  Ortschaft    |        12         |       0.6        |
       |Einweihung Jahr|        10         |       0.5        |
       |_______________|___________________|__________________|
