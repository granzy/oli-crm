DELETE
FROM s_user_medias
WHERE EXISTS (SELECT 1 FROM s_user WHERE account IN ('superadmin') AND s_user.id = s_user_medias.user_id);
DELETE
FROM s_user
WHERE account in ('superadmin');
INSERT INTO s_user (id, create_time, update_time, account, password, username, description, status, delete_time)
VALUES (UUID(), UTC_TIMESTAMP(), NULL, 'superadmin', '$2a$10$XQC14sLvLH7mtcPvUQszxu0KV6DdH.aXb9B9GOmDNbZ7cZ8gyYT/.',
        '超级管理员', '', 'ENABLED', NULL);