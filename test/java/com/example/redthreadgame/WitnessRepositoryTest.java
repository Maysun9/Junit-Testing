package com.example.redthreadgame;

import com.example.redthreadgame.Model.Case;
import com.example.redthreadgame.Model.Witness;
import com.example.redthreadgame.Repository.CaseRepository;
import com.example.redthreadgame.Repository.WitnessRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WitnessRepositoryTest {

    @Autowired
    private WitnessRepository witnessRepository;

    @Autowired
    private CaseRepository caseRepository;

    Witness witness, witness1, witness2, witness3;
    Case savedCase;
    List<Witness> witnessList;

    @BeforeEach
    void setUp() {
        savedCase = new Case(null, "The Missing Diamond", "A diamond was stolen.", "EASY", "PUBLISHED", null, null, null, null, null);
        caseRepository.save(savedCase);

        witness1 = new Witness(null, "Sara Ali", "I saw someone near the display case.", 80.0, "FEMALE", "NERVOUS", savedCase, null);
        witness2 = new Witness(null, "James Brown", "I was outside and saw nothing unusual.", 60.0, "MALE", "CALM", savedCase, null);
        witness3 = new Witness(null, "Khalid Nasser", "I heard a loud noise at midnight.", 70.0, "MALE", "SUSPICIOUS", savedCase, null);

        witnessRepository.save(witness1);
        witnessRepository.save(witness2);
        witnessRepository.save(witness3);
    }

    @Test
    public void findWitnessById() {
        witness = witnessRepository.findWitnessById(witness1.getId());
        Assertions.assertThat(witness).isEqualTo(witness1);
    }

    @Test
    public void findWitnessesByWitnessCaseId() {
        witnessList = witnessRepository.findWitnessesByWitnessCaseId(savedCase.getId());
        Assertions.assertThat(witnessList.size()).isEqualTo(3);
        Assertions.assertThat(witnessList.get(0).getWitnessCase().getId()).isEqualTo(savedCase.getId());
    }

    @Test
    public void findWitnessById_notFound() {
        witness = witnessRepository.findWitnessById(9999);
        Assertions.assertThat(witness).isNull();
    }
}
