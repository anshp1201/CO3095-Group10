import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TrendingFilmsWhiteBoxTest {

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private TrendingFilmsService trendingFilmsService;

    private Film testFilm1;
    private Film testFilm2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        
        testFilm1 = new Film();
        testFilm1.setTitle("Inception");
        testFilm1.setDirector("Christopher Nolan");
        testFilm1.setGenre("Sci-Fi");
        testFilm1.setViewCount(100L);
        testFilm1.setAverageRating(4.5);

        testFilm2 = new Film();
        testFilm2.setTitle("The Dark Knight");
        testFilm2.setDirector("Christopher Nolan");
        testFilm2.setGenre("Action");
        testFilm2.setViewCount(150L);
        testFilm2.setAverageRating(4.8);
    }

    
    @Test
    void testGetTrendingFilmsRepositoryInteraction() {
        
        List<Film> expectedFilms = Arrays.asList(testFilm2, testFilm1);
        when(filmRepository.findAll(any(Sort.class))).thenReturn(expectedFilms);

        
        List<Film> actualFilms = trendingFilmsService.getTrendingFilms();

        
        verify(filmRepository).findAll(any(Sort.class));
        assertEquals(expectedFilms, actualFilms);
    }

    
    @Test
    void testViewCountIncrementLogic() {
        
        String filmTitle = "Inception";
        when(filmRepository.findByTitle(filmTitle)).thenReturn(Optional.of(testFilm1));
        when(filmRepository.save(any(Film.class))).thenReturn(testFilm1);

        
        Long initialCount = testFilm1.getViewCount();
        trendingFilmsService.incrementViewCount(filmTitle);

        
        assertEquals(initialCount + 1, testFilm1.getViewCount());
        verify(filmRepository).save(testFilm1);
    }

    
    @Test
    void testErrorHandlingPath() {
        
        String nonExistentFilm = "NonExistentFilm";
        when(filmRepository.findByTitle(nonExistentFilm)).thenReturn(Optional.empty());

        
        assertThrows(FilmNotFoundException.class, () -> 
            trendingFilmsService.incrementViewCount(nonExistentFilm)
        );
        verify(filmRepository, never()).save(any(Film.class));
    }

    
    @Test
    void testSortingLogic() {
        
        List<Film> unsortedFilms = Arrays.asList(testFilm1, testFilm2);
        List<Film> expectedSortedFilms = Arrays.asList(testFilm2, testFilm1);
        when(filmRepository.findAll(any(Sort.class))).thenReturn(expectedSortedFilms);

        
        List<Film> actualSortedFilms = trendingFilmsService.getTrendingFilms();

      
        assertEquals(expectedSortedFilms, actualSortedFilms);
        assertTrue(actualSortedFilms.get(0).getViewCount() >= 
                  actualSortedFilms.get(1).getViewCount());
    }

    
    @Test
    void testNullHandling() {
        
        when(filmRepository.findAll(any(Sort.class))).thenReturn(null);

        
        assertThrows(NullPointerException.class, () -> 
            trendingFilmsService.getTrendingFilms()
        );
    }

    
    @Test
    void testTransactionRollback() {
        
        String filmTitle = "Inception";
        when(filmRepository.findByTitle(filmTitle)).thenReturn(Optional.of(testFilm1));
        when(filmRepository.save(any(Film.class))).thenThrow(new RuntimeException("Database error"));

        
        assertThrows(RuntimeException.class, () -> 
            trendingFilmsService.incrementViewCount(filmTitle)
        );
        verify(filmRepository).findByTitle(filmTitle);
        verify(filmRepository).save(testFilm1);
    }
}
