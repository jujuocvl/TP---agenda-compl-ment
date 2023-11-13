package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : A repetitive event that terminates after a given date, or after
 * a given number of occurrences
 */
public class FixedTerminationEvent extends RepetitiveEvent {

    private LocalDate terminationInclusive;
    private long numberOfOccurrences;
    private LocalDate calculTermination;
    private List<LocalDate> exceptions = new ArrayList<>();


    /**
     * Constructs a fixed terminationInclusive event ending at a given date
     *
     * @param title the title of this event
     * @param start the start time of this event
     * @param duration the duration of this event
     * @param frequency one of :
     * <UL>
     * <LI>ChronoUnit.DAYS for daily repetitions</LI>
     * <LI>ChronoUnit.WEEKS for weekly repetitions</LI>
     * <LI>ChronoUnit.MONTHS for monthly repetitions</LI>
     * </UL>
     * @param terminationInclusive the date when this event ends
     */
    public FixedTerminationEvent(String title, LocalDateTime start, Duration duration, ChronoUnit frequency, LocalDate terminationInclusive) {
        super(title, start, duration, frequency);
        this.terminationInclusive = terminationInclusive;
        this.numberOfOccurrences = frequency.between(start.toLocalDate(), terminationInclusive) + 1;
    }


    /**
     * Constructs a fixed termination event ending after a number of iterations
     *
     * @param title the title of this event
     * @param start the start time of this event
     * @param duration the duration of this event
     * @param frequency one of :
     * <UL>
     * <LI>ChronoUnit.DAYS for daily repetitions</LI>
     * <LI>ChronoUnit.WEEKS for weekly repetitions</LI>
     * <LI>ChronoUnit.MONTHS for monthly repetitions</LI>
     * </UL>
     * @param numberOfOccurrences the number of occurrences of this repetitive event
     */
    public FixedTerminationEvent(String title, LocalDateTime start, Duration duration, ChronoUnit frequency, long numberOfOccurrences) {
        super(title, start, duration, frequency);
        this.numberOfOccurrences = numberOfOccurrences;
        this.calculTermination = calculateTerminationDate(start, frequency, numberOfOccurrences);
    }

    private LocalDate calculateTerminationDate(LocalDateTime start, ChronoUnit frequency, long numberOfOccurrences) {
        LocalDate terminationDate = start.toLocalDate();
        for (int i = 1; i < numberOfOccurrences; i++) {
            terminationDate = terminationDate.plus(1, frequency);
        }
        return terminationDate;
    }


    @Override
    public String toString() {
        return super.toString() +
                "terminationInclusive=" + terminationInclusive +
                ", numberOfOccurrences=" + numberOfOccurrences +
                '}';
    }

    /**
     *
     * @return the termination date of this repetitive event
     */
    public LocalDate getTerminationDate() {

        return calculTermination;
    }

    public long getNumberOfOccurrences() {
        return numberOfOccurrences;
    }

    public void addException(LocalDate date) {
        exceptions.add(date);
    }

    @Override
    public boolean isInDay(LocalDate aDay) {
        if (getStart().toLocalDate().isEqual(aDay)) {
            return true; // The start date is considered the first occurrence
        } else {
            LocalDate dateOccurrence = getStart().toLocalDate();
            for (int i = 1; i < getNumberOfOccurrences(); i++) { // Start from 1 to exclude the start date
                dateOccurrence = dateOccurrence.plus(1, getFrequency());
                if (dateOccurrence.isEqual(aDay) && !exceptions.contains(aDay)) {
                    return true;
                }
            }
        }
        return false;
    }


}