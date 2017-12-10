package co.uk.sakalauskas.n26_challenge.controllers;

import co.uk.sakalauskas.n26_challenge.model.Stats;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatsControllerTest {

    @Autowired private MockMvc mvc;

    private final double amounts[] = {10.0,0.01, 100.0, 50.0, 20.0, 99.0, 1.00, 33.31, 10.0, 88.90};

    @Test
    public void testTooOld() throws Exception {
        long minutesAgo = System.currentTimeMillis() - 1000 * 120;

        this.mvc.perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"amount\": \"%s\", \"timestamp\": \"%d\"}", 100, minutesAgo))
        ).andExpect(status().is(204));
    }
    @Test
    public void testPostAndGenerateTransactions() throws Exception {
        for (int i = 0; i < amounts.length; i++) {

            this.mvc.perform(
                    post("/transactions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format("{\"amount\": \"%s\", \"timestamp\": \"%d\"}", amounts[i], System.currentTimeMillis() - i * 1000))
            ).andExpect(status().is(201));
        }

        MvcResult result =  this.mvc.perform(
                get("/statistics").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(200)).andReturn();

        Stats transaction = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Stats.class);

        double max = Arrays.stream(amounts).max().getAsDouble();
        double min = Arrays.stream(amounts).min().getAsDouble();
        double sum = Arrays.stream(amounts).sum();
        double avg = Arrays.stream(amounts).sum() / amounts.length;

        assertEquals(sum, transaction.getSum(), 0.001);
        assertEquals(min, transaction.getMin(), 0.001);
        assertEquals(max, transaction.getMax(), 0.001);
        assertEquals(avg, transaction.getAvg(), 0.001);
        assertEquals(amounts.length, transaction.getCount());
    }


}
