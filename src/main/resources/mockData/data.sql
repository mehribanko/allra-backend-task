
INSERT IGNORE INTO TB02USER (email, name, phone, address, created_at, updated_at)
VALUES
    ('john@test.com', 'John Doe', '010-1234-5678', 'Seoul, Gangnam-gu', NOW(), NOW()),
    ('sarah@test.com', 'Sarah Kim', '010-2345-6789', 'Seoul, Songpa-gu', NOW(), NOW()),
    ('mike@test.com', 'Mike Lee', '010-3456-7890', 'Busan, Haeundae-gu', NOW(), NOW());

INSERT IGNORE INTO TB01CTGRY (name, description, created_at, updated_at)
VALUES
    ('전자제품', '스마트폰, 노트북, 태블릿 등', NOW(), NOW()),
    ('의류', '남성복, 여성복, 아동복', NOW(), NOW()),
    ('식품', '과자, 음료, 신선식품', NOW(), NOW()),
    ('생활용품', '주방용품, 욕실용품, 청소용품', NOW(), NOW());

-- Insert Products (Electronics - category_id = 1)
INSERT IGNORE INTO TB03PRDCT (name, description, price, stock_quantity, is_available, category_id, created_at, updated_at)
VALUES
    ('iPhone 15 Pro', 'Apple의 최신 스마트폰', 1500000, 50, true, 1, NOW(), NOW()),
    ('MacBook Air M2', '가볍고 강력한 노트북', 1690000, 30, true, 1, NOW(), NOW()),
    ('AirPods Pro 2', '노이즈 캔슬링 이어폰', 359000, 100, true, 1, NOW(), NOW()),
    ('iPad Pro 12.9', '프로페셔널 태블릿', 1729000, 0, false, 1, NOW(), NOW()),
    ('Samsung Galaxy S24', '삼성 플래그십 스마트폰', 1400000, 60, true, 1, NOW(), NOW());

-- Insert Products (Clothing - category_id = 2)
INSERT IGNORE INTO TB03PRDCT (name, description, price, stock_quantity, is_available, category_id, created_at, updated_at)
VALUES
    ('나이키 에어맥스', '편안한 운동화', 189000, 75, true, 2, NOW(), NOW()),
    ('유니클로 후리스', '따뜻한 겨울 후리스', 39900, 200, true, 2, NOW(), NOW()),
    ('노스페이스 패딩', '고급 다운 패딩', 450000, 40, true, 2, NOW(), NOW());

-- Insert Products (Food - category_id = 3)
INSERT IGNORE INTO TB03PRDCT (name, description, price, stock_quantity, is_available, category_id, created_at, updated_at)
VALUES
    ('허니버터칩', '달콤짭짤한 스낵', 2500, 500, true, 3, NOW(), NOW()),
    ('코카콜라 1.5L', '탄산음료', 2800, 300, true, 3, NOW(), NOW()),
    ('새우깡', '국민 과자', 1500, 800, true, 3, NOW(), NOW());

-- Insert Products (Household - category_id = 4)
INSERT IGNORE INTO TB03PRDCT (name, description, price, stock_quantity, is_available, category_id, created_at, updated_at)
VALUES
    ('락앤락 밀폐용기 세트', '다양한 크기의 밀폐용기', 45000, 80, true, 4, NOW(), NOW()),
    ('다이슨 청소기 V15', '강력한 무선 청소기', 1290000, 15, true, 4, NOW(), NOW()),
    ('쿠쿠 전기밥솥', '10인용 압력밥솥', 380000, 25, true, 4, NOW(), NOW());

-- Insert Carts (one cart per user)
INSERT IGNORE INTO TB04CART (user_id, created_at, updated_at)
VALUES
    (1, NOW(), NOW()),  -- John's cart
    (2, NOW(), NOW()),  -- Sarah's cart
    (3, NOW(), NOW());  -- Mike's cart

-- Insert Cart Items for John (user_id=1, cart_id=1)
INSERT IGNORE INTO TB05CARTITEM (cart_id, product_id, quantity, created_at, updated_at)
VALUES
    (1, 1, 1, NOW(), NOW()),   -- iPhone 15 Pro x1
    (1, 3, 2, NOW(), NOW()),   -- AirPods Pro 2 x2
    (1, 7, 5, NOW(), NOW());   -- 허니버터칩 x5

-- Insert Cart Items for Sarah (user_id=2, cart_id=2)
INSERT IGNORE INTO TB05CARTITEM (cart_id, product_id, quantity, created_at, updated_at)
VALUES
    (2, 2, 1, NOW(), NOW()),   -- MacBook Air M2 x1
    (2, 5, 1, NOW(), NOW()),   -- 나이키 에어맥스 x1
    (2, 10, 3, NOW(), NOW());  -- 코카콜라 x3

-- Insert Cart Items for Mike (user_id=3, cart_id=3)
INSERT IGNORE INTO TB05CARTITEM (cart_id, product_id, quantity, created_at, updated_at)
VALUES
    (3, 13, 1, NOW(), NOW()),  -- 다이슨 청소기 x1
    (3, 6, 2, NOW(), NOW());   -- 유니클로 후리스 x2


-- Insert Orders
INSERT IGNORE INTO TB06ORDER (id, user_id, total_amount, delivery_address, status, created_at, updated_at)
VALUES
    (1, 1, 2218000, 'Seoul, Gangnam-gu', 'PAYMENT_COMPLETED', NOW(), NOW()),  -- John's completed order
    (2, 2, 1879000, 'Seoul, Songpa-gu', 'PAYMENT_COMPLETED', NOW(), NOW()),    -- Sarah's completed order
    (3, 3, 1369800, 'Busan, Haeundae-gu', 'PENDING', NOW(), NOW());            -- Mike's pending order

-- Insert Order Items for Order 1 (John)
INSERT IGNORE INTO TB07ORDERITEM (id, order_id, product_id, quantity, price, created_at, updated_at)
VALUES
    (1, 1, 1, 1, 1500000, NOW(), NOW()),  -- iPhone 15 Pro
    (2, 1, 3, 2, 359000, NOW(), NOW());   -- AirPods Pro 2 x2

-- Insert Order Items for Order 2 (Sarah)
INSERT IGNORE INTO TB07ORDERITEM (id, order_id, product_id, quantity, price, created_at, updated_at)
VALUES
    (3, 2, 2, 1, 1690000, NOW(), NOW()),  -- MacBook Air M2
    (4, 2, 6, 1, 189000, NOW(), NOW());   -- 나이키 에어맥스

-- Insert Order Items for Order 3 (Mike)
INSERT IGNORE INTO TB07ORDERITEM (id, order_id, product_id, quantity, price, created_at, updated_at)
VALUES
    (5, 3, 13, 1, 1290000, NOW(), NOW()),  -- 다이슨 청소기
    (6, 3, 7, 2, 39900, NOW(), NOW());     -- 유니클로 후리스 x2

-- Insert Payments
INSERT IGNORE INTO TB08PAYMENT (id, order_id, amount, status, transaction_id, message, created_at, updated_at)
VALUES
    (1, 1, 2218000, 'SUCCESS', 'txn_1234567890', 'Payment processed successfully', NOW(), NOW()),
    (2, 2, 1879000, 'SUCCESS', 'txn_0987654321', 'Payment processed successfully', NOW(), NOW()),
    (3, 3, 1369800, 'PENDING', NULL, NULL, NOW(), NOW());