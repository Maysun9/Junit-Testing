package com.example.redthreadgame;


import com.example.redthreadgame.Api.ApiException;
import com.example.redthreadgame.DTO.OUT.WitnessOut;
import com.example.redthreadgame.Model.*;
import static org.mockito.Mockito.*;
import com.example.redthreadgame.Model.*;
import static org.mockito.Mockito.*;
import com.example.redthreadgame.Model.Case;
import com.example.redthreadgame.Repository.CaseRepository;
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
public class CaseRepositoryTest {

    @Autowired
    private CaseRepository caseRepository;

    Case case1, case2, case3;
    List<Case> caseList;

    @BeforeEach
    void setUp() {
        caseRepository.deleteAll(); // امسح الداتا الموجوده قبل كل تست عشان النتيجه

        case1 = new Case(null, "The Missing Diamond", "A diamond was stolen from a museum.", "EASY", "PUBLISHED", null, null, null, null, null);
        case2 = new Case(null, "The Vanishing Heir", "An heir disappeared without a trace.", "MEDIUM", "DRAFT", null, null, null, null, null);
        case3 = new Case(null, "The Silent Killer", "A series of mysterious deaths.", "HARD", "PUBLISHED", null, null, null, null, null);

        caseRepository.save(case1);
        caseRepository.save(case2);
        caseRepository.save(case3);
    }
    @Test
    public void deleteCase() {
        caseRepository.delete(case1);
        Case deleted = caseRepository.findCaseById(case1.getId());
        Assertions.assertThat(deleted).isNull();
    }

    @Test
    public void findCaseById() {
        Case found = caseRepository.findCaseById(case1.getId());
        Assertions.assertThat(found).isEqualTo(case1);
    }

    @Test
    public void findCasesByStatus() {
        caseList = caseRepository.findCasesByStatus("PUBLISHED");
        Assertions.assertThat(caseList.size()).isEqualTo(2);
        Assertions.assertThat(caseList.get(0).getStatus()).isEqualTo("PUBLISHED");
    }

    @Test
    public void findFirstByOrderByIdDesc() {
        Case lastCase = caseRepository.findFirstByOrderByIdDesc();
        Assertions.assertThat(lastCase).isEqualTo(case3);
    }
}