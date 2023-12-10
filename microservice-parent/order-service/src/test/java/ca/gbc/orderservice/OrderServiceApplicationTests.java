package ca.gbc.orderservice;

import ca.gbc.orderservice.dto.InventoryResponse;
import ca.gbc.orderservice.dto.OrderLineItemDto;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceApplicationTests extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    private final String TEST_SKU_CODE = "IPAD2023";

    private static MockWebServer mockWebServer;


    private OrderLineItemDto getOrderLineItemDto(String skuCode) {
        return OrderLineItemDto.builder()
                .id(new Random().nextLong())
                .skuCode(skuCode)
                .quantity(2)
                .price(BigDecimal.valueOf(1200.00))
                .build();
    }

    private OrderRequest getOrderRequest(String skuCode) {

        List<OrderLineItemDto> orderLineItemList = new ArrayList<>();
        orderLineItemList.add(getOrderLineItemDto(skuCode));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItemDtoList(orderLineItemList);

        return orderRequest;
    }

    @BeforeAll
    static void setupServer() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        System.setProperty("inventory.service.url", "http://localhost:"
                + mockWebServer.getPort());
    }

    @AfterAll
    static void shutdownServer() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void placeOrder() throws Exception {

        InventoryResponse  inventoryResponse =
                new InventoryResponse(TEST_SKU_CODE, true);

        mockWebServer.enqueue(
               new MockResponse()
                       .setBody(objectMapper.writeValueAsString(List.of(inventoryResponse)))
                       .setHeader("Content-Type", "application/json")
        );


        OrderRequest orderRequest = getOrderRequest(TEST_SKU_CODE);
        String orderRequestJsonString = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        List<Order> orderList = orderRepository.findAll();

        assertTrue(orderList.size() > 0);

        String returnedSkuCode = orderList.get(0).getOrderLineItemList().get(0).getSkuCode();
        assertEquals(TEST_SKU_CODE, returnedSkuCode);

    }


}