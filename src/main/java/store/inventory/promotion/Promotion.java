package store.inventory.promotion;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int purchaseCount;
    private final int freeCount;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int purchaseCount, int freeCount, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.purchaseCount = purchaseCount;
        this.freeCount = freeCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return this.name;
    }

    public int getPurchaseCount() {
        return this.purchaseCount;
    }

    public int getFreeCount() {
        return this.freeCount;
    }

    public boolean isNotNow() {
        LocalDate currentDate = LocalDate.from(DateTimes.now());
        return !(currentDate.isEqual(startDate) || currentDate.isAfter(startDate) &&
                (currentDate.isEqual(endDate) || currentDate.isBefore(endDate)));
    }
}
