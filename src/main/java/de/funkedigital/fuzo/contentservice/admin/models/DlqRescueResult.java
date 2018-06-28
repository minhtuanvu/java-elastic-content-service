package de.funkedigital.fuzo.contentservice.admin.models;

public class DlqRescueResult {
    private final Integer rowsRecovered;
    private final Integer rowsRequeued;

    public DlqRescueResult(Integer rowsRecovered, Integer rowsRequeued) {
        this.rowsRecovered = rowsRecovered;
        this.rowsRequeued = rowsRequeued;
    }

    public Integer getRowsRecovered() {
        return rowsRecovered;
    }

    public Integer getRowsRequeued() {
        return rowsRequeued;
    }
}
