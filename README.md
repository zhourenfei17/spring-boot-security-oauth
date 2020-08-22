###SpringBoot2集成Security、Oauth、redis、mybatis实现统一认证 服务端

[博客地址](https://blog.csdn.net/zhourenfei17/article/details/108022884)

##### 技术清单

1.spring boot 2.x

2.mybatis plus

3.spring security

4.spring security oauth2.0

5.redis

6.thymeleaf

7.vue

##### client details信息

| clientId            | clientSecret                     | scope      | redirectUri                 |
| ------------------- | -------------------------------- | ---------- | --------------------------- |
| 1293380307393789953 | jskJzLhsye02mqKbn09SXSx0j28IehjG | read write | http://localhost:8090/index |

##### user details信息

| 账号  | 密码   | 姓名     | 角色    | OAuth资源权限 |
| ----- | ------ | -------- | ------- | ------------- |
| admin | 123456 | 管理员   | admin   | get、update   |
| test  | 123456 | 普通用户 | visitor | get           |

