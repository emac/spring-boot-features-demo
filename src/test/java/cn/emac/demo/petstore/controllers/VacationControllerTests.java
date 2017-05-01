package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.BaseWiremockTests;
import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.domain.vacation.VacationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;

import static cn.emac.demo.petstore.domain.vacation.VacationType.PERSONAL;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.time.DayOfWeek.SUNDAY;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Emac
 * @since 2017-05-01
 */
@WebMvcTest(VacationController.class)
public class VacationControllerTests extends BaseWiremockTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void before() throws JsonProcessingException {
        JsonResult<Boolean> expected = JsonResult.ok(true);
        stubFor(get(urlPathEqualTo("/api/vacation/isWeekend"))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                        .withBody(objectMapper.writeValueAsString(expected))));
    }

    @Test
    public void testIsWeekendProxy() throws Exception {
        VacationRequest request = new VacationRequest();
        request.setType(PERSONAL);
        OffsetDateTime lastSunday = OffsetDateTime.now().with(TemporalAdjusters.previous(SUNDAY));
        request.setStart(lastSunday);
        request.setEnd(lastSunday.plusDays(1));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/vacation/isWeekend");
        request.toMap().forEach((k, v) -> builder.param(k, v));
        JsonResult<Boolean> expected = JsonResult.ok(true);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }
}
