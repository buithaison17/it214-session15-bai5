# BÁO CÁO PHÂN TÍCH
# Mở rộng Saga Orchestrator với Semantic Lock và Compensating Transaction

## 1. Tổng quan

### 1.1. Mục tiêu

Bài tập xây dựng hệ thống đặt vé concert sử dụng kiến trúc Microservices và mô hình Saga Orchestration.

Hệ thống gồm các service chính:

- `orchestrator-service`: Đóng vai trò "nhạc trưởng", điều phối toàn bộ Saga.
- `payment-service`: Xử lý thanh toán và hoàn tiền.
- `concert-service`: Quản lý ghế/vé concert.

Trong Bài tập 4, `orchestrator-service` sử dụng State Machine để quản lý trạng thái của một booking.

Các trạng thái chính:

- `INITIATED`
- `PAYMENT_PENDING`
- `PAYMENT_COMPLETED`
- `SEAT_RESERVING`
- `BOOKING_CONFIRMED`
- `CANCELLED`

Các sự kiện:

- `PROCESS_PAYMENT`
- `PAYMENT_SUCCESS`
- `PAYMENT_FAILED`
- `RESERVE_SEATS`
- `RESERVATION_SUCCESS`
- `RESERVATION_FAILED`

Bài tập này mở rộng State Machine trên bằng hai cơ chế quan trọng:

1. **Semantic Lock**
2. **Compensating Transaction**

Hai cơ chế này giúp hệ thống xử lý tốt hơn các tình huống đặt vé đồng thời và lỗi xảy ra giữa nhiều microservice.

---

# 2. Kết nối với Bài tập 4

## 2.1. State Machine trong Bài tập 4

Trong Bài tập 4, Orchestrator chịu trách nhiệm điều phối quy trình:

```text
INITIATED
    |
    | PROCESS_PAYMENT
    v
PAYMENT_PENDING
    |
    | PAYMENT_SUCCESS
    v
PAYMENT_COMPLETED
    |
    | RESERVE_SEATS
    v
SEAT_RESERVING
    |
    | RESERVATION_SUCCESS
    v
BOOKING_CONFIRMED
