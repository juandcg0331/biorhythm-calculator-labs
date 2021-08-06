package report;

import biorhytms.Biorhythm;
import report.format.BiorhythmTemplateFormat;
import report.format.DaysFormat;
import report.format.MultilineTextFormat;
import report.format.OrdinalDateFormat;

import java.text.Format;
import java.util.stream.Stream;

public class SummaryReport extends AbstractReport {
    private static final Format ORDINAL_DATE_FORMAT = new OrdinalDateFormat();
    private static final Format DAYS_FORMAT = new DaysFormat();
    private final Format shortInfo = new BiorhythmTemplateFormat(getString("short.biorhythm.format"));
    private final Format multilineFormat = new MultilineTextFormat();

    public SummaryReport(final ReportData reportData) {
        super(reportData);
    }

    @Override
    public void run() {
        printf("summary.header.format");
        biorhythms().map(shortInfo::format).forEach(this::println);
        biorhythms().forEach(this::printInfo);
    }

    private Stream<Biorhythm.Value> biorhythms() {
        return Biorhythm.primary().map(biorhythm -> biorhythm.new Value(birthday(), date()));
    }

    private void printInfo(Biorhythm.Value value) {
        printf("summary.biorhythm.name.format", value.getBiorhythm());
        print(multilineFormat.format(format(getString(value.getStage().name()),
                value.getBiorhythm().name().toLowerCase(),
                value.getBiorhythm().getAttributes(),
                ORDINAL_DATE_FORMAT.format(value.cycleLastDay()),
                DAYS_FORMAT.format(value.changesInDays()))));
    }
}
