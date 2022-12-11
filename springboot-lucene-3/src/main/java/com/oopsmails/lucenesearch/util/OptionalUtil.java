package com.oopsmails.lucenesearch.util;

import java.util.function.Supplier;

/**
 * Ref: https://stackoverflow.com/questions/3458451/check-chains-of-get-calls-for-null
 */
public class OptionalUtil {
    /**
     * Using this to avoid null check chain
     *
     * NOTE: This omits NullPointerException
     *
     * e.g, house.getFloor(0).getWall(WEST).getDoor().getDoorknob();
     *
     * To avoid a NullPointerException, I'd have to do the following if:
     *
     * if (house != null && house.getFloor(0) && house.getFloor(0).getWall(WEST) != null
     *   && house.getFloor(0).getWall(WEST).getDoor() != null) ...
     *
     * Doorknob knob = getFieldValue(() -> house.getFloor(0).getWall(WEST).getDoor().getDoorknob());
     *
     * @param statement
     * @param <T>
     * @return
     */
    public static <T> T getFieldValue(Supplier<T> statement, T defaultValue) {
        try {
            return statement.get();
        } catch (NullPointerException exc) {
            return defaultValue;
        }
    }
}
