package com.example.trainingsystem;

import com.example.trainingsystem.controller.UserController;
import com.example.trainingsystem.controller.WordController;
import com.example.trainingsystem.repository.DictionaryRepository;
import com.example.trainingsystem.repository.ScheduleRepository;
import com.example.trainingsystem.repository.UserRepository;
import com.example.trainingsystem.repository.WordRepository;
import com.example.trainingsystem.security.UserDetailSecurityService;
import com.example.trainingsystem.service.WordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ControllerSecurityTest.class)
@Import({WordController.class, UserController.class} )
public class ControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserDetailSecurityService userService;

    @MockBean
    WordService wordService;

    @MockBean
    WordRepository repository;

    @MockBean
    DictionaryRepository dictionaryRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ScheduleRepository scheduleRepository;

    @WithMockUser(
            username = "lenu",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testWordsAllOnUser() throws Exception {
        mockMvc.perform(get("/words/all").param("dictId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testWordsAllOnUserRedirectLogin() throws Exception {
        mockMvc.perform(get("/words/all").param("dictId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(
            username = "lenu",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testEditWordOnUser() throws Exception {
        mockMvc.perform(get("/words/edit").param("id", "1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "lenu",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testAddWordOnUser() throws Exception {
        mockMvc.perform(get("/words/addWord").param("id", "1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "lenu",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testScheduleOnUser() throws Exception {
        mockMvc.perform(get("/words/schedule").param("dictId", "1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void testGetAllUsersOnAdmin() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void testAddUsersOnAdmin() throws Exception {
        mockMvc.perform(get("/users/addUser"))
                .andExpect(status().isOk());
    }

}
