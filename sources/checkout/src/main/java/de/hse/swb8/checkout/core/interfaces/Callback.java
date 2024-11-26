package de.hse.swb8.checkout.core.interfaces;

import de.hse.swb8.checkout.core.Records.DataBaseInfo;

public interface Callback {
    void execute(DataBaseInfo info);
}
