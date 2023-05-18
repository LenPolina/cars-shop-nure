package com.implemica.order.service;

import com.amazonaws.services.cloud9.model.BadRequestException;
import com.implemica.order.model.Order;
import com.implemica.order.model.OrderDTO;
import com.implemica.order.model.OrderStatus;
import com.implemica.order.repository.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.joda.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    //private final KafkaProducer<String, String> kafkaProducer;
    private final ReplyingKafkaTemplate<String, String, Long> replyingKafkaTemplate;
    private static final String BASE_URL_FOR_CART = "http://localhost:8080/cart/";

    private String requestTopic = "getUserId";
    private static final String BASE_URL_FOR_LOGIN = "http://localhost:8080/";
    private static final String BASE_URL_FOR_STORAGE = "http://localhost:8080/";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private final String usersTopic = "users";
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(KafkaTemplate<String, String> kafkaTemplate,
                        ReplyingKafkaTemplate<String, String, Long> replyingKafkaTemplate,
                        ModelMapper modelMapper, OrderRepository orderRepository, RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public OrderDTO getOrder(Long id, String username) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("Getting the order from db = {}", id);
        return convertTo(orderRepository.findByIdAndUser(id, Long.valueOf(getUserId(username))));
    }

    public OrderDTO save(OrderDTO orderDTO) throws ExecutionException, InterruptedException, TimeoutException {
        orderDTO.setOrderDate(new LocalDateTime());
        orderDTO.setUser(getUserId(orderDTO.getUser()));
        if (orderDTO.getOrderStatus() == null){
            orderDTO.setOrderStatus(OrderStatus.ORDERED);
        }
        Order orderToSave = convertTo(orderDTO);
        Order savedOrder = orderRepository.save(orderToSave);

        log.info("Add order to db = {}", savedOrder.getId());
        return convertTo(savedOrder);
    }

    private String getUserId(String username) throws ExecutionException, InterruptedException, TimeoutException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set the request body
        HttpEntity<String> request = new HttpEntity<>(username, headers);
        Long id = getUserIdByLogin(username);// restTemplate.postForObject(BASE_URL_FOR_LOGIN +"api/users", request, Long.class);

        return Objects.requireNonNull(id).toString();
    }

    public void delete(Long Id) {
        orderRepository.deleteById(Id);
    }
    public Order convertTo(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }
    public OrderDTO convertTo(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    public String handleOrder(Long id) {
        Order order = orderRepository.findById(id).get();

        List<Long> carsId = order.getCars();
        carsId.forEach(carId -> removeFromStorage(carId));

        String username = getUsername(order.getUser());
        cleanCart(username);
        return "Success handle order!";
    }
    public Long getUserIdByLogin(String login) throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>(requestTopic, null, login , login);
        RequestReplyFuture<String, String, Long> future = replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Long> response = future.get();
        return response.value();
    }
    private String getUsername(Long user) {
        String username;
        try {
             username = restTemplate.getForObject(BASE_URL_FOR_LOGIN + "api/user/name/" + user, String.class);
        }catch (HttpClientErrorException | HttpServerErrorException ex){
            throw new BadRequestException(String.format("The cart user %s can not be cleaned.",user)+ex.getMessage());
        }
        return username;
    }

    private void cleanCart(String username) {
        //kafkaProducer.initTransactions();
        //kafkaProducer.beginTransaction();
        //ProducerRecord<String, String> record = new ProducerRecord<>("CleanUserCart", "clen_cart", username.toLowerCase());
        //kafkaProducer.send(record);
        kafkaTemplate.send("CleanUserCart",username.toLowerCase());
       /* try {
            restTemplate.delete(BASE_URL_FOR_CART +"clean/user/"+username.toLowerCase());
        }catch (HttpClientErrorException | HttpServerErrorException ex){
            throw new BadRequestException(String.format("The cart user %s can not be cleaned.",username)+ex.getMessage());
        }*/
    }

    private void removeFromStorage(Long carId) {
        //kafkaProducer.beginTransaction();
        //ProducerRecord<String, String> record = new ProducerRecord<>("RemoveOneCarFromStorage", "storage", carId.toString());
        //kafkaProducer.send(record);
        kafkaTemplate.send("RemoveOneCarFromStorage",carId.toString());
        /*HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Long> requestEntity = new HttpEntity<>(1L,headers);
        try {
            restTemplate.postForEntity(BASE_URL_FOR_STORAGE +"storage/delete/car/"+carId, requestEntity, Void.class);
        }catch (HttpClientErrorException | HttpServerErrorException ex){
            throw new BadRequestException("The car can not be bought."+ex.getMessage());
        }*/
    }

    public List<OrderDTO> getOrders(String remoteUser) throws ExecutionException, InterruptedException, TimeoutException {
        List<Order> orders;
        if(remoteUser.equals("admin")){
            orders = orderRepository.findAll();
        }else {
            orders = orderRepository.findOrdersByUser(Long.valueOf(getUserId(remoteUser)));
        }
        return orders.stream().map(this::convertTo).collect(Collectors.toCollection(LinkedList::new));
    }
}
