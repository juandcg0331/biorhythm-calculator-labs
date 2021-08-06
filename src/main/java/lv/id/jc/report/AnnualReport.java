package lv.id.jc.report;

import lv.id.jc.biorhytm.Biorhythm;
import lv.id.jc.biorhytm.Condition;
import lombok.val;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class AnnualReport extends AbstractReport {
    public AnnualReport(final Context context) {
        super(context);
    }

    @Override
    public void run() {
        val leftPart = format("annual.header.left.format", birthday());
        val rightPart = format("annual.header.right.format", date());

        printf("annual.header.format", leftPart, rightPart);

        val months = Arrays.stream(Month.values())
                .map(m -> m.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
                .toArray();
        printf("annual.months.format", months);


        for (int day = 1; day < 32; day++) {
            System.out.printf("%3d   ", day);
            for (int month = 1; month < 13; month++) {
                System.out.printf("%-6s", getIndicators(month, day));
            }
            System.out.println();
        }
    }

    private String getIndicators(final int month, final int day) {
        try {
            final var date = LocalDate.of(context.getYear(), month, day);
            return Biorhythm.primary()
                    .map(biorhythm -> biorhythm.new Value(birthday(), date))
                    .map(Biorhythm.Value::getValue)
                    .map(Condition::of)
                    .map(Condition::name)
                    .map(this::getString)
                    .collect(Collectors.joining());

        } catch (DateTimeException e) {
            return "";
        }
    }

}
