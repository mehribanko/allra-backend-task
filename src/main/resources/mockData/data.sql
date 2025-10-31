
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
-- INSERT IGNORE INTO TB03PRDCT (name, description, price, stock_quantity, is_available, category_id, created_at, updated_at)
-- VALUES
--     ('iPhone 15 Pro', 'Apple의 최신 스마트폰', 1500000, 50, true, 1, NOW(), NOW()),
--     ('MacBook Air M2', '가볍고 강력한 노트북', 1690000, 30, true, 1, NOW(), NOW()),
--     ('AirPods Pro 2', '노이즈 캔슬링 이어폰', 359000, 100, true, 1, NOW(), NOW()),
--     ('iPad Pro 12.9', '프로페셔널 태블릿', 1729000, 0, false, 1, NOW(), NOW()),
--     ('Samsung Galaxy S24', '삼성 플래그십 스마트폰', 1400000, 60, true, 1, NOW(), NOW());
--
-- -- Insert Products (Clothing - category_id = 2)
-- INSERT IGNORE INTO TB03PRDCT (name, description, price, stock_quantity, is_available, category_id, created_at, updated_at)
-- VALUES
--     ('나이키 에어맥스', '편안한 운동화', 189000, 75, true, 2, NOW(), NOW()),
--     ('유니클로 후리스', '따뜻한 겨울 후리스', 39900, 200, true, 2, NOW(), NOW()),
--     ('노스페이스 패딩', '고급 다운 패딩', 450000, 40, true, 2, NOW(), NOW());
--
-- -- Insert Products (Food - category_id = 3)
-- INSERT IGNORE INTO TB03PRDCT (name, description, price, stock_quantity, is_available, category_id, created_at, updated_at)
-- VALUES
--     ('허니버터칩', '달콤짭짤한 스낵', 2500, 500, true, 3, NOW(), NOW()),
--     ('코카콜라 1.5L', '탄산음료', 2800, 300, true, 3, NOW(), NOW()),
--     ('새우깡', '국민 과자', 1500, 800, true, 3, NOW(), NOW());
--
-- -- Insert Products (Household - category_id = 4)
-- INSERT IGNORE INTO TB03PRDCT (name, description, price, stock_quantity, is_available, category_id, created_at, updated_at)
-- VALUES
--     ('락앤락 밀폐용기 세트', '다양한 크기의 밀폐용기', 45000, 80, true, 4, NOW(), NOW()),
--     ('다이슨 청소기 V15', '강력한 무선 청소기', 1290000, 15, true, 4, NOW(), NOW()),
--     ('쿠쿠 전기밥솥', '10인용 압력밥솥', 380000, 25, true, 4, NOW(), NOW());
