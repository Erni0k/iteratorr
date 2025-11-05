package edu.io;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: itx <mode> [<filename> [<pattern>]]");
            System.err.println("Modes: c=chars, w=words, s=sentences, n=numbers, r=regex");
            return;
        }

        String mode = args[0];
        String filename = args.length >= 2 ? args[1] : null;
        String pattern = args.length >= 3 ? args[2] : null;

        if (filename == null) {
            try (TextSource src = TextSource.fromStdin()) {
                runMode(mode, src, pattern);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            try (TextSource src = TextSource.fromFile(filename)) {
                runMode(mode, src, pattern);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void runMode(String mode, TextSource src, String pattern) {
        Iterator<String> it;
        switch (mode) {
            case "c":
                it = src.iterator();
                break;
            case "w":
                it = src.wordIterator();
                break;
            case "s":
                it = src.sentenceIterator();
                break;
            case "n":
                it = src.numberIterator();
                break;
            case "r":
                if (pattern == null) {
                    System.err.println("Regex mode requires a pattern argument");
                    return;
                }
                it = src.regexIterator(pattern);
                break;
            default:
                System.err.println("Unknown mode: " + mode);
                return;
        }

        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}