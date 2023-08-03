package ru.speedcam.readers;

import com.opencsv.CSVReader;
import com.opencsv.ICSVParser;

import java.io.IOException;
import java.io.Reader;
import java.util.Locale;

public class CSVLineReader extends CSVReader {
    public CSVLineReader(Reader reader, int skipLines, ICSVParser parser, boolean keepCR, boolean verifyReader, int multilineLimit, Locale errorLocale) {
        super(reader);
    }

    @Override
    public String getNextLine() throws IOException {
        return super.getNextLine();
    }
}
