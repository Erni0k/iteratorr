# Wzorzec Iterator
Przykład ilustrujący budowę i wykorzystanie wzorca _Iterator_.

**Przy okazji**:
- obsługa argumentów wiersza poleceń,
- korzystanie z interfejsów biblioteki standardowej Javy,
- zastosowanie wyrażeń regularnych,
- bliższe spojrzenie na budowanie aplikacji,
- generowanie uruchamialnych programów: narzędzia _jpackage_ i _GraalVM_.


## Zadanie
Zadanie polega na stworzeniu narzędzia służącego do iterowania po pliku tekstowym.
Jego działanie opiszę na przykładzie.
Program nazwałem `itx` (powiedzmy, że to skrót od _itertowanie po texście_ ;).
Chcę go używać następująco:

```
itx <tryb> [<nazwa_pliku>]
```
W ten sposób wskazuję, w jakim **trybie** iterowania chcę działać, i na jakim **pliku**.
Jak widać, nazwa pliku jest opcjonalna. Jeżeli jej nie podamy, program ma czytać ze standardowego wejścia.

Bazowo (poziom 3) dostępne są dwa tryby iterowania:
- `c` -- iterowanie po znakach,
- `w` -- iterowanie po słowach.

Działanie programu polega na wyświetlaniu w wierszach kolejnych elementów, po których iterujemy.
Na przykład dla pliku zawierającego tekst `abc 123` i iterowaniu po dowolnych znakach (tryb `c`)
dostaniemy na wyjściu:
```
a
b
c

1
2
3
```

W trybie `w` na wyjściu uzyskalibyśmy:
```
abc
123
```

**Uwaga**: ponieważ piszemy w Javie, po zbudowaniu powstanie plik JAR. Żeby go uruchomić, trzeba zaangażować Javę:
```
java -cp itx.jar <tryb> <nazwa_pliku> edu.io.Main
```
- Na pewno będzie wygodniej, jeśli nie trzeba podawać nazwy głównej klasy (w moim przypadku `edu.io.Main`).
  Trzeba odpowiednio skonfigurować budowanie. Żeby było jeszcze ładniej, można przygotować skrypt powłoki, który "opakuje" to wywołanie.
  Te zadania wpisałem do **poziomu 4**.
- Jeżeli zależy nam na bardziej "natywnym" charakterze programu, możemy wygenerować plik wykonywalny
  dla konkretnego systemu operacyjnego. Przyda się tu GraalVM. To zostawiam na **poziom 5**.


## Wymagania

**Poziom 3**
- przygotowanie logiki odczytującej argumenty (tryb, opcjonalna nazwa pliku),
- przygotowanie klasy `TextSource`, która jest źródłem tekstu, po którym iterujemy,
- zaimplementowanie iterowania po znakach,
- zaimplementowanie iterowania po słowach.

**Poziom 4**
- zaimplementowanie iterowania po zdaniach,
- zaimplementowanie iterowania po liczbach (tylko zapis dziesiętny),
- zaimplementowanie iterowania po własnym szablonie (regex),
- skorzystanie z narzędzia _jpackage_ i zbudowanie wersji z osadzonym JRE,
- przygotowanie skryptu powłoki opakowującego wywołanie narzędzia.

**Poziom 5**
- skorzystanie z GraalVM i zbudowanie wykonywalnego programu natywnego
  dla systemu operacyjnego (Windows/Mac/Linux),
- do _build.gradle.kts_ dodać zadanie `nativeImage` tworzące wykonywalny program.