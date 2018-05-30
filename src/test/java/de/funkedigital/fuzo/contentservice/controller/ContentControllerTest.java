package de.funkedigital.fuzo.contentservice.controller;

import de.funkedigital.fuzo.contentservice.service.ContentService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ContentControllerTest {

    @Mock
    private ContentService contentService;

    @InjectMocks
    private ContentController contentController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(contentController).build();
    }

    @Test
    public void shouldReturnBodyOnExistentContent() throws Exception {
        //Given
        Long id = 1L;
        String body = "{\"key\" : \"value\"}";
        when(contentService.get(id)).thenReturn(Optional.of(body));

        //When //Then
        mockMvc.perform(get("/content/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", is("value")));
    }

    @Test
    public void shouldReturnNotFoundOnNonExistentContent() throws Exception {
        //Given
        Long id = 1L;
        when(contentService.get(id)).thenReturn(Optional.empty());

        //When //Then
        mockMvc.perform(get("/content/{id}", id))
                .andExpect(status().isNotFound());
    }
}