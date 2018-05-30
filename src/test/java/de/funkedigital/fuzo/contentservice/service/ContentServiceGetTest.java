package de.funkedigital.fuzo.contentservice.service;

import de.funkedigital.fuzo.contentservice.models.Content;
import de.funkedigital.fuzo.contentservice.repo.ContentRepo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContentServiceGetTest {

    @Mock
    private ContentRepo contentRepo;

    @InjectMocks
    private ContentService contentService;

    @Test
    public void shouldReturnBodyWhenPresent() {
        //Given
        Long id = 1L;
        String body = "{\"key\" : \"value\"}";
        when(contentRepo.findById(id)).thenReturn(Optional.of(new Content(id, body)));

        //When
        Optional<String> result = contentService.get(id);

        //Then
        assertThat(result).contains(body);
    }

    @Test
    public void shouldReturnEmptyWhenNotFound() {
        //Given
        Long id = 1L;
        when(contentRepo.findById(id)).thenReturn(Optional.empty());

        //When
        Optional<String> result = contentService.get(id);

        //Then
        assertThat(result).isEmpty();
    }
}