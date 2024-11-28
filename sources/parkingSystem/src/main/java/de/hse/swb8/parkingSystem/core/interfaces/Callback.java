package de.hse.swb8.parkingSystem.core.interfaces;

import de.hse.swb8.parkingSystem.core.Records.DataBaseInfo;

public interface Callback {
    void execute(DataBaseInfo info);
}
