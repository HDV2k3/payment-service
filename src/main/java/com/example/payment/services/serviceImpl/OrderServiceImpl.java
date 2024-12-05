package com.example.payment.services.serviceImpl;

import com.example.payment.controller.dto.reponse.OrderResponse;
import com.example.payment.controller.dto.reponse.PageResponse;
import com.example.payment.mappers.OrderMapper;
import com.example.payment.repositories.OrderRepository;
import com.example.payment.repositories.entity.OrderEntity;
import com.example.payment.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    @Override
    public PageResponse<OrderResponse> getAllByUser(int userId, int page, int size) {
        // Validate input parameters
        if (page < 1) {
            throw new IllegalArgumentException("Page number must be at least 1");
        }

        // Create pageable object (subtract 1 from page to convert to zero-based index)
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createDate").descending());

        // Find orders for specific user with pagination
        Page<OrderEntity> pageData = orderRepository.findAllByUserId(
                userId,
                pageable
        );

        // Convert to response
        List<OrderResponse> orderResponses = pageData.getContent().stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());

        // Create and return page response
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(orderResponses)
                .build();
    }
}
