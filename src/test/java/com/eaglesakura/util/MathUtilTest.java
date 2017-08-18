package com.eaglesakura.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MathUtilTest {

    @Test
    public void 加重平均が計算できる() throws Throwable {
        {
            Iterable<Double> values = Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0);
            assertEquals(MathUtil.weightedAverage(values, 0.0), 1.0, 0.001);
            assertEquals(MathUtil.weightedAverage(values, 0.25), 1.0, 0.001);
            assertEquals(MathUtil.weightedAverage(values, 0.50), 1.0, 0.001);
            assertEquals(MathUtil.weightedAverage(values, 1.0), 1.0, 0.001);
        }
        {
            Iterable<Double> values = Arrays.asList(1.0, 0.0);
            assertEquals(MathUtil.weightedAverage(values, 0.0), 1.0, 0.001);
            assertEquals(MathUtil.weightedAverage(values, 0.50), 0.6666666, 0.001);
            assertEquals(MathUtil.weightedAverage(values, 1.0), 0.5, 0.001);
        }
    }
}