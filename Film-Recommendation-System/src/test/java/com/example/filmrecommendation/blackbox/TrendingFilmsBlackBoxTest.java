import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TrendingFilmsBlackBoxTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        
    }

    
    @Test
    void shouldDisplayTrendingFilms() throws Exception {
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(view().name("dashboard"))
               .andExpect(model().attributeExists("trendingFilms"))
               .andExpect(xpath("//div[@class='trending-section']").exists())
               .andExpect(xpath("//div[@class='trending-film-card']").exists());
    }

    
    @Test
    void shouldDisplayFilmsSortedByViewCount() throws Exception {
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("trendingFilms", hasSize(greaterThan(0))))
               .andExpect(model().attribute("trendingFilms", 
                   hasItem(hasProperty("viewCount", greaterThanOrEqualTo(
                       any(Long.class))))));
    }

    
    @Test
    void shouldIncrementViewCount() throws Exception {
        String filmTitle = "Inception";
        
        
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk());

        
        mockMvc.perform(post("/api/films/" + filmTitle + "/view")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());

        
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("trendingFilms", 
                   hasItem(allOf(
                       hasProperty("title", is(filmTitle)),
                       hasProperty("viewCount", greaterThan(0L))
                   ))));
    }

    
    @Test
    void shouldHandleNonExistentFilm() throws Exception {
        mockMvc.perform(post("/api/films/NonExistentFilm/view")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
    }

    
    @Test
    void shouldDisplayAllFilmInformation() throws Exception {
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(xpath("//div[@class='trending-film-card']/h3").exists())
               .andExpect(xpath("//div[@class='trending-film-card']//p[contains(text(),'Director:')]").exists())
               .andExpect(xpath("//div[@class='trending-film-card']//p[contains(text(),'Genre:')]").exists())
               .andExpect(xpath("//div[@class='trending-film-card']//p[contains(text(),'Rating:')]").exists())
               .andExpect(xpath("//div[@class='trending-film-card']//p[contains(text(),'Views:')]").exists());
    }

    
    @Test
    void shouldHaveWorkingViewButton() throws Exception {
        mockMvc.perform(get("/dashboard"))
               .andExpect(status().isOk())
               .andExpect(xpath("//button[@class='view-button']").exists())
               .andExpect(xpath("//button[@class='view-button']/@onclick").exists());
    }
}
