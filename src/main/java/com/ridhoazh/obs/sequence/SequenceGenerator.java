package com.ridhoazh.obs.sequence;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 24, 2025
 */
// @formatter:on

public interface SequenceGenerator {

    String getNext(String prefix);

    String getNextBatch(String prefix);

}
