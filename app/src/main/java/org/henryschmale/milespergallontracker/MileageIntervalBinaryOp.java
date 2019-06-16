package org.henryschmale.milespergallontracker;

public class MileageIntervalBinaryOp {
    public static class Carry {

    }

    public static MileageInterval computeTotalMileage(final MileageInterval a, final MileageInterval b) {
        MileageInterval c = new MileageInterval();
        c.milesTraveled = a.milesTraveled + b.milesTraveled;
        c.mpg = a.mpg + b.mpg;
        c.costPerMile = a.costPerMile + b.costPerMile;
        return c;
    }
}
