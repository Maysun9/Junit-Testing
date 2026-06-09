package com.example.redthreadgame;

import com.example.redthreadgame.Api.ApiException;
import com.example.redthreadgame.DTO.OUT.WitnessOut;
import com.example.redthreadgame.Model.*;
import static org.mockito.Mockito.*;
import com.example.redthreadgame.Enums.GameSessionStatusType;
import com.example.redthreadgame.Model.Case;
import com.example.redthreadgame.Model.GameSession;
import com.example.redthreadgame.Model.Witness;
import com.example.redthreadgame.Repository.GameSessionRepository;
import com.example.redthreadgame.Repository.WitnessRepository;
import com.example.redthreadgame.Service.CaseService;
import com.example.redthreadgame.Service.OpenAiService;
import com.example.redthreadgame.Service.WitnessService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class WitnessServiceTest {

    @InjectMocks
    WitnessService witnessService;

    @Mock
    WitnessRepository witnessRepository;
    @Mock
    CaseService caseService;
    @Mock
    OpenAiService openAiService;
    @Mock
    GameSessionRepository gameSessionRepository;
    @Mock
    ModelMapper modelMapper;

    Case testCase;
    GameSession gameSession;
    Witness witness1, witness2;

    @BeforeEach
    void setUp() {
        testCase = new Case(null, "The Missing Diamond", "A diamond was stolen from a museum.", "EASY", "PUBLISHED", null, null, null, null, null);
        testCase.setId(1);

        //الحاله الطبيعيه اللي يشتغل فيها IN_PROGRESS,
        gameSession = new GameSession(null, GameSessionStatusType.IN_PROGRESS, false, "123456", 2, 0, 60, null, null, testCase, null, null, null, null, null, null, null);
        gameSession.setId(1);
        gameSession.setQuestions(new HashSet<>());

        witness1 = new Witness(null, "Taif Alsalem", "I saw someone near the display case.", 80.0, "FEMALE", "NERVOUS", testCase, null);
        witness1.setId(1);
        witness2 = new Witness(null, "Faisal salah", "I was outside and saw nothing unusual.", 60.0, "MALE", "CALM", testCase, null);
        witness2.setId(2);
    }

    @Test
    //اتاكد ان مواجهة الشهود not in progress لو الجلسه مو active
    void confrontWitnesses_sessionNotInProgress() {
        //1 when
        gameSession.setStatus(GameSessionStatusType.PENDING);
        when(gameSessionRepository.findGameSessionById(1)).thenReturn(gameSession);
        //3 logic
        ApiException ex = org.junit.jupiter.api.Assertions.assertThrows(ApiException.class, () ->
                witnessService.confrontWitnesses(1, 2, 1));
        Assertions.assertThat(ex.getMessage()).isEqualTo("Game session is not in progress");
        //2
        verify(gameSessionRepository, times(1)).findGameSessionById(1);
    }

    @Test
    void confrontWitnesses_success() {
        //1 when
        when(gameSessionRepository.findGameSessionById(1)).thenReturn(gameSession);
        when(witnessRepository.findWitnessById(1)).thenReturn(witness1);
        when(witnessRepository.findWitnessById(2)).thenReturn(witness2);
        //ارجع رد وهمي لانه مافيه اتصال بالai
        when(openAiService.generateAnswer(anyString())).thenReturn("{\"agree\": \"Both saw the theft\", \"contradict\": \"Location differs\"}");

        //3 logic
        // اتاكد ان المثيود agree
        String result = witnessService.confrontWitnesses(1, 2, 1);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).contains("agree");
        //2 verify
        //  اتاكد الai استدعى
        verify(openAiService, times(1)).generateAnswer(anyString());
    }

    @Test
    void getNotQuestionedWitnesses_success() {
        //1 when
        when(gameSessionRepository.findGameSessionById(1)).thenReturn(gameSession);
        when(witnessRepository.findWitnessesByWitnessCaseId(1)).thenReturn(List.of(witness1, witness2));
        when(modelMapper.map(witness1, WitnessOut.class)).thenReturn(new WitnessOut());
        when(modelMapper.map(witness2, WitnessOut.class)).thenReturn(new WitnessOut());
        //3 logic
        List<WitnessOut> result = witnessService.getNotQuestionedWitnesses(1);
        Assertions.assertThat(result.size()).isEqualTo(2);
        //2 verify
        verify(witnessRepository, times(1)).findWitnessesByWitnessCaseId(1);
        verify(modelMapper, times(2)).map(any(Witness.class), eq(WitnessOut.class));
    }
}