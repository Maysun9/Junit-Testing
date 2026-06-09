package com.example.redthreadgame;
import com.example.redthreadgame.DTO.IN.CaseIn;
import com.example.redthreadgame.DTO.OUT.CaseOut;
import com.example.redthreadgame.Model.Case;
import com.example.redthreadgame.Model.Player;
import com.example.redthreadgame.Repository.CaseRepository;
import com.example.redthreadgame.Repository.PlayerRepository;
import com.example.redthreadgame.Repository.SessionPlayerRepository;
import com.example.redthreadgame.Service.AdminService;
import com.example.redthreadgame.Service.CaseService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

//بدل قاعدة بيانات حقيقيه نستخدم mock
@ExtendWith(MockitoExtension.class)
public class CaseServiceTest {

    @InjectMocks
    CaseService caseService;

    @Mock
    CaseRepository caseRepository;
    @Mock
    PlayerRepository playerRepository;
    @Mock
    SessionPlayerRepository sessionPlayerRepository;
    @Mock
    AdminService adminService;
    @Mock
    ModelMapper modelMapper;

    Case case1, case2, case3;
    List<Case> caseList;
    Player player;

    //قاعدة بيانات وهميه بدون داتا بيس حقيقيه
    @BeforeEach
    void setUp() {
        case1 = new Case(null, "The Missing Diamond", "A diamond was stolen.", "EASY", "PUBLISHED", null, null, null, null, null);
        case2 = new Case(null, "The Vanishing Heir", "An heir disappeared.", "MEDIUM", "DRAFT", null, null, null, null, null);
        case3 = new Case(null, "The Silent Killer", "Mysterious deaths.", "HARD", "PUBLISHED", null, null, null, null, null);

        case1.setId(1); case2.setId(2); case3.setId(3);
        case1.setGameSessions(new HashSet<>()); case2.setGameSessions(new HashSet<>()); case3.setGameSessions(new HashSet<>());

        caseList = List.of(case1, case2, case3);
        player = new Player(null, "Maysun", "detectiveـMay ", "m.alharbi@test.com", "+966500000000", "123456", 25, 0, null, null, null, null, null, null, null, null);
        player.setId(1);
    }

    @Test
    void deleteCase() {
        //1
        when(adminService.verifyAdmin(1, "password")).thenReturn(null);
        when(caseRepository.findCaseById(1)).thenReturn(case1);
        //3
        caseService.deleteCase(1, "password", 1);
        //2
        verify(caseRepository, times(1)).delete(case1);
    }

    @Test
    void publishCase() {
        //1
        when(adminService.verifyAdmin(1, "password")).thenReturn(null);
        when(caseRepository.findCaseById(2)).thenReturn(case2);
        //3
        caseService.publishCase(1, "password", 2);
        Assertions.assertThat(case2.getStatus()).isEqualTo("PUBLISHED");
        //2
        verify(adminService, times(1)).verifyAdmin(1, "password");
        verify(caseRepository, times(1)).save(case2);
    }

    @Test
    void moveCaseToDraft() {
        //1
        when(adminService.verifyAdmin(1, "password")).thenReturn(null);
        when(caseRepository.findCaseById(1)).thenReturn(case1);
        //3
        caseService.moveCaseToDraft(1, "password", 1);
        Assertions.assertThat(case1.getStatus()).isEqualTo("DRAFT");
        //2
        verify(adminService, times(1)).verifyAdmin(1, "password");
        verify(caseRepository, times(1)).save(case1);
    }


    @Test
    void getNotPlayedCases() {
        //1
        when(playerRepository.findPlayerById(1)).thenReturn(player);
        when(caseRepository.findCasesByStatus("PUBLISHED")).thenReturn(List.of(case1, case3));
        when(sessionPlayerRepository.findAllByPlayerId(1)).thenReturn(new ArrayList<>());
        when(modelMapper.map(case1, CaseOut.class)).thenReturn(new CaseOut());
        when(modelMapper.map(case3, CaseOut.class)).thenReturn(new CaseOut());
        //3
        List<CaseOut> result = caseService.getNotPlayedCases(1);
        Assertions.assertThat(result.size()).isEqualTo(2);
        //2
        verify(playerRepository, times(1)).findPlayerById(1);
        verify(sessionPlayerRepository, times(1)).findAllByPlayerId(1);
    }
}
