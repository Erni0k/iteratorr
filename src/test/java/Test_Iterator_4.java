import edu.io.TextSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Test_Iterator_4 {

    @Test
    void can_iterate_by_sentences() {
        var txt = "To jest pierwsze zdanie. A to drugie! Gdzie jest trzecie?";
        var it = new TextSource(txt).sentenceIterator();
        Assertions.assertEquals("To jest pierwsze zdanie.", it.next());
        Assertions.assertEquals("A to drugie!", it.next());
        Assertions.assertEquals("Gdzie jest trzecie?", it.next());
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    void can_iterate_by_numbers() {
        var txt = "W tym tekście można znaleźć 3 liczby: 13, 3.14 i sto.";
        var it = new TextSource(txt).numberIterator();
        Assertions.assertEquals("3", it.next());
        Assertions.assertEquals("13", it.next());
        Assertions.assertEquals("3.14", it.next());
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    void can_iterate_by_regex() {
        var txt = "Ala ma kota, a kot ma Alę";
        var it = new TextSource(txt).regexIterator("kot(a)?");
        Assertions.assertEquals("kota", it.next());
        Assertions.assertEquals("kot", it.next());
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    void cannot_iterate_over_empty_string() {
        var iters = new Iterator[] {
                new TextSource("").sentenceIterator(),
                new TextSource("").numberIterator(),
                new TextSource("").regexIterator()};
        for (var it : iters) {
            Assertions.assertFalse(it.hasNext());
            Assertions.assertThrows(
                    NoSuchElementException.class,
                    it::next);
        }
    }

}