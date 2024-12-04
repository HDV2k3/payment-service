//package com.example.payment.controller;
//
//import com.example.payment.controller.dto.reponse.GenericApiResponse;
//import com.example.payment.services.VNPayService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//
//@Tag(name = "VNPay Controller", description = "API để quản lý VNPay Payment.")
//@Slf4j
//@RestController
//@RequestMapping("/vnPay")
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class VNPayController {
//    VNPayService vnPayService;
//
//    @Operation(
//            summary = "Tạo đơn hàng VNPay",
//            description = "API này tạo một đơn hàng mới với thông tin đơn hàng và số tiền được cung cấp, và trả về URL để thanh toán trên VNPay."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "URL thanh toán thành công",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericApiResponse.class))),
//            @ApiResponse(responseCode = "400", description = "Lỗi đầu vào không hợp lệ",
//                    content = @Content),
//            @ApiResponse(responseCode = "500", description = "Lỗi trong quá trình thanh toán",
//                    content = @Content)
//    })
//    @PostMapping("/submitOrder")
//    public GenericApiResponse<String> submitOrder(
//            @RequestParam("amount") int orderTotal,
//            @RequestParam("orderInfo") String orderInfo,
//            HttpServletRequest request) {
//        try {
//            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//            String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
//            return GenericApiResponse.success(vnpayUrl);
//        } catch (Exception e) {
//            log.error("Error creating VNPay order: ", e);
//            return GenericApiResponse.error("Lỗi trong quá trình thanh toán");
//        }
//    }
//
//    @Operation(
//            summary = "Xử lý kết quả thanh toán từ VNPay",
//            description = "API này xử lý phản hồi từ VNPay sau khi thanh toán, trả về trạng thái thanh toán."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Xử lý thành công trạng thái thanh toán",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericApiResponse.class))),
//            @ApiResponse(responseCode = "500", description = "Lỗi trong quá trình xử lý phản hồi",
//                    content = @Content)
//    })
//    @GetMapping("/vnpay-payment-return")
//    public GenericApiResponse<String> paymentReturn(HttpServletRequest request) {
//        try {
//            int paymentStatus = vnPayService.orderReturn(request);
//
//            String message = switch (paymentStatus) {
//                case 1 -> "Payment completed successfully";
//                case 0 -> "Payment failed";
//                default -> "Invalid payment verification";
//            };
//
//            return GenericApiResponse.success(message);
//        } catch (Exception e) {
//            log.error("Error processing payment return: ", e);
//            return GenericApiResponse.error("Failed to process payment return");
//        }
//    }
//}
//
package com.example.payment.controller;

import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.services.UserPaymentService;
import com.example.payment.services.VNPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VNPay Controller", description = "API để quản lý VNPay Payment.")
@Slf4j
@RestController
@RequestMapping("/vnPay")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayController {

    VNPayService vnPayService;
    UserPaymentService userPaymentService; // Inject UserPaymentService để cập nhật balance

    @Operation(
            summary = "Tạo đơn hàng VNPay",
            description = "API này tạo một đơn hàng mới với thông tin đơn hàng và số tiền được cung cấp, và trả về URL để thanh toán trên VNPay."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL thanh toán thành công",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Lỗi đầu vào không hợp lệ",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi trong quá trình thanh toán",
                    content = @Content)
    })
    @PostMapping("/submitOrder")
    public GenericApiResponse<String> submitOrder(
            @RequestParam("amount") int orderTotal,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) {
        try {
            // Lưu số tiền vào session trước khi tạo URL thanh toán
            HttpSession session = request.getSession();
            session.setAttribute("orderTotal", orderTotal);

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);

            return GenericApiResponse.success(vnpayUrl);
        } catch (Exception e) {
            log.error("Error creating VNPay order: ", e);
            return GenericApiResponse.error("Lỗi trong quá trình thanh toán");
        }
    }

    @Operation(
            summary = "Xử lý kết quả thanh toán từ VNPay",
            description = "API này xử lý phản hồi từ VNPay sau khi thanh toán, trả về trạng thái thanh toán.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xử lý thành công trạng thái thanh toán",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Lỗi trong quá trình xử lý phản hồi",
                    content = @Content)

    }
    )
    @GetMapping("/vnpay-payment-return")
    public GenericApiResponse<String> paymentReturn(HttpServletRequest request) {
        try {
            int paymentStatus = vnPayService.orderReturn(request);
            String message = switch (paymentStatus) {
                case 1 -> "Payment completed successfully";
                case 0 -> "Payment failed";
                default -> "Invalid payment verification";
            };

            if (paymentStatus == 1) {
                // Lấy số tiền đã lưu trong session
                HttpSession session = request.getSession();
                Double amount = (Double) session.getAttribute("orderTotal");

                if (amount != null) {
                    // Cập nhật balance cho người dùng sau khi thanh toán thành công
                    userPaymentService.updateUserBalance(amount);

                    // Xóa orderTotal khỏi session để tránh trùng lặp trong lần giao dịch tiếp theo
                    session.removeAttribute("orderTotal");
                }
            }

            return GenericApiResponse.success(message);
        } catch (Exception e) {
            log.error("Error processing payment return: ", e);
            return GenericApiResponse.error("Failed to process payment return");
        }
    }
}
