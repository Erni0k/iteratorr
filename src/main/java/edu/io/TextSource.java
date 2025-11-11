package edu.io;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextSource implements AutoCloseable, Iterable<String> {
    private final Reader reader;
    private final boolean ownsReader;

    public TextSource(String text) {
        this.reader = new StringReader(text == null ? "" : text);
        this.ownsReader = false;
    }

    private TextSource(Reader reader, boolean ownsReader) {
        this.reader = reader;
        this.ownsReader = ownsReader;
    }

    public TextSource(Reader reader) {
        this(reader, false);
    }

    public static TextSource fromFile(String filename) throws FileNotFoundException {
        return new TextSource(new BufferedReader(new FileReader(filename)), true);
    }

    public static TextSource fromStdin() {
        return new TextSource(new BufferedReader(new InputStreamReader(System.in)), false);
    }

    @Override
    public void close() {
        if (ownsReader) {
            try {
                reader.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new CharacterIterator(reader);
    }

    public Iterator<String> wordIterator() {
        return new WordIterator(reader);
    }

    public Iterator<String> sentenceIterator() { return new SentenceIterator(reader); }
    public Iterator<String> numberIterator() { return new NumberIterator(reader); }
    public Iterator<String> regexIterator(String pattern) { return new RegexIterator(reader, pattern); }
    public Iterator<String> regexIterator() { return new RegexIterator(reader, ".+"); }

    private static class CharacterIterator implements Iterator<String> {
        private final Reader r;
        private int next = -2; // -2 means unread

        CharacterIterator(Reader r) { this.r = r; }

        private void ensure() {
            if (next == -2) {
                try { next = r.read(); } catch (IOException e) { next = -1; }
            }
        }

        @Override
        public boolean hasNext() { ensure(); return next != -1; }

        @Override
        public String next() {
            ensure();
            if (next == -1) throw new NoSuchElementException();
            char c = (char) next;
            next = -2;
            return String.valueOf(c);
        }
    }

    private static class WordIterator implements Iterator<String> {
        private final Scanner scanner;

        WordIterator(Reader r) { this.scanner = new Scanner(r); }

        @Override
        public boolean hasNext() { return scanner.hasNext(); }

        @Override
        public String next() { if (!hasNext()) throw new NoSuchElementException(); return scanner.next(); }
    }

    private static class SentenceIterator implements Iterator<String> {
        private final String content;
        private final Matcher matcher;
        private String nextSentence;

        private static final Pattern SENTENCE_PATTERN = Pattern.compile("([^.!?]*[.!?])");

        SentenceIterator(Reader r) {
            this.content = readAll(r);
            this.matcher = SENTENCE_PATTERN.matcher(this.content);
        }

        private static String readAll(Reader r) {
            try {
                StringBuilder sb = new StringBuilder();
                char[] buf = new char[4096];
                int n;
                while ((n = r.read(buf)) != -1) sb.append(buf, 0, n);
                return sb.toString();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private void fillNext() {
            if (nextSentence != null) return;
            while (matcher.find()) {
                String s = matcher.group(1).trim();
                if (!s.isEmpty()) { nextSentence = s; return; }
            }
        }

        @Override
        public boolean hasNext() { fillNext(); return nextSentence != null; }

        @Override
        public String next() {
            fillNext();
            if (nextSentence == null) throw new NoSuchElementException();
            String s = nextSentence;
            nextSentence = null;
            return s;
        }
    }

    private static class NumberIterator implements Iterator<String> {
        private final String content;
        private final Matcher matcher;
        private String nextNumber;
        private static final Pattern NUMBER_PATTERN = Pattern.compile("[+-]?\\d+(?:\\.\\d+)?");

        NumberIterator(Reader r) {
            this.content = readAll(r);
            this.matcher = NUMBER_PATTERN.matcher(this.content);
        }

        private static String readAll(Reader r) {
            try {
                StringBuilder sb = new StringBuilder();
                char[] buf = new char[4096];
                int n;
                while ((n = r.read(buf)) != -1) sb.append(buf, 0, n);
                return sb.toString();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private void fillNext() {
            if (nextNumber != null) return;
            if (matcher.find()) {
                nextNumber = matcher.group();
            }
        }

        @Override
        public boolean hasNext() { fillNext(); return nextNumber != null; }

        @Override
        public String next() {
            fillNext();
            if (nextNumber == null) throw new NoSuchElementException();
            String s = nextNumber;
            nextNumber = null;
            return s;
        }
    }

    private static class RegexIterator implements Iterator<String> {
        private final String content;
        private final Matcher matcher;
        private String nextMatch;

        RegexIterator(Reader r, String pat) {
            this.content = readAll(r);
            Pattern p = Pattern.compile(pat == null ? ".+" : pat);
            this.matcher = p.matcher(this.content);
        }

        private static String readAll(Reader r) {
            try {
                StringBuilder sb = new StringBuilder();
                char[] buf = new char[4096];
                int n;
                while ((n = r.read(buf)) != -1) sb.append(buf, 0, n);
                return sb.toString();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private void fillNext() {
            if (nextMatch != null) return;
            if (matcher.find()) {
                nextMatch = matcher.group();
            }
        }

        @Override
        public boolean hasNext() { fillNext(); return nextMatch != null; }

        @Override
        public String next() {
            fillNext();
            if (nextMatch == null) throw new NoSuchElementException();
            String s = nextMatch;
            nextMatch = null;
            return s;
        }
    }
}
