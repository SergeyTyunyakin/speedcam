package ru.speedcam.readers;

import com.opencsv.CSVParserBuilder;
import com.opencsv.ICSVParser;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Reader;
import java.util.Locale;
import java.util.ResourceBundle;

public class CSVLineReaderBuilder {
    private final CSVParserBuilder parserBuilder = new CSVParserBuilder();
    private final Reader reader;
    private int skipLines = 0;
    private ICSVParser icsvParser = null;
    private boolean keepCR;
    private boolean verifyReader = true;
    private CSVReaderNullFieldIndicator nullFieldIndicator;
    private int multilineLimit;
    private Locale errorLocale;

    public CSVLineReaderBuilder(Reader reader) {
        this.nullFieldIndicator = CSVReaderNullFieldIndicator.NEITHER;
        this.multilineLimit = 0;
        this.errorLocale = Locale.getDefault();
        if (reader == null) {
            throw new IllegalArgumentException(ResourceBundle.getBundle("opencsv").getString("reader.null"));
        } else {
            this.reader = reader;
        }
    }

    protected Reader getReader() {
        return this.reader;
    }

    protected int getSkipLines() {
        return this.skipLines;
    }

    protected ICSVParser getCsvParser() {
        return this.icsvParser;
    }

    protected int getMultilineLimit() {
        return this.multilineLimit;
    }

    public CSVLineReaderBuilder withSkipLines(int skipLines) {
        this.skipLines = skipLines <= 0 ? 0 : skipLines;
        return this;
    }

    public CSVLineReaderBuilder withCSVParser(ICSVParser icsvParser) {
        this.icsvParser = icsvParser;
        return this;
    }

    public CSVLineReader build() {
        ICSVParser parser = this.getOrCreateCsvParser();
        return new CSVLineReader(this.reader, this.skipLines, parser, this.keepCR, this.verifyReader, this.multilineLimit, this.errorLocale);
    }

    public CSVLineReaderBuilder withKeepCarriageReturn(boolean keepCR) {
        this.keepCR = keepCR;
        return this;
    }

    protected boolean keepCarriageReturn() {
        return this.keepCR;
    }

    protected ICSVParser getOrCreateCsvParser() {
        return (ICSVParser) ObjectUtils.defaultIfNull(this.icsvParser, this.parserBuilder.withFieldAsNull(this.nullFieldIndicator).withErrorLocale(this.errorLocale).build());
    }

    public CSVLineReaderBuilder withVerifyReader(boolean verifyReader) {
        this.verifyReader = verifyReader;
        return this;
    }

    public boolean isVerifyReader() {
        return this.verifyReader;
    }

    public CSVLineReaderBuilder withFieldAsNull(CSVReaderNullFieldIndicator indicator) {
        this.nullFieldIndicator = indicator;
        return this;
    }

    public CSVLineReaderBuilder withMultilineLimit(int multilineLimit) {
        this.multilineLimit = multilineLimit;
        return this;
    }

    public CSVLineReaderBuilder withErrorLocale(Locale errorLocale) {
        this.errorLocale = (Locale)ObjectUtils.defaultIfNull(errorLocale, Locale.getDefault());
        return this;
    }

    public Locale getErrorLocale() {
        return this.errorLocale;
    }

}
