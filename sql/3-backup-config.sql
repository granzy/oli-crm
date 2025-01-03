delete
from s_config
where group_code = '网络设备配置备份配置';
insert into s_config(id, create_time, code, group_code, name, content,description)
values (uuid(), UTC_TIMESTAMP, 'exec_backup_report_url', '网络设备配置备份配置', '配置备份回调接口地址','http://172.31.98.146:18088/umop/configBackupExec/taskReport', '');
insert into s_config(id, create_time, code, group_code, name, content,description)
values (uuid(), UTC_TIMESTAMP, 'exec_command_report_url', '网络设备配置备份配置', '网络设备执行脚本回调接口地址','http://172.31.98.146:18088/umop/deviceExecCommand/taskReport', '');
insert into s_config(id, create_time, code, group_code, name, content,description)
values (uuid(), UTC_TIMESTAMP, 'storage_dir', '网络设备配置备份配置', '配置备份存储基础路径', '/mnt/umop-shared/backup_config/','');
insert into s_config(id, create_time, code, group_code, name, content,description)
values (uuid(), UTC_TIMESTAMP, 'umop_server_storage_mount_dir', '网络设备配置备份配置', 'serve上配置文件挂载基础路径', '/mnt/umop-shared/backup_config/','');