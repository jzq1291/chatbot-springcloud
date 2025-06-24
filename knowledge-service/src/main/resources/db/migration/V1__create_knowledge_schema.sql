-- 创建知识库 schema
CREATE SCHEMA IF NOT EXISTS knowledge;

-- 切换到知识库 schema
SET search_path TO knowledge;

-- 创建知识库表
CREATE TABLE IF NOT EXISTS knowledge_base (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(50),
    source VARCHAR(255),
    url VARCHAR(1000),
    author VARCHAR(100),
    create_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_knowledge_title ON knowledge_base (title);
CREATE INDEX IF NOT EXISTS idx_knowledge_category ON knowledge_base (category);
CREATE INDEX IF NOT EXISTS idx_knowledge_create_time ON knowledge_base (create_time);

-- 创建更新时间触发器
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_knowledge_timestamp
    BEFORE UPDATE ON knowledge_base
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp(); 