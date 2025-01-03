-- $IP、$HostName为变量
SET @utc = UTC_TIMESTAMP();
INSERT INTO umop_x.a_notice_strategy (id, create_time, update_time, name, priority, delay, repeatable, repeat_duration, repeat_times, enable, create_user, update_user, delete_user, delete_time)
VALUES (UUID(), @utc, NULL, '[蓝鲸平台]$IP端口带宽利用率、DOWN告警通知', 'WARN,HIGH', 0, false, 1, 1, true, 'whale', null, null, null);
SET @id = (SELECT id FROM umop_x.a_notice_strategy WHERE name = '[蓝鲸平台]$IP端口带宽利用率、DOWN告警通知' limit 1);

INSERT INTO umop_x.s_rule_filter (id, create_time, update_time, scene, scene_id, group_index, row_index, field_code, field_type, operator, content, create_user)
VALUES (UUID(), @utc, NULL, 'NOTICE', @id, 0, 2, 'information', 'string', 'like', '带宽发送使用率大于85%', 'whale'),
       (UUID(), @utc, NULL, 'NOTICE', @id, 0, 0, 'displayName', 'string', 'eq', '$HostName', 'whale'),
       (UUID(), @utc, NULL, 'NOTICE', @id, 2, 2, 'information', 'string', 'like', '处于Down状态', 'whale'),
       (UUID(), @utc, NULL, 'NOTICE', @id, 1, 0, 'displayName', 'string', 'eq', '$HostName', 'whale'),
       (UUID(), @utc, NULL, 'NOTICE', @id, 1, 1, 'ip', 'string', 'eq', '$IP', 'whale'),
       (UUID(), @utc, NULL, 'NOTICE', @id, 1, 2, 'information', 'string', 'like', '带宽接收使用率大于85%', 'whale'),
       (UUID(), @utc, NULL, 'NOTICE', @id, 0, 1, 'ip', 'string', 'eq', '$IP', 'whale'),
       (UUID(), @utc, NULL, 'NOTICE', @id, 2, 0, 'displayName', 'string', 'eq', '$HostName', 'whale'),
       (UUID(), @utc, NULL, 'NOTICE', @id, 2, 1, 'ip', 'string', 'eq', '$IP', 'whale');

INSERT INTO umop_x.a_notice_step (id, create_time, update_time, type, seq, media, upgrade_user, upgrade_time, upgrade_unit, create_user, update_user, delete_time, notice_strategy_id)
VALUES (UUID(), @utc, NULL, 'HAPPEN', 0, 'WHALE', 'SELF', 1, 'minute', 'whale', null, null, @id),
       (UUID(), @utc, NULL, 'RECOVER', 1, 'WHALE', 'SELF', 1, 'minute', 'whale', null, null, @id);

INSERT INTO umop_x.s_contact (id, create_time, update_time, scene, scene_id, type, contact_id, create_user, update_user) VALUES (UUID(), @utc, NULL, 'NOTICE', @id, 'USER', 'd7b316e1-e04e-436d-9bd1-7a1b7958aaec', 'whale', null);