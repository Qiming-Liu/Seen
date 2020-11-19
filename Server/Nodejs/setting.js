//器运行的端口，此端口不能被占用
exports.port = 3000;

//是否开启数据库，如不开启，数据将运行在内存中
exports.useSQL = false;
//如果开启数据库，mysql数据库的配置
exports.SQLConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'seen',
    port: '3306'
}