-- ENUM 타입 정의
CREATE TYPE source_type AS ENUM ('USER', 'OPEN_API');
CREATE TYPE job_type AS ENUM ('INDEX_INFO', 'INDEX_DATA');
CREATE TYPE job_result AS ENUM ('SUCCESS', 'FAILURE');

-- 지수 정보 테이블
CREATE TABLE index_info (
    id Serial PRIMARY KEY,
    index_classification VARCHAR NOT NULL,
    index_name VARCHAR NOT NULL,
    employment_items_count INTEGER NOT NULL,
    base_point_in_time TIMESTAMP NOT NULL,
    base_index INTEGER NOT NULL,
    favorite BOOLEAN NOT NULL DEFAULT FALSE,
    source_type source_type NOT NULL,
    UNIQUE (index_classification, index_name)
);

-- 자동 연동 설정 테이블
CREATE TABLE index_auto_sync (
     id Serial PRIMARY KEY,
     index_info_id BIGINT NOT NULL,
     enabled BOOLEAN NOT NULL DEFAULT FALSE,
     FOREIGN KEY (index_info_id) REFERENCES index_info(id) ON DELETE CASCADE
);

-- 지수 데이터 테이블
CREATE TABLE index_data (
    id BigSerial PRIMARY KEY,
    index_info_id BIGINT NOT NULL,
    base_date TIMESTAMP NOT NULL,
    source_type source_type NOT NULL,
    market_price NUMERIC(15, 4),
    closing_price NUMERIC(15, 4),
    high_price NUMERIC(15, 4),
    low_price NUMERIC(15, 4),
    versus NUMERIC(15, 4),
    fluctuation_rate NUMERIC(15, 4),
    trading_quantity INTEGER,
    trading_amount INTEGER,
    trading_price INTEGER,
    market_total_amount INTEGER,
    UNIQUE (index_info_id, base_date),
    FOREIGN KEY (index_info_id) REFERENCES index_info(id) ON DELETE CASCADE
);

-- 연동 이력 테이블
CREATE TABLE sync_job_history (
      id BigSerial PRIMARY KEY,
      job_type job_type NOT NULL,
      index_info_id BIGINT,
      target_date TIMESTAMP NOT NULL,
      worker VARCHAR NOT NULL,
      job_time TIMESTAMP NOT NULL,
      result job_result NOT NULL,
      FOREIGN KEY (index_info_id) REFERENCES index_info(id) ON DELETE SET NULL
);
