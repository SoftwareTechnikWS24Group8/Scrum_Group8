package de.hse.swb8.pay.core.interfaces;

import de.hse.swb8.pay.core.Records.DataBaseInfo;

public interface Callback {
    void execute(DataBaseInfo info);
}
