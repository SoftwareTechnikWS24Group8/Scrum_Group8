package de.hse.swb8.checkin.core.interfaces;

import de.hse.swb8.checkin.core.Records.DataBaseInfo;

public interface Callback {
    void execute(DataBaseInfo info);
}
