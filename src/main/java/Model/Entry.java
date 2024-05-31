package Model;

import java.util.List;


public class Entry {
    private List<Change> changes;

    public List<Change> getChanges() {
        return changes;
    }

    public void setChanges(List<Change> changes) {
        this.changes = changes;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "changes=" + changes +
                '}';
    }
}
