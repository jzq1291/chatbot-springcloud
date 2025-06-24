-- 迁移数据从 public schema 到 knowledge schema
INSERT INTO knowledge.knowledge_base (
    id,
    title,
    content,
    category,
    source,
    url,
    author,
    create_time,
    update_time,
    deleted
)
SELECT
    id,
    title,
    content,
    category,
    source,
    url,
    author,
    create_time,
    update_time,
    deleted
FROM public.knowledge_base
WHERE NOT EXISTS (
    SELECT 1
    FROM knowledge.knowledge_base k
    WHERE k.id = public.knowledge_base.id
); 