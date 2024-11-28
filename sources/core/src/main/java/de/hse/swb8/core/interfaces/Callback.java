package de.hse.swb8.core.interfaces;

import de.hse.swb8.core.Records.DataBaseInfo;

public interface Callback {
    void execute(DataBaseInfo info);
}
