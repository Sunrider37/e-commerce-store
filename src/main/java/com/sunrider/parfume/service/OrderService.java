package com.sunrider.parfume.service;

import com.sunrider.parfume.model.Order;
import com.sunrider.parfume.model.OrderItem;
import com.sunrider.parfume.model.Perfume;
import com.sunrider.parfume.repository.OrderItemRepository;
import com.sunrider.parfume.repository.OrderRepository;
import com.sunrider.parfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PerfumeRepository perfumeRepository;
    private final MailSender mailSender;

    public List<Order> findAll(){
        return orderRepository.findAllByOrderByIdAsc();
    }

    public List<Order> findByEmail(String email){
        return orderRepository.findByEmail(email);
    }

    @Transactional
    public Order postOrder(Order validOrder, Map<Long,Long> perfumesId) throws MessagingException {
        Order order = new Order();
        List<OrderItem> orderItemList = new ArrayList<>();
        for(Map.Entry<Long,Long> entry : perfumesId.entrySet()){
            Perfume perfume = perfumeRepository.findById(entry.getKey()).orElseThrow();
            OrderItem orderItem = new OrderItem();
            orderItem.setPerfume(perfume);
            orderItem.setAmount(perfume.getPrice() * entry.getValue());
            orderItem.setQuantity(entry.getValue());
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
        }

        order.getOrderItems().addAll(orderItemList);
        order.setTotalPrice(validOrder.getTotalPrice());
        order.setFirstName(validOrder.getFirstName());
        order.setLastName(validOrder.getLastName());
        order.setCity(validOrder.getCity());
        order.setAddress(validOrder.getAddress());
        order.setPostIndex(validOrder.getPostIndex());
        order.setEmail(validOrder.getEmail());
        order.setPhoneNumber(validOrder.getPhoneNumber());
        orderRepository.save(order);

        String subject = "Order #" + order.getId();
        String template = "order-template";
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("order", order);
        mailSender.sendMessageHtml(order.getEmail(),subject,template,attributes);
        return order;
    }

    @Transactional
    public List<Order> deleteOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.getOrderItems().forEach(orderItem -> orderItemRepository.deleteById(orderItem.getId()));
        orderRepository.delete(order);
        return orderRepository.findAllByOrderByIdAsc();
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByIdAsc();
    }

    public List<Order> findOrderByEmail(String email) {
        return orderRepository.findByEmail(email);
    }
}
