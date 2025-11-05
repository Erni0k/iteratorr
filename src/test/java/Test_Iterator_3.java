import edu.io.TextSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Test_Iterator_3 {

    @Test
    void can_iterate_by_chars() {
        var txt = "abc 123";
        var textSource = new TextSource(txt);
        var i = 0;
        for (var c : textSource) {
            Assertions.assertEquals(c, txt.substring(i, ++i));
        }
    }

    @Test
    void can_iterate_by_words() {
        var txt = "abc 123";
        var it = new TextSource(txt).wordIterator();
        Assertions.assertEquals("abc", it.next());
        Assertions.assertEquals("123", it.next());
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    void cannot_iterate_over_empty_string() {
        var iters = new Iterator[] {
                new TextSource("").iterator(),
                new TextSource("").wordIterator()};
        for (var it : iters) {
            Assertions.assertFalse(it.hasNext());
            Assertions.assertThrows(
                    NoSuchElementException.class,
                    it::next);
        }
    }

}
