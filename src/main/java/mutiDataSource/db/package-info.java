/**
 * 配置多数据库源
 * 使用方法：
 * 在类或方法上添加形如 @DataSource(Global.MASTER_DB) 的注解 则当前类或方法将使用该数据库
 * 方法上的注解优先级高于类 若两者都未配置 默认使用Global.MASTER_DB数据源
 */
package mutiDataSource.db;