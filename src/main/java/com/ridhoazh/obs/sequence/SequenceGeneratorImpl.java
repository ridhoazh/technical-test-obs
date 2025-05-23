package com.ridhoazh.obs.sequence;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 24, 2025
 */
// @formatter:on

@Service
@Transactional(readOnly = true)
public class SequenceGeneratorImpl
        implements SequenceGenerator {

    private final SequenceRepository sequenceRepository;

    public SequenceGeneratorImpl(SequenceRepository sequenceRepository) {
        super();
        this.sequenceRepository = sequenceRepository;
    }

    @Transactional
    public Sequence setAndGetNext(String prefix) {
        Sequence seq = null;
        if (sequenceRepository.findNamePrefix(prefix).isEmpty()) {
            seq = new Sequence();
            seq.setName(prefix);
            seq.setOrdinal(1L);
            seq.setLastUpdate(LocalDate.now());
            sequenceRepository.save(seq);
        } else {
            sequenceRepository.setNext(prefix);
            seq = sequenceRepository.getNext(prefix);
        }
        return seq;
    }

    @Override
    @Transactional
    public String getNext(String prefix) {
        Sequence val = setAndGetNext(prefix);
        return String.format("%s%d", prefix, val.getOrdinal().intValue());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getNextBatch(String prefix) {
        Sequence seq = sequenceRepository
                .findSequenceByName(prefix);
        if (seq == null) {
            seq = new Sequence();
        }
        seq.setName(prefix);
        seq.setOrdinal(seq.getOrdinal() + 1);
        seq.setLastUpdate(LocalDate.now());
        Integer val = seq.getOrdinal().intValue();
        sequenceRepository.save(seq);
        return String.format("%s%d", prefix, val);
    }
}
